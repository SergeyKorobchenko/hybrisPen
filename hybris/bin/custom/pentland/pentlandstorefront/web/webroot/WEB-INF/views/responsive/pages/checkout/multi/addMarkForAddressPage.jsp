<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="address" tagdir="/WEB-INF/tags/responsive/address"%>
<%@ taglib prefix="multi-checkout" tagdir="/WEB-INF/tags/responsive/checkout/multi"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<template:page pageTitle="${pageTitle}" hideHeaderLinks="true">

<div class="row">
    <div class="col-sm-6">
	    <div class="checkout-headline">
            <span class="glyphicon glyphicon-lock"></span>
            <spring:theme code="checkout.multi.secure.checkout" />
        </div>
        <multi-checkout:checkoutSteps checkoutSteps="${checkoutSteps}" progressBarId="${progressBarId}">
            <jsp:body>
                <ycommerce:testId code="checkoutStepThree">
                    <div class="checkout-shipping">
                            <%--<multi-checkout:shipmentItems cartData="${cartData}" showDeliveryAddress="true" />--%>

                            <div class="checkout-indent">
                                <div class="headline"><spring:theme code="checkout.multi.deliveryAddress.markFor" /></div>
                                <c:choose>
                                    <c:when test="${not empty deliveryAddresses}">
                                        <p><spring:theme code="checkout.multi.deliveryAddress.markFor.description"/></p>
                                    </c:when>
                                    <c:otherwise>
                                        <p><spring:theme code="checkout.multi.deliveryAddress.markFor.empty"/></p>
                                    </c:otherwise>
                                </c:choose>


                                    <address:addressFormSelector supportedCountries="${countries}"
                                        regions="${regions}" cancelUrl="${currentStepUrl}"
                                        country="${country}" />

                                        <div id="addressbook"  class="long-scrollable ">

                                            <c:forEach items="${deliveryAddresses}" var="deliveryAddress" varStatus="status">
                                                <div class="addressEntry">
                                                    <form action="${request.contextPath}/checkout/multi/mark-for/select" method="GET">
                                                        <input type="hidden" name="selectedAddressCode" value="${fn:escapeXml(deliveryAddress.id)}" />
                                                        <ul>
                                                            <li>
                                                                <strong>${fn:escapeXml(deliveryAddress.title)}&nbsp;
                                                                ${fn:escapeXml(deliveryAddress.firstName)}&nbsp;
                                                                ${fn:escapeXml(deliveryAddress.lastName)}</strong>
                                                                <br>
                                                                ${fn:escapeXml(deliveryAddress.line1)}&nbsp;
                                                                ${fn:escapeXml(deliveryAddress.line2)}
                                                                <br>
                                                                ${fn:escapeXml(deliveryAddress.town)}
                                                                <c:if test="${not empty deliveryAddress.region.name}">
                                                                    &nbsp;${fn:escapeXml(deliveryAddress.region.name)}
                                                                </c:if>
                                                                <br>
                                                                ${fn:escapeXml(deliveryAddress.country.name)}&nbsp;
                                                                ${fn:escapeXml(deliveryAddress.postalCode)}
                                                            </li>
                                                        </ul>
                                                        <button type="submit" class="btn btn-primary btn-block">
                                                            <spring:theme code="checkout.multi.deliveryAddress.useThisAddress" />
                                                        </button>
                                                    </form>
                                                </div>
                                            </c:forEach>
                                        </div>
                            </div>
                    </div>

                    <c:url var="nextStep" value="${nextStepUrl}"/>
                    <a href="${nextStep}"
                        class="btn btn-primary btn-block checkout-next"><spring:theme code="checkout.multi.deliveryAddress.continue"/></a>
                </ycommerce:testId>
            </jsp:body>
        </multi-checkout:checkoutSteps>
    </div>

    <div class="col-sm-6 hidden-xs">
		<multi-checkout:checkoutOrderDetails cartData="${cartData}" showDeliveryAddress="true" showPaymentInfo="false" showTaxEstimate="false" showTax="true" />
    </div>

    <div class="col-sm-12 col-lg-12">
        <cms:pageSlot position="SideContent" var="feature" element="div" class="checkout-help">
            <cms:component component="${feature}"/>
        </cms:pageSlot>
    </div>
</div>

</template:page>
