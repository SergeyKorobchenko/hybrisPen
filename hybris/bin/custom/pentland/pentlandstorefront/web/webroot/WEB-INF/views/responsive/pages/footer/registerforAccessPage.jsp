<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>  
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/responsive/formElement"%>
	<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>

<template:page pageTitle="${pageTitle}">
    <jsp:body>
     <spring:url value="/login/registered" var="registerUser" htmlEscape="false" />
        <!-- <div align="center"> -->
        <spring:url value="/login/registered" var="registerUser" htmlEscape="false" />
    <h4>Request for Register Access</h4>
        <form:form action="${registerUser}" method="POST" commandName="pentlandCustomerRegistrationForm">
            <table border="0">
                <tr>
                    <td>FIRST NAME</td>
                    <td><form:input path="firstName" /></td>
                    <td style="color: red;"><form:errors path="firstName" cssClass="error" /></td>
                </tr>
                 <tr>
                    <td>LAST NAME</td>
                    <td><form:input path="lastName" /></td>
                    <td style="color: red;"><form:errors path="lastName" cssClass="error" /></td>
                </tr>
                 <tr>
                    <td>POSITION</td>
                    <td><form:input path="position" /></td>
                     <td style="color: red;"><form:errors path="position" cssClass="error" /></td>
                </tr>
                 <tr>
                    <td>REQUIRED ROLE</td>
                    <td>
                    <select name="accessRequired">
					<option value="StandardRole">Standard role</option>
					<option value="ManagerRole">Manager role</option>
					</select>
                    </td>
                     <td style="color: red;"><form:errors path="accessRequired" cssClass="error" /></td>
                </tr>
                <tr>
                    <td>E-MAIL</td>
                    <td><form:input path="email" /></td>
                     <td style="color: red;"><form:errors path="email" cssClass="error" /></td>
                </tr>
                <tr>
                    <td>ACCOUNT NUMBER:</td>
                    <td><form:input path="accountNumber" /></td>
                     <td style="color: red;"><form:errors path="accountNumber" cssClass="error" /></td>
                </tr>
                <tr>
                    <td colspan="2" align="center"><input type="submit" value="Submit" /></td>
                    <td colspan="2" align="center"><input type="reset" value="Reset" /></td>
                </tr>
            </table>
        </form:form>
    <!-- </div> -->
    </jsp:body>
</template:page>

