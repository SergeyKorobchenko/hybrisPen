package com.worldpay.merchant.impl;

import com.worldpay.config.merchant.WorldpayMerchantConfigData;
import com.worldpay.merchant.WorldpayMerchantConfigDataService;
import de.hybris.platform.acceleratorservices.config.SiteConfigService;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.util.Config;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * Default implementation of {@link WorldpayMerchantConfigDataService}
 */
public class DefaultWorldpayMerchantConfigDataService implements WorldpayMerchantConfigDataService, ApplicationContextAware {

    protected static final String WORLDPAY_MERCHANT_CONFIG = "worldpaymerchantconfig";

    private SiteConfigService  siteConfigService;
    private ApplicationContext applicationContext;
    private BaseSiteService    baseSiteService;
    private CommonI18NService  commonI18NService;

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, WorldpayMerchantConfigData> getMerchantConfiguration() {
        final String merchantConfigurationBeanName = getSiteCurrencyDependentProperty(WORLDPAY_MERCHANT_CONFIG);
        return getConfiguredMerchants(merchantConfigurationBeanName);
    }

    private Map<String, WorldpayMerchantConfigData> getConfiguredMerchants(final String merchantConfigurationBeanName) {
        return (Map<String, WorldpayMerchantConfigData>) applicationContext.getBean(merchantConfigurationBeanName);
    }

    protected String getSiteCurrencyDependentProperty(String code){
        BaseSiteModel currentBaseSite = baseSiteService.getCurrentBaseSite();
        String currentBaseSiteUid = "." + currentBaseSite.getUid();
        CurrencyModel currentCurrency = commonI18NService.getCurrentCurrency();
        String currentCurrencyIso = "." + currentCurrency.getIsocode();
        String config = Config.getString(code + currentBaseSiteUid + currentCurrencyIso, StringUtils.EMPTY);
        if(StringUtils.isEmpty(config)){
            config = Config.getString(code + currentBaseSiteUid, StringUtils.EMPTY);
        }
        if(StringUtils.isEmpty(config)){
            config = Config.getString(code, StringUtils.EMPTY);
        }
        return config;
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Required
    public void setSiteConfigService(SiteConfigService siteConfigService) {
        this.siteConfigService = siteConfigService;
    }

    @Required
    public void setBaseSiteService(BaseSiteService baseSiteService) {
        this.baseSiteService = baseSiteService;
    }

    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService) {
        this.commonI18NService = commonI18NService;
    }
}
