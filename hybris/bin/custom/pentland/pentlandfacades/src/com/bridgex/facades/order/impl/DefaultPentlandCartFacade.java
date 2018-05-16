package com.bridgex.facades.order.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.spockframework.util.Nullable;
import org.springframework.beans.factory.annotation.Required;

import com.bridgex.core.category.PentlandCategoryService;
import com.bridgex.core.model.ApparelSizeVariantProductModel;
import com.bridgex.core.model.ApparelStyleVariantProductModel;
import com.bridgex.core.product.OrderSimulationService;
import com.bridgex.core.services.PentlandB2BUnitService;
import com.bridgex.facades.order.PentlandCartFacade;
import com.bridgex.integration.constants.ErpintegrationConstants;
import com.bridgex.integration.domain.ETReturnDto;
import com.bridgex.integration.domain.FutureStocksDto;
import com.bridgex.integration.domain.MaterialInfoDto;
import com.bridgex.integration.domain.MaterialOutputGridDto;
import com.bridgex.integration.domain.MultiBrandCartDto;
import com.bridgex.integration.domain.MultiBrandCartInput;
import com.bridgex.integration.domain.MultiBrandCartResponse;
import com.bridgex.integration.domain.SizeDataDto;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.impl.DefaultCartFacade;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.VariantMatrixElementData;
import de.hybris.platform.commercefacades.storesession.StoreSessionFacade;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.variants.model.VariantProductModel;
import net.sf.ehcache.search.aggregator.Count;

/**
 * Created by dmitry.konovalov@masterdata.ru on 30.10.2017.
 */
public class DefaultPentlandCartFacade extends DefaultCartFacade implements PentlandCartFacade {

  private static final Logger LOG = Logger.getLogger(DefaultPentlandCartFacade.class);

  private StoreSessionFacade      storeSessionFacade;
  private PentlandB2BUnitService  pentlandB2BUnitService;
  private PentlandCategoryService categoryService;
  private OrderSimulationService  orderSimulationService;

  @Override
  public void saveB2BCartData(CartData cartData) {
    if (hasSessionCart()) {
      final CartModel cart = getCartService().getSessionCart();
      cart.setPurchaseOrderNumber(cartData.getPurchaseOrderNumber());
      cart.setRdd(cartData.getRdd());
      cart.setCustomerNotes(cartData.getCustomerNotes());
      getModelService().save(cart);
    }
  }

  @Override
  public void populateVariantMatrixQuantity(ProductData productData) {
    if (hasSessionCart() && productData.getVariantMatrix() != null) {
      final CartModel cart = getCartService().getSessionCart();
      final Map<String, Long> codeToQuantity = getCodeToQuantityMap(cart);
      VariantMatrixElementData root = getVariantMatrixRoot(productData);
      root.getElements().stream().forEach(e -> populateSizeQuantities(e, codeToQuantity));
    }
  }
  
