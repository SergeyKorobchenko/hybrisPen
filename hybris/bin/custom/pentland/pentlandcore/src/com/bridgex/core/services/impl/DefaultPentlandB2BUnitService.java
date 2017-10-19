package com.bridgex.core.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.Assert;

import com.bridgex.core.services.PentlandB2BUnitService;

import de.hybris.platform.b2b.constants.B2BConstants;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.impl.DefaultB2BUnitService;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.europe1.constants.Europe1Constants;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;

/**
 * Created by Lenar on 10/13/2017.
 */
public class DefaultPentlandB2BUnitService extends DefaultB2BUnitService implements PentlandB2BUnitService {

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

          List<Object> userPriceGroups = new ArrayList<>();
          unitsOfCustomer.forEach(unitOfCustomer -> {
            final EnumerationValueModel userPriceGroup = (unitOfCustomer.getUserPriceGroup() != null ? getTypeService().getEnumerationValue(unitOfCustomer.getUserPriceGroup()) : lookupPriceGroupFromClosestParent(unitOfCustomer));
            userPriceGroups.add(userPriceGroup);
          });
          return new Object[]
            { getRootUnit(unitsOfCustomer.get(0)), getBranch(unitsOfCustomer.get(0)), unitsOfCustomer.get(0), userPriceGroups };
        }
      });

      getSessionService().setAttribute(B2BConstants.CTX_ATTRIBUTE_ROOTUNIT, branchInfo[0]);
      getSessionService().setAttribute(B2BConstants.CTX_ATTRIBUTE_BRANCH, branchInfo[1]);
      getSessionService().setAttribute(B2BConstants.CTX_ATTRIBUTE_UNIT, branchInfo[2]);
      getSessionService().setAttribute(Europe1Constants.PARAMS.UPG, branchInfo[3]);


    }
  }



  protected List<B2BUnitModel> getParents(final B2BCustomerModel employee)
  {
    if (employee == null)
    {
      return null;
    }
    else if (employee.getDefaultB2BUnit() != null)
    {
      // the customer has selected a unit to use
      return Collections.singletonList(employee.getDefaultB2BUnit());
    }
    else
    {
      // customer has not selected a default parent unit, all B2BUnit parents will be used.
      List<B2BUnitModel> groups = new ArrayList<>();
      employee.getGroups().stream().filter(u -> u instanceof B2BUnitModel).forEach(u -> groups.add((B2BUnitModel) u));
      return groups;
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

}
