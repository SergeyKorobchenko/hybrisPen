package com.bridgex.facades.csv.impl;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import com.bridgex.facades.csv.PentlandCsvFacade;
import com.bridgex.facades.order.data.MiniOrderEntry;

import de.hybris.platform.acceleratorfacades.csv.impl.DefaultCsvFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.ProductData;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 11/13/2017.
 */
public class DefaultPentlandCsvFacade extends DefaultCsvFacade implements PentlandCsvFacade{

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
  public void generateCsvFromJson(List<String> headers, boolean includeHeader, List<MiniOrderEntry> collectedProducts, Writer writer) throws IOException {
    addHeaders(headers, includeHeader, writer);

    if (CollectionUtils.isNotEmpty(collectedProducts)) {
    	for(MiniOrderEntry productData: collectedProducts){
    		final StringBuilder csvContent = new StringBuilder();
    		csvContent.append(StringEscapeUtils.escapeCsv(productData.getStylecode())).append(DELIMITER);
    		List<String> materialKey = productData.getMaterialKey();
    		for (String string1 : materialKey) {
    			csvContent.append(StringEscapeUtils.escapeCsv(string1));
    		}
    		csvContent.append(DELIMITER);
    		List<String> sku = productData.getSku();
    		for (String string : sku) {
    			csvContent.append(StringEscapeUtils.escapeCsv(string));
    		} 
    		csvContent.append(DELIMITER);
    		csvContent.append(StringEscapeUtils.escapeCsv(productData.getName()))
    		.append(DELIMITER);	
    		if (StringUtils.isNotEmpty(productData.getUpc())) {
    			csvContent.append(StringEscapeUtils.escapeCsv(productData.getUpc()));
    		}
    		csvContent.append(DELIMITER);
    		csvContent.append(1).append(DELIMITER);
    		csvContent.append(StringEscapeUtils.escapeCsv(productData.getPrice()))
    		.append(DELIMITER);
    		csvContent.append(LINE_SEPERATOR);
    		writer.write(csvContent.toString());
    	}
    }
  }
}
