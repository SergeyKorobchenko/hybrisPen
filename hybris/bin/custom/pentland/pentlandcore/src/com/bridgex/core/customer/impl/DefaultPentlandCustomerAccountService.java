package com.bridgex.core.customer.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import com.bridgex.core.customer.PentlandCustomerAccountService;
import com.bridgex.core.customer.dao.PentlandCustomerAccountDao;
import com.bridgex.core.services.PentlandB2BUnitService;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commerceservices.customer.impl.DefaultCustomerAccountService;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.store.BaseStoreModel;

/**
 * Created by dmitry.konovalov@masterdata.ru on 18.10.2017.
 */
public class DefaultPentlandCustomerAccountService extends DefaultCustomerAccountService implements PentlandCustomerAccountService {

  private PentlandCustomerAccountDao pentlandCustomerAccountDao;
  
  private PentlandB2BUnitService b2BUnitService;

  @Override
  public SearchPageData<OrderModel> getB2BOrderList(final CustomerModel customerModel, final BaseStoreModel store, final OrderStatus[] status, final PageableData pageableData)
  {
    validateParameterNotNull(customerModel, "Customer model cannot be null");
    validateParameterNotNull(store, "Store must not be null");
    validateParameterNotNull(pageableData, "PageableData must not be null");
    List<B2BUnitModel> b2BUnitModels = getCustomerB2BUnits(customerModel);
    return getPentlandCustomerAccountDao().findOrdersByB2BUnitsAndStore(b2BUnitModels, store, status, pageableData);
  }

  protected PentlandCustomerAccountDao getPentlandCustomerAccountDao() {
    return pentlandCustomerAccountDao;
  }

  @Required
  public void setPentlandCustomerAccountDao(PentlandCustomerAccountDao pentlandCustomerAccountDao) {
    this.pentlandCustomerAccountDao = pentlandCustomerAccountDao;
  }

  protected List<B2BUnitModel> getCustomerB2BUnits(CustomerModel customerModel) {
    return customerModel.getGroups().stream().filter(g -> g instanceof B2BUnitModel).map(g -> (B2BUnitModel)g).collect(Collectors.toList());
  }

@Override
public List<AddressModel> getDeliveryAddressesForCustomer(UserModel currentCustomer) {

	final Set<AddressModel> addressesCustomer = new HashSet<>();
	   if (currentCustomer instanceof B2BCustomerModel) {

	        B2BCustomerModel b2bCustomer = (B2BCustomerModel) currentCustomer;
	        Collection<B2BUnitModel> firstLevelParents = b2BUnitService.getFirstLevelParents(b2bCustomer);

	        if(CollectionUtils.isNotEmpty(firstLevelParents)){
	          for(B2BUnitModel b2BUnit: firstLevelParents){
	           addressesCustomer.addAll(b2BUnitService.findDeliveryAddressesForUnits(b2BUnit));
	          }
	        }
	      }

	      return new ArrayList<>(addressesCustomer);
}

@Required
public void setB2BUnitService(PentlandB2BUnitService b2BUnitService) {
  this.b2BUnitService = b2BUnitService;
}

}
