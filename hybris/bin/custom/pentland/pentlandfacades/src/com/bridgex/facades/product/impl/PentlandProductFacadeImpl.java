package com.bridgex.facades.product.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.spockframework.util.Nullable;
import org.springframework.beans.factory.annotation.Required;

import com.bridgex.core.product.OrderSimulationService;
import com.bridgex.core.services.PentlandB2BUnitService;
import com.bridgex.facades.product.PentlandProductFacade;
import com.bridgex.integration.constants.ErpintegrationConstants;
import com.bridgex.integration.domain.MaterialInfoDto;
import com.bridgex.integration.domain.MultiBrandCartDto;
import com.bridgex.integration.domain.MultiBrandCartInput;
import com.bridgex.integration.domain.MultiBrandCartResponse;
import com.bridgex.integration.domain.SizeDataDto;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.impl.DefaultProductFacade;
import de.hybris.platform.commercefacades.storesession.StoreSessionFacade;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;

/**
 * @author Created by konstantin.pavlyukov on 11/8/2017.
 */
public class PentlandProductFacadeImpl extends DefaultProductFacade implements PentlandProductFacade {

  private static final Logger LOG = Logger.getLogger(PentlandProductFacadeImpl.class);

  private OrderSimulationService orderSimulationService;
  private StoreSessionFacade     storeSessionFacade;
  private PentlandB2BUnitService pentlandB2BUnitService;
  private B2BCustomerService<B2BCustomerModel, B2BUnitModel>     customerService;
  private PriceDataFactory priceDataFactory;

  @Override
  public boolean populateCustomerPrice(final ProductData product) {
    if (product.getMaterialKey() == null)
    {
      return false;
    }
    final MultiBrandCartDto request = createSimulateOrderRequest(product);
    final MultiBrandCartResponse response = getOrderSimulationService().simulateOrder(request);
    if (!successResponse(response)) {
      return false;
    }
    final List<MaterialInfoDto> materialList = response.getMaterialInfo();
    if (CollectionUtils.isNotEmpty(materialList)) {
      final MaterialInfoDto responseProduct = materialList.stream().filter(m -> product.getMaterialKey().equals(m.getMaterialNumber())).findFirst().orElse(new MaterialInfoDto());
      final String price = responseProduct.getTotalPrice();
      if (StringUtils.isNotBlank(price)) {
        try {
          final BigDecimal bPrice = BigDecimal.valueOf(Double.valueOf(price));
          product.setCustomerPrice(getPriceDataFactory().create(PriceDataType.BUY, bPrice, getStoreSessionFacade().getCurrentCurrency().getIsocode()));
          return true;
        } catch (NumberFormatException e) {
          LOG.warn("NumberFormatException on response from Order Simulation");
          LOG.debug(e.getMessage());
        }
      }
    }
    return false;
  }

  protected boolean successResponse(final MultiBrandCartResponse response) {
    return response.getEtReturn() == null || (ErpintegrationConstants.RESPONSE.ET_RETURN.SUCCESS_TYPE.equals(response.getEtReturn().getType())) || (ErpintegrationConstants.RESPONSE.ET_RETURN.INFO_TYPE.equals(response.getEtReturn().getType()));
  }

  private MultiBrandCartDto createSimulateOrderRequest(final List<ProductData> products) {
    final MultiBrandCartDto requestRoot = new MultiBrandCartDto();

    // Defaults are set in the dto itself
    //requestRoot.setServiceConsumer();
    //requestRoot.setDocType();

    requestRoot.setLang(getStoreSessionFacade().getCurrentLanguage().getIsocode().toUpperCase());
    requestRoot.setRdd(new Date());
    requestRoot.setPricingCheck(ErpintegrationConstants.REQUEST.DEFAULT_ERP_FLAG_TRUE);

    final List<B2BUnitModel> units = getPentlandB2BUnitService().getCurrentUnits();
    final B2BUnitModel userUnit = units.get(0);

    requestRoot.setSapCustomerID(userUnit.getSapID());

    final List<MultiBrandCartInput> cartInput = new ArrayList<>();

    for (final ProductData product: products) {
      final MultiBrandCartInput reqProduct = createMultiBrandCartInput(product, createBrandUnitsMap(units));
      cartInput.add(reqProduct);
    }

    requestRoot.setCartInput(cartInput);

    return requestRoot;
  }

  private MultiBrandCartDto createSimulateOrderRequest(final ProductData product) {
    return createSimulateOrderRequest(Collections.singletonList(product));
  }

  private MultiBrandCartInput createMultiBrandCartInput(final ProductData product, Map<String, B2BUnitModel> brandUnitsMap) {
    final String brandCode = product.getBrand();
    final MultiBrandCartInput reqProduct = new MultiBrandCartInput();
    reqProduct.setBrandCode(brandCode);
    reqProduct.setMaterialNumber(product.getMaterialKey());

    if(MapUtils.isNotEmpty(brandUnitsMap)) {
      final B2BUnitModel targetUnit = brandUnitsMap.get(brandCode);
      reqProduct.setDistrChannel(targetUnit.getDistCh());
      reqProduct.setSalesOrg(targetUnit.getSalesOrg());
      reqProduct.setPriceList(targetUnit.getSapPriceList());
    }

    final List<SizeDataDto> sizeData = new ArrayList<>();

    final SizeDataDto size = new SizeDataDto();

    size.setEan(product.getCode());
    size.setQuantity("1");

    //to check if required
    //size.setUnit();

    sizeData.add(size);
    reqProduct.setSizeData(sizeData);

    return reqProduct;
  }

  @Nullable
  private Map<String, B2BUnitModel> createBrandUnitsMap(final List<B2BUnitModel> units) {
    final Map<String, B2BUnitModel> result = new HashMap<>();
    if (CollectionUtils.isNotEmpty(units)) {
      units.stream().forEach(u -> {result.put(u.getSapBrand(), u);});
    }
    return result;
  }

  public OrderSimulationService getOrderSimulationService() {
    return orderSimulationService;
  }

  @Required
  public void setOrderSimulationService(OrderSimulationService orderSimulationService) {
    this.orderSimulationService = orderSimulationService;
  }

  public StoreSessionFacade getStoreSessionFacade() {
    return storeSessionFacade;
  }

  @Required
  public void setStoreSessionFacade(StoreSessionFacade storeSessionFacade) {
    this.storeSessionFacade = storeSessionFacade;
  }

  public PentlandB2BUnitService getPentlandB2BUnitService() {
    return pentlandB2BUnitService;
  }

  @Required
  public void setPentlandB2BUnitService(PentlandB2BUnitService pentlandB2BUnitService) {
    this.pentlandB2BUnitService = pentlandB2BUnitService;
  }

  public B2BCustomerService<B2BCustomerModel, B2BUnitModel> getCustomerService() {
    return customerService;
  }

  @Required
  public void setCustomerService(B2BCustomerService<B2BCustomerModel, B2BUnitModel> customerService) {
    this.customerService = customerService;
  }

  public PriceDataFactory getPriceDataFactory() {
    return priceDataFactory;
  }

  @Required
  public void setPriceDataFactory(PriceDataFactory priceDataFactory) {
    this.priceDataFactory = priceDataFactory;
  }
}
