package com.bridgex.core.export.generic.output.csv;

import java.util.Iterator;
import java.util.Map;

import de.hybris.platform.acceleratorservices.dataexport.generic.output.csv.ConvertObjectsToCSV;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 10/25/2017.
 */
public class PentlandConvertObjectsToCSV extends ConvertObjectsToCSV{

  private static final String COMMENT_SYMBOL = "#";

  @Override
  protected String generateHeader(final ClassAnnotationProperties dataProperties) {
    final StringBuilder builder = new StringBuilder(COMMENT_SYMBOL);
    final Map<Integer, String> map = dataProperties.getPropertyNames();
    final Iterator<Map.Entry<Integer, String>> entriesIterator = map.entrySet().iterator();

    while (entriesIterator.hasNext()) {
      final Map.Entry<Integer, String> currentEntry = entriesIterator.next();
      final String dataProperty = currentEntry.getValue();
      builder.append(dataProperty);
      if (entriesIterator.hasNext()) {
        builder.append(dataProperties.getDelimiter());
      }
    }

    return builder.toString();
  }
}
