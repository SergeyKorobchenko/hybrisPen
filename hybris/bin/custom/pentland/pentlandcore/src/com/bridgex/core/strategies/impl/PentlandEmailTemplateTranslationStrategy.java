package com.bridgex.core.strategies.impl;

import com.bridgex.core.services.PentlandBaseSiteService;
import de.hybris.platform.acceleratorservices.process.strategies.impl.DefaultEmailTemplateTranslationStrategy;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PentlandEmailTemplateTranslationStrategy extends DefaultEmailTemplateTranslationStrategy {

    private PentlandBaseSiteService siteService;

    @Override
    protected List<String> buildMessageSources(final String languageIso, final BufferedReader reader, final String line)
            throws IOException {

        final List<String> messageSources = new ArrayList<>();
        String lineToProcess = line;

        while (StringUtils.isNotEmpty(lineToProcess)) {
            String messageSource = null;

            if (lineToProcess.trim().startsWith("<")) {
                break;
            } else if (lineToProcess.contains("## messageSource=")) {
                messageSource = StringUtils.substring(lineToProcess, lineToProcess.indexOf("## messageSource=") + 17);
            } else if (lineToProcess.contains("##messageSource=")) {
                messageSource = StringUtils.substring(lineToProcess, lineToProcess.indexOf("##messageSource=") + 16);
            }

            if (StringUtils.isNotEmpty(messageSource)) {
                if (messageSource.contains("$lang")) {
                    messageSource = messageSource.replace("$lang", languageIso);
                }

                if (messageSource.contains("$site")) {
                    BaseSiteModel site = siteService.getCurrentBaseSite();
                    String siteUid;

                    if (site != null && StringUtils.isNotEmpty(siteUid = site.getUid())) {
                        messageSource = messageSource.replace("$site", siteUid);

                    }
                }

                messageSources.add(messageSource);
            }

            lineToProcess = reader.readLine();
        }

        return messageSources;
    }


    public PentlandBaseSiteService getSiteService() {
        return siteService;
    }

    public void setSiteService(PentlandBaseSiteService siteService) {
        this.siteService = siteService;
    }
}
