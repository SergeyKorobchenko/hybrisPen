<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="styleClass" required="false" type="java.lang.String"%>
<%@ attribute name="targetUrl" required="true" type="java.lang.String" %>
<%@ attribute name="product" required="true" type="de.hybris.platform.commercefacades.product.data.ProductData" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="js-product-order-form display-none">
    <div class="back-link product-details">
            <div class="js-show-editable-product-form-grid" data-read-only-multid-grid="false">
                <span class="glyphicon no-border glyphicon-chevron-down"></span>
                <span><spring:theme code="order.form"/>&nbsp;</span>
            </div>
    </div>
    <div id="productShowGridAction">
    <div class="product-action" id="productGridAction">
        <div class="hidden-xs">
            <spring:theme code="product.grid.items.selected"/>&nbsp;
            <span class="js-total-items-count">0</span>
        </div>
        <div class="hidden-sm hidden-md hidden-lg">
            <spring:theme code="product.grid.formDescription"/>
        </div>
        <ol>
            <product:productFormAddToCartPanel product="${product}"/>
        </ol>
    </div>
    <div class="cart-totals-taxes text-right">
        <spring:theme code="basket.page.totals.noNetTax"/>
    </div>

    <div class="add-to-cart-order-form-wrap">
        <c:url value="/cart/addGrid" var="addToCartGridUrl"/>
        <spring:theme code="product.grid.confirmQtys.message" var="gridConfirmMessage"/>

        <form:form name="AddToCartOrderForm" id="AddToCartOrderForm" class="add_to_cart_order_form scrollContent visible"
                   action="${addToCartGridUrl}" method="post"
                   data-grid-confirm-message="${gridConfirmMessage}">
            <div id="ajaxGrid" class="${styleClass}"></div>
        </form:form>

        <div class="order-form-scroll right hidden-xs"><span class="glyphicon glyphicon-chevron-right"></span></div>
        <div class="order-form-scroll left hidden-xs"><span class="glyphicon glyphicon-chevron-left"></span></div>
        <div class="order-form-scroll up hidden-xs"><span class="glyphicon glyphicon-chevron-up"></span></div>
        <div class="order-form-scroll down hidden-xs"><span class="glyphicon glyphicon-chevron-down"></span></div>
    </div>

    </div>
    <div style="display:none" id="grid" data-target-url="${targetUrl}" data-product-code="${product.code}"></div>

    <product:productOrderFormJQueryTemplates/>
</div>
