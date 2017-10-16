package com.bridgex.core.order.impex.translators;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.fest.util.Collections;

import com.bridgex.core.services.impl.DefaultPentlandB2BUnitService;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.impex.jalo.header.StandardColumnDescriptor;
import de.hybris.platform.impex.jalo.translators.AbstractValueTranslator;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

/**
 * Created by dmitry.konovalov@masterdata.ru on 16.10.2017.
 */
public class PentlandB2BUnitToUserTranslator extends AbstractValueTranslator {

  private DefaultPentlandB2BUnitService pentlandB2BUnitService;
  private ModelService modelService;

  @Override
  public void init(StandardColumnDescriptor descriptor) {
    super.init(descriptor);
    pentlandB2BUnitService = (DefaultPentlandB2BUnitService) Registry.getApplicationContext().getBean("pentlandB2BUnitService");
    modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
  }

  @Override
  public Object importValue(String valueExpr, Item toItem) throws JaloInvalidParameterException {
    clearStatus();
    String result = null;
    if (StringUtils.isNotBlank(valueExpr)) {
      B2BUnitModel b2BUnitModel = pentlandB2BUnitService.getUnitForUid(valueExpr);
      UserModel contact = b2BUnitModel.getContact();
      if (contact != null) {
        result = contact.getUid();
      } else {
        Set<B2BCustomerModel> unitCustomers = pentlandB2BUnitService.getB2BCustomers(b2BUnitModel);
        if (!Collections.isEmpty(unitCustomers)) {
          contact = unitCustomers.iterator().next();
          b2BUnitModel.setContact(contact);
          modelService.save(b2BUnitModel);
          result = contact.getUid();
        } else {
          contact = modelService.create(B2BCustomerModel.class);
          contact.setName(b2BUnitModel.getName() + " Default Customer");
          contact.setUid(b2BUnitModel.getUid() + "defaultCustomer");

//          ((B2BCustomerModel)contact).set
          modelService.save(contact);
          b2BUnitModel.setContact(contact);
          modelService.save(b2BUnitModel);
          result = contact.getUid();
        }
      }
    }
    return result;
  }

  @Override
  public String exportValue(Object o) throws JaloInvalidParameterException {
    return null;
  }
}
