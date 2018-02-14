package com.bridgex.core.job;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;


import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.jobs.AbstractMaintenanceJobPerformable;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.mail.MailUtils;

/**
 * @author Created by ekaterina.agievich@masterdata.ru on 2/14/2018.
 */
public class PricelessProductsMaintenanceJobPerformable extends AbstractMaintenanceJobPerformable {

  private static final Logger LOG = Logger.getLogger(PricelessProductsMaintenanceJobPerformable.class);

  @Override
  public FlexibleSearchQuery getFetchQuery(CronJobModel cronJob) {

    String query = "SELECT DISTINCT {product:pk} FROM {ApparelStyleVariantProduct! as product} " +
                   "where not exists ({{ select {pr:pk} from {PriceRow as pr} where {pr:productId} = {product:code} }})";
    return new FlexibleSearchQuery(query);
  }

  @Override
  public void process(List<ItemModel> elements, CronJobModel cronJob) {
    if(CollectionUtils.isNotEmpty(elements)){
      List<String> materialKeys = elements.stream().map(item -> ((ProductModel) item).getCode()).collect(Collectors.toList());
      mailPricelessProducts(cronJob, materialKeys);
    }
  }

  protected void mailPricelessProducts(CronJobModel cronJob, List<String> materialKeys){
    if(StringUtils.isNotEmpty(cronJob.getEmailAddress())){
      try{
        String emailAddress = cronJob.getEmailAddress();
        String msg = buildEmailBody(cronJob, materialKeys);

        Email email = MailUtils.getPreConfiguredEmail();
        MailUtils.validateEmailAddress(emailAddress, "TO");

        email.addTo(emailAddress);
        String subject = "Color-level products with no prices assigned";

        email.setSubject(subject);

        email.setMsg(msg);

        email.send();
        if(LOG.isDebugEnabled()) {
          LOG.debug("Email successfully sent.");
        }
      }
      catch (EmailException e) {
        LOG.warn("Make sure your mail properties (mail.*) are correctly set.", e);
      }
    }
  }

  private String buildEmailBody(CronJobModel cronJob, List<String> materialKeys) {

    StringBuilder message = new StringBuilder();

    message.append(MessageFormat.format(Config.getParameter(Config.Params.CRONJOB_MAIL_SUBJECT_FAIL), cronJob.getCode(), cronJob.getJob().getCode())).append("\n");
    message.append("Found products with no prices assigned: \n");
    materialKeys.forEach(key -> message.append(key + "\n"));
    message.append("\n").append("Manual actions required.");

    return message.toString();
  }
}
