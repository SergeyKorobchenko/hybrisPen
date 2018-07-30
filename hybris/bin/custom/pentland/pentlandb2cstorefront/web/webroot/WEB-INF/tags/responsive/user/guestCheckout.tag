<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="actionNameKey" required="true" type="java.lang.String" %>
<%@ attribute name="action" required="true" type="java.lang.String" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/responsive/formElement" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="user-register__headline"><spring:theme code="guest.checkout" arguments="${fn:escapeXml(siteName)}"/></div>

<form:form action="${action}" method="post" commandName="guestForm">
    <div class="form-group">
        <formElement:formInputBox idKey="guest.email" labelKey="guest.email" inputCSS="input-sm guestEmail" path="email"
                                  mandatory="true"/>
    </div>

    <div class="form-group">
        <label class="control-label" for="guest.confirm.email"> <spring:theme code="guest.confirm.email"/></label>
        <input class="confirmGuestEmail form-control input-sm" id="guest.confirm.email"/>
    </div>

    <ycommerce:testId code="guest_Checkout_button">
        <button type="submit" disabled="true" class="btn btn-default btn-block guestCheckoutBtn"><spring:theme
                code="${actionNameKey}"/></button>
    </ycommerce:testId>

</form:form>