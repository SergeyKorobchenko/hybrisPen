package com.bridgex.facades.csv.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.util.StreamUtils;

import com.bridgex.facades.csv.PentlandCsvFacade;
import com.bridgex.facades.export.PentlandExportFacade;
import com.bridgex.facades.order.data.MiniOrderEntry;
import com.bridgex.facades.product.PentlandProductFacade;

import de.hybris.platform.acceleratorfacades.csv.impl.DefaultCsvFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.VariantMatrixElementData;
import de.hybris.platform.commercefacades.product.data.VariantOptionData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData;
import de.hybris.platform.core.GenericSearchConstants.LOG;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 11/13/2017.
 */
public class DefaultPentlandCsvFacade extends DefaultCsvFacade implements PentlandCsvFacade{
	private static final Logger LOG = Logger.getLogger(DefaultPentlandCsvFacade.class);
	@Resource(name = "productFacade")
	private PentlandProductFacade productFacade;

	@Resource
	private PentlandExportFacade pentlandExportFacade;

	@Override
	public void generateCsvFromCart(final List<String> headers, final boolean includeHeader, final CartData cartData,
			final Writer writer) throws IOException {
		addHeaders(headers, includeHeader, writer);

		if (cartData != null && CollectionUtils.isNotEmpty(cartData.getEntries())) {
			writeOrderEntries(writer, cartData.getEntries());
		}
	}

	@Override
	protected void writeOrderEntry(final Writer writer, final OrderEntryData entry) throws IOException {
		if(entry.getQuantity() > 0) {
			final StringBuilder csvContent = new StringBuilder();
			ProductData product = entry.getProduct();
			csvContent.append(StringEscapeUtils.escapeCsv(product.getStylecode())).append(DELIMITER);
			csvContent.append(StringEscapeUtils.escapeCsv(product.getMaterialKey())).append(DELIMITER);
			csvContent.append(StringEscapeUtils.escapeCsv(product.getCode()))
			.append(DELIMITER)
			.append(StringEscapeUtils.escapeCsv(product.getName()))
			.append(DELIMITER);
			if (StringUtils.isNotEmpty(product.getUpc())) {
				csvContent.append(StringEscapeUtils.escapeCsv(product.getUpc()));
			}
			csvContent.append(DELIMITER);
			csvContent.append(StringEscapeUtils.escapeCsv(entry.getQuantity().toString()))
			.append(DELIMITER);
			csvContent.append(StringEscapeUtils.escapeCsv(entry.getBasePrice().getFormattedValue()))
			.append(DELIMITER);


			csvContent.append(LINE_SEPERATOR);

			writer.write(csvContent.toString());
		}
	}

	private void addHeaders(List<String> headers, boolean includeHeader, Writer writer) throws IOException {
		writer.write("\ufeff");
		if (includeHeader && CollectionUtils.isNotEmpty(headers)) {
			final StringBuilder csvHeader = new StringBuilder();
			int i = 0;
			for (; i < headers.size() - 1; i++) {
				csvHeader.append(StringEscapeUtils.escapeCsv(headers.get(i))).append(DELIMITER);
			}
			csvHeader.append(StringEscapeUtils.escapeCsv(headers.get(i))).append(LINE_SEPERATOR);
			writer.write(csvHeader.toString());
		}
	}
	@Override
	public void GenerateCsvAndImageDownlaod(
			ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData, boolean exportImages,
			boolean exportCsv, String url, HttpServletRequest request, HttpServletResponse response, Locale locale,
			MessageSource messageSource) throws IOException 
	{
		if(exportImages||exportCsv)
		{
			List<ProductData> results = searchPageData.getResults();
			downloadData(results,exportImages,exportCsv,response,locale,messageSource);
		}

	}

	@Override
	public void GenerateCsvAndImageDownlaodWhileSearch(ProductSearchPageData<SearchStateData, ProductData> searchPageData,
			boolean exportImages, boolean exportCsv, String url, HttpServletRequest request, HttpServletResponse response,
			Locale locale, MessageSource messageSource) throws IOException {
		if(exportImages||exportCsv)
		{
			List<ProductData> results = searchPageData.getResults();
			downloadData(results,exportImages,exportCsv,response,locale,messageSource);
		}

	}

