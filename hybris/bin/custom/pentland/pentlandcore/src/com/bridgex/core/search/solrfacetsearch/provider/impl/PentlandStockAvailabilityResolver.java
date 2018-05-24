package com.bridgex.core.search.solrfacetsearch.provider.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import com.bridgex.core.product.OrderSimulationService;
import com.bridgex.core.services.PentlandB2BUnitService;
import com.bridgex.integration.constants.ErpintegrationConstants;
import com.bridgex.integration.domain.MaterialInfoDto;
import com.bridgex.integration.domain.MaterialOutputGridDto;
import com.bridgex.integration.domain.MultiBrandCartDto;
import com.bridgex.integration.domain.MultiBrandCartInput;
import com.bridgex.integration.domain.MultiBrandCartOutput;
import com.bridgex.integration.domain.MultiBrandCartResponse;
import com.bridgex.integration.domain.SizeDataDto;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.variants.model.VariantProductModel;

public class PentlandStockAvailabilityResolver extends AbstractBaseProductValueResolver
{
	private static final Logger LOG = Logger.getLogger(PentlandStockAvailabilityResolver.class);
	
	
	private PentlandB2BUnitService pentlandB2BUnitService;
	private UserService userService;
	private OrderSimulationService orderSimulationService;
	private ConfigurationService configurationService;
	private I18NService i18nService;

	@Override
	protected void addFieldValues(InputDocument inputDocument, IndexerBatchContext indexerBatchContext, IndexedProperty indexedProperty, ProductModel productModel,
			ValueResolverContext<Object, Object> valueResolverContext)
			throws FieldValueProviderException 
	{
		ProductModel baseProductModel = getBaseProductModel(productModel);
		
		final MultiBrandCartDto request = createOrderFormRequest(baseProductModel, new Date());
		final MultiBrandCartResponse response = getOrderSimulationService().simulateOrder(request);
		MultiBrandCartOutput multiBrandCartOutput = response.getMultiBrandCartOutput();
		
		List<MaterialInfoDto> materialInfo = multiBrandCartOutput.getMaterialInfo();
		List<MaterialInfoDto> materialInfoDtos = materialInfo.stream().filter(m->m.getMaterialNumber().equals(baseProductModel.getCode())).collect(Collectors.<MaterialInfoDto>toList());
		if(CollectionUtils.isNotEmpty(materialInfoDtos))
		{
		List<Long> styleVariantStock=new ArrayList<>();
		MaterialInfoDto materialInfoDto=materialInfoDtos.get(0);
		List<MaterialOutputGridDto> materialOutputGridDto= materialInfoDto.getMaterialOutputGridList();
		
		for (MaterialOutputGridDto materialOutputGridDto2 : materialOutputGridDto)
		{
			long stock=(long) Double.parseDouble(materialOutputGridDto2.getAvailableQty());
			if(stock>0)
			{
				styleVariantStock.add((long) Double.parseDouble(materialOutputGridDto2.getAvailableQty()));
			}
		}
		
		if(CollectionUtils.isNotEmpty(styleVariantStock))
		{
			inputDocument.addField(indexedProperty, new String("in stock"), valueResolverContext.getFieldQualifier());
		}
		}
		
	}
	
	private MultiBrandCartDto createOrderFormRequest(final ProductModel product, Date requestedDeliveryDate)
	{
		
		final MultiBrandCartDto requestRoot = createRequestRoot(requestedDeliveryDate, true, false);
		
		B2BCustomerModel testCustomer = getTestCustomer();
		final List<B2BUnitModel> units = getPentlandB2BUnitService().getUsersB2BUnits(testCustomer);
	    final List<MultiBrandCartInput> cartInput = new ArrayList<>();

	      final MultiBrandCartInput reqProduct = createMultiBrandCartInput(product, createBrandUnitsMap(units));
	      cartInput.add(reqProduct);

	    requestRoot.setCartInput(cartInput);
		
		return requestRoot;
	}
	
