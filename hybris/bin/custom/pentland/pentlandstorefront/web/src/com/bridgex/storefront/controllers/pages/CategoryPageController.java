/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.bridgex.storefront.controllers.pages;


import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractCategoryPageController;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.VariantMatrixElementData;
import de.hybris.platform.commercefacades.search.data.SearchQueryData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.FacetRefinement;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bridgex.facades.csv.PentlandCsvFacade;
import com.bridgex.facades.export.PentlandExportFacade;
import com.bridgex.facades.order.data.MiniOrderEntry;
import com.bridgex.facades.product.PentlandProductFacade;


/**
 * Controller for a category page
 */
@Controller
@RequestMapping(value = "/**/c")
public class CategoryPageController extends AbstractCategoryPageController
{
	private static final Logger LOG = Logger.getLogger(CategoryPageController.class);
	@Resource
	  private PentlandCsvFacade csvFacade;

	  @Resource
	  private PentlandExportFacade pentlandExportFacade;
	  
	  @Resource(name = "productFacade")
	  private PentlandProductFacade productFacade;
	  
	  private ObjectMapper mapper = new ObjectMapper();
	  
    @RequestMapping(value = CATEGORY_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
    public String category(@PathVariable("categoryCode") final String categoryCode, // NOSONAR
    					   @RequestParam(value="exportImages",required=false) final boolean exportImages,
    					   @RequestParam(value="url",required=false) final String url,
    					   @RequestParam(value="exportCsv",required=false) final boolean exportCsv,
                           @RequestParam(value = "q", required = false) final String searchQuery,
                           @RequestParam(value = "page", defaultValue = "0") final int page,
                           @RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
                           @RequestParam(value = "sort", required = false) final String sortCode, final Model model,
                           final HttpServletRequest request, final HttpServletResponse response) throws IOException {
    	
    	if(exportImages||exportCsv)
    	{
    		ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = null;
    		final SearchStateData searchState = new SearchStateData();
    		SearchQueryData searchQueryData=new SearchQueryData();
    		searchQueryData.setValue(searchQuery);
    		searchState.setQuery(searchQueryData);
    		searchState.setUrl(url);
    		int data;

    		if (searchQueryData.getValue() == null)
    		{
    			searchPageData = getProductSearchFacade().categorySearch(categoryCode);
    		}
    		else
    		{
    			final PageableData pageableData = createPageableData(page, getSearchPageSize(), sortCode, showMode);
    			searchPageData = getProductSearchFacade().categorySearch(categoryCode, searchState, pageableData);
    		}
    		data=(int) searchPageData.getPagination().getTotalNumberOfResults();
    		final PageableData pageableData = createPageableData(page, data, sortCode, showMode);
    		searchPageData = getProductSearchFacade().categorySearch(categoryCode, searchState, pageableData);
    		List<String> imageContent=new ArrayList<>();
    		List<ProductData> results = searchPageData.getResults();
    		for (ProductData productData : results) {
    			imageContent.add(productData.getCode());
    		}
    		List<MiniOrderEntry> csvContent=new ArrayList<>();
    		List<ProductData> results1 = searchPageData.getResults();


    		for (ProductData productData : results1) {
    			List<String> variantCodeAndSizes = new ArrayList<>();
    			List<String> variantCode = new ArrayList<>();
    			MiniOrderEntry productEntry=new MiniOrderEntry();
    			final ProductData productData1 = productFacade.getProductForCodeAndOptions(productData.getBaseProduct(),
    					Arrays.asList(ProductOption.BASIC, ProductOption.VARIANT_FULL,ProductOption.VARIANT_MATRIX_BASE ));
    			List<VariantMatrixElementData> variantMatrix = productData1.getVariantMatrix();
    			for (VariantMatrixElementData variantMatrixElementData : variantMatrix) {
    				List<VariantMatrixElementData> elements = variantMatrixElementData.getElements();
    				for (VariantMatrixElementData variantMatrixElementData2 : elements) {
    					List<VariantMatrixElementData> elements2 = variantMatrixElementData2.getElements();
    					List<String> variantCodesize = new ArrayList<>();
    					for (VariantMatrixElementData variantMatrixElementData3 : elements2) {
    						String size = variantMatrixElementData3.getVariantOption().getCode();
    						variantCodesize.add(size);
    					}
    					String variantCode1 = variantMatrixElementData2.getVariantCode();
    					variantCode.add(variantCode1+",");
    					variantCodeAndSizes.add(variantCode1+" "+variantCodesize+",");
    				}
    			}
    			productEntry.setStylecode(productData.getBaseProduct());
    			productEntry.setMaterialKey(variantCode);
    			productEntry.setSku(variantCodeAndSizes);
    			productEntry.setName(productData.getName());
    			productEntry.setUpc(productData.getUpc());
    			productEntry.setPrice(productData.getPrice().getFormattedValue());
    			csvContent.add(productEntry);	
    		}

    		if(exportImages)
    		{
    			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    			ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);

    			pentlandExportFacade.exportImagesForProductList(zipOutputStream, new HashSet<>(imageContent), true);

    			zipOutputStream.finish();

    			String fileName = String.format("images_%d.zip", System.currentTimeMillis());
    			response.setHeader("Content-Disposition", String.format("attachment;filename=%s", fileName));
    			response.setContentType("application/zip");

    			try{
    				response.getOutputStream().write(byteArrayOutputStream.toByteArray());
    				response.flushBuffer();
    			}catch (IOException e){
    				LOG.error(e.getMessage(), e);
    			} finally{
    				byteArrayOutputStream.close();

    			}
    			return null;
    		}
    		else if(exportCsv)
    		{
    			String fileName = String.format("product_%d.csv", System.currentTimeMillis());
    			response.setHeader("Content-Disposition", String.format("attachment;filename=%s", fileName));

    			try (final StringWriter writer = new StringWriter()) {
    				try {
    					final List<String> headers = new ArrayList<>();
    					headers.add(getMessageSource().getMessage("basket.export.cart.item.stylecode", null, getI18nService().getCurrentLocale()));
    					headers.add(getMessageSource().getMessage("basket.export.cart.item.materialKey", null, getI18nService().getCurrentLocale()));
    					headers.add(getMessageSource().getMessage("basket.export.cart.item.sku", null, getI18nService().getCurrentLocale()));
    					headers.add(getMessageSource().getMessage("basket.export.cart.item.name", null, getI18nService().getCurrentLocale()));
    					headers
    					.add(getMessageSource().getMessage("basket.export.cart.item.upc", null, getI18nService().getCurrentLocale()));
    					headers.add(
    							getMessageSource().getMessage("basket.export.cart.item.quantity", null, getI18nService().getCurrentLocale()));
    					headers
    					.add(getMessageSource().getMessage("basket.export.cart.item.price", null, getI18nService().getCurrentLocale()));

    					/*List<MiniOrderEntry> collectedProducts =  mapper.readValue(content, TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, MiniOrderEntry.class));*/

    					csvFacade.generateCsvFromJson(headers, true, csvContent, writer);


    					StreamUtils.copy(writer.toString(), StandardCharsets.UTF_8, response.getOutputStream());
    				}
    				catch (final IOException e)
    				{
    					LOG.error(e.getMessage(), e);
    				}

    			}

    			return null;
    		}

    	}
    	
        return performSearchAndGetResultsPage(categoryCode, searchQuery, page, showMode, sortCode, model, request, response);
    }