  @Override
  public  List<String> validateStock() {
	  List<String> validateData = new ArrayList<>();
	  int inStockCount=0;
	  String validateMessage;
	  if (hasSessionCart()) {
		  final CartModel cartModel = getCartService().getSessionCart();
		  Date rdd = cartModel.getRdd();
		  if(rdd != null)
		  {
			  List<Date> listOfDates = new ArrayList<>();
			  if (isCartNotEmpty(cartModel)) {
				  final MultiBrandCartDto request = createSimulateOrderRequest(cartModel);
				  final MultiBrandCartResponse response = getOrderSimulationService().simulateOrder(request);
				  final List<MaterialInfoDto> materialList = response.getMultiBrandCartOutput().getMaterialInfo();
				
				  /* To check that all products stock is 0*/
				  for (MaterialInfoDto materialInfoDto : materialList) {
					  List<MaterialOutputGridDto> materialOutputGridList = materialInfoDto.getMaterialOutputGridList();
					  for (MaterialOutputGridDto materialOutputGridDto : materialOutputGridList) {
						  if(CollectionUtils.isNotEmpty(materialOutputGridDto.getFutureStocksDtoList()))
						  {
							  if(materialOutputGridDto.getFutureStocksDtoList()!=null)
							  {
								  List<FutureStocksDto> futureStocksDtoList = materialOutputGridDto.getFutureStocksDtoList();
								  if(futureStocksDtoList.get(0)!=null)
								  {
									  FutureStocksDto futureStocksDto = futureStocksDtoList.get(0);
									  if(futureStocksDto.getFutureDate()!=null)
									  {
										  Date futureDate = futureStocksDto.getFutureDate();
										  listOfDates.add(futureDate);
									  }
								  }
							  }
						  }
						  if(Double.valueOf(materialOutputGridDto.getAvailableQty())!=0)
						  {
							  inStockCount=inStockCount+1;
						  }
					  }
				  }
				  if(inStockCount!=0)
				  {
					  for (MaterialInfoDto materialInfoDto : materialList) {
						  List<MaterialOutputGridDto> materialOutputGridList = materialInfoDto.getMaterialOutputGridList();
						  for (MaterialOutputGridDto materialOutputGridDto : materialOutputGridList) {
							  if(Double.valueOf(materialOutputGridDto.getUserRequestedQty()) > Double.valueOf(materialOutputGridDto.getAvailableQty()))
							  {
								  String ean = materialOutputGridDto.getEan();
								  if(CollectionUtils.isNotEmpty(materialOutputGridDto.getFutureStocksDtoList()))
								  {
									  if(materialOutputGridDto.getFutureStocksDtoList()!=null)
									  {
										  List<FutureStocksDto> futureStocksDtoList = materialOutputGridDto.getFutureStocksDtoList();
										  if(futureStocksDtoList.get(0)!=null)
										  {
											  FutureStocksDto futureStocksDto = futureStocksDtoList.get(0);
											  if(futureStocksDto.getFutureDate()!=null)
											  {
												  Date futureDate = futureStocksDto.getFutureDate();
												  if(futureDate.compareTo(rdd)>0)
												  {
													  validateMessage=ean+" isn't available for the "+rdd+" and the item will be delivered at "+futureDate;
													  validateData.add(validateMessage);
												  }
											  }
										  }
									  }
								  }
								  else
								  {
									  validateMessage=ean+" isn't available for the "+rdd;
									  validateData.add(validateMessage);
								  }
							  }
						  }
					  }
				  }
				  else
				  {
					  if(CollectionUtils.isNotEmpty(listOfDates))
					  {
						  Date minDate = Collections.min(listOfDates);
						  if(minDate.compareTo(rdd)>0)
						  {
							  validateMessage="Please select RDD on "+minDate+" to Deliver";
							  validateData.add(validateMessage);
						  }
					  }
					  else
					  {
						  validateMessage="Product Stock on RDD not available";
						  validateData.add(validateMessage);
					  }

				  }
			  }
		  }
	  }
	  return validateData;
  }
  
  @Override
  public void populateCart() {
    if (hasSessionCart()) {
      final CartModel cartModel = getCartService().getSessionCart(); 
      if (isCartNotEmpty(cartModel)) {
        final MultiBrandCartDto request = createSimulateOrderRequest(cartModel);
        final MultiBrandCartResponse response = getOrderSimulationService().simulateOrder(request);
        if (successResponse(response)) {
          resetCartPrices(cartModel);
          final List<MaterialInfoDto> materialList = response.getMultiBrandCartOutput().getMaterialInfo();
          if (CollectionUtils.isNotEmpty(materialList)) {
            materialList.forEach(m -> populatePrices(m, cartModel));
          }
          try {
            cartModel.setSubtotal(Double.parseDouble(response.getMultiBrandCartOutput().getSubtotalPrice()));
          } catch (NullPointerException | NumberFormatException ex) {
            cartModel.setSubtotal(0d);
          }
          try {
            cartModel.setTotalPrice(Double.parseDouble(response.getMultiBrandCartOutput().getTotalPrice()));
          } catch (NullPointerException | NumberFormatException ex) {
            cartModel.setTotalPrice(0d);
          }
          try {
            cartModel.setTotalTax(Double.parseDouble(response.getMultiBrandCartOutput().getTotalTaxPrice()));
          } catch (NullPointerException | NumberFormatException ex) {
            cartModel.setTotalTax(0d);
          }
          getModelService().save(cartModel);
        }
      }else{
        cartModel.setTotalTax(0d);
        cartModel.setSubtotal(0d);
        cartModel.setTotalPrice(0d);
        getModelService().save(cartModel);
      }
    }
  }

  private boolean isCartNotEmpty(CartModel cartModel) {
    return cartModel.getEntries().size() > 0;
  }

  private void resetCartPrices(CartModel cartModel) {
    cartModel.getEntries().forEach(e -> {
      e.setErpPrice(0d);
      e.setTotalPrice(0d);
      getModelService().save(e);
    });
  }

