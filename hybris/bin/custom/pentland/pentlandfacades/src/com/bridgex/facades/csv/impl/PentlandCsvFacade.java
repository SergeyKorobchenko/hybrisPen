package com.bridgex.facades.csv.impl;

import java.io.IOException;
import java.io.Writer;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import de.hybris.platform.acceleratorfacades.csv.impl.DefaultCsvFacade;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.ProductData;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 11/13/2017.
 */
public class PentlandCsvFacade extends DefaultCsvFacade{

  @Override
  protected void writeOrderEntry(final Writer writer, final OrderEntryData entry) throws IOException {
    final StringBuilder csvContent = new StringBuilder();
    ProductData product = entry.getProduct();
    csvContent.append(StringEscapeUtils.escapeCsv(product.getCode())).append(DELIMITER)
              .append(StringEscapeUtils.escapeCsv(entry.getQuantity().toString())).append(DELIMITER)
              .append(StringEscapeUtils.escapeCsv(product.getName())).append(DELIMITER)
              .append(StringEscapeUtils.escapeCsv(entry.getBasePrice().getFormattedValue()));

    if(StringUtils.isNotEmpty(product.getUpc())) {
      csvContent.append(StringEscapeUtils.escapeCsv(product.getUpc()));
    }
    csvContent.append(LINE_SEPERATOR);


    writer.write(csvContent.toString());
  }
}
