<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<spring:htmlEscape defaultHtmlEscape="true" />
<c:url value="/cart/checkout" var="checkoutUrl" scope="session"/>
<c:url value="/quote/create" var="createQuoteUrl" scope="session"/>
<c:url value="${continueUrl}" var="continueShoppingUrl" scope="session"/>
<div class="row">

    <div class="col-sm-4 col-md-3 pull-right">
        <ycommerce:testId code="checkoutButton">
            <button class="btn btn-primary btn-block btn--continue-checkout js-continue-checkout-button" data-checkout-url="${checkoutUrl}">
                <spring:theme code="checkout.checkout"/>
            </button>
        </ycommerce:testId>
    </div>

    <c:if test="${not empty siteQuoteEnabled and siteQuoteEnabled eq 'true'}">
        <div class="col-sm-4 col-md-3 pull-right">
            <button class="btn btn-default btn-block btn-create-quote js-create-quote-button" data-create-quote-url="${createQuoteUrl}">
                <spring:theme code="quote.create"/>
            </button>
        </div>
    </c:if>

    <div class="col-sm-4 col-md-3 pull-right">
        <button class="btn btn-default btn-block btn--savecart-checkout js-savecart-checkout-button">
            <spring:theme code="checkout.savecart"/>
        </button>
    </div>

    <div class="col-sm-4 col-md-3">
        <button class="btn btn-default btn-block btn--continue-shopping js-continue-shopping-button" data-continue-shopping-url="${continueShoppingUrl}">
            <spring:theme code="cart.page.continue"/>
        </button>
    </div>
</div>
