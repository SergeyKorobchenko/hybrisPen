<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%-- Verified that there's a pre-existing bug regarding the setting of showTax; created issue  --%>
<div class="row">
    <div class="col-sm-8">
        <%--<div class="cart-voucher">--%>
            <%--<cart:cartVoucher cartData="${cartData}"/>--%>
        <%--</div>--%>
    </div>
    <div class="col-sm-4">
        <div class="cart-totals">
            <cart:cartTotals cartData="${cartData}" showTax="false"/>
            <cart:ajaxCartTotals/>
        </div>

        <div class="cart-totals-taxes text-right">
            <spring:theme code="basket.page.totals.noNetTax"/>
        </div>

    </div>
</div>