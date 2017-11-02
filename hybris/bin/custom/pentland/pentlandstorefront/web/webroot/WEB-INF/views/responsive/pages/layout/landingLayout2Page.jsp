<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>

<template:page pageTitle="${pageTitle}">
        <div class="col-xs-12">
            <cms:pageSlot position="Section1" var="feature" element="div">
                <cms:component component="${feature}" element="div"/>
            </cms:pageSlot>
        </div>
        <div class="col-xs-12 section2">
            <cms:pageSlot position="Section2" var="feature">
                <cms:component component="${feature}" element="div"/>
            </cms:pageSlot>
        </div>
        <div class="col-xs-12 section3">
            <cms:pageSlot position="Section3" var="feature" element="div">
                <cms:component component="${feature}" element="div" class="col-xs-2"/>
            </cms:pageSlot>
        </div>

    <cms:pageSlot position="Section4" var="feature" element="div" class="row no-margin">
        <cms:component component="${feature}" element="div" class="no-space yComponentWrapper"/>
    </cms:pageSlot>


</template:page>
