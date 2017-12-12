<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart" %>

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
    </div>
</div>