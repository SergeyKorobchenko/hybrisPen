package com.bridgex.core.strategies.impl;

import java.util.*;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import com.bridgex.core.services.PentlandB2BUnitService;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2bacceleratorservices.strategies.impl.DefaultB2BDeliveryAddressesLookupStrategy;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 10/30/2017.
 */
public class PentlandB2BDeliveryAddressesLookupStrategy extends DefaultB2BDeliveryAddressesLookupStrategy {

  private PentlandB2BUnitService b2BUnitService;

  @Override
  public List<AddressModel> getDeliveryAddressesForOrder(final AbstractOrderModel abstractOrder, final boolean visibleAddressesOnly) {

    //collect to set to avoid duplicates
    final Set<AddressModel> addressesForOrder = new HashSet<>();

    final UserModel user = abstractOrder.getUser();
    if (user instanceof B2BCustomerModel) {

      B2BCustomerModel b2bCustomer = (B2BCustomerModel) user;
      Collection<B2BUnitModel> firstLevelParents = b2BUnitService.getFirstLevelParents(b2bCustomer);

      if(CollectionUtils.isNotEmpty(firstLevelParents)){
        for(B2BUnitModel b2BUnit: firstLevelParents){
         addressesForOrder.addAll(b2BUnitService.findDeliveryAddressesForUnits(b2BUnit));
        }
      }
    }

    return new ArrayList<>(addressesForOrder);
  }

  @Required
  public void setB2BUnitService(PentlandB2BUnitService b2BUnitService) {
    this.b2BUnitService = b2BUnitService;
  }
}
