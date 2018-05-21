<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<spring:url value="/login/registered" var="registerUser" htmlEscape="false" />
<body>
    <div align="center">
    <h2>New User Registration</h2>
        <form:form action="${registerUser}" method="POST" commandName="pentlandCustomerRegistrationForm">
            <table border="0">
                <tr>
                    <td>First Name:</td>
                    <td><form:input path="firstName" /></td>
                    <td style="color: red;"><form:errors path="firstName" cssClass="error" /></td>
                </tr>
                 <tr>
                    <td>Last Name:</td>
                    <td><form:input path="lastName" /></td>
                    <td style="color: red;"><form:errors path="lastName" cssClass="error" /></td>
                </tr>
                 <tr>
                    <td>Position:</td>
                    <td><form:input path="position" /></td>
                     <td style="color: red;"><form:errors path="position" cssClass="error" /></td>
                </tr>
                 <tr>
                    <td>accessRequired:</td>
                    <td><form:input path="accessRequired" /></td>
                     <td style="color: red;"><form:errors path="accessRequired" cssClass="error" /></td>
                </tr>
                <tr>
                    <td>E-mail:</td>
                    <td><form:input path="email" /></td>
                     <td style="color: red;"><form:errors path="email" cssClass="error" /></td>
                </tr>
                <tr>
                    <td>AccountNumber:</td>
                    <td><form:input path="accountNumber" /></td>
                     <td style="color: red;"><form:errors path="accountNumber" cssClass="error" /></td>
                </tr>
                <tr>
                    <td colspan="2" align="center"><input type="submit" value="Send" /></td>
                </tr>
                  <tr>
                    <td colspan="2" align="center"><input type="reset" value="Reset" /></td>
                </tr>
            </table>
        </form:form>
    </div>
</body>
</html>