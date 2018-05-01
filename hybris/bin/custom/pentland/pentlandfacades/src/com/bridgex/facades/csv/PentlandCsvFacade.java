package com.bridgex.facades.csv;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.MessageSource;

import com.bridgex.facades.order.data.MiniOrderEntry;

import de.hybris.platform.acceleratorfacades.csv.CsvFacade;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 11/17/2017.
 */
public interface PentlandCsvFacade extends CsvFacade{

  void generateCsvFromJson(final List<String> headers, final boolean includeHeader, final List<MiniOrderEntry> collectedProducts, final Writer writer)
    throws IOException;
  
  void GenerateCsvAndImageDownlaod (ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData,boolean exportImages,boolean exportCsv,String url, final HttpServletRequest request, final HttpServletResponse response,Locale locale, MessageSource messageSource )throws IOException;
  
  void GenerateCsvAndImageDownlaodWhileSearch (ProductSearchPageData<SearchStateData, ProductData> searchPageData,boolean exportImages,boolean exportCsv,String url, final HttpServletRequest request, final HttpServletResponse response,Locale locale, MessageSource messageSource )throws IOException;


}
