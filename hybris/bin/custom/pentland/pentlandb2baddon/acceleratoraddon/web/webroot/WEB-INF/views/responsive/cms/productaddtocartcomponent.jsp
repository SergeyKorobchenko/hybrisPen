<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="action" tagdir="/WEB-INF/tags/responsive/action" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="b2b-product" tagdir="/WEB-INF/tags/addons/pentlandb2baddon/responsive/product" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<c:set var="maxQty" value="FORCE_IN_STOCK"/>

<c:set var="qtyMinus" value="0" />
		
<div class="addtocart-component">
            <input type="hidden" maxlength="3" class="form-control js-qty-selector-input" size="1" value="${qtyMinus}" data-max="${maxQty}" data-min="1" name="pdpAddtoCartInput"  id="pdpAddtoCartInput" />

<%--    <div class="stock-wrapper clearfix">
        <ycommerce:testId code="productDetails_productInStock_label">
            <b2b-product:productStockThreshold product="${product}"/>
        </ycommerce:testId>
        <product:productFutureAvailability product="${product}" futureStockEnabled="${futureStockEnabled}" />
    </div>--%>

    <div class="actions">
        <action:actions element="div"  parentComponent="${component}"/>
        <c:if test="${empty showAddToCart ? true : showAddToCart}">
            <c:url value="${product.url}#orderForm" var="productOrderFormUrl"/>
            <a href="${productOrderFormUrl}" class="btn btn-default btn-block btn-icon js-add-to-cart glyphicon-list-alt">
                <spring:theme code="order.form" />
            </a>
        </c:if>
    </div>
</div>