package com.bridgex.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;
import de.hybris.platform.variants.model.VariantCategoryModel;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Created by ekaterina.agievich@masterdata.ru on 9/25/2017.
 */
public class CategoryVisibilityValueResolver extends AbstractValueResolver<ProductModel, Object, Object> {
  @Override
  protected void addFieldValues(InputDocument inputDocument, IndexerBatchContext indexerBatchContext, IndexedProperty indexedProperty, ProductModel productModel, ValueResolverContext<Object, Object> valueResolverContext) throws FieldValueProviderException {
    Collection<CategoryModel> supercategories = productModel.getSupercategories();
    CategoryModel categoryModel = supercategories.stream().filter(category ->
                                                                    !(category instanceof ClassificationClassModel) && !(category instanceof VariantCategoryModel)).findFirst().orElse(null);
    if(categoryModel != null){
      List<PrincipalModel> allowedPrincipals = categoryModel.getAllowedPrincipals();
      List<String> principalUids = allowedPrincipals.stream().filter(principalModel -> !"customergroup".equals(principalModel.getUid()))
                                                    .map(PrincipalModel::getUid).collect(Collectors.toList());
      inputDocument.addField(indexedProperty, principalUids, valueResolverContext.getFieldQualifier());
    }
  }

}
