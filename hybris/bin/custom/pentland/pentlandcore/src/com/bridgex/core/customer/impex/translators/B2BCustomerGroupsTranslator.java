package com.bridgex.core.customer.impex.translators;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import de.hybris.platform.b2b.jalo.B2BCustomer;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.header.HeaderValidationException;
import de.hybris.platform.impex.jalo.header.SpecialColumnDescriptor;
import de.hybris.platform.impex.jalo.translators.AbstractSpecialValueTranslator;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

/**
 * Created by dmitry.konovalov@masterdata.ru on 24.10.2017.
 */
public class B2BCustomerGroupsTranslator extends AbstractSpecialValueTranslator {

  private static final Logger LOG = Logger.getLogger(B2BCustomerGroupsTranslator.class);

  private ModelService                  modelService;
  private UserService                   userService;
  private String defaultValue;

  public void init(SpecialColumnDescriptor columnDescriptor) throws HeaderValidationException {
    modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
    userService = (UserService) Registry.getApplicationContext().getBean("userService");
    defaultValue = columnDescriptor.getDefaultValueDefinition();
  }

  @Override
  public void performImport(String cellValue, Item processedItem) throws ImpExException {
    if (StringUtils.isEmpty(cellValue)) {
      cellValue = defaultValue;
    }
    if (StringUtils.isNotEmpty(cellValue) && processedItem != null && processedItem instanceof B2BCustomer) {
      B2BCustomerModel customerModel = ((B2BCustomerModel) userService.getUserForUID(((B2BCustomer) processedItem).getUid()));
      try {
        String[] groups = cellValue.split(",");
        Set<PrincipalGroupModel> principalGroups = new HashSet<>(customerModel.getGroups());
        Arrays.stream(groups).forEach(s -> {
          UserGroupModel userGroupModel = userService.getUserGroupForUID(s);
          principalGroups.add(userGroupModel);
        });
        customerModel.setGroups(principalGroups);
        modelService.save(customerModel);
      } catch (Exception e) {
        LOG.error("User import has caused an error! User(B2BCustomer) uid=" + customerModel.getUid() + ". Message: " + e.getMessage());
      }
    }
  }

}
