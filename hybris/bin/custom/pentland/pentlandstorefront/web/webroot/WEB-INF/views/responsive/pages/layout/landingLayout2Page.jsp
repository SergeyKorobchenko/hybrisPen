<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>

<template:page pageTitle="${pageTitle}">

    <div class="container b-margin-bottom">
        <div class="b-section--full-width">
            <cms:pageSlot position="Section1" var="feature" element="div" class="row">
                <cms:component component="${feature}" element="div" class="col-xs-12"/>
            </cms:pageSlot>
        </div>
    </div>

    <div class="c-slogan">
        <div class="container">
            <div class="b-section--full-width">
                <cms:pageSlot position="Section2" var="feature" element="div" class="row">
                    <cms:component component="${feature}" element="div" class="col-lg-8 col-lg-offset-2"/>
                </cms:pageSlot>
            </div>
        </div>
    </div>

    <div class="c-link-banners b-margin-bottom">
        <div class="container">
            <div class="b-section--full-width">
                <cms:pageSlot position="Section3" var="feature" element="div" class="row">
                    <cms:component component="${feature}" element="div" class="col-xs-12 col-sm-4 col-md-3 col-lg-3"/>
                </cms:pageSlot>
            </div>
        </div>
    </div>

    <div class="c-best-products">
        <div class="container">
            <div class="b-section--full-width">
                <cms:pageSlot position="Section4" var="feature" element="div" class="row no-margin">
                    <cms:component component="${feature}" element="div" class="no-space yComponentWrapper"/>
                </cms:pageSlot>
            </div>
        </div>
    </div>

</template:page>