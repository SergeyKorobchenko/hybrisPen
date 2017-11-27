package com.bridgex.core.impex.decorator;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import de.hybris.platform.impex.jalo.header.AbstractImpExCSVCellDecorator;

/**
 * Add catalog version to category identifier
 * Depends on catalog version being found on position 2
 * @author Created by ekaterina.agievich@bridge-x.com on 10/25/2017.
 */
public class CategoryCatalogVersionDecorator extends AbstractImpExCSVCellDecorator {

  public static final String COLLECTION_DELIMITER_REGEXP = "\\|";
  public static final String COLLECTION_DELIMITER = "|";

  @Override
  public String decorate(int i, Map<Integer, String> map) {
    String categories = map.get(i);
    if(StringUtils.isNotEmpty(categories)) {
      String catalogVersion = map.get(2);
      String[] categoryArray = categories.split(COLLECTION_DELIMITER_REGEXP);
      for(int k = 0;k < categoryArray.length; k++){
        categoryArray[k] = categoryArray[k] + ":" + catalogVersion;
        if(k == 0){
          categories = categoryArray[k];
        }else{
          categories += COLLECTION_DELIMITER + categoryArray[k];
        }
      }
    }
    return categories;
  }
}
