<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%-- Verified that there's a pre-existing bug regarding the setting of showTax; created issue  --%>
<div class="row">
    <div class="col-sm-8">
        <%--<div class="cart-voucher">--%>
            <%--<cart:cartVoucher cartData="${cartData}"/>--%>
        <%--</div>--%>
    </div>
    <div class="col-sm-4">
     <c:if test="${not empty cartData.surCharge}">
     <div class="cart-totals row">
    <div class="col-xs-6 cart-totals-left grand-total"><spring:theme code="basket.page.surcharge"/></div>
     <div class="col-xs-6 cart-totals-right text-right grand-total">
        <ycommerce:testId code="cart_totalPrice_label">
            <format:price priceData="${cartData.surCharge}"/>
        </ycommerce:testId>
    </div>
    </div>
     <div class="cart-totals-taxes text-right" style="margin-left:-163px;">
            <spring:theme code="basket.page.totals.included.surcharges"/>
        </div>
    
    </c:if>
        <div class="cart-totals">
            <cart:cartTotals cartData="${cartData}" showTax="false"/>
            <cart:ajaxCartTotals/>
        </div>

        
        <div class="cart-totals-taxes text-right">
            <spring:theme code="basket.page.totals.noNetTax"/>
        </div>

    </div>
</div>