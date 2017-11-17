package com.bridgex.core.product.dao;

import com.bridgex.core.model.ProductSizeModel;
import de.hybris.platform.servicelayer.internal.dao.GenericDao;

public interface ProductSizeDao extends GenericDao<ProductSizeModel> {

    ProductSizeModel findByCode(String code);

}
