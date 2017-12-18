package com.bridgex.core.address.dao.impl;

import java.util.List;

import com.bridgex.core.address.dao.PentlandAddressDao;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.user.daos.impl.DefaultAddressDao;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 10/30/2017.
 */
public class DefaultPentlandAddressDao extends DefaultAddressDao implements PentlandAddressDao{
  @Override
  public List<AddressModel> findDeliveryAddressesForSapId(B2BUnitModel b2BUnitModel) {
    StringBuilder queryBuilder = new StringBuilder("select {pk} from {");
    queryBuilder.append(AddressModel._TYPECODE).append("} where ");
    queryBuilder.append("{").append(AddressModel.SAPCUSTOMERID).append("}=?sapId ");
    queryBuilder.append("and {").append(AddressModel.VISIBLEINADDRESSBOOK).append("}=?visible ");
    queryBuilder.append("and {").append(AddressModel.SHIPPINGADDRESS).append("}=?shippingAddress ");
    queryBuilder.append("and {").append(AddressModel.MARKFORADDRESS).append("}=?markForAddress ");

    FlexibleSearchQuery query = new FlexibleSearchQuery(queryBuilder.toString());
    query.addQueryParameter("sapId", b2BUnitModel.getSapID());
    query.addQueryParameter("visible", Boolean.TRUE);
    query.addQueryParameter("shippingAddress", Boolean.TRUE);
    query.addQueryParameter("markForAddress", Boolean.FALSE);

    return getFlexibleSearchService().<AddressModel>search(query).getResult();
  }
}
