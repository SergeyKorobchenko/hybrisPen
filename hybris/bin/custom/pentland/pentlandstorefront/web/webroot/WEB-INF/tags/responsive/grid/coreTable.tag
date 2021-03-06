<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="variants" required="true" type="java.util.List"%>
<%@ attribute name="inputTitle" required="true" type="java.lang.String"%>
<%@ attribute name="loopIndex" required="true" type="java.lang.Integer"%>
<%@ attribute name="readOnly" required="false" type="java.lang.Boolean"%>
<%@ attribute name="product" required="false" type="de.hybris.platform.commercefacades.product.data.ProductData" %>
<%@ attribute name="skusId" required="true" type="java.lang.String"%>
<%@ attribute name="firstVariant" required="true" type="de.hybris.platform.commercefacades.product.data.VariantMatrixElementData" %>
<%@ attribute name="showName" required="false" type="java.lang.Boolean"%>
<%@ attribute name="showLastDimensionTitle" required="false" type="java.lang.Boolean"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="grid" tagdir="/WEB-INF/tags/responsive/grid" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<c:if test="${not empty sessionScope.skuIndex}">
	<c:set var="loopIndex" value="${sessionScope.skuIndex}"/>
</c:if>

<c:set var="inputDisabled" value=""/>

<c:if test="${readOnly == true}">
    <c:set var="inputDisabled" value="disabled"/>
</c:if>

<c:set var="colspan" value="1"/>
<c:if test="${firstVariant.maxVariants > firstVariant.elements.size()}">
    <c:set var="colspan" value="${firstVariant.maxVariants - firstVariant.elements.size() + 1}"/>
</c:if>

<tr class="hidden-size">
    <th style="width: 200px;">
        <spring:theme code="order.form.color"/>
    </th>
    <c:forEach items="${variants}" var="variant" varStatus="loop">
        <th style="max-width: 200px;">${fn:escapeXml(variant.variantName)}</th>
    </c:forEach>
    <th colspan="${colspan}"></th>
</tr>

<tr>
    <td class="variant-detail" data-variant-property="${fn:escapeXml(variants[0].variantName)}">
        <div>
            <c:forEach items="${firstVariant.variantOption.variantOptionQualifiers}" var="qualifierData">
                <c:if test="${qualifierData.image.format eq 'thumbnail'}">
                    <img src="${qualifierData.image.url}" alt="${firstVariant.variantName}" />
                </c:if>
            </c:forEach>
            <br/><br/>
        </div>
        <div class="variant-code">
            <div>${fn:escapeXml(firstVariant.variantCode)}</div>
        </div>
        <div class="description">
            <div>${fn:escapeXml(firstVariant.variantName)}</div>
        </div>
        <common:hidePricesForUser>
            <div class="price">
                <div>${fn:escapeXml(firstVariant.variantOption.priceData.formattedValue)}</div>
            </div>
        </common:hidePricesForUser>
        <c:if test="${empty firstVariant.variantOption.priceData}">
            <c:set var="inputDisabled" value="disabled"/>
        </c:if>
    </td>

    <c:set var="colspan" value="1"/>
    <c:forEach items="${variants}" var="variant" varStatus="loop">
        <c:if test="${loop.last}">
            <c:if test="${firstVariant.maxVariants > firstVariant.elements.size()}">
                <c:set var="colspan" value="${firstVariant.maxVariants - firstVariant.elements.size() + 1}"/>
            </c:if>
        </c:if>
        <c:set var="cssStockClass" value=""/>
        <%--<c:if test="${variant.variantOption.stock.stockLevel <= 0}">--%>
            <%--<c:set var="cssStockClass" value="out-of-stock"/>--%>
        <%--</c:if>--%>

        <td class="${cssStockClass} widthReference hidden-xs" style="width: 200px;">

            <%--<div class="variant-prop hidden-sm hidden-md hidden-lg" data-variant-prop="${fn:escapeXml(variant.variantValueCategory.name)}">--%>
                <%--<span>${fn:escapeXml(variants[0].parentVariantCategory.name)}:</span>--%>
                <%--${fn:escapeXml(variant.variantValueCategory.name)}--%>
            <%--</div>--%>

            <%--<span class="price" data-variant-price="${variant.variantOption.priceData.value}">--%>
                <%--<c:set var="disableForOutOfStock" value="${inputDisabled}"/>--%>
                <%--<format:price priceData="${variant.variantOption.priceData}"/>--%>
            <%--</span>--%>
            <input type="hidden" id="productPrice[${loopIndex}]" value="${firstVariant.variantOption.priceData.value}" />

            <%--<c:if test="${variant.variantOption.stock.stockLevel == 0}">--%>
                <%--<c:set var="disableForOutOfStock" value="disabled"/>--%>
            <%--</c:if>--%>

            <input type="hidden" class="${fn:escapeXml(skusId)} sku" name="cartEntries[${loopIndex}].sku" id="cartEntries[${loopIndex}].sku" value="${fn:escapeXml(variant.variantOption.code)}" />
                <span class="block">
                    <label for="cartEntries[${loopIndex}].quantity">
                        <c:choose>
                            <c:when test="${variant.packSize > 0}">
                                <spring:theme code="order.form.moq" arguments="${variant.packSize}"/>
                            </c:when>
                            <c:otherwise>
                                <spring:theme code="order.form.moq.any"/>
                            </c:otherwise>
                        </c:choose>
                    </label>
                </span>

                    <input type="textbox" maxlength="9" class="sku-quantity" data-instock="${variant.variantOption.stock.stockLevel}"  data-variant-id="variant_${loopIndex}"
                           name="cartEntries[${loopIndex}].quantity" data-product-selection='{"product":"${fn:escapeXml(variant.variantOption.code)}"}' data-current-value=""
                           id="cartEntries[${loopIndex}].quantity" data-pack-size="${variant.packSize}" value="0" ${disableForOutOfStock} data-parent-id="${fn:escapeXml(product.code)}"/>
                    <grid:coreTableStockRow variant="${variant}" />

            <%--<span class="data-grid-total" data-grid-total-id="total_value_${loopIndex}"></span>--%>

            <c:set var="loopIndex" value="${loopIndex +1}"/>
            <c:set var="skuIndex" scope="session" value="${sessionScope.skuIndex + 1}"/>
        </td>
    </c:forEach>
    <td colspan="${colspan}"></td>

    <td class="mobile-cart-actions hide">
        <a href="#" class="btn btn-primary closeVariantModal"><spring:theme code="popup.done"/></a>
    </td>

    <td class="variant-select hidden-sm hidden-md hidden-lg">
        <a href="#" class="variant-select-btn">
            <span class="selectSize"><spring:theme code="product.grid.selectSize"/>&nbsp;${fn:escapeXml(variants[0].parentVariantCategory.name)}</span>
            <span class="editSize"><spring:theme code="product.grid.editSize"/></span>
        </a>
    </td>
</tr>