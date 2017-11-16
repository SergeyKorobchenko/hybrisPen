package com.bridgex.facades.csv.impl;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import de.hybris.platform.acceleratorfacades.csv.impl.DefaultCsvFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.ProductData;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 11/13/2017.
 */
public class PentlandCsvFacade extends DefaultCsvFacade{

  public static final String DELIMITER = ";";

  @Override
  public void generateCsvFromCart(final List<String> headers, final boolean includeHeader, final CartData cartData,
                                  final Writer writer) throws IOException {
    if (includeHeader && CollectionUtils.isNotEmpty(headers)) {
      final StringBuilder csvHeader = new StringBuilder();
      int i = 0;
      for (; i < headers.size() - 1; i++) {
        csvHeader.append(StringEscapeUtils.escapeCsv(headers.get(i))).append(DELIMITER);
      }
      csvHeader.append(StringEscapeUtils.escapeCsv(headers.get(i))).append(LINE_SEPERATOR);
      writer.write(csvHeader.toString());
    }

    if (cartData != null && CollectionUtils.isNotEmpty(cartData.getEntries())) {
      writeOrderEntries(writer, cartData.getEntries());
    }
  }

  @Override
  protected void writeOrderEntry(final Writer writer, final OrderEntryData entry) throws IOException {
    final StringBuilder csvContent = new StringBuilder();
    ProductData product = entry.getProduct();
    csvContent.append(StringEscapeUtils.escapeCsv(product.getStylecode())).append(DELIMITER);
    csvContent.append(StringEscapeUtils.escapeCsv(product.getMaterialKey())).append(DELIMITER);
    csvContent.append(StringEscapeUtils.escapeCsv(product.getCode())).append(DELIMITER)
              .append(StringEscapeUtils.escapeCsv(entry.getQuantity().toString())).append(DELIMITER)
              .append(StringEscapeUtils.escapeCsv(product.getName())).append(DELIMITER)
              .append(StringEscapeUtils.escapeCsv(entry.getBasePrice().getFormattedValue())).append(DELIMITER);

    if(StringUtils.isNotEmpty(product.getUpc())) {
      csvContent.append(StringEscapeUtils.escapeCsv(product.getUpc()));
    }
    csvContent.append(LINE_SEPERATOR);


    writer.write(csvContent.toString());
  }
}
