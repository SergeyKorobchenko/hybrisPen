<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<spring:htmlEscape defaultHtmlEscape="true"/>

<%-- Temporary variable for only MVP1, helps to disable unused components, must be deleted in future --%>
<c:set var="isMVP1" value="true"/>

<template:page pageTitle="${pageTitle}" isMVP1="true">

    <cart:cartValidation/>
    <cart:cartPickupValidation/>

    <c:if test="${empty isMVP1}">
        <div class="cart-top-bar">
            <div class="text-right">
                <a href="" class="help js-cart-help" data-help="<spring:theme code="text.help" />"><spring:theme
                        code="text.help"/>
                    <span class="glyphicon glyphicon-info-sign"></span>
                </a>
                <div class="help-popup-content-holder js-help-popup-content">
                    <div class="help-popup-content">
                        <strong>${fn:escapeXml(cartData.code)}</strong>
                        <spring:theme code="basket.page.cartHelpContent" htmlEscape="false"/>
                    </div>
                </div>
            </div>
        </div>
    </c:if>

    <div>
        <div>
            <cms:pageSlot position="TopContent" var="feature">
                <cms:component component="${feature}" element="div" class="yComponentWrapper"/>
            </cms:pageSlot>
        </div>

        <c:if test="${not empty cartData.rootGroups}">
            <cms:pageSlot position="CenterLeftContentSlot" var="feature">
                <cms:component component="${feature}" element="div" class="yComponentWrapper"/>
            </cms:pageSlot>
        </c:if>

        <c:if test="${not empty cartData.rootGroups}">
            <cms:pageSlot position="CenterRightContentSlot" var="feature">
                <cms:component component="${feature}" element="div" class="yComponentWrapper"/>
            </cms:pageSlot>

            <cms:pageSlot position="BottomContentSlot" var="feature">
                <cms:component component="${feature}" element="div" class="yComponentWrapper"/>
            </cms:pageSlot>
        </c:if>

        <c:if test="${empty cartData.rootGroups}">
            <cms:pageSlot position="EmptyCartMiddleContent" var="feature">
                <cms:component component="${feature}" element="div" class="yComponentWrapper content__empty"/>
            </cms:pageSlot>
        </c:if>
    </div>
</template:page>