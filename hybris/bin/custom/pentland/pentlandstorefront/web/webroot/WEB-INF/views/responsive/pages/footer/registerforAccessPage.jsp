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
        <spring:url value="/login/registered" var="registerUser" htmlEscape="false" />
        <div align="center">
    <h4>Request for Register Access</h4>
    </div>
        <form:form action="${registerUser}" method="POST" commandName="pentlandCustomerRegistrationForm">
            <table border="0" style="width:35%;" align="center">
                <tr>
                    <td>FIRST NAME</td>
                    <td><form:input path="firstName" style="width: 250px;"/></td>
                    <td style="color: red;"><form:errors path="firstName" cssClass="error" /></td>
                </tr>
                 <tr>
                    <td>LAST NAME</td>
                    <td><form:input path="lastName" style="width: 250px;"/></td>
                    <td style="color: red;"><form:errors path="lastName" cssClass="error" /></td>
                </tr>
                 <tr>
                    <td>POSITION</td>
                    <td><form:input path="position" style="width: 250px;"/></td>
                     <td style="color: red;"><form:errors path="position" cssClass="error" /></td>
                </tr>
                 <tr>
                    <td>REQUIRED ROLE</td>
                    <td>
                    <select name="accessRequired" style="width: 250px;">
					<option value="StandardRole">Standard role</option>
					<option value="ManagerRole">Manager role</option>
					</select>
                    </td>
                     <td style="color: red;"><form:errors path="accessRequired" cssClass="error" /></td>
                </tr>
                <tr>
                    <td>E-MAIL</td>
                    <td><form:input path="email" style="width: 250px;"/></td>
                     <td style="color: red;"><form:errors path="email" cssClass="error" /></td>
                </tr>
                <tr>
                    <td>ACCOUNT NUMBER:</td>
                    <td><form:input path="accountNumber" style="width: 250px;"/></td>
                     <td style="color: red;"><form:errors path="accountNumber" cssClass="error" /></td>
                </tr>
                </table>
                <div class="buttons" align="center" style="padding-top: 20px;">
                    <input type="submit" value="Submit" style="margin-left: 120px;"/>
                    <input type="reset" value="Reset" style="margin-left: 70px;"/>
            	</div>
        </form:form>
    </jsp:body>
</template:page>

