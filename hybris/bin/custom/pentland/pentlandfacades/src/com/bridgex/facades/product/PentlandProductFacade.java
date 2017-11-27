package com.bridgex.facades.product;

import java.util.Date;
import java.util.List;

import com.bridgex.integration.domain.MultiBrandCartDto;

import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.VariantMatrixElementData;

/**
 * @author Created by konstantin.pavlyukov on 11/8/2017.
 */
public interface PentlandProductFacade extends ProductFacade {

  boolean populateCustomerPrice(ProductData productData);

  boolean populateCustomerPrice(ProductData productData, Date requestedDeliveryDate);

  boolean populateOrderForm(ProductData product);

  boolean populateOrderForm(ProductData product, Date requestedDeliveryDate);

  MultiBrandCartDto createOrderFormRequest(List<VariantMatrixElementData> materials, Date requestedDeliveryDate);

  MultiBrandCartDto createRequestRoot(Date requestedDeliveryDate, boolean availabilityCheck, boolean creditCheck);
}