  private void populatePrices(MaterialInfoDto material, CartModel cart) {
  /*  AbstractOrderEntryModel entry = cart.getEntries().stream().filter(e -> material.getMaterialNumber().equals(getColorCode(e.getProduct()))).findFirst().orElse(null);*/
    List<AbstractOrderEntryModel> allEntries =cart.getEntries().stream().filter(e -> material.getMaterialNumber().equals(getColorCode(e.getProduct()))).collect(Collectors.<AbstractOrderEntryModel>toList());
   for (AbstractOrderEntryModel abstractOrderEntryModel : allEntries)
   {
	   if (abstractOrderEntryModel != null) {
		      try {
		    	  abstractOrderEntryModel.setErpPrice(Double.parseDouble(material.getUnitPrice()));
		      } catch (NumberFormatException nfe) {
		    	  abstractOrderEntryModel.setErpPrice(0.0);
		      }
		      try {
		    	  if(abstractOrderEntryModel.getTotalPrice()<=0)
		    	  {
		    	    abstractOrderEntryModel.setTotalPrice(Double.parseDouble(material.getTotalPrice()));
		    	  }
		      } catch (NumberFormatException nfe) {
		    	  abstractOrderEntryModel.setErpPrice(0.0);
		      }
		      getModelService().save(abstractOrderEntryModel);
		    }
   }
    
  }

  private String getColorCode(ProductModel productModel) {
    if (productModel instanceof ApparelSizeVariantProductModel) {
      return ((ApparelSizeVariantProductModel) productModel).getBaseProduct().getCode();
    }
    return StringUtils.EMPTY;
  }

  private MultiBrandCartDto createSimulateOrderRequest(CartModel cartModel) {
    final MultiBrandCartDto requestRoot = createRequestRoot(cartModel);
    final Map<String, B2BUnitModel> brandUnitsMap = createBrandUnitsMap(getPentlandB2BUnitService().getCurrentUnits());
    Map<ProductModel, List<AbstractOrderEntryModel>> groupedEntries =
      cartModel.getEntries().stream().collect(Collectors.groupingBy(e -> ((VariantProductModel) e.getProduct()).getBaseProduct()));
    final List<MultiBrandCartInput> cartInput =
      groupedEntries.entrySet().stream().map(groupedEntry -> createMultiBrandCartInput(groupedEntry.getKey(), groupedEntry.getValue(), brandUnitsMap)).collect(Collectors.toList());

    requestRoot.setCartInput(cartInput);

    return requestRoot;
  }

  private MultiBrandCartInput createMultiBrandCartInput(ProductModel material, List<AbstractOrderEntryModel> sizes, Map<String, B2BUnitModel> brandUnitsMap) {
    final MultiBrandCartInput reqProduct = new MultiBrandCartInput();

    final String brandCode = material.getSapBrand();
    reqProduct.setBrandCode(StringUtils.isNotBlank(brandCode) ? brandCode : StringUtils.EMPTY);
    reqProduct.setMaterialNumber(material.getCode());

    if(MapUtils.isNotEmpty(brandUnitsMap)) {
      final B2BUnitModel targetUnit = brandUnitsMap.get(brandCode);
      if (targetUnit != null) {
        reqProduct.setDistrChannel(targetUnit.getDistCh());
        reqProduct.setSalesOrg(targetUnit.getSalesOrg());
      } else {
        LOG.warn("B2BUnit with brand code - " + brandCode + " not found for product - " + material.getCode());
      }
    }

    final List<SizeDataDto> sizeData = new ArrayList<>();

    for(AbstractOrderEntryModel sizeEntry: sizes) {
      final SizeDataDto size = new SizeDataDto();
      size.setEan(sizeEntry.getProduct().getCode());
      size.setQuantity(sizeEntry.getQuantity().toString());
      sizeData.add(size);
    }

    reqProduct.setSizeData(sizeData);

    return reqProduct;
  }

