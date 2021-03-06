<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="grid" tagdir="/WEB-INF/tags/responsive/grid" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common" %>

<div class="row">

	<div class="col-xs-12 col-sm-12 col-md-6">
		<product:productImagePanel galleryImages="${galleryImages}" />

		<div class="product-details__external-links">
			<c:if test="${not empty product.videoURL}">
				<span data-toggle="modal" data-target="#videoURLModal"><a class="product-details__external-links--link" href="javascript:void(0);"><span class="glyphicon glyphicon-facetime-video"></span><spring:theme code="product.videourl"/></a></span>
			</c:if>

			<c:if test="${not empty product.externalURL}">
				<a class="product-details__external-links--link" target="_blank" href="${fn:escapeXml(product.externalURL)}"><span class="glyphicon glyphicon-link"></span><spring:theme code="product.externalurl"/></a>
			</c:if>
		</div>
	</div>

	<div class="clearfix hidden-sm hidden-md hidden-lg"></div>

	<div class="col-sm-12 col-md-6">
		<div class="product-details page-title">
			<ycommerce:testId code="productDetails_productNamePrice_label_${product.code}">
				<h1>${fn:escapeXml(product.name)}</h1>
			</ycommerce:testId>
		</div>

		<div class="product-main-info">
			<div class="row">
				<div class="col-lg-12">
					<div class="product-details">
						<p class="product-details__sub-info--id"><spring:theme code="product.sku"/>: ${fn:escapeXml(product.materialKey)}</p>
						<c:if test="${not empty product.brandName}">
							<p class="product-details__sub-info"><spring:theme code="product.brand"/>: ${fn:escapeXml(product.brandName)}</p>
						</c:if>
						<c:if test="${not empty product.season}">
							<p class="product-details__sub-info"><spring:theme code="product.season"/>: ${fn:escapeXml(product.season)}</p>
						</c:if>
						<c:if test="${not empty product.unit}">
							<p class="product-details__sub-info"><spring:theme code="product.unit"/>: ${fn:escapeXml(product.unit)}</p>
						</c:if>
					</div>

					<div class="product-details-price">
						<common:hidePricesForUser>
							<div class="price">
								<spring:theme code="product.customer.price"/>:&nbsp;<strong><format:price priceData="${product.customerPrice}"/></strong>
							</div>
							<ycommerce:testId code="productDetails_productNamePrice_label_${product.code}">
								<product:productPricePanel product="${product}" />
							</ycommerce:testId>
						</common:hidePricesForUser>
					</div>

					<cms:pageSlot position="VariantSelector" var="component" element="div" class="page-details-variants-select">
						<cms:component component="${component}" element="div" class="yComponentWrapper page-details-variants-select-component"/>
					</cms:pageSlot>

					<c:if test="${not empty product.sizeChartGuide}">
						<c:url value="${product.sizeChartGuide.url}" var="sizeChartGuideUrl" />
						<div class="size-chart-guide" data-toggle="modal" data-target="#sizeChartModal" ><a href="javascript:void(0);"><spring:theme code="product.sizeChartGuide"/></a></div>
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
								<div id="tabs-1" style="overflow:auto;height:200px">
									<p>${ycommerce:sanitizeHTML(product.description)}</p>
								</div>
							</c:if>
							<c:if test="${not empty product.featureDescription}">
								<div id="tabs-2" style="overflow:auto;height:200px">
									<p>${ycommerce:sanitizeHTML(product.featureDescription)}</p>
								</div>
							</c:if>
						</div>
					</c:if>
				</div>
			</div>
		</div>

	</div>

</div>
<div class="product-details-order-form">
	<spring:url value="getProductVariantMatrix" var="targetUrl"/>
	<grid:productGridWrapper styleClass="add-to-cart-order-form-wrap display-none"
							 targetUrl="${targetUrl}" product="${product}"/>
</div>

<div class="modal fade" id="videoURLModal" tabindex="-1" role="dialog" aria-labelledby="videoURLModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-body">
				<iframe id="iframeYoutube" width="560" height="315"  src="${fn:escapeXml(product.videoURL)}" frameborder="0" allowfullscreen></iframe>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal" onclick="$('#iframeYoutube').attr('src', $('#iframeYoutube').attr('src')); return false;">Close</button>
			</div>
		</div>
	</div>
</div>
<c:if test="${not empty product.sizeChartGuide}">
	<div id="sizeChartModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="sizeChartModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-body">
					<img src="${sizeChartGuideUrl}" class="img-responsive">
				</div>
			</div>
		</div>
	</div>
</c:if>