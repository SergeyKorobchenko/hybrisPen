package com.bridgex.core.order.impex.translators;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.bridgex.core.services.impl.DefaultPentlandB2BUnitService;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.impex.jalo.header.StandardColumnDescriptor;
import de.hybris.platform.impex.jalo.translators.SingleValueTranslator;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

/**
 * Created by dmitry.konovalov@masterdata.ru on 16.10.2017.
 */
public class PentlandB2BUnitToUserTranslator extends SingleValueTranslator {

  private static final String B2B_CUSTOMER_GROUP_UID = "b2bcustomergroup";
  private static final String DEFAULT_CUSTOMER_NAME  = "Default Customer";
  private static final String DEFAULT_CUSTOMER_UID   = "DefaultCustomer";
  private static final String DEFAULT_CUSTOMER_EMAIL = "empty@empty.com";

  private static final Logger LOG = Logger.getLogger(PentlandB2BUnitToUserTranslator.class);

  private DefaultPentlandB2BUnitService pentlandB2BUnitService;
  private ModelService modelService;
  private UserService userService;

  @Override
  public void init(StandardColumnDescriptor descriptor) {
    super.init(descriptor);
    pentlandB2BUnitService = (DefaultPentlandB2BUnitService) Registry.getApplicationContext().getBean("pentlandB2BUnitService");
    modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
    userService = (UserService) Registry.getApplicationContext().getBean("userService");
  }

  @Override
  protected Object convertToJalo(String value, Item item) throws JaloInvalidParameterException {
    clearStatus();
    User result = null;
    if (StringUtils.isNotBlank(value)) {
      UserModel contact = null;
      B2BUnitModel b2BUnitModel = pentlandB2BUnitService.getUnitForUid(value);
      if (b2BUnitModel != null) {
        try {
          contact = b2BUnitModel.getContact();
          if (contact == null) {
              Set<B2BCustomerModel> unitCustomers = pentlandB2BUnitService.getB2BCustomers(b2BUnitModel);
              if (!CollectionUtils.isEmpty(unitCustomers)) {
                contact = unitCustomers.iterator().next();
              }
              else {
                contact = createContact(b2BUnitModel);
              }
              b2BUnitModel.setContact(contact);
              modelService.save(b2BUnitModel);
          }
          result = modelService.getSource(contact);
          if (item != null && item instanceof AbstractOrder) {
            AbstractOrder order = (AbstractOrder) item;
            order.setUser(result);
          }
        } catch (Exception e) {
          setError();
          LOG.error(e.getMessage(), e);
        }
      }
    }
    return result;
  }

  @Override
  protected String convertToString(Object o) {
    throw new UnsupportedOperationException();
  }

  protected UserModel createContact(B2BUnitModel b2BUnitModel) {
    B2BCustomerModel b2bCustomer = modelService.create(B2BCustomerModel.class);
    b2bCustomer.setName(b2BUnitModel.getName() + " " + DEFAULT_CUSTOMER_NAME);
    b2bCustomer.setUid(b2BUnitModel.getUid() + DEFAULT_CUSTOMER_UID);
    b2bCustomer.setEmail(DEFAULT_CUSTOMER_EMAIL);
    Set<PrincipalGroupModel> groups = new HashSet<>();
    groups.add(userService.getUserGroupForUID(B2B_CUSTOMER_GROUP_UID));
    groups.add(b2BUnitModel);
    b2bCustomer.setGroups(groups);
    return b2bCustomer;
  }

}
