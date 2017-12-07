<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<h1><spring:theme code="text.account.hidePrices"/></h1>

<p><spring:theme code="text.account.hidePrices.notice"/></p>

<div class="account-section-content">
    <div class="account-section-form">
        <c:url var="submitForm" value="/my-account/show-hide-prices/set"/>
        <form id="showHidePrices" method="get" action="${submitForm}">
            <div class="radio">
                <label><input type="radio" name="hidePrices" value="false" <c:if test="${not hidePricesForUser}">checked</c:if>><spring:theme code="text.account.hidePrices.show"/></label>
            </div>
            <div class="radio">
                <label><input type="radio" name="hidePrices" value="true" <c:if test="${ hidePricesForUser}">checked</c:if>><spring:theme code="text.account.hidePrices.hide"/></label>
            </div>
            <div class="row">
                <div class="col-sm-6 col-md-3">
                    <button type="submit" class="btn btn-primary btn-block">
                        <spring:theme code="text.button.save" text="Cancel" />
                    </button>
                </div>
            </div>
        </form>
    </div>
</div>