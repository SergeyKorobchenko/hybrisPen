package com.bridgex.core.services.impl;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import com.bridgex.core.address.dao.PentlandAddressDao;
import com.bridgex.core.constants.PentlandcoreConstants;
import com.bridgex.core.services.PentlandB2BUnitService;

import de.hybris.platform.b2b.constants.B2BConstants;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.impl.DefaultB2BUnitService;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.europe1.constants.Europe1Constants;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;

/**
 * Created by Lenar on 10/13/2017.
 */
public class DefaultPentlandB2BUnitService extends DefaultB2BUnitService implements PentlandB2BUnitService {

  private PentlandAddressDao addressDao;

  @Override
  public void updateBranchInSession(final Session session, final UserModel currentUser)
  {
    if (currentUser instanceof B2BCustomerModel)
    {
      final Object[] branchInfo = getSessionService().executeInLocalView(new SessionExecutionBody()
      {
        @Override
        public Object[] execute()
        {
          getSearchRestrictionService().disableSearchRestrictions();
          final B2BCustomerModel currentCustomer = (B2BCustomerModel) currentUser;
          final List<B2BUnitModel> unitsOfCustomer = getParents(currentCustomer);

          List<Object> userPriceGroups = unitsOfCustomer.stream()
                                                        .filter(u -> u.getUserPriceGroup() != null)
                                                        .map(u -> getTypeService().getEnumerationValue(u.getUserPriceGroup()))
                                                        .collect(Collectors.toList());

          List<Object> parentUserPriceGroups = new ArrayList<>();

          unitsOfCustomer.forEach(unit -> {
            final List<B2BUnitModel> groups = unit.getGroups().stream().filter(B2BUnitModel.class::isInstance).map(u -> (B2BUnitModel) u).collect(Collectors.toList());
            groups.stream().filter(g -> g.getUserPriceGroup() != null)
                  .map(g -> getTypeService().getEnumerationValue(g.getUserPriceGroup())).sequential()
                  .collect(Collectors.toCollection(() -> parentUserPriceGroups));
          });

          return new Object[]
            { getRootUnit(unitsOfCustomer.get(0)), getBranch(unitsOfCustomer.get(0)), unitsOfCustomer.get(0), userPriceGroups, parentUserPriceGroups, unitsOfCustomer };
        }
      });

      getSessionService().setAttribute(B2BConstants.CTX_ATTRIBUTE_ROOTUNIT, branchInfo[0]);
      getSessionService().setAttribute(B2BConstants.CTX_ATTRIBUTE_BRANCH, branchInfo[1]);
      getSessionService().setAttribute(B2BConstants.CTX_ATTRIBUTE_UNIT, branchInfo[2]);
      getSessionService().setAttribute(Europe1Constants.PARAMS.UPG, branchInfo[3]);
      getSessionService().setAttribute(PentlandcoreConstants.PARENT_UPG, branchInfo[4]);
      getSessionService().setAttribute(PentlandcoreConstants.CTX_ATTRIBUTE_UNITS, branchInfo[5]);
    }
  }

  public List<B2BUnitModel> getUsersB2BUnits(final B2BCustomerModel customer) {
    return getParents(customer);
  }

  @Override
  public List<B2BUnitModel> getCurrentUnits() {
    return getSessionService().getAttribute(PentlandcoreConstants.CTX_ATTRIBUTE_UNITS);
  }

  protected List<B2BUnitModel> getParents(final B2BCustomerModel employee)
  {
    if (employee == null)
    {
      return null;
    }
    else
    {
      // customer has not selected a default parent unit, all B2BUnit parents will be used.
      return employee.getGroups().stream().filter(B2BUnitModel.class::isInstance).map(u -> (B2BUnitModel)u).collect(Collectors.toList());
    }
  }

  @Override
  public B2BUnitModel getUnitBySapID(String sapID)
  {
    Assert.notNull(sapID);

    final Map<String, Object> paramMap = new HashMap<String, Object>();
    paramMap.put("sapID", sapID);

    final List<B2BUnitModel> B2BUnits = getB2bUnitDao().find(paramMap);
    if (B2BUnits != null && !B2BUnits.isEmpty())
    {
      return B2BUnits.get(0);
    }

    throw new ModelNotFoundException("No B2BUnit with sapID " + sapID + " found");
  }

  @Override
  public List<AddressModel> findDeliveryAddressesForUnits(B2BUnitModel b2bUnit) {
    List<AddressModel> deliveryAddresses = new ArrayList<>();
    if(StringUtils.isNotEmpty(b2bUnit.getSapID())){
      deliveryAddresses.addAll(addressDao.findDeliveryAddressesForSapId(b2bUnit));
    }
    return deliveryAddresses;
  }

  @Override
  public Collection<B2BUnitModel> getFirstLevelParents(B2BCustomerModel employee) {

    return CollectionUtils.select(employee.getGroups(), PredicateUtils.instanceofPredicate(B2BUnitModel.class));
  }

  @Required
  public void setAddressDao(PentlandAddressDao addressDao) {
    this.addressDao = addressDao;
  }
}
