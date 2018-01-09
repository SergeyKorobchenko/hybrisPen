package com.bridgex.core.customer.dao.impl;

import java.util.List;
import java.util.Set;

import com.bridgex.core.customer.dao.PentlandPrincipalGroupMemberDao;

import de.hybris.platform.b2b.dao.impl.DefaultPrincipalGroupMembersDao;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

/**
 * @author Goncharenko Mikhail, created on 23.11.2017.
 */
public class DefaultPentlandPrincipalGroupMemberDao extends DefaultPrincipalGroupMembersDao implements PentlandPrincipalGroupMemberDao {

  private static final String SALESREPGROUP = "salesrepgroup";

  private static final String FIND_EMPLOYEES_FOR_DESIRED_UNITS = "SELECT DISTINCT pk FROM {Employee" +
                                                                 " AS emp JOIN PrincipalGroupRelation as pgr ON {emp.pk} = {pgr.source}" +
                                                                 " JOIN UserGroup as ug on {pgr.target} = {ug.pk}" +
                                                                 " JOIN PrincipalGroupRelation as pgr2 on {emp.pk} = {pgr2.source}" +
                                                                 " JOIN B2BUnit as bbu on {pgr2.target} = {bbu.pk}}" +
                                                                 " WHERE {ug.uid} = ?group " +
                                                                 " AND {bbu.uid} IN (?units)";

  @Override
  public List<EmployeeModel> findEmployeesForB2BUnits(Set<String> b2BUnitIds) {
    FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_EMPLOYEES_FOR_DESIRED_UNITS);
    query.addQueryParameter("group", SALESREPGROUP);
    query.addQueryParameter("units", b2BUnitIds);
    SearchResult<EmployeeModel> result = getFlexibleSearchService().search(query);
    return result.getResult();
  }
}
