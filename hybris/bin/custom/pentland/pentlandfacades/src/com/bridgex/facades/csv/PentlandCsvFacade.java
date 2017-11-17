package com.bridgex.facades.csv;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import com.bridgex.facades.order.data.MiniOrderEntry;

import de.hybris.platform.acceleratorfacades.csv.CsvFacade;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 11/17/2017.
 */
public interface PentlandCsvFacade extends CsvFacade{

  void generateCsvFromJson(final List<String> headers, final boolean includeHeader, final List<MiniOrderEntry> collectedProducts, final Writer writer)
    throws IOException;
}
