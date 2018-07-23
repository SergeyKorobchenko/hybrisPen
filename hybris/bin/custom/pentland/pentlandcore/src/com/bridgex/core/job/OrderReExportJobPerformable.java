package com.bridgex.core.job;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.bridgex.core.event.OrderStatusChangedEvent;
import com.bridgex.core.model.OrderExportCronJobModel;
import com.bridgex.core.order.PentlandOrderExportService;

import de.hybris.platform.core.LazyLoadItemList;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.enums.ExportStatus;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.cronjob.TypeAwareJobPerformable;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.internal.model.ServicelayerJobModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.impl.LazyLoadModelList;
import de.hybris.platform.servicelayer.search.internal.resolver.ItemObjectResolver;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.mail.MailUtils;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 11/8/2017.
 */
public class OrderReExportJobPerformable extends AbstractJobPerformable<OrderExportCronJobModel> implements TypeAwareJobPerformable {

  private static final Logger LOG = Logger.getLogger(OrderReExportJobPerformable.class);

  private static final int DEFAULT_TIMEOUT = 15;
  private static final int DEFAULT_RETRIES = 3;

  private int pageSize = 100;
  private boolean abortOnError = false;

  private PentlandOrderExportService pentlandOrderExportService;
  private ItemObjectResolver         modelResolver;

  private List<OrderModel> failedOrders;

  private EventService eventService;

  @Override
  public PerformResult perform(OrderExportCronJobModel orderExportCronJobModel) {
    failedOrders = new ArrayList<>();

    boolean caughtException = false;

    //first, get the query from the strategy
    final FlexibleSearchQuery createFetchQuery = getFetchQuery(orderExportCronJobModel);
    if (createFetchQuery == null) {
      throw new IllegalStateException("The FlexibleSearchQuery object was null, cannot proceed!");
    }

    //and remember what the strategy wants as result class
    //if nothing is set in the interface impl, this list contains Item.class as default value
    final List<Class> expectedClassList = createFetchQuery.getResultClassList();

    //before we search, we'll overwrite this anyway because PKs are smaller
    createFetchQuery.setResultClassList(Arrays.asList(PK.class));

    //do the search, result should be a lazyloadedlist with PKs
    final SearchResult<PK> searchRes = flexibleSearchService.search(createFetchQuery);

    //paging through the PKs and for each page, we get the real deal
    final int totalCount = searchRes.getTotalCount();
    for (int i = 0; i < totalCount; i += pageSize) {
      final List<PK> sublist = searchRes.getResult().subList(i, Math.min(i + pageSize, totalCount));
      final LazyLoadModelList llml = new LazyLoadModelList(new LazyLoadItemList(null, sublist, pageSize), pageSize,
                                                           expectedClassList, modelResolver);
      try {
        process(llml, orderExportCronJobModel);
        for (final Object obj : llml) {
          modelService.detach(obj);
        }
      }catch (final Exception e) {
        caughtException = true;
        LOG.error("Caught exception during process call. " + e.getClass().getName() + ": " + e.getMessage());
        if (abortOnError) {
          LOG.error("stacktrace:", e);
          this.mailFailedOrders(orderExportCronJobModel);
          return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
        }
      }
    }
    this.mailFailedOrders(orderExportCronJobModel);
    LOG.info(String.format("Processed %d orders", totalCount));
    return new PerformResult(caughtException ? CronJobResult.FAILURE : CronJobResult.SUCCESS, CronJobStatus.FINISHED);
  }

  public FlexibleSearchQuery getFetchQuery(OrderExportCronJobModel cronJob) {
    int timeout = cronJob.getTimeoutMinutes() > 0 ? cronJob.getTimeoutMinutes() : DEFAULT_TIMEOUT;

    StringBuilder queryBuilder = new StringBuilder("select {pk} from {" + OrderModel._TYPECODE + "} ");
    queryBuilder.append("where {" + OrderModel.EXPORTSTATUS + "}=?exportStatus ");
    queryBuilder.append("and {" + OrderModel.MODIFIEDTIME + "} < ?modifiedMargin ");
    queryBuilder.append("and {" + OrderModel.SITE + "} = ?site");
    FlexibleSearchQuery query = new FlexibleSearchQuery(queryBuilder.toString());
    query.addQueryParameter("exportStatus", ExportStatus.NOTEXPORTED);
    query.addQueryParameter("modifiedMargin", DateUtils.addMinutes( new Date(), -timeout));
    query.addQueryParameter("site", cronJob.getSite());

    return query;
  }

  public void process(List<ItemModel> elements, OrderExportCronJobModel cronJob) {
    if(CollectionUtils.isNotEmpty(elements)){
      int allowedRetries = cronJob.getRetries() > 0 ? cronJob.getRetries() : DEFAULT_RETRIES;

      elements.forEach(item -> {
        OrderModel orderModel = (OrderModel)item;
        if(!pentlandOrderExportService.exportOrder(orderModel) && orderModel.getReexportRetries() > allowedRetries){
          failedOrders.add(orderModel);
        }else{
          submitChangeStatusEvent(orderModel);
        }
      });
    }
  }

  private void submitChangeStatusEvent(OrderModel order) {
    if(CollectionUtils.isNotEmpty(order.getByBrandOrderList())) {
      eventService.publishEvent(new OrderStatusChangedEvent(order.getByBrandOrderList().get(0).getCode(), true));
    }
  }

  protected void mailFailedOrders(OrderExportCronJobModel cronJob){
    if(CollectionUtils.isNotEmpty(this.failedOrders) && StringUtils.isNotEmpty(cronJob.getEmailAddress())){
      try{
        String emailAddress = cronJob.getEmailAddress();
        String msg = buildEmailBody(cronJob);

        Email email = MailUtils.getPreConfiguredEmail();
        MailUtils.validateEmailAddress(emailAddress, "TO");

        email.addTo(emailAddress);
        String subject = "Failed to automatically re-export orders";

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

  private String buildEmailBody(CronJobModel cronJob) {

    StringBuilder message = new StringBuilder();

    message.append(MessageFormat.format(Config.getParameter(Config.Params.CRONJOB_MAIL_SUBJECT_FAIL), cronJob.getCode(), cronJob.getJob().getCode())).append("\n");
    message.append("Exceeded re-export attempts for orders: ");
    this.failedOrders.forEach(order -> message.append(order.getCode() + " "));
    message.append("\n").append("Manual actions required.");

    return message.toString();
  }

  public void setPageSize(final int pagesize) {
    if (pagesize < 0) {
      throw new IllegalArgumentException("pagesize cannot be negative");
    }
    this.pageSize = pagesize;
  }

  @Override
  public String getType() {
    return ServicelayerJobModel._TYPECODE;
  }

  public void setAbortOnError(final boolean abort)
  {
    this.abortOnError = abort;
  }

  @Required
  public void setPentlandOrderExportService(PentlandOrderExportService pentlandOrderExportService) {
    this.pentlandOrderExportService = pentlandOrderExportService;
  }

  @Required
  public void setModelResolver(final ItemObjectResolver modelResolver)
  {
    this.modelResolver = modelResolver;
  }

  @Required
  public void setEventService(EventService eventService) {
    this.eventService = eventService;
  }
}
