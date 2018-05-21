/**
 * 
 */
package com.bridgex.storefront.controllers.pages;

/**
 * @author santoshi
 *
 */
public class PentlandCustomerRegistrationForm {
 private String firstName; 
 private String lastName;
    private String position;
 private String accessRequired;
    private String accountNumber;
 private String email;

 public String getFirstName() {
  return firstName;
 }

 public void setFirstName(String firstName) {
  this.firstName = firstName;
 }

 public String getLastName() {
  return lastName;
 }

 public void setLastName(String lastName) {
  this.lastName = lastName;
 }

 public String getPosition() {
  return position;
 }

 public void setPosition(String position) {
  this.position = position;
 }

 public String getAccessRequired() {
  return accessRequired;
 }

 public void setAccessRequired(String accessRequired) {
  this.accessRequired = accessRequired;
 }

 public String getAccountNumber() {
  return accountNumber;
 }

 public void setAccountNumber(String accountNumber) {
  this.accountNumber = accountNumber;
 }

 public String getEmail() {
  return email;
 }

 public void setEmail(String email) {
  this.email = email;
 }
}
