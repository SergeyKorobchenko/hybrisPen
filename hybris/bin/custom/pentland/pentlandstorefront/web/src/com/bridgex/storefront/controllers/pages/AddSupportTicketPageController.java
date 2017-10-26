package com.bridgex.storefront.controllers.pages;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sap.security.core.server.csi.XSSEncoder;

import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.Breadcrumb;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.ResourceBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.ThirdPartyConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractSearchPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.customerticketingaddon.constants.CustomerticketingaddonConstants;
import de.hybris.platform.customerticketingaddon.controllers.pages.AccountSupportTicketsPageController;
import de.hybris.platform.customerticketingaddon.forms.SupportTicketForm;
import de.hybris.platform.customerticketingfacades.TicketFacade;
import de.hybris.platform.customerticketingfacades.data.StatusData;
import de.hybris.platform.customerticketingfacades.data.TicketData;
import de.hybris.platform.ticket.service.UnsupportedAttachmentException;

/**
 * Created by Goncharenko Mikhail on 26.10.2017.
 */
@Controller
@RequestMapping("/add-ticket")
public class AddSupportTicketPageController extends AbstractSearchPageController {

  private static final Logger LOG = Logger.getLogger(AccountSupportTicketsPageController.class);

  // CMS Pages
  private static final String SUPPORT_TICKET_CODE_PATH_VARIABLE_PATTERN = "{ticketId:.*}";
  private static final String REDIRECT_TO_SUPPORT_TICKETS_PAGE = REDIRECT_PREFIX + "/my-account/support-tickets";

  @Resource(name = "csAttachmentUploadMaxSize")
  private long maxUploadSizeValue;

  @Resource(name = "accountBreadcrumbBuilder")
  private ResourceBreadcrumbBuilder accountBreadcrumbBuilder;

  @Resource(name = "defaultTicketFacade")
  private TicketFacade ticketFacade;

  @Resource(name = "cartFacade")
  private CartFacade cartFacade;

  @Resource(name = "customerFacade")
  private CustomerFacade customerFacade;

  @Resource(name = "validator")
  private Validator validator;

  @Resource(name = "allowedUploadedFormats")
  private String allowedUploadedFormats;

  private final String[] DISALLOWED_FIELDS = new String[] {};

  @InitBinder
  public void initBinder(final WebDataBinder binder)
  {
    binder.setDisallowedFields(DISALLOWED_FIELDS);
  }

  @InitBinder
  public void init(final WebDataBinder binder)
  {
    binder.setBindEmptyMultipartFiles(false);
  }

  /**
   * Used for retrieving page to create a customer support ticket.
   *
   * @param model
   * @return View String
   * @throws CMSItemNotFoundException
   */
  @RequestMapping(method = RequestMethod.GET)
  @RequireHardLogIn
  public String addSupportTicket(final Model model) throws CMSItemNotFoundException
  {
    storeCmsPageInModel(model, getContentPageForLabelOrId(CustomerticketingaddonConstants.ADD_SUPPORT_TICKET_PAGE));
    setUpMetaDataForContentPage(model, getContentPageForLabelOrId(CustomerticketingaddonConstants.ADD_SUPPORT_TICKET_PAGE));

    model.addAttribute(WebConstants.BREADCRUMBS_KEY,
                       getBreadcrumbs(CustomerticketingaddonConstants.TEXT_SUPPORT_TICKETING_ADD));
    model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
    model.addAttribute(CustomerticketingaddonConstants.SUPPORT_TICKET_FORM, new SupportTicketForm());
    model.addAttribute(CustomerticketingaddonConstants.MAX_UPLOAD_SIZE, Long.valueOf(maxUploadSizeValue));
    model.addAttribute(CustomerticketingaddonConstants.MAX_UPLOAD_SIZE_MB,
                       FileUtils.byteCountToDisplaySize(maxUploadSizeValue));
    try
    {
      model.addAttribute(CustomerticketingaddonConstants.SUPPORT_TICKET_ASSOCIATED_OBJECTS,
                         ticketFacade.getAssociatedToObjects());
      model.addAttribute(CustomerticketingaddonConstants.SUPPORT_TICKET_CATEGORIES, ticketFacade.getTicketCategories());
    }
    catch (final UnsupportedOperationException ex)
    {
      LOG.error(ex.getMessage(), ex);
    }


    return getViewForPage(model);
  }

