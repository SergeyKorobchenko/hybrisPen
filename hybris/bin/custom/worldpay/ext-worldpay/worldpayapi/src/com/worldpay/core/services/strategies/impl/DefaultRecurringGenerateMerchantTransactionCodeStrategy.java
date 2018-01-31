package com.worldpay.core.services.strategies.impl;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.bridgex.core.model.ApparelSizeVariantProductModel;
import com.bridgex.core.services.PentlandB2BUnitService;
import de.hybris.platform.core.model.user.UserGroupModel;
import com.worldpay.core.services.strategies.RecurringGenerateMerchantTransactionCodeStrategy;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.variants.model.VariantProductModel;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

/**
 * Strategy to generate the WorldpayOrderCode to send to Worldpay. Identifies an order in Worldpay.
 */
public class DefaultRecurringGenerateMerchantTransactionCodeStrategy implements RecurringGenerateMerchantTransactionCodeStrategy {

    private ModelService           modelService;
    private CartService            cartService;
    private PentlandB2BUnitService pentlandB2BUnitService;
    private UserService            userService;

    @Override
    public String generateCode(final CartModel cartModel) {
        return internalGenerateCode(cartModel);
    }

    @Override
    public String generateCode(final AbstractOrderModel abstractOrderModel) {
        return internalGenerateCode(abstractOrderModel);
    }

    protected String internalGenerateCode(final AbstractOrderModel abstractOrderModel) {
        AbstractOrderModel parameterOrder = abstractOrderModel;
        if (parameterOrder == null) {
            parameterOrder = cartService.getSessionCart();
        }
        final List<B2BUnitModel> units = pentlandB2BUnitService.getCurrentUnits();
        String customerSAPId = "";
        if (CollectionUtils.isNotEmpty(units)) {
           customerSAPId = units.get(0).getSapID();
        }
        final String worldpayOrderCode = parameterOrder.getCode().replaceFirst("^0+(?!$)", "") + "-" + customerSAPId + getBrands(parameterOrder);
        parameterOrder.setWorldpayOrderCode(worldpayOrderCode);
        modelService.save(parameterOrder);
        return worldpayOrderCode;
    }

    private String getBrands(AbstractOrderModel parameterOrder) {
        Set<String> collectedBrandCodes = parameterOrder.getEntries().stream().map(AbstractOrderEntryModel::getProduct).filter(product -> product instanceof VariantProductModel)
                                               .map(product -> ((VariantProductModel)product).getBaseProduct().getSapBrand()).filter(Objects::nonNull)
                                               .collect(Collectors.toSet());
        //it's guaranteed that brand group always exists and uid is equal to sapBrand, otherwise products of this brand couldn't be visible and purchased
        String brandString = collectedBrandCodes.stream().map(code -> userService.getUserGroupForUID(code).getDescription())
                                                .filter(Objects::nonNull).collect(Collectors.joining("-"));
        return StringUtils.isEmpty(brandString) ? "" :"-" + brandString;
    }

    protected long getTime() {
        return System.currentTimeMillis();
    }

    @Required
    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }

    @Required
    public void setCartService(CartService cartService) {
        this.cartService = cartService;
    }

    @Required
    public void setPentlandB2BUnitService(PentlandB2BUnitService pentlandB2BUnitService) {
        this.pentlandB2BUnitService = pentlandB2BUnitService;
    }

    @Required
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
