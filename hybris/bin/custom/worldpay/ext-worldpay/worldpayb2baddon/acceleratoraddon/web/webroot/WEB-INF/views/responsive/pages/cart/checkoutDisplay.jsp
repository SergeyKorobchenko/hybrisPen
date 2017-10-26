<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<c:url value="/cart/checkout" var="checkoutUrl" scope="session"/>
<div class="row">
    <div class="col-xs-12 col-sm-10 col-md-7 col-lg-6 pull-right cart-actions--print">
        <div class="express-checkout">
            <div class="headline"><spring:theme code="text.expresscheckout.header"/></div>
            <strong><spring:theme code="text.expresscheckout.title"/></strong>
            <ul>
                <li><spring:theme code="text.expresscheckout.line1"/></li>
                <li><spring:theme code="text.expresscheckout.line2"/></li>
                <li><spring:theme code="text.expresscheckout.line3"/></li>
            </ul>
            <sec:authorize access="isFullyAuthenticated()">
            <c:if test="${expressCheckoutAllowed}">
                <div class="checkbox">
                    <label>
                        <c:url value="/checkout/multi/worldpay/summary/express" var="expressCheckoutUrl" scope="session"/>
                        <input type="checkbox" class="express-checkout-checkbox" data-express-checkout-url="${expressCheckoutUrl}">
                        <spring:theme text="I would like to Express checkout" code="cart.expresscheckout.checkbox"/>
                    </label>
                </div>
            </c:if>
            </sec:authorize>
        </div>
    </div>
</div>

<div class="cart__actions">
    <div class="row">
        <div class="col-sm-4 col-md-3 pull-right">
            <ycommerce:testId code="checkoutButton">
                <button class="btn btn-primary btn-block btn--continue-checkout js-continue-checkout-button" data-checkout-url="${checkoutUrl}">
                    <spring:theme code="checkout.checkout"/>
                </button>
            </ycommerce:testId>
        </div>

        <sec:authorize access="!hasAnyRole('ROLE_ANONYMOUS')">
            <c:if test="${not empty siteQuoteEnabled and siteQuoteEnabled eq 'true'}">
                <div class="col-sm-4 col-md-3 col-md-offset-3 pull-right">
                    <button class="btn btn-default btn-block btn--continue-shopping js-continue-shopping-button"    data-continue-shopping-url="${createQuoteUrl}">
                        <spring:theme code="quote.create"/>
                    </button>
                </div>
            </c:if>
        </sec:authorize>

        <div class="col-sm-4 col-md-3 pull-right">
            <button class="btn btn-default btn-block btn--continue-shopping js-continue-shopping-button" data-continue-shopping-url="${continueShoppingUrl}">
                <spring:theme code="cart.page.continue"/>
            </button>
        </div>
    </div>
</div>
