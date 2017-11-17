<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="grid" tagdir="/WEB-INF/tags/responsive/grid" %>

<div class="product-details page-title">
	<ycommerce:testId code="productDetails_productNamePrice_label_${product.code}">
		<div class="name">${fn:escapeXml(product.name)}<span class="sku"><spring:theme code="product.sku"/></span><span class="code">${fn:escapeXml(product.materialKey)}</span></div>
	</ycommerce:testId>
</div>
<div class="row">

	<div class="col-xs-10 col-xs-push-1 col-sm-6 col-sm-push-0 col-lg-4">
		<product:productImagePanel galleryImages="${galleryImages}" />
		<c:if test="${not empty product.videoURL}">
			<div class=""><spring:theme code="product.videourl"/> :<a href="${fn:escapeXml(product.videoURL)}">${fn:escapeXml(product.videoURL)}</a></div>
		</c:if>
		<c:if test="${not empty product.externalURL}">
			<div class=""><spring:theme code="product.externalurl"/>: <a href="${fn:escapeXml(product.externalURL)}">${fn:escapeXml(product.externalURL)}</a></div>
		</c:if>
	</div>

	<div class="clearfix hidden-sm hidden-md hidden-lg"></div>
	<div class="col-sm-6 col-lg-8">
		<div class="product-main-info">
			<div class="row">
				<div class="col-lg-6">
					<div class="product-details">
						<c:if test="${not empty product.brandName}">
							<div class=""><spring:theme code="product.brand"/>: ${fn:escapeXml(product.brandName)}</div>
						</c:if>
						<p class="price">
							<spring:theme code="product.customer.price"/>: <format:price priceData="${product.customerPrice}"/>
						</p>
						<ycommerce:testId code="productDetails_productNamePrice_label_${product.code}">
							<product:productPricePanel product="${product}" />
						</ycommerce:testId>
						<c:if test="${not empty product.season}">
							<div class=""><spring:theme code="product.season"/>: ${fn:escapeXml(product.season)}</div>
						</c:if>

					</div>
					<cms:pageSlot position="VariantSelector" var="component" element="div" class="page-details-variants-select">
						<cms:component component="${component}" element="div" class="yComponentWrapper page-details-variants-select-component"/>
					</cms:pageSlot>
					<c:if test="${not empty product.sizeChartGuide}">
						<div class="name"><spring:theme code="product.sizeChartGuide"/>: ${fn:escapeXml(product.sizeChartGuide.url)}</div>
					</c:if>
					<c:if test="${not empty product.unit}">
						<div class="name"><spring:theme code="product.unit"/>: ${fn:escapeXml(product.unit)}</div>
					</c:if>
					<cms:pageSlot position="AddToCart" var="component" element="div" class="page-details-variants-select">
						<cms:component component="${component}" element="div" class="yComponentWrapper page-details-add-to-cart-component"/>
					</cms:pageSlot>
					<c:if test="${not empty product.description or not empty product.featureDescription}" >
						<div id="tabs">
							<ul>
								<c:if test="${not empty product.description}">
									<li><a href="#tabs-1"><spring:theme code="product.description"/></a></li>
								</c:if>
								<c:if test="${not empty product.featureDescription}">
									<li><a href="#tabs-2"><spring:theme code="product.features"/></a></li>
								</c:if>
							</ul>
							<c:if test="${not empty product.description}">
								<div id="tabs-1">
									<p>${ycommerce:sanitizeHTML(product.description)}</p>
								</div>
							</c:if>
							<c:if test="${not empty product.featureDescription}">
								<div id="tabs-2">
									<p>${ycommerce:sanitizeHTML(product.featureDescription)}</p>
								</div>
							</c:if>
						</div>
					</c:if>
				</div>

<%--				<div class="col-sm-12 col-md-9 col-lg-6">

				</div>--%>
			</div>
		</div>

	</div>
	<div class="clearfix hidden-sm hidden-md hidden-lg"></div>
	<div>
		<li>
			<spring:url value="getProductVariantMatrix" var="targetUrl"/>
			<grid:productGridWrapper styleClass="add-to-cart-order-form-wrap display-none"
							  targetUrl="${targetUrl}" product="${product}"/>
		</li>
	</div>
</div>

<product:productOrderFormJQueryTemplates />