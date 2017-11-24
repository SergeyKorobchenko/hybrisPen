package com.bridgex.facades.product.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.bridgex.integration.domain.ETReturnDto;
import com.bridgex.integration.domain.MaterialInfoDto;
import com.bridgex.integration.domain.MaterialOutputGridDto;
import com.bridgex.integration.domain.MultiBrandCartDto;
import com.bridgex.integration.domain.MultiBrandCartInput;
import com.bridgex.integration.domain.MultiBrandCartOutput;
import com.bridgex.integration.domain.MultiBrandCartResponse;
import com.bridgex.integration.domain.SizeDataDto;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.StockData;
import de.hybris.platform.commercefacades.product.data.VariantMatrixElementData;
import de.hybris.platform.commercefacades.product.impl.DefaultProductFacade;
import de.hybris.platform.commercefacades.storesession.StoreSessionFacade;

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
  public boolean populateCustomerPrice(ProductData productData) {
    return populateCustomerPrice(productData, new Date());
  }

  @Override
  public boolean populateCustomerPrice(final ProductData product, Date requestedDeliveryDate) {
    if (product.getMaterialKey() == null)
    {
      return false;
    }
    final MultiBrandCartDto request = createSimulateOrderRequest(product, requestedDeliveryDate);
    final MultiBrandCartResponse response = getOrderSimulationService().simulateOrder(request);
    if (!successResponse(response)) {
      return false;
    }
    final MultiBrandCartOutput multiBrandCartOutput = response.getMultiBrandCartOutput();
    if (multiBrandCartOutput == null) {
      return false;
    }
    final List<MaterialInfoDto> materialList = multiBrandCartOutput.getMaterialInfo();
    if (CollectionUtils.isNotEmpty(materialList)) {
      final MaterialInfoDto responseProduct = materialList.stream().filter(m -> product.getMaterialKey().equals(m.getMaterialNumber())).findFirst().orElse(new MaterialInfoDto());
      final String price = responseProduct.getUnitPrice();
      if (StringUtils.isNotBlank(price)) {
        try {
          final BigDecimal bPrice = BigDecimal.valueOf(Double.valueOf(price));
          product.setCustomerPrice(getPriceDataFactory().create(PriceDataType.BUY, bPrice, getStoreSessionFacade().getCurrentCurrency().getIsocode()));
          return true;
        } catch (NumberFormatException e) {
          LOG.warn("NumberFormatException on response from Order Simulation");
          LOG.debug(e.getMessage());
        } catch (NullPointerException npe) {
          LOG.warn("NullPointerException on response from Order Simulation. Price is empty.");
          LOG.debug(npe.getMessage());
        }
      }
    }
    return false;
  }

  @Override
  public boolean populateOrderForm(ProductData product) {
    return populateOrderForm(product, new Date());
  }

  @Override
  public boolean populateOrderForm(final ProductData product, Date requestedDeliveryDate) {
    if (CollectionUtils.isEmpty(product.getVariantMatrix())) {
      return false;
    }
    final VariantMatrixElementData root = product.getVariantMatrix().get(0);

    final List<VariantMatrixElementData> materials = root.getElements();
    final MultiBrandCartDto request = createOrderFormRequest(materials, requestedDeliveryDate);
    final MultiBrandCartResponse response = getOrderSimulationService().simulateOrder(request);
    if (!successResponse(response)) {
      return false;
    }
    final MultiBrandCartOutput multiBrandCartOutput = response.getMultiBrandCartOutput();
    if (multiBrandCartOutput == null) {
      return false;
    }
    final List<MaterialInfoDto> matListResp = multiBrandCartOutput.getMaterialInfo();
    if (CollectionUtils.isEmpty(matListResp)) {
      return false;
    }
    final List<VariantMatrixElementData> resultMatList = new ArrayList<>();
    for (final MaterialInfoDto material : matListResp) {
      final String matCode = material.getMaterialNumber();
      final VariantMatrixElementData rMat = materials.stream().filter(tm -> tm.getVariantOption().getCode().equals(matCode)).findFirst().orElse(null);
      if (rMat == null) {
        continue;
      }
      final String price = material.getUnitPrice();
      if (StringUtils.isNotBlank(price)) {
        try {
          final BigDecimal bPrice = BigDecimal.valueOf(Double.valueOf(price));
          rMat.getVariantOption().setPriceData(getPriceDataFactory().create(PriceDataType.BUY, bPrice, getStoreSessionFacade().getCurrentCurrency().getIsocode()));
        } catch (NumberFormatException e) {
          LOG.warn("NumberFormatException on response from Order Simulation");
          LOG.debug(e.getMessage());
        } catch (NullPointerException npe) {
          LOG.warn("NullPointerException on response from Order Simulation. Price is empty.");
          LOG.debug(npe.getMessage());
        }
      }

      final List<VariantMatrixElementData> resultSizeList = new ArrayList<>();

      for(final MaterialOutputGridDto size: material.getMaterialOutputGridList()) {
        final String ean = size.getEan();
        final VariantMatrixElementData rSize = rMat.getElements().stream().filter(rs -> rs.getVariantOption().getCode().equals(ean)).findFirst().orElse(null);
        if (StringUtils.isNotBlank(size.getAvailableQty())) {
          final StockData stockData = new StockData();
          stockData.setStockLevel(Long.valueOf(size.getAvailableQty()));
          // todo remove hardcode
          stockData.setStockThreshold(100);
          rSize.getVariantOption().setStock(stockData);
        }
        resultSizeList.add(rSize);
      }
      rMat.setElements(resultSizeList);
      resultMatList.add(rMat);
    }

    product.getVariantMatrix().get(0).setElements(resultMatList);
    return true;
  }

  private MultiBrandCartDto createOrderFormRequest(final List<VariantMatrixElementData> materials, Date requestedDeliveryDate) {
    final MultiBrandCartDto requestRoot = createRequestRoot(requestedDeliveryDate, true, false);

    final List<B2BUnitModel> units = getPentlandB2BUnitService().getCurrentUnits();
    final List<MultiBrandCartInput> cartInput = new ArrayList<>();

    for (final VariantMatrixElementData product: materials) {
      final MultiBrandCartInput reqProduct = createMultiBrandCartInput(product, createBrandUnitsMap(units));
      cartInput.add(reqProduct);
    }

    requestRoot.setCartInput(cartInput);

    return requestRoot;
  }

  protected boolean successResponse(final MultiBrandCartResponse response) {
    boolean result = true;
    final List<ETReturnDto> returnList = response.getEtReturnList();
    if (CollectionUtils.isNotEmpty(returnList)) {
      for (final ETReturnDto returnDto : returnList) {
        if (ErpintegrationConstants.RESPONSE.ET_RETURN.SUCCESS_TYPE.equals(returnDto.getType())) {
          return true;
        }
        if (ErpintegrationConstants.RESPONSE.ET_RETURN.ERROR_TYPE.equals(returnDto.getType())) {
          result = false;
        }
      }
    }
    return result;
  }

  private MultiBrandCartDto createSimulateOrderRequest(final List<ProductData> products, Date requestedDeliveryDate) {
    final MultiBrandCartDto requestRoot = createRequestRoot(requestedDeliveryDate, true, true);

    final List<B2BUnitModel> units = getPentlandB2BUnitService().getCurrentUnits();
    final List<MultiBrandCartInput> cartInput = new ArrayList<>();

    for (final ProductData product: products) {
      final MultiBrandCartInput reqProduct = createMultiBrandCartInput(product, createBrandUnitsMap(units));
      cartInput.add(reqProduct);
    }

    requestRoot.setCartInput(cartInput);

    return requestRoot;
  }

  private MultiBrandCartDto createRequestRoot(final Date requestedDeliveryDate, boolean availabilityCheck, boolean creditCheck) {
    final MultiBrandCartDto requestRoot = new MultiBrandCartDto();

    // Defaults are set in the dto itself
    //requestRoot.setServiceConsumer();
    //requestRoot.setDocType();

    requestRoot.setLang(getStoreSessionFacade().getCurrentLanguage().getIsocode().toUpperCase());
    requestRoot.setRdd(requestedDeliveryDate);
    requestRoot.setPricingCheck(ErpintegrationConstants.REQUEST.DEFAULT_ERP_FLAG_TRUE);
    requestRoot.setAvailabilityCheck(availabilityCheck ? ErpintegrationConstants.REQUEST.DEFAULT_ERP_FLAG_TRUE : StringUtils.EMPTY);
    requestRoot.setCreditCheck(creditCheck ? ErpintegrationConstants.REQUEST.DEFAULT_ERP_FLAG_TRUE : StringUtils.EMPTY);

    final List<B2BUnitModel> units = getPentlandB2BUnitService().getCurrentUnits();
    final B2BUnitModel userUnit = units.get(0);

    requestRoot.setSapCustomerID(userUnit.getSapID());

    return requestRoot;
  }

  private MultiBrandCartDto createSimulateOrderRequest(final ProductData product, Date requestedDeliveryDate) {
    return createSimulateOrderRequest(Collections.singletonList(product), requestedDeliveryDate);
  }
  private MultiBrandCartInput createMultiBrandCartInput(final VariantMatrixElementData product, Map<String, B2BUnitModel> brandUnitsMap) {
    final List<VariantMatrixElementData> sizes = product.getElements();
    final String brandCode = product.getVariantOption().getBrandCode();
    final MultiBrandCartInput reqProduct = new MultiBrandCartInput();
    reqProduct.setBrandCode(brandCode);
    reqProduct.setMaterialNumber(product.getVariantOption().getCode());

    if(MapUtils.isNotEmpty(brandUnitsMap)) {
      final B2BUnitModel targetUnit = brandUnitsMap.get(brandCode);
      if (targetUnit != null) {
        reqProduct.setDistrChannel(targetUnit.getDistCh());
        reqProduct.setSalesOrg(targetUnit.getSalesOrg());
      } else {
        LOG.warn("B2BUnit with brand code - " + brandCode + " not found for product - " + product.getVariantOption().getCode());
      }
    }

    final List<SizeDataDto> sizeData = new ArrayList<>();
    for (final VariantMatrixElementData size : sizes) {
      final SizeDataDto sizeReq = new SizeDataDto();

      sizeReq.setEan(size.getVariantOption().getCode());
      sizeReq.setQuantity(size.getQty() == null ? "0" : String.valueOf(size.getQty()));

      //to check if required
      //size.setUnit();

      sizeData.add(sizeReq);
    }
    reqProduct.setSizeData(sizeData);

    return reqProduct;
  }

  private MultiBrandCartInput createMultiBrandCartInput(final ProductData product, Map<String, B2BUnitModel> brandUnitsMap) {
    final String brandCode = product.getBrandCode();
    final MultiBrandCartInput reqProduct = new MultiBrandCartInput();
    reqProduct.setBrandCode(brandCode);
    reqProduct.setMaterialNumber(product.getMaterialKey());

    if(MapUtils.isNotEmpty(brandUnitsMap)) {
      final B2BUnitModel targetUnit = brandUnitsMap.get(brandCode);
      if (targetUnit != null) {
        reqProduct.setDistrChannel(targetUnit.getDistCh());
        reqProduct.setSalesOrg(targetUnit.getSalesOrg());
      } else {
        LOG.warn("B2BUnit with brand code - " + brandCode + " not found for product - " + product.getCode());
      }
    }

    final List<SizeDataDto> sizeData = new ArrayList<>();

    final SizeDataDto size = new SizeDataDto();

    size.setEan(product.getCode());
    size.setQuantity("0");

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