	private void downloadData(List<ProductData> results, boolean exportImages, boolean exportCsv, HttpServletResponse response,
			Locale locale, MessageSource messageSource) throws IOException {

		List<String> productVariantCode = new ArrayList<>();
		List<MiniOrderEntry> csvContent=new ArrayList<>();
		for (ProductData productData : results) {
			Map<String, List<String>> variantCodeAndSizes = new HashMap<>();
			List<String> variantCode = new ArrayList<>();
			Map<String, String> variantProductName = new HashMap<>();
			MiniOrderEntry productEntry=new MiniOrderEntry();
			final ProductData productData1 = productFacade.getProductForCodeAndOptions(productData.getBaseProduct(),
					Arrays.asList(ProductOption.BASIC, ProductOption.VARIANT_FULL,ProductOption.VARIANT_MATRIX_BASE,ProductOption.PRICE ));
			List<VariantMatrixElementData> variantMatrix = productData1.getVariantMatrix();
			for (VariantMatrixElementData variantMatrixElementData : variantMatrix) {
				List<VariantMatrixElementData> elements = variantMatrixElementData.getElements();
				for (VariantMatrixElementData variantMatrixElementData2 : elements) {
					List<VariantMatrixElementData> elements2 = variantMatrixElementData2.getElements();
					List<String> variantCodesize = new ArrayList<>();
					List<String> variantCodesize1 = new ArrayList<>();
					for (VariantMatrixElementData variantMatrixElementData3 : elements2) {
						String size = variantMatrixElementData3.getVariantOption().getCode();
						variantCodesize.add(size);
					}
					String variantCode1 = variantMatrixElementData2.getVariantCode();
					String variantName=  variantMatrixElementData2.getVariantName();
					variantProductName.put(variantCode1, variantName);
					variantCode.add(variantCode1);
					productVariantCode.add(variantCode1);
					variantCodesize1.add(variantCode1 +":"+variantCodesize);
					variantCodeAndSizes.put(variantCode1, variantCodesize1);
				}
			}
			Map<String, String> variantProductPrice = new HashMap<>();
			List<VariantOptionData> variantOptions = productData1.getVariantOptions();
			for (VariantOptionData variantOptionData : variantOptions) {
				String code = variantOptionData.getCode();
				PriceData priceData = variantOptionData.getPriceData();
				String formattedValue = priceData.getFormattedValue();
				variantProductPrice.put(code, formattedValue);
			}
			productEntry.setStylecode(productData.getBaseProduct());
			productEntry.setMaterialKey(variantCode);
			productEntry.setSku(variantCodeAndSizes);
			productEntry.setName(variantProductName);
			productEntry.setUpc(productData.getUpc());
			productEntry.setPrice(variantProductPrice);
			csvContent.add(productEntry);	
		}

		if(exportImages)
		{
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);

			pentlandExportFacade.exportImagesForProductList(zipOutputStream, new HashSet<>(productVariantCode), true);

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
		}
		else if(exportCsv)
		{
			String fileName = String.format("product_%d.csv", System.currentTimeMillis());
			response.setHeader("Content-Disposition", String.format("attachment;filename=%s", fileName));

			try (final StringWriter writer = new StringWriter()) {
				try {
					final List<String> headers = new ArrayList<>();
					headers.add(messageSource.getMessage("basket.export.cart.item.stylecode", null, locale));
					headers.add(messageSource.getMessage("basket.export.cart.item.materialKey", null, locale));
					headers.add(messageSource.getMessage("basket.export.cart.item.sku", null, locale));
					headers.add(messageSource.getMessage("basket.export.cart.item.name", null, locale));
					headers
					.add(messageSource.getMessage("basket.export.cart.item.upc", null, locale));
					headers.add(
							messageSource.getMessage("basket.export.cart.item.quantity", null, locale));
					headers
					.add(messageSource.getMessage("basket.export.cart.item.price", null, locale));
					generateCsvFromJson(headers, true, csvContent, writer);
					StreamUtils.copy(writer.toString(), StandardCharsets.UTF_8, response.getOutputStream());
				}
				catch (final IOException e)
				{
					LOG.error(e.getMessage(), e);
				}
			}
		}


	}
	@Override
	public void generateCsvFromJson(List<String> headers, boolean includeHeader, List<MiniOrderEntry> collectedProducts, Writer writer) throws IOException {
		addHeaders(headers, includeHeader, writer);

		if (CollectionUtils.isNotEmpty(collectedProducts)) {
			for(MiniOrderEntry productData: collectedProducts){
				final StringBuilder csvContent = new StringBuilder();

				List<String> materialKey1 = productData.getMaterialKey();
				for (String materialKeyCode : materialKey1) {
					csvContent.append(StringEscapeUtils.escapeCsv(productData.getStylecode())).append(DELIMITER);
					csvContent.append(StringEscapeUtils.escapeCsv(materialKeyCode)).append(DELIMITER);
					Map<String, List<String>> sku2 = productData.getSku();
					for (Entry<String, List<String>> skuCode : sku2.entrySet()) {
						if(materialKeyCode.equals(skuCode.getKey()))
						{
							csvContent.append(StringEscapeUtils.escapeCsv(skuCode.getValue().iterator().next())).append(DELIMITER);
						}
					}
					Map<String, String> ProductVariantName = productData.getName();
					if(ProductVariantName.containsKey(materialKeyCode))
					{
						csvContent.append(StringEscapeUtils.escapeCsv(ProductVariantName.get(materialKeyCode)))
						.append(DELIMITER);	
					}

					if (StringUtils.isNotEmpty(productData.getUpc())) {
						csvContent.append(StringEscapeUtils.escapeCsv(productData.getUpc()));
					}
					csvContent.append(DELIMITER);
					csvContent.append(1).append(DELIMITER);
					Map<String, String> price = productData.getPrice();
					if(price.containsKey(materialKeyCode))
					{
						csvContent.append(StringEscapeUtils.escapeCsv(price.get(materialKeyCode)))
						.append(DELIMITER);
					}


					csvContent.append(LINE_SEPERATOR);

				}
				writer.write(csvContent.toString());
			}
		}
	}


}
