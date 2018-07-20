package com.bridgex.core.product.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.bridgex.core.model.ApparelStyleVariantProductModel;

import de.hybris.platform.b2bacceleratorservices.dao.impl.DefaultPagedB2BProductDao;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.PaginationData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.product.ProductModel;

/**
 * @author Created by murali.venkata@bridge-x.com on 09/04/2018.
 */
public class DefaultPentlandPagedB2BProductDao extends DefaultPagedB2BProductDao
{
	private static final String FIND_PRODUCTS_BY_SKUS = "SELECT DISTINCT query.PK FROM " + "(" + "   {{"
			+ "       SELECT {PK} AS PK FROM {Product} WHERE {code} IN (?skulist) " + "   }}" + "   UNION ALL " + "   {{"
			+ "       SELECT {p:PK} AS PK FROM {GenericVariantProduct AS v JOIN Product AS p ON {v:baseproduct} = {p:PK} } WHERE {v:code} IN (?skulist) "
			+ "   }}" + ") query";

	public DefaultPentlandPagedB2BProductDao(String typeCode) {
		super(typeCode);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public SearchPageData<ProductModel> findPagedProductsForSkus(Collection<String> skus, PageableData pageableData) 
	{
		final Map<String, Object> params = new HashMap<String, Object>(1);
		params.put("skulist", skus);

		SearchPageData<ProductModel> search = getPagedFlexibleSearchService().search(FIND_PRODUCTS_BY_SKUS, params, pageableData);
		
		List<ProductModel> results = search.getResults();
		List<ProductModel> sizeProducts=new ArrayList<>();
		for (ProductModel productModel : results) {
			if(productModel instanceof ApparelStyleVariantProductModel)
			{
				if(CollectionUtils.isNotEmpty(productModel.getVariants()))
				{
					sizeProducts.add(productModel.getVariants().iterator().next());
					search.setResults(sizeProducts);
				}
			}
		}
		
		PaginationData pagination = search.getPagination();
		if(sizeProducts.size() >0 && pagination.getTotalNumberOfResults()!=sizeProducts.size())
		{
			pagination.setTotalNumberOfResults(sizeProducts.size());
		}
		return search;
		
	}
}
