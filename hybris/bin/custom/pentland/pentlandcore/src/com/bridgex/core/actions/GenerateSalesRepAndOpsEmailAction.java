package com.bridgex.core.actions;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Required;

import com.bridgex.core.model.DivisionModel;
import com.bridgex.core.services.PentlandB2BUnitService;

import de.hybris.platform.acceleratorservices.model.email.EmailAddressModel;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.core.model.user.UserModel;
/**
 * @author Goncharenko Mikhail, created on 22.11.2017.
 */
public class GenerateSalesRepAndOpsEmailAction extends GenerateSalesRepEmailAction {

  @Override
  protected List<EmailAddressModel> getSalesRepAddresses(UserModel user) {
    Set<String> unitIds = b2BUnitService.getAllParents(user).stream()
                              .map(B2BUnitModel::getUid)
                              .collect(Collectors.toSet());
    List<EmailAddressModel> addresses =
      groupMemberDao.findEmployeesForB2BUnits(unitIds).stream().map(emp -> emailFetchStrategy.fetch(emp.getUid(), emp.getName())).collect(Collectors.toList());
    addresses.add(this.getCustomerOpsEmail(user));
    return addresses;
  }

  private EmailAddressModel getCustomerOpsEmail(UserModel user) {
    B2BUnitModel b2bUnit = (B2BUnitModel)user.getGroups().stream().filter(group -> group instanceof B2BUnitModel).findFirst().orElse(null);
    String email;
    if(b2bUnit != null && b2bUnit.getDivision() != null){
      email = b2bUnit.getDivision().getEmail();
    }else {
      email = Optional.ofNullable(((B2BCustomerModel) user)).map(B2BCustomerModel::getDivision).map(DivisionModel::getEmail).orElse(null);
    }
    return emailFetchStrategy.fetch(email, email);
  }

  @Required
  public void setB2BUnitService(PentlandB2BUnitService b2BUnitService) {
    this.b2BUnitService = b2BUnitService;
  }
}
