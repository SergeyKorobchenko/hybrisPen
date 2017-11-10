<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="order" required="true" type="de.hybris.platform.commercefacades.order.data.AbstractOrderData" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ attribute name="containerCSS" required="false" type="java.lang.String" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<div class="orderTotal">
    <div class="row">
        <div class="col-xs-6">
            <spring:theme code="text.account.order.subtotal"/>
        </div>
        <div class="col-xs-6">
            <div class="text-right">
                <ycommerce:testId code="orderTotal_subTotal_label">
                    <format:price priceData="${order.subTotal}"/>
                </ycommerce:testId>
            </div>
        </div>

            <div class="col-xs-6">
                <spring:theme code="text.account.order.netTax"/>
            </div>
            <div class="col-xs-6">
                <div class="text-right">
                    <format:price priceData="${order.totalTax}"/>
                </div>
            </div>

        <div class="col-xs-6">
            <div class="totals">
                <spring:theme code="text.account.order.orderTotals"/>
            </div>
        </div>
        <div class="col-xs-6">
            <div class="text-right totals">
                <format:price priceData="${order.totalPriceWithTax}"/>
            </div>
        </div>

    </div>
</div>