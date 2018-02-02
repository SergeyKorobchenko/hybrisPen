<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common" %>
<%@ taglib prefix="storepickup" tagdir="/WEB-INF/tags/responsive/storepickup" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<template:page pageTitle="${pageTitle}">

    <cms:pageSlot position="Section1" var="feature" element="div" class="product-grid-section1-slot">
        <cms:component component="${feature}" element="div"
                       class="yComponentWrapper map product-grid-section1-component"/>
    </cms:pageSlot>

    <div class="container">
        <div class="b-section--full-width">

            <c:choose>
                <c:when test="${searchPageData.pagination.totalNumberOfResults > 0}">
                    <c:set var="isResultEmpty" value="false"/>
                    <c:set var="productGridClass" value="col-xs-12 col-md-8 col-lg-9"/>
                </c:when>
                <c:otherwise>
                    <c:set var="isResultEmpty" value="true"/>
                    <c:set var="productGridClass" value="col-sm-12"/>
                </c:otherwise>
            </c:choose>

            <div class="row">
                <c:if test="${!isResultEmpty}">
                    <cms:pageSlot position="ProductLeftRefinements" var="feature" element="div"
                                  class="product-grid-left-refinements-slot col-md-4 col-lg-3">
                        <cms:component component="${feature}" element="div"
                                       class="yComponentWrapper product-grid-left-refinements-component"/>
                    </cms:pageSlot>
                </c:if>

                <cms:pageSlot position="ProductGridSlot" var="feature" element="div"
                              class="product-grid-right-result-slot ${productGridClass}">
                    <cms:component component="${feature}" element="div"
                                   class="product__list--wrapper yComponentWrapper product-grid-right-result-component"/>
                </cms:pageSlot>
            </div>
        </div>
    </div>

    <storepickup:pickupStorePopup/>

</template:page>