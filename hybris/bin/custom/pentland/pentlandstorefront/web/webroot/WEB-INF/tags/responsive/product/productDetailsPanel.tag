<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="grid" tagdir="/WEB-INF/tags/responsive/grid" %>
<script>

</script>
<div class="product-details page-title">
	<ycommerce:testId code="productDetails_productNamePrice_label_${product.code}">
		<div class="name">${fn:escapeXml(product.name)}<span class="sku"><spring:theme code="product.sku"/></span><span class="code">${fn:escapeXml(product.materialKey)}</span></div>
	</ycommerce:testId>
</div>
<div class="row">

	<div class="col-xs-10 col-xs-push-1 col-sm-6 col-sm-push-0 col-lg-4">
		<product:productImagePanel galleryImages="${galleryImages}" />
		<c:if test="${not empty product.videoURL}">
			<div class=""><spring:theme code="product.videourl"/>: <span data-toggle="modal" data-target="#videoURLModal"><a href="javascript:void(0);">${fn:escapeXml(product.videoURL)}</a></span></div>
		</c:if>
		<c:if test="${not empty product.externalURL}">
			<div class=""><spring:theme code="product.externalurl"/>: <a target="_blank" href="${fn:escapeXml(product.externalURL)}">${fn:escapeXml(product.externalURL)}</a></div>
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
						<c:url value="${product.sizeChartGuide.url}" var="sizeChartGuideUrl" />
						<div class="name" data-toggle="modal" data-target="#sizeChartModal" ><a href="javascript:void(0);"><spring:theme code="product.sizeChartGuide"/></a></div>
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

<%--				<div class="col-sm-12 col-md-9 col-lg-6">

				</div>--%>
			</div>
		</div>

	</div>

</div>
<div  class="row" >
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
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
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