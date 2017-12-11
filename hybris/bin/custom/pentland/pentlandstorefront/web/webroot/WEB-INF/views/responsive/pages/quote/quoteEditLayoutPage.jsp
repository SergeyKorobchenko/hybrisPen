<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart" %>

<template:page pageTitle="${pageTitle}">
    <div class="container">
        <div class="b-section--full-width">
            <cart:cartValidation/>

            <cms:pageSlot position="TopContent" var="feature" element="div" class="accountPageTopContent">
                <cms:component component="${feature}"/>
            </cms:pageSlot>

            <cms:pageSlot position="BodyContent" var="feature" element="div" class="account-section-content">
                <cms:component component="${feature}"/>
            </cms:pageSlot>

            <cms:pageSlot position="BottomContent" var="feature" element="div" class="accountPageBottomContent">
                <cms:component component="${feature}"/>
            </cms:pageSlot>
        </div>
    </div>

</template:page>