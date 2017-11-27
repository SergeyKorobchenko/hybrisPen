package com.bridgex.core.customer.dao;

import java.util.List;
import java.util.Set;

import de.hybris.platform.b2b.dao.PrincipalGroupMembersDao;
import de.hybris.platform.core.model.user.EmployeeModel;

/**
 * @author Goncharenko Mikhail, created on 23.11.2017.
 */
public interface PentlandPrincipalGroupMemberDao extends PrincipalGroupMembersDao {

  List<EmployeeModel> findEmployeesForB2BUnits(Set<String> b2BUnits);

}
