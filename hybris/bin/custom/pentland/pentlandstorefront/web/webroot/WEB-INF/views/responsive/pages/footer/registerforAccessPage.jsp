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
			<h4><spring:theme code="request.register.access"/></h4>
	    </div>
	    
        <form:form action="${registerUser}" method="POST" commandName="pentlandCustomerRegistrationForm">
        	<div class="table-responsive" align="center">
	            <table class="table" style="width:auto;">
	                <tr>
	                    <td>COMPANY NAME <span style="color:red;">*</span></td>
	                    <td><form:input path="companyName" required="true" style="width:250px"/></td>
	                    <td style="color: red;"><form:errors path="firstName" cssClass = "error" /></td>
	                </tr>
	                <tr>
	                    <td>FIRST NAME <span style="color:red;">*</span></td>
	                    <td><form:input path="firstName" required="true" style="width:250px"/></td>
	                    <td style="color: red;"><form:errors path="firstName" cssClass="error" /></td>
	                </tr>
	                 <tr>
	                    <td>LAST NAME <span style="color:red;">*</span></td>
	                    <td><form:input path="lastName" required="true" style="width:250px"/></td>
	                    <td style="color: red;"><form:errors path="lastName" cssClass="error" /></td>
	                </tr>
	                 <tr>
	                    <td>POSITION <span style="color:red;">*</span></td>
	                    <td><form:input path="position" required="true" style="width:250px"/></td>
	                    <td style="color: red;"><form:errors path="position" cssClass="error" /></td>
	                </tr>
	                 <tr>
	                    <td>REQUIRED ROLE</td>
	                    <td>
		                    <select name="accessRequired">
								<option value="StandardRole" style="width:230px">Standard role</option>
								<option value="ManagerRole" style="width:230px">Manager role</option>
							</select>
	                    </td>
	                    <td style="color: red;"><form:errors path="accessRequired" cssClass="error" /></td>
	                </tr>
	                <tr>
	                    <td>E-MAIL <span style="color:red;">*</span></td>
	                    <td><form:input path="email" type="email" required="true" style="width:250px"/></td>
	                    <td style="color: red;"><form:errors path="email" cssClass="error" /></td>
	                </tr>
	                <tr>
	                    <td>ACCOUNT NUMBER: <span style="color:red;">*</span></td>
	                    <td><form:input path="accountNumber" required="true" style="width:250px"/></td>
	                    <td style="color: red;"><form:errors path="accountNumber" cssClass="error" /></td>
	                </tr>
	                <tr>
	                    <td></td>
	                    <td>
			                <div class="buttons" style="padding-top: 20px;">
			                    <input type="submit" value="Submit" />
			                    <input type="reset" value="Reset" style="float: right;"/>
			            	</div>
	            		</td>
	                    <td></td>
	                </tr>
                </table>
            </div>
        </form:form>
	</jsp:body>
</template:page>

