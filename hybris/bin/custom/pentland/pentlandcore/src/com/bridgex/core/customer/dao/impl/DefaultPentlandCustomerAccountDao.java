package com.bridgex.core.customer.dao.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.bridgex.core.customer.dao.PentlandCustomerAccountDao;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commerceservices.customer.dao.impl.DefaultCustomerAccountDao;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.commerceservices.search.flexiblesearch.data.SortQueryData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.store.BaseStoreModel;

/**
 * Created by dmitry.konovalov@masterdata.ru on 18.10.2017.
 */
public class DefaultPentlandCustomerAccountDao extends DefaultCustomerAccountDao implements PentlandCustomerAccountDao {

  private static final String FIND_ORDERS_BY_B2BUNITS_AND_STORE_QUERY = "SELECT {" + OrderModel.PK + "}, {" + OrderModel.CREATIONTIME + "}, {" + OrderModel.CODE
    + "} FROM {" + OrderModel._TYPECODE + "} WHERE {" + OrderModel.UNIT + "} IN (?b2bUnitList) AND {" + OrderModel.VERSIONID + "} IS NULL AND {" + OrderModel.STORE + "} = ?store";

  private static final String FIND_ORDERS_BY_B2BUNIT_AND_STORE_QUERY_AND_STATUS = FIND_ORDERS_BY_B2BUNITS_AND_STORE_QUERY + " AND {" + OrderModel.STATUS + "} IN (?statusList)";

  private static final String FILTER_ORDER_STATUS = " AND {" + OrderModel.STATUS + "} NOT IN (?filterStatusList)";

  private static final String FILTER_ORDER_SALESAPPLICATION = " AND {" + OrderModel.SALESAPPLICATION + "} NOT IN (?filterSalesApplicationList)";

  private static final Map<String, String> SORT_QUERIES = new LinkedHashMap<String, String>()
  {{
    put("byDate", " ORDER BY {" + OrderModel.CREATIONTIME + "} DESC, {" + OrderModel.PK + "}");
    put("byOrderNumber", " ORDER BY {" + OrderModel.CODE + "},{" + OrderModel.CREATIONTIME + "} DESC, {" + OrderModel.PK + "}");
    put("byPurchaseOrderNumber", " ORDER BY {" + OrderModel.PURCHASEORDERNUMBER + "},{" + OrderModel.CREATIONTIME + "} DESC, {" + OrderModel.PK + "}");
    put("byStatus", " ORDER BY {" + OrderModel.STATUS + "},{" + OrderModel.CREATIONTIME + "} DESC, {" + OrderModel.PK + "}");
    put("byOrderType", " ORDER BY {" + OrderModel.ORDERTYPE + "},{" + OrderModel.CREATIONTIME + "} DESC, {" + OrderModel.PK + "}");
    put("byRdd", " ORDER BY {" + OrderModel.RDD + "},{" + OrderModel.CREATIONTIME + "} DESC, {" + OrderModel.PK + "}");
  }};

  private List<SalesApplication> filterSalesApplicationList;

  @Override
  public SearchPageData<OrderModel> findOrdersByB2BUnitsAndStore(final List<B2BUnitModel> b2bUnits, final BaseStoreModel store, final OrderStatus[] status, final PageableData pageableData)
  {
    validateParameterNotNull(b2bUnits, "B2BUnits must not be null");
    validateParameterNotNull(store, "Store must not be null");

    final Map<String, Object> queryParams = new HashMap<String, Object>();
    queryParams.put("b2bUnitList", b2bUnits);
    queryParams.put("store", store);

    String filterClause = StringUtils.EMPTY;
    if (CollectionUtils.isNotEmpty(getFilterOrderStatusList()))
    {
      queryParams.put("filterStatusList", getFilterOrderStatusList());
      filterClause = FILTER_ORDER_STATUS;
    }

    if (CollectionUtils.isNotEmpty(getFilterSalesApplicationList())) {
      queryParams.put("filterSalesApplicationList", getFilterSalesApplicationList());
      filterClause += FILTER_ORDER_SALESAPPLICATION;
    }

    final List<SortQueryData> sortQueries;

    if (ArrayUtils.isNotEmpty(status))
    {
      queryParams.put("statusList", Arrays.asList(status));
      sortQueries = createSortQueries(FIND_ORDERS_BY_B2BUNIT_AND_STORE_QUERY_AND_STATUS, filterClause);
    }
    else
    {
      sortQueries = createSortQueries(FIND_ORDERS_BY_B2BUNITS_AND_STORE_QUERY, filterClause);
    }

    return getPagedFlexibleSearchService().search(sortQueries, "byDate", queryParams, pageableData);
  }

  protected List<SortQueryData> createSortQueries(final String query, final String filterClause) {
    return SORT_QUERIES.entrySet().stream()
                       .map( e -> createSortQueryData(e.getKey(), createQuery(query, filterClause, e.getValue())))
                       .collect(Collectors.toList());
  }

  protected List<SalesApplication> getFilterSalesApplicationList() {
    return filterSalesApplicationList;
  }

  public void setFilterSalesApplicationList(List<SalesApplication> filterSalesApplicationList) {
    this.filterSalesApplicationList = filterSalesApplicationList;
  }
}
