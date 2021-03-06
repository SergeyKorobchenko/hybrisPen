package com.bridgex.core.search.solrfacetsearch.provider.impl;

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;

import com.bridgex.core.model.ApparelSizeVariantProductModel;

import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 10/23/2017.
 */
public abstract class AbstractNavCategoryResolver extends AbstractValueResolver<ProductModel, Object, Object> {
  @Override
  protected void addFieldValues(InputDocument inputDocument,
                                IndexerBatchContext indexerBatchContext,
                                IndexedProperty indexedProperty,
                                ProductModel productModel,
                                ValueResolverContext<Object, Object> valueResolverContext) throws FieldValueProviderException
  {
    ProductModel baseProductModel = getBaseProductModel(productModel);
    Collection<CategoryModel> categories = baseProductModel.getSupercategories();
    if(CollectionUtils.isNotEmpty(categories)) {

      for (CategoryModel category : categories) {
        if (!isCategoryBlocked(category)) {
          addFieldValue(inputDocument, indexedProperty, valueResolverContext, category);
          Collection<CategoryModel> allSupercategories = category.getAllSupercategories();
          for (CategoryModel cat : allSupercategories) {
            if (!isCategoryBlocked(cat)) {
              addFieldValue(inputDocument, indexedProperty, valueResolverContext, cat);
            }
          }
        }
      }
    }
  }

  public ProductModel getBaseProductModel(final ProductModel model)
  {
    if (model instanceof ApparelSizeVariantProductModel) {
      //most attributes are stored at style level
      return ((ApparelSizeVariantProductModel) model).getBaseProduct();
    }else{
      //in case of non-variant or style-only products
      return model;
    }
  }

  private boolean isCategoryBlocked(CategoryModel category){
    if(category.isHidden()){
      return true;
    }
    if(CollectionUtils.isEmpty(category.getSupercategories()) || category instanceof ClassificationClassModel) {
      return true;
    }
    return false;
  }

  protected abstract void addFieldValue(InputDocument inputDocument, IndexedProperty indexedProperty, ValueResolverContext<Object, Object> valueResolverContext, CategoryModel cat)
    throws FieldValueProviderException;
}
