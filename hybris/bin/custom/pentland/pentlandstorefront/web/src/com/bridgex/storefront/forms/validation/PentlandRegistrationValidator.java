/**
 * 
 */
package com.bridgex.storefront.forms.validation;


import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.bridgex.storefront.controllers.pages.PentlandCustomerRegistrationForm;

/**
 * @author murali
 *
 */
@Component("pentlandRegistrationValidator")
public class PentlandRegistrationValidator implements Validator{
 public static final Pattern EMAIL_REGEX = Pattern.compile("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b");

 @Override
 public boolean supports(final Class<?> aClass)
 {
  return PentlandCustomerRegistrationForm.class.equals(aClass);
 }

 @Override
 public void validate(final Object object, final Errors errors)
 {
  final PentlandCustomerRegistrationForm registerForm = (PentlandCustomerRegistrationForm) object;
  final String firstName = registerForm.getFirstName();
  final String lastName = registerForm.getLastName();
  final String email = registerForm.getEmail();
  final String position = registerForm.getPosition();
  final String accessRequired = registerForm.getAccessRequired();
  //final String accountNumber = registerForm.getAccountNumber();
  final String companyName =   registerForm.getCompanyName();
  //validateTitleCode(errors, titleCode);
  validateName(errors, firstName, "firstName", "register.firstName.invalid");
  validateName(errors, lastName, "lastName", "register.lastName.invalid");
  validatePosition(errors, position, "position", "register.position.invalid");
  validateAccessRequired(errors, accessRequired, "accessRequired", "register.accessRequired.invalid");
  //validateAccountNumber(errors, accountNumber, "accountNumber", "register.accountNumber.invalid");
  validateCompanyName(errors, companyName, "companyName", "register.companyName.invalid");
  
  if (StringUtils.length(firstName) + StringUtils.length(lastName) > 255)
  {
   errors.rejectValue("lastName", "register.name.invalid");
   errors.rejectValue("firstName", "register.name.invalid");
  }

  validateEmail(errors, email);
  
 }
 
 private void validateCompanyName(Errors errors, String companyName, String propertyName, String property) {
	// TODO Auto-generated method stub
	 if (StringUtils.isBlank(companyName))
	  {
	   errors.rejectValue(propertyName, property);
	  }
	  else if (StringUtils.length(companyName) > 255)
	  {
	   errors.rejectValue(propertyName, property);
	  }
}

protected void validateAccountNumber(Errors errors, String accountNumber, String propertyName, String property) {

  if (StringUtils.isEmpty(accountNumber))
  {
   errors.rejectValue("accountNumber", "register.accountNumber.invalid");
  }
  else if (StringUtils.length(accountNumber) > 255 )
  {
   errors.rejectValue("accountNumber", "register.accountNumber.invalid");
  }
  
 }

 protected void validateAccessRequired(Errors errors, String accessRequired, String propertyName, String property) {
  if (StringUtils.isEmpty(accessRequired))
  {
   errors.rejectValue("accessRequired", "register.accessRequired.invalid");
  }
  else if (StringUtils.length(accessRequired) > 255)
  {
   errors.rejectValue("accessRequired", "register.accessRequired.invalid");
  }
 }

 protected void validatePosition(Errors errors, String position, String propertyName, String property) {
  if (StringUtils.isEmpty(position))
  {
   errors.rejectValue("position", "register.position.invalid");
  }
  else if (StringUtils.length(position) > 255)
  {
   errors.rejectValue("position", "register.position.invalid");
  }
  
 }

 protected void validateEmail(final Errors errors, final String email)
 {
  if (StringUtils.isEmpty(email))
  {
   errors.rejectValue("email", "register.email.invalid");
  }
  else if (StringUtils.length(email) > 255 || !validateEmailAddress(email))
  {
   errors.rejectValue("email", "register.email.invalid");
  }
 }
 
 
 
 protected void validateName(final Errors errors, final String name, final String propertyName, String property)
 {
  if (StringUtils.isBlank(name))
  {
   errors.rejectValue(propertyName, property);
  }
  else if (StringUtils.length(name) > 255)
  {
   errors.rejectValue(propertyName, property);
  }
 }
 public boolean validateEmailAddress(final String email)
 {
  final Matcher matcher = EMAIL_REGEX.matcher(email);
  return matcher.matches();
 }

}