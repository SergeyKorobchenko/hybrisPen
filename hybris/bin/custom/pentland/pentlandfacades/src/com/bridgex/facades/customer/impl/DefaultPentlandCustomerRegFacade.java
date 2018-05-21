/**
 * 
 */
package com.bridgex.facades.customer.impl;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;
import com.bridgex.facades.customer.PentlandCustomerRegFacade;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.commercefacades.user.data.RegisterData;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;
import de.hybris.platform.commerceservices.event.RegisterEvent;
import de.hybris.platform.commerceservices.strategies.CustomerNameStrategy;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.impl.UniqueAttributesInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;

/**
 * @author santoshi
 *
 */
public class DefaultPentlandCustomerRegFacade implements PentlandCustomerRegFacade{
  
 private ModelService modelService;
 private EventService eventService;
 private BaseStoreService baseStoreService;
 private BaseSiteService baseSiteService;
 private CommonI18NService commonI18NService;
 private I18NService i18nService;
 
 @Resource(name="customerNameStrategy")
 private CustomerNameStrategy customerNameStrategy;
 
 @Override
 public void saveFormData(RegisterData registerData) throws DuplicateUidException
 {
  
  validateParameterNotNullStandardMessage("registerData", registerData);
  //Assert.hasText(registerData.getFirstName(), "The field [FirstName] cannot be empty");
  //Assert.hasText(registerData.getLastName(), "The field [LastName] cannot be empty");  
  CustomerModel customerModel = getModelService().create(CustomerModel.class);
  customerModel.setName(customerNameStrategy.getName(registerData.getFirstName(), registerData.getLastName()));
  if (StringUtils.isNotBlank(registerData.getFirstName()) && StringUtils.isNotBlank(registerData.getLastName()))
  {
   customerModel.setName(customerNameStrategy.getName(registerData.getFirstName(), registerData.getLastName()));
  }
  customerModel.setPosition(registerData.getPosition());
  customerModel.setAccessRequired(registerData.getAccessRequired());
  customerModel.setAccountNumber(registerData.getAccountNumber());
  setUidForRegister(registerData, customerModel);
  try
  {
   getModelService().save(customerModel);
  }
  catch (final ModelSavingException e)
  {
   if (e.getCause() instanceof InterceptorException
     && ((InterceptorException) e.getCause()).getInterceptor().getClass().equals(UniqueAttributesInterceptor.class))
   {
    throw new DuplicateUidException(customerModel.getUid(), e);
   }
   else
   {
    throw e;
   }
  }
  catch (final AmbiguousIdentifierException e)
  {
   throw new DuplicateUidException(customerModel.getUid(), e);
  }
  /*modelService.save(customerModel);*/ 
  getEventService().publishEvent(initializeEvent(new RegisterEvent(), customerModel));
 }
 
 

 @SuppressWarnings({ "unchecked", "rawtypes" })
 protected AbstractCommerceUserEvent initializeEvent(final AbstractCommerceUserEvent event, final CustomerModel customerModel)
 {
  event.setBaseStore(getBaseStoreService().getCurrentBaseStore());
  event.setSite(getBaseSiteService().getCurrentBaseSite());
  event.setCustomer(customerModel);
  event.setLanguage(getCommonI18NService().getCurrentLanguage());
  event.setCurrency(getCommonI18NService().getCurrentCurrency());
  return event;
 }

 protected void setUidForRegister(final RegisterData registerData, final CustomerModel customer)
 {
  customer.setUid(registerData.getUserEmail().toLowerCase());
  customer.setOriginalUid(registerData.getUserEmail());
 }
 
 public ModelService getModelService() {
  return modelService;
 }
 @Required
 public void setModelService(ModelService modelService) {
  this.modelService = modelService;
 }

 public EventService getEventService() {
  return eventService;
 }
 @Required
 public void setEventService(EventService eventService) {
  this.eventService = eventService;
 }

 public BaseStoreService getBaseStoreService() {
  return baseStoreService;
 }
 @Required
 public void setBaseStoreService(BaseStoreService baseStoreService) {
  this.baseStoreService = baseStoreService;
 }

 public BaseSiteService getBaseSiteService() {
  return baseSiteService;
 }
 @Required
 public void setBaseSiteService(BaseSiteService baseSiteService) {
  this.baseSiteService = baseSiteService;
 }

 public CommonI18NService getCommonI18NService() {
  return commonI18NService;
 }
 @Required
 public void setCommonI18NService(CommonI18NService commonI18NService) {
  this.commonI18NService = commonI18NService;
 }

 public I18NService getI18nService() {
  return i18nService;
 }
 @Required
 public void setI18nService(I18NService i18nService) {
  this.i18nService = i18nService;
 }

 
}