	public MultiBrandCartDto createRequestRoot(final Date requestedDeliveryDate, boolean availabilityCheck, boolean creditCheck) {
	    final MultiBrandCartDto requestRoot = new MultiBrandCartDto();
	    
	    B2BCustomerModel testCustomer = getTestCustomer();
	    LanguageModel sessionLanguage = testCustomer.getSessionLanguage();
	    
	    requestRoot.setLang(sessionLanguage.getIsocode().toUpperCase());
	    requestRoot.setRdd(requestedDeliveryDate);
	    requestRoot.setPricingCheck(ErpintegrationConstants.REQUEST.DEFAULT_ERP_FLAG_TRUE);
	    requestRoot.setAvailabilityCheck(availabilityCheck ? ErpintegrationConstants.REQUEST.DEFAULT_ERP_FLAG_TRUE : StringUtils.EMPTY);
	    requestRoot.setCreditCheck(creditCheck ? ErpintegrationConstants.REQUEST.DEFAULT_ERP_FLAG_TRUE : StringUtils.EMPTY);

	    final List<B2BUnitModel> units = getPentlandB2BUnitService().getUsersB2BUnits(testCustomer);
	    if (CollectionUtils.isNotEmpty(units)) {
	      final B2BUnitModel userUnit = units.get(0);
	      requestRoot.setSapCustomerID(userUnit.getSapID());
	    } else {
	      LOG.error("B2BCustomer with id - " + testCustomer.getUid() + " have no B2BUnit assigned");
	    }
	    return requestRoot;
	  }
	
	private B2BCustomerModel getTestCustomer()
	{
		String userForIndex = getConfigurationService().getConfiguration().getString("test.user.index");
		UserModel userModel = getUserService().getUserForUID(userForIndex);
	    B2BCustomerModel testCustomer=(B2BCustomerModel)userModel;
	    return testCustomer;
	}
	
	 private Map<String, B2BUnitModel> createBrandUnitsMap(final List<B2BUnitModel> units) {
		    final Map<String, B2BUnitModel> result = new HashMap<>();
		    if (CollectionUtils.isNotEmpty(units)) {
		      units.stream().forEach(u -> {result.put(u.getSapBrand(), u);});
		    }
		    return result;
		  }
	 
	 private MultiBrandCartInput createMultiBrandCartInput(final ProductModel product, Map<String, B2BUnitModel> brandUnitsMap) {
		 
		 Collection<VariantProductModel> variants = product.getVariants();
		 
		    final String brandCode = product.getSapBrand();
		    final MultiBrandCartInput reqProduct = new MultiBrandCartInput();
		    reqProduct.setBrandCode(StringUtils.isNotBlank(brandCode) ? brandCode : StringUtils.EMPTY);
		    reqProduct.setMaterialNumber(product.getCode());

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
		    for (final VariantProductModel size : variants) {
		      final SizeDataDto sizeReq = new SizeDataDto();

		      sizeReq.setEan(size.getCode());
		      sizeReq.setQuantity("0");

		      //to check if required
		      //size.setUnit();

		      sizeData.add(sizeReq);
		    }
		    reqProduct.setSizeData(sizeData);

		    return reqProduct;
		  }
	 


	public PentlandB2BUnitService getPentlandB2BUnitService() {
		return pentlandB2BUnitService;
	}
	
	 @Required
	public void setPentlandB2BUnitService(PentlandB2BUnitService pentlandB2BUnitService) {
		this.pentlandB2BUnitService = pentlandB2BUnitService;
	}

	public UserService getUserService() {
		return userService;
	}

	 @Required
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public OrderSimulationService getOrderSimulationService() {
		return orderSimulationService;
	}
	
	 @Required
	public void setOrderSimulationService(OrderSimulationService orderSimulationService) {
		this.orderSimulationService = orderSimulationService;
	}

	public I18NService getI18nService() {
		return i18nService;
	}

	@Required
	public void setI18nService(I18NService i18nService) {
		this.i18nService = i18nService;
	}

	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	@Required
	public void setConfigurationService(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}
	
	
	
	

}


