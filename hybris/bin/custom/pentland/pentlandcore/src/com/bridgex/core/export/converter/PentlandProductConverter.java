package com.bridgex.core.export.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
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

  private static final String PENTLAND_CLASSIFICATION_CATALOG_1_0 = "pentlandClassificationCatalog/1.0/";
  private static final String CL_TEAMWEAR_TEAMWEARTYPE = PENTLAND_CLASSIFICATION_CATALOG_1_0 + "clTeamwear.teamweartype";
  private static final String CL_TRAINING_WEAR_TRAININGWEARTYPE = PENTLAND_CLASSIFICATION_CATALOG_1_0 + "clTrainingWear.trainingweartype";
  private static final String CL_BALLS_SPORT = PENTLAND_CLASSIFICATION_CATALOG_1_0 + "clBalls.sport";
  private static final String CL_EQUIPMENT_ACCESSORYTYPE = PENTLAND_CLASSIFICATION_CATALOG_1_0 + "clEquipment.accessorytype";
  private static final String CL_BALLS_USEAGEOCCASION = PENTLAND_CLASSIFICATION_CATALOG_1_0 + "clBalls.useageoccasion";
  private static final String CL_BALLS_SURFACE = PENTLAND_CLASSIFICATION_CATALOG_1_0 + "clBalls.surface";
  private static final String CL_EQUIPMENT_TRAININGMETHOD = PENTLAND_CLASSIFICATION_CATALOG_1_0 + "clEquipment.trainingmethod";

  private static final String DELIMITER = "|";

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
          case CL_BALLS_SPORT: if(StringUtils.isEmpty(pentlandProduct.getSport())){
            pentlandProduct.setSport(feature.getValue().toString());
          }else{
            pentlandProduct.setSport(pentlandProduct.getSport() + DELIMITER + feature.getValue().toString());
          }
               break;
          case CL_BALLS_USEAGEOCCASION: if(StringUtils.isEmpty(pentlandProduct.getUseageOccasion())){
            pentlandProduct.setUseageOccasion(feature.getValue().toString());
          }else{
            pentlandProduct.setUseageOccasion(pentlandProduct.getUseageOccasion() + DELIMITER + feature.getValue().toString());
          }
               break;
          case CL_BALLS_SURFACE: if(StringUtils.isEmpty(pentlandProduct.getSurface())){
            pentlandProduct.setSurface(feature.getValue().toString());
          }else{
            pentlandProduct.setSurface(pentlandProduct.getSurface() + DELIMITER + feature.getValue().toString());
          }
               break;
          case CL_EQUIPMENT_TRAININGMETHOD: if(StringUtils.isEmpty(pentlandProduct.getTrainingMethod())){
            pentlandProduct.setTrainingMethod(feature.getValue().toString());
          }else{
            pentlandProduct.setTrainingMethod(pentlandProduct.getTrainingMethod() + DELIMITER + feature.getValue().toString());
          }
               break;
        }
      });
    }
  }

  private void convertCategories(PentlandProduct pentlandProduct, ProductModel productModel) {
    String categories = productModel.getSupercategories().stream().map(CategoryModel::getCode).collect(Collectors.joining(DELIMITER));
    pentlandProduct.setCategories(categories);
  }

  //wrap text in "" in case in contains symbols used as delimiters by impex or line breaks and escape every " found inside
  private String wrapTextForReimport(String attribute){
    if(StringUtils.isNotEmpty(attribute)){
      return StringEscapeUtils.escapeCsv(attribute);
    }else{
      return attribute;
    }
  }
}
