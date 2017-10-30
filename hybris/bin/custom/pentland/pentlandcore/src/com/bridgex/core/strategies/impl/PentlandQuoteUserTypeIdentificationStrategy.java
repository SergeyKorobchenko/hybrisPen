package com.bridgex.core.strategies.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import java.util.Optional;

import de.hybris.platform.commerceservices.enums.QuoteUserType;
import de.hybris.platform.commerceservices.order.strategies.impl.DefaultQuoteUserTypeIdentificationStrategy;
import de.hybris.platform.core.model.user.UserModel;

/**
 * Created by Goncharenko Mikhail on 23.10.2017.
 */
public class PentlandQuoteUserTypeIdentificationStrategy extends DefaultQuoteUserTypeIdentificationStrategy {

  private String b2bCustomerGroup;
  private String b2bManagerGroup;
  private String salesRepGroup;
  private String b2bApproverGroup;

  @Override
  public Optional<QuoteUserType> getCurrentQuoteUserType(UserModel userModel) {

    validateParameterNotNullStandardMessage("userModel", userModel);

    if (getUserService().isMemberOfGroup(userModel, getUserService().getUserGroupForUID(getSalesRepGroup())) ||
        getUserService().isMemberOfGroup(userModel, getUserService().getUserGroupForUID(getB2bApproverGroup())))
    {
      return Optional.of(QuoteUserType.SELLER);
    }

    else if (getUserService().isMemberOfGroup(userModel, getUserService().getUserGroupForUID(getB2bCustomerGroup())) ||
             getUserService().isMemberOfGroup(userModel, getUserService().getUserGroupForUID(getB2bManagerGroup())))
    {
      return Optional.of(QuoteUserType.BUYER);
    }

    return Optional.empty();
  }

  public String getB2bCustomerGroup() {
    return b2bCustomerGroup;
  }

  public void setB2bCustomerGroup(String b2bCustomerGroup) {
    this.b2bCustomerGroup = b2bCustomerGroup;
  }

  public String getSalesRepGroup() {
    return salesRepGroup;
  }

  public void setSalesRepGroup(String salesRepGroup) {
    this.salesRepGroup = salesRepGroup;
  }

  public String getB2bApproverGroup() {
    return b2bApproverGroup;
  }

  public void setB2bApproverGroup(String b2bApproverGroup) {
    this.b2bApproverGroup = b2bApproverGroup;
  }

  public String getB2bManagerGroup() {
    return b2bManagerGroup;
  }

  public void setB2bManagerGroup(String b2bManagerGroup) {
    this.b2bManagerGroup = b2bManagerGroup;
  }
}
