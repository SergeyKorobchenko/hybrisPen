package com.bridgex.core.product.dao.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import com.bridgex.core.model.ProductSizeModel;
import com.bridgex.core.product.dao.ProductSizeDao;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Nonnull;

import org.springframework.util.CollectionUtils;

public class DefaultProductSizeDao extends DefaultGenericDao<ProductSizeModel> implements ProductSizeDao {

    public DefaultProductSizeDao()
    {
        super("ProductSize");
    }

    @Override
    public ProductSizeModel findByCode(@Nonnull String code) {
        validateParameterNotNull(code, "No code specified");

        List<ProductSizeModel> list = find(new HashMap<String, String>(){{put("code", code);}});
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }
}
