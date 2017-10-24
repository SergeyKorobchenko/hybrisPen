package com.bridgex.core.export.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.messaging.Message;

import com.bridgex.core.export.model.PentlandProduct;

import de.hybris.platform.catalog.model.ProductFeatureModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.converter.impl.AbstractConverter;
import de.hybris.platform.core.model.product.ProductModel;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 10/24/2017.
 */
public class PentlandProductConverter extends AbstractConverter<Message<ProductModel>, PentlandProduct> {
  @Override
  public void populate(Message<ProductModel> productModelMessage, PentlandProduct pentlandProduct) {
    final ProductModel productModel = productModelMessage.getPayload();

    pentlandProduct.setCode(productModel.getCode());
    pentlandProduct.setName(wrapTextForReimport(productModel.getName()));
    pentlandProduct.setDescription(wrapTextForReimport(productModel.getDescription()));
    pentlandProduct.setFeatures(wrapTextForReimport(productModel.getFeatureDescription()));
    pentlandProduct.setPackSize(productModel.getPackSize());
    pentlandProduct.setVideoURL(productModel.getVideoURL());
    pentlandProduct.setExternalURL(productModel.getExternalURL());
    if(productModel.getSizeChartImage() != null) {
      pentlandProduct.setSizeChartImage(productModel.getSizeChartImage().getCode());
    }

    convertCategories(pentlandProduct, productModel);

    convertFeatures(pentlandProduct, productModel);

  }

  private void convertFeatures(PentlandProduct pentlandProduct, ProductModel productModel) {List<ProductFeatureModel> features = productModel.getFeatures();
    if(CollectionUtils.isNotEmpty(features)){
      features.forEach(feature ->{
        switch(feature.getQualifier()){
          case "pentlandClassificationCatalog/1.0/clClass.teamwearType": pentlandProduct.setTeamwearType(feature.getValue().toString());
               break;
          case "pentlandClassificationCatalog/1.0/clClass.trainingwearType": pentlandProduct.setTrainingwearType(feature.getValue().toString());
               break;
          case "pentlandClassificationCatalog/1.0/clClass.sport": pentlandProduct.setSport(feature.getValue().toString());
               break;
          case "pentlandClassificationCatalog/1.0/clClass.accessoryType": pentlandProduct.setAccessoryType(feature.getValue().toString());
               break;
          case "pentlandClassificationCatalog/1.0/clClass.useageOccasion": pentlandProduct.setUseageOccasion(feature.getValue().toString());
               break;
          case "pentlandClassificationCatalog/1.0/clClass.surface": pentlandProduct.setSurface(feature.getValue().toString());
               break;
          case "pentlandClassificationCatalog/1.0/clClass.trainingMethod": pentlandProduct.setTrainingMethod(feature.getValue().toString());
               break;
        }
      });
    }
  }

  private void convertCategories(PentlandProduct pentlandProduct, ProductModel productModel) {
    String categories = productModel.getSupercategories().stream().map(CategoryModel::getCode).collect(Collectors.joining(","));
    pentlandProduct.setCategories(categories);
  }

  //wrap text in "" in case in contains symbols used as delimiters by impex or line breaks and escape every " found inside
  private String wrapTextForReimport(String attribute){
    if(StringUtils.isNotEmpty(attribute)){
      if(attribute.contains("\"")){
        attribute = attribute.replaceAll("\"", "\"\"");
      }
      return "\"" + attribute + "\"";
    }else{
      return attribute;
    }
  }
}
