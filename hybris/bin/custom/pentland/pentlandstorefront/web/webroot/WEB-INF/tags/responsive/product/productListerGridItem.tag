<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="product" required="true" type="de.hybris.platform.commercefacades.product.data.ProductData" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="action" tagdir="/WEB-INF/tags/responsive/action" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<spring:theme code="text.addToCart" var="addToCartText"/>
<c:url value="${product.url}" var="productUrl"/>
<c:set value="${not empty product.potentialPromotions}" var="hasPromotion"/>
<div class="product-item">
	<ycommerce:testId code="product_wholeProduct">
		<input type="hidden" name="ean" value="${product.code}"/>
		<input type="hidden" name="stylecode" value="${product.baseProduct}"/>
		<input type="hidden" name="materialKey" value="${product.materialKey}"/>
		<input type="hidden" name="upc" value="${product.upc}"/>
		<input type="hidden" name="price" value="<format:price priceData="${product.price}"/>"/>
		<a class="thumb" href="${productUrl}" title="${fn:escapeXml(product.name)}">
			<product:productPrimaryImage product="${product}" format="product"/>
		</a>
		<div class="details">
			<ycommerce:testId code="product_productName">
				<a class="name" href="${productUrl}">
					<c:out escapeXml="false" value="${ycommerce:sanitizeHTML(product.name)}" />
				</a>
			</ycommerce:testId>
		
			<c:if test="${not empty product.potentialPromotions}">
				<div class="promo">
					<c:forEach items="${product.potentialPromotions}" var="promotion">
						${ycommerce:sanitizeHTML(promotion.description)}
					</c:forEach>
				</div>
			</c:if>
			
			<ycommerce:testId code="product_productPrice">
				<div class="price"><product:productListerItemPrice product="${product}"/></div>
			</ycommerce:testId>
			<c:forEach var="variantOption" items="${product.variantOptions}" begin="0" end="${product.variantOptions.size() > 5 ? 4 : product.variantOptions.size()}">
				<c:forEach items="${variantOption.variantOptionQualifiers}" var="variantOptionQualifier">
					<c:if test="${variantOptionQualifier.qualifier eq 'rollupProperty'}">
	                    <c:set var="rollupProperty" value="${variantOptionQualifier.value}"/>
	                </c:if>
					<c:if test="${variantOptionQualifier.qualifier eq 'thumbnail'}">
	                    <c:set var="imageUrl" value="${fn:escapeXml(variantOptionQualifier.value)}"/>
	                </c:if>
	                <c:if test="${variantOptionQualifier.qualifier eq rollupProperty}">
	                    <c:set var="variantName" value="${fn:escapeXml(variantOptionQualifier.value)}"/>
	                </c:if>
				</c:forEach>
				<img style="width: 32px; height: 32px;" src="${imageUrl}" title="${variantName}" alt="${variantName}"/>
			</c:forEach>
			<c:if test="${product.variantOptions.size() > 5}">
				<p><spring:theme code="product.grid.variants.more"/></p>
			</c:if>
			<c:if test="${product.clearance}">
				<p><spring:theme code="product.grid.variants.clearance"/></p>
			</c:if>
		</div>


		<c:set var="product" value="${product}" scope="request"/>
		<c:set var="addToCartText" value="${addToCartText}" scope="request"/>
		<c:set var="addToCartUrl" value="${addToCartUrl}" scope="request"/>
		<c:set var="isGrid" value="true" scope="request"/>
		<div class="addtocart">
			<div class="actions-container-for-${fn:escapeXml(component.uid)} <c:if test="${ycommerce:checkIfPickupEnabledForStore() and product.availableForPickup}"> pickup-in-store-available</c:if>">
				<action:actions element="div" parentComponent="${component}"/>
			</div>
		</div>
	</ycommerce:testId>
</div>