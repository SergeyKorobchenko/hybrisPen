<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%--
    ~ /*
    ~  * [y] hybris Platform
    ~  *
    ~  * Copyright (c) 2000-2017 SAP SE or an SAP affiliate company.
    ~  * All rights reserved.
    ~  *
    ~  * This software is the confidential and proprietary information of SAP
    ~  * ("Confidential Information"). You shall not disclose such Confidential
    ~  * Information and shall use it only in accordance with the terms of the
    ~  * license agreement you entered into with SAP.
    ~  *
    ~  */
--%>

<spring:htmlEscape defaultHtmlEscape="true"/>
<spring:url value="/quote/{/quotecode}/newcart/" var="newCartUrl" htmlEscape="false">
	<spring:param name="quotecode"  value="${cartData.quoteData.code}"/>
</spring:url>

<div class="cart-header">
    <div class="row">
        <div class="col-xs-12 col-sm-5">
            <h1>
                <spring:theme code="text.cart"/>
                <c:if test="${not empty cartData.code}">
                    <span class="cart__id--label">
                        <spring:theme code="basket.page.cartIdShort"/><span class="cart__id">${fn:escapeXml(cartData.code)}</span>
                    </span>
                </c:if>
            </h1>
        </div>
        <div class="col-xs-12 col-sm-5" style="float: right;">
            <c:if test="${not empty savedCartCount and savedCartCount ne 0}">
                <spring:url value="/my-account/saved-carts" var="listSavedCartUrl" htmlEscape="false"/>
                <a href="${listSavedCartUrl}" class="save__quote--link cart__head--link">
                    <spring:theme code="saved.cart.total.number" arguments="${savedCartCount}"/>
                </a>
            </c:if>
            <c:if test="${not empty quoteCount and quoteCount ne 0}">
                <spring:url value="/my-account/my-quotes" var="listQuotesUrl" htmlEscape="false"/>
                    <a href="${listQuotesUrl}" class="cart__quote--link cart__head--link">
                        <span class="glyphicon glyphicon-flash"></span><spring:theme code="saved.quote.total.number" arguments="${quoteCount}"/>
                    </a>
            </c:if>
            <a href="${newCartUrl}" class="new__quote--link cart__head--link">
                <span class="glyphicon glyphicon-floppy-disk"></span><spring:theme code="quote.edit.done" />
            </a>
        </div>
    </div>
</div>
