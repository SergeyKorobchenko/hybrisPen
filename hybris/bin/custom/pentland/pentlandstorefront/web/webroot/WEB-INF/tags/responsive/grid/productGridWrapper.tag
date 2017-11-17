<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="styleClass" required="false" type="java.lang.String"%>
<%@ attribute name="targetUrl" required="true" type="java.lang.String" %>
<%@ attribute name="product" required="true" type="de.hybris.platform.commercefacades.product.data.ProductData" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>

<div class="product-action" style="display:none" id="productGridAction">
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

<div id="ajaxGrid" class="${styleClass}"></div>
<div style="display:none" id="grid" data-target-url="${targetUrl}" data-product-code="${product.code}"></div>