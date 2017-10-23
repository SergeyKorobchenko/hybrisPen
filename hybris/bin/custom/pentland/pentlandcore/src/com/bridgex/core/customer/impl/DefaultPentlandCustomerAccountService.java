package com.bridgex.core.customer.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Required;

import com.bridgex.core.customer.PentlandCustomerAccountService;
import com.bridgex.core.customer.dao.PentlandCustomerAccountDao;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commerceservices.customer.impl.DefaultCustomerAccountService;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.store.BaseStoreModel;

/**
 * Created by dmitry.konovalov@masterdata.ru on 18.10.2017.
 */
public class DefaultPentlandCustomerAccountService extends DefaultCustomerAccountService implements PentlandCustomerAccountService {

  private PentlandCustomerAccountDao pentlandCustomerAccountDao;

  @Override
  public SearchPageData<OrderModel> getB2BOrderList(final CustomerModel customerModel, final BaseStoreModel store, final OrderStatus[] status, final PageableData pageableData)
  {
    validateParameterNotNull(customerModel, "Customer model cannot be null");
    validateParameterNotNull(store, "Store must not be null");
    validateParameterNotNull(pageableData, "PageableData must not be null");
    Set<B2BUnitModel> b2BUnitModels = getCustomerB2BUnits(customerModel);
    return getPentlandCustomerAccountDao().findOrdersByB2BUnitsAndStore(b2BUnitModels, store, status, pageableData);
  }

  protected PentlandCustomerAccountDao getPentlandCustomerAccountDao() {
    return pentlandCustomerAccountDao;
  }

  @Required
  public void setPentlandCustomerAccountDao(PentlandCustomerAccountDao pentlandCustomerAccountDao) {
    this.pentlandCustomerAccountDao = pentlandCustomerAccountDao;
  }

  protected Set<B2BUnitModel> getCustomerB2BUnits(CustomerModel customerModel) {
    return customerModel.getGroups().stream().filter(g -> g instanceof B2BUnitModel).map(g -> (B2BUnitModel)g).collect(Collectors.toSet());
  }

}
