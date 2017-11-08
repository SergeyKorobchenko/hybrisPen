<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<div class="product-details page-title">
	<ycommerce:testId code="productDetails_productNamePrice_label_${product.code}">
		<div class="name">${fn:escapeXml(product.name)}<span class="sku"><spring:theme code="product.sku"/></span><span class="code">${fn:escapeXml(product.materialKey)}</span></div>
	</ycommerce:testId>
</div>
<div class="row">

	<div class="col-xs-10 col-xs-push-1 col-sm-6 col-sm-push-0 col-lg-4">
		<product:productImagePanel galleryImages="${galleryImages}" />
	</div>
	<div class="name"><spring:theme code="product.videourl"/>${fn:escapeXml(product.videoURL)}</div>
	<div class="name"><spring:theme code="product.externalurl"/>${fn:escapeXml(product.externalURL)}</div>
	<div class="clearfix hidden-sm hidden-md hidden-lg"></div>
	<div class="col-sm-6 col-lg-8">
		<div class="product-main-info">
			<div class="row">
				<div class="col-lg-6">
					<div class="product-details">
						<div class="name"><spring:theme code="product.brand"/>${fn:escapeXml(product.brand)}</div>
						<p class="price">
							<spring:theme code="product.customer.price"/><format:fromPrice priceData="${product.customerPrice}"/>
						</p>
						<ycommerce:testId code="productDetails_productNamePrice_label_${product.code}">
							<product:productPricePanel product="${product}" />
						</ycommerce:testId>
						<div class="name"><spring:theme code="product.season"/>${fn:escapeXml(product.season)}</div>
						<div class="description">${ycommerce:sanitizeHTML(product.description)}</div>
						<div class="description">${ycommerce:sanitizeHTML(product.featureDescription)}</div>
					</div>
					<cms:pageSlot position="VariantSelector" var="component" element="div" class="page-details-variants-select">
						<cms:component component="${component}" element="div" class="yComponentWrapper page-details-variants-select-component"/>
					</cms:pageSlot>
					<cms:pageSlot position="AddToCart" var="component" element="div" class="page-details-variants-select">
						<cms:component component="${component}" element="div" class="yComponentWrapper page-details-add-to-cart-component"/>
					</cms:pageSlot>
					<div class="name"><spring:theme code="product.sizeChartGuide"/>${fn:escapeXml(product.sizeChartGuide.url)}</div>
					<div class="name"><spring:theme code="product.unit"/>${fn:escapeXml(product.unit)}</div>
				</div>

<%--				<div class="col-sm-12 col-md-9 col-lg-6">

				</div>--%>
			</div>
		</div>

	</div>
</div>