    @ResponseBody
    @RequestMapping(value = CATEGORY_CODE_PATH_VARIABLE_PATTERN + "/facets", method = RequestMethod.GET)
    public FacetRefinement<SearchStateData> getFacets(@PathVariable("categoryCode") final String categoryCode,
                                                      @RequestParam(value = "q", required = false) final String searchQuery,
                                                      @RequestParam(value = "page", defaultValue = "0") final int page,
                                                      @RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
                                                      @RequestParam(value = "sort", required = false) final String sortCode) throws UnsupportedEncodingException {
        return performSearchAndGetFacets(categoryCode, searchQuery, page, showMode, sortCode);
    }

    @ResponseBody
    @RequestMapping(value = CATEGORY_CODE_PATH_VARIABLE_PATTERN + "/results", method = RequestMethod.GET)
    public SearchResultsData<ProductData> getResults(@PathVariable("categoryCode") final String categoryCode,
                                                     @RequestParam(value = "q", required = false) final String searchQuery,
                                                     @RequestParam(value = "page", defaultValue = "0") final int page,
                                                     @RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
                                                     @RequestParam(value = "sort", required = false) final String sortCode) throws UnsupportedEncodingException {
        return performSearchAndGetResultsData(categoryCode, searchQuery, page, showMode, sortCode);
    }
}