  /**
   * Creates a ticket.
   *
   * @param supportTicketForm
   * @param bindingResult
   * @return View String
   */
  @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  @RequireHardLogIn
  @ResponseBody
  public ResponseEntity<String> addSupportTicket(final SupportTicketForm supportTicketForm,
                                                                    final BindingResult bindingResult)
  {
    validator.validate(supportTicketForm, bindingResult);
    if (bindingResult.hasErrors())
    {
      final List<Map<String, String>> list = buildErrorMessagesMap(bindingResult);
      list.add(buildMessageMap(CustomerticketingaddonConstants.FORM_GLOBAL_ERROR_KEY,
                               CustomerticketingaddonConstants.FORM_GLOBAL_ERROR));

      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    try
    {
      ticketFacade.createTicket(this.populateTicketData(supportTicketForm));
    }
    catch (final UnsupportedAttachmentException usAttEx)
    {
      LOG.error(usAttEx.getMessage(), usAttEx);
      final Map<String, String> map = Maps.newHashMap();
      map.put(CustomerticketingaddonConstants.SUPPORT_TICKET_TRY_LATER, getMessageSource()
        .getMessage(CustomerticketingaddonConstants.TEXT_SUPPORT_TICKETING_ATTACHMENT_BLOCK_LISTED, new Object[]
          { allowedUploadedFormats }, getI18nService().getCurrentLocale()));
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    catch (final RuntimeException rEX)
    {
      final Map<String, String> map = Maps.newHashMap();
      LOG.error(rEX.getMessage(), rEX);

      map.put(CustomerticketingaddonConstants.SUPPORT_TICKET_TRY_LATER, getMessageSource().getMessage(
        CustomerticketingaddonConstants.TEXT_SUPPORT_TICKETING_TRY_LATER, null, getI18nService().getCurrentLocale()));
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity<>(HttpStatus.OK);

  }

  /**
   * Populated the data from the form bean to ticket data object.
   *
   * @param supportTicketForm
   * @return TicketData
   */
  protected TicketData populateTicketData(final SupportTicketForm supportTicketForm) // TODO: should be moved to populator class
  {
    final TicketData ticketData = new TicketData();
    if (cartFacade.hasSessionCart())
    {
      final CartData cartData = cartFacade.getSessionCart();
      if (!cartData.getEntries().isEmpty())
      {
        ticketData.setCartId(cartData.getCode());
      }
    }
    if (StringUtils.isNotBlank(supportTicketForm.getId()))
    {
      ticketData.setId(supportTicketForm.getId());
    }
    final StatusData status = new StatusData();
    status.setId(supportTicketForm.getStatus());
    ticketData.setStatus(status);
    ticketData.setCustomerId(customerFacade.getCurrentCustomerUid());
    ticketData.setSubject(supportTicketForm.getSubject());
    ticketData.setMessage(supportTicketForm.getMessage());
    ticketData.setAssociatedTo(supportTicketForm.getAssociatedTo());
    ticketData.setTicketCategory(supportTicketForm.getTicketCategory());
    ticketData.setAttachments(supportTicketForm.getFiles());
    return ticketData;
  }

  protected List<Breadcrumb> getBreadcrumbs(final String breadcrumbCode)
  {
    final List<Breadcrumb> breadcrumbs = accountBreadcrumbBuilder.getBreadcrumbs(null);
    breadcrumbs.add(new Breadcrumb("#", getMessageSource().getMessage(breadcrumbCode, null, getI18nService().getCurrentLocale()), null));
    return breadcrumbs;
  }

  /**
   * Build the error message list with map contains the validation error code and localised message.
   *
   * @param bindingResult
   * @return Map of error code and message
   */
  protected List<Map<String, String>> buildErrorMessagesMap(final BindingResult bindingResult)
  {
    return bindingResult.getAllErrors()
                        .stream()
                        .filter(err -> err.getCode() != null && err.getCode().length() > 0)
                        .map(err ->
                             {
                               final Map<String, String> map = Maps.newHashMap();
                               map.put(err.getCodes()[0].replaceAll("\\.", "-"), err.getDefaultMessage());
                               return map;
                             })
                        .collect(Collectors.toList());
  }

  /**
   * Build a map with key and localsed Message.
   *
   * @param key
   *           the render key
   * @param localisedKey
   *           the localised message key
   * @return Map of error code and message
   */
  protected Map<String, String> buildMessageMap(final String key, final String localisedKey)
  {
    final Map<String, String> map = Maps.newHashMap();
    map.put(key, getMessageSource().getMessage(localisedKey, null, getI18nService().getCurrentLocale()));

    return map;
  }

}
