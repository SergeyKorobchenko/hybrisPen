package com.bridgex.core.search.solrfacetsearch.provider.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.util.ObjectUtils;

import com.bridgex.core.category.PentlandCategoryService;
import com.bridgex.core.product.dao.PentlandProductDao;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;

public class SMUProductVisibilityValueResolver extends AbstractBaseProductValueResolver {

	private static final String CATEGORY_CATALOG = "pentlandProductCatalog";
	private static final String CATEGORY_CATALOG_VERSION = "Online";	
	private PentlandCategoryService categoryService;
	private CatalogVersionService catalogVersionService;
	private PentlandProductDao pentlandProductDao;
	
	@Override
	protected void addFieldValues(InputDocument inputDocument, IndexerBatchContext indexerBatchContext, IndexedProperty indexedProperty, ProductModel productModel,ValueResolverContext<Object, Object> valueResolverContext)throws FieldValueProviderException {
		ProductModel baseProductModel = getBaseProductModel(productModel);
		Collection<CategoryModel> supercategories = baseProductModel.getSupercategories();
		 CategoryModel categoryModel1 = supercategories.stream().filter(category ->category.getCode().contains("_SMU")).findFirst().orElse(null);
		 if(categoryModel1 != null && baseProductModel.isSmu()){
		      List<PrincipalModel> allowedPrincipals = categoryModel1.getAllowedPrincipals();
		      List<String> principalUids = allowedPrincipals.stream().filter(principalModel -> !"customergroup".equals(principalModel.getUid()))
		                                                    .map(PrincipalModel::getUid).collect(Collectors.toList());
		  
		      
		      CatalogVersionModel catalogVersion = getCatalogVersionService().getCatalogVersion(CATEGORY_CATALOG, CATEGORY_CATALOG_VERSION);
		      Set<String> smuUsers=new HashSet<>();
		      for (String string : principalUids) {
				
		    	  Collection<CategoryModel> smuCategoriesForCurrentUser = getCategoryService().getSMUCategoriesForCurrentUser(string, catalogVersion);
		    	 
					for (CategoryModel categoryModel : smuCategoriesForCurrentUser)
					{
						ProductModel checkProductWithCurrentUserSMUCategory = pentlandProductDao.checkProductWithCurrentUserSMUCategory(baseProductModel.getCode(), categoryModel.getCode(), catalogVersion);
						if(checkProductWithCurrentUserSMUCategory!=null && !ObjectUtils.isEmpty(checkProductWithCurrentUserSMUCategory))
						{
							smuUsers.add(string);
						}
					}
			   }
		      
		      
		      inputDocument.addField(indexedProperty, smuUsers, valueResolverContext.getFieldQualifier());
		    
		 }
	}
	
	public PentlandCategoryService getCategoryService() {
		return categoryService;
	}

	public void setCategoryService(PentlandCategoryService categoryService) {
		this.categoryService = categoryService;
	}

	public CatalogVersionService getCatalogVersionService() {
		return catalogVersionService;
	}

	public void setCatalogVersionService(CatalogVersionService catalogVersionService) {
		this.catalogVersionService = catalogVersionService;
	}

	public PentlandProductDao getPentlandProductDao() {
		return pentlandProductDao;
	}

	public void setPentlandProductDao(PentlandProductDao pentlandProductDao) {
		this.pentlandProductDao = pentlandProductDao;
	}


}