  private MultiBrandCartDto createRequestRoot(CartModel cartModel) {
    final MultiBrandCartDto requestRoot = new MultiBrandCartDto();

    // Defaults are set in the dto itself
    //requestRoot.setServiceConsumer();
    //requestRoot.setDocType();

    requestRoot.setLang(getStoreSessionFacade().getCurrentLanguage().getIsocode().toUpperCase());
    requestRoot.setRdd(Optional.ofNullable(cartModel.getRdd()).orElse(new Date()));
    requestRoot.setPricingCheck(ErpintegrationConstants.REQUEST.DEFAULT_ERP_FLAG_TRUE);
    requestRoot.setAvailabilityCheck(ErpintegrationConstants.REQUEST.DEFAULT_ERP_FLAG_TRUE);
    requestRoot.setCreditCheck(ErpintegrationConstants.REQUEST.DEFAULT_ERP_FLAG_TRUE);

    final List<B2BUnitModel> units = getPentlandB2BUnitService().getCurrentUnits();
    if (CollectionUtils.isNotEmpty(units)) {
      final B2BUnitModel userUnit = units.get(0);
      requestRoot.setSapCustomerID(userUnit.getSapID());
    } else {
      LOG.error("B2BCustomer with id - " + getUserService().getCurrentUser().getUid() + " have no B2BUnit assigned");
    }

    return requestRoot;
  }

  private String getMaterialKeyFor(ProductModel source) {
    if (source instanceof ApparelSizeVariantProductModel) {
      ProductModel styleProduct = ((VariantProductModel) source).getBaseProduct();
      return styleProduct.getCode();
    } else if (source instanceof ApparelStyleVariantProductModel) {
      return source.getCode();
    }
    return StringUtils.EMPTY;
  }

  private String getBrandCodeFor(ProductModel source) {
    CategoryModel brand = getCategoryService().getBrandCategoryForProduct(source);
    if (brand == null) {
      return StringUtils.EMPTY;
    }
    return brand.getCode();
  }

  @Nullable
  private Map<String, B2BUnitModel> createBrandUnitsMap(final List<B2BUnitModel> units) {
    final Map<String, B2BUnitModel> result = new HashMap<>();
    if (CollectionUtils.isNotEmpty(units)) {
      units.stream().forEach(u -> {result.put(u.getSapBrand(), u);});
    }
    return result;
  }

  protected boolean successResponse(final MultiBrandCartResponse response) {
    boolean result = true;
    final List<ETReturnDto> returnList = response.getEtReturnList();
    if (CollectionUtils.isNotEmpty(returnList)) {
      for (final ETReturnDto returnDto : returnList) {
        if (ErpintegrationConstants.RESPONSE.ET_RETURN.SUCCESS_TYPE.equals(returnDto.getType())) {
          return true;
        }
        if (ErpintegrationConstants.RESPONSE.ET_RETURN.ERROR_TYPE.equals(returnDto.getType()) || ErpintegrationConstants.RESPONSE.ET_RETURN.WARNING_TYPE.equals(returnDto.getType())) {
          result = false;
        }
      }
    }
    return result;
  }

  protected void populateSizeQuantities(VariantMatrixElementData colorLevelVariantData, final Map<String, Long> codeToQuantity) {
    colorLevelVariantData.getElements().stream().filter(e -> codeToQuantity.containsKey(e.getVariantOption().getCode()))
                         .forEach(e -> e.setQty(codeToQuantity.get(e.getVariantOption().getCode())));
  }

  protected Map<String, Long> getCodeToQuantityMap(CartModel cart) {
    return cart.getEntries().stream().collect(Collectors.toMap(e -> e.getProduct().getCode(), e -> e.getQuantity()));
  }

  protected VariantMatrixElementData getVariantMatrixRoot(ProductData productData) {
    return productData.getVariantMatrix().get(0);
  }

  protected StoreSessionFacade getStoreSessionFacade() {
    return storeSessionFacade;
  }

  @Required
  public void setStoreSessionFacade(StoreSessionFacade storeSessionFacade) {
    this.storeSessionFacade = storeSessionFacade;
  }

  protected PentlandB2BUnitService getPentlandB2BUnitService() {
    return pentlandB2BUnitService;
  }

  @Required
  public void setPentlandB2BUnitService(PentlandB2BUnitService pentlandB2BUnitService) {
    this.pentlandB2BUnitService = pentlandB2BUnitService;
  }

  protected PentlandCategoryService getCategoryService() {
    return categoryService;
  }

  @Required
  public void setCategoryService(PentlandCategoryService categoryService) {
    this.categoryService = categoryService;
  }

  protected OrderSimulationService getOrderSimulationService() {
    return orderSimulationService;
  }

  @Required
  public void setOrderSimulationService(OrderSimulationService orderSimulationService) {
    this.orderSimulationService = orderSimulationService;
  }

}
