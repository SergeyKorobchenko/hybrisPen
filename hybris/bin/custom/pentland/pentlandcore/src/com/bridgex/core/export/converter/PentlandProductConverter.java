package com.bridgex.core.export.converter;

import org.springframework.messaging.Message;

import com.bridgex.core.export.model.PentlandProduct;

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
    pentlandProduct.setName(productModel.getName());
    pentlandProduct.setDescription(productModel.getDescription());

  }
}
