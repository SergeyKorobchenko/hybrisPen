<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<spring:htmlEscape defaultHtmlEscape="true"/>

<template:page pageTitle="${pageTitle}">

    <cart:cartValidation/>
    <cart:cartPickupValidation/>

    <div class="container">
        <div class="b-section--full-width">
            <cms:pageSlot position="TopContent" var="feature">
                <cms:component component="${feature}" element="div" class="yComponentWrapper"/>
            </cms:pageSlot>

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
    </div>

    <div class="hidden">
        <div class="unsaved_popup_content" data-popup-title="<spring:theme code='cart.unsaved.popup.title'/>">
            <p class="lead text-center"><spring:theme code="cart.unsaved.popup.warning"/></p>
            <div class="modal-actions">
                <div class="row">
                    <div class="col-xs-12 col-sm-6">
                        <a class="btn btn-primary btn-block js-save-cart" href="#"><spring:theme
                                code="cart.unsaved.popup.save"/></a>
                    </div>
                    <div class="col-xs-12 col-sm-6">
                        <a class="btn btn-default btn-block js-close-unsaved-popup" href="#"><spring:theme
                                code="cart.unsaved.popup.cancel"/></a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template:page>