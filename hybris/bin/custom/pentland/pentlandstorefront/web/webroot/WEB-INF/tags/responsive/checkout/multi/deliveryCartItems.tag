<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="cartData" required="true" type="de.hybris.platform.commercefacades.order.data.CartData" %>
<%@ attribute name="showDeliveryAddress" required="true" type="java.lang.Boolean" %>
<%@ attribute name="showPotentialPromotions" required="false" type="java.lang.Boolean" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="grid" tagdir="/WEB-INF/tags/responsive/grid" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<c:set var="hasShippedItems" value="${cartData.deliveryItemsQuantity > 0}" />
<c:set var="deliveryAddress" value="${cartData.deliveryAddress}"/>

<c:if test="${not hasShippedItems}">
	<spring:theme code="checkout.pickup.no.delivery.required"/>
</c:if>

<ul class="checkout-order-summary-list">
<c:if test="${hasShippedItems}">
	<li class="checkout-order-summary-list-heading">
		<c:choose>
			<c:when test="${showDeliveryAddress and not empty deliveryAddress}">
				<div class="title"><spring:theme code="checkout.pickup.items.to.be.shipped"/></div>
				<div class="address">
						${fn:escapeXml(deliveryAddress.sapId)}&nbsp;${fn:escapeXml(deliveryAddress.displayName)}
					<br>
					<c:if test="${ not empty deliveryAddress.line1 }">
						${fn:escapeXml(deliveryAddress.line1)},&nbsp;
					</c:if>
					<c:if test="${ not empty deliveryAddress.line2 }">
						${fn:escapeXml(deliveryAddress.line2)},&nbsp;
					</c:if>
					<c:if test="${not empty deliveryAddress.town }">
						${fn:escapeXml(deliveryAddress.town)},&nbsp;
					</c:if>
					<c:if test="${ not empty deliveryAddress.region.name }">
						${fn:escapeXml(deliveryAddress.region.name)},&nbsp;
					</c:if>
					<c:if test="${ not empty deliveryAddress.postalCode }">
						${fn:escapeXml(deliveryAddress.postalCode)},&nbsp;
					</c:if>
					<c:if test="${ not empty deliveryAddress.country.name }">
						${fn:escapeXml(deliveryAddress.country.name)}
					</c:if>
                    <br/>
					<c:if test="${ not empty deliveryAddress.phone }">
						${fn:escapeXml(deliveryAddress.phone)}
					</c:if>
				</div>
				<c:if test="${not empty cartData.markForAddress}">
					<div class="title"><spring:theme code="checkout.multi.summary.markFor"/></div>
					<div class="address">
							${fn:escapeXml(cartData.markForAddress.sapId)}&nbsp;${fn:escapeXml(cartData.markForAddress.displayName)}
						<br>
						<c:if test="${ not empty cartData.markForAddress.line1 }">
							${fn:escapeXml(cartData.markForAddress.line1)},&nbsp;
						</c:if>
						<c:if test="${ not empty cartData.markForAddress.line2 }">
							${fn:escapeXml(cartData.markForAddress.line2)},&nbsp;
						</c:if>
						<c:if test="${not empty cartData.markForAddress.town }">
							${fn:escapeXml(cartData.markForAddress.town)},&nbsp;
						</c:if>
						<c:if test="${ not empty cartData.markForAddress.region.name }">
							${fn:escapeXml(cartData.markForAddress.region.name)},&nbsp;
						</c:if>
						<c:if test="${ not empty cartData.markForAddress.postalCode }">
							${fn:escapeXml(cartData.markForAddress.postalCode)},&nbsp;
						</c:if>
						<c:if test="${ not empty cartData.markForAddress.country.name }">
							${fn:escapeXml(cartData.markForAddress.country.name)}
						</c:if>
						<br/>
						<c:if test="${ not empty cartData.markForAddress.phone }">
							${fn:escapeXml(cartData.markForAddress.phone)}
						</c:if>
					</div>
				</c:if>
			</c:when>
			<c:otherwise>
				<spring:theme code="checkout.pickup.items.to.be.delivered" />
			</c:otherwise>
		</c:choose>

	</li>
</c:if>

<c:forEach items="${cartData.entries}" var="entry" varStatus="loop">
	<c:if test="${entry.deliveryPointOfService == null}">
		<c:url value="${entry.product.url}" var="productUrl"/>
		<li class="checkout-order-summary-list-items">
			<div class="thumb">
				<a href="${productUrl}">
					<product:productPrimaryImage product="${entry.product}" format="thumbnail"/>
				</a>
			</div>
			<div class="price"><format:price priceData="${entry.totalPrice}" displayFreeForZero="true"/></div>
			<div class="details">
				<div class="name"><a href="${productUrl}">${fn:escapeXml(entry.product.name)}</a></div>
				<div>
                    <span class="label-spacing"><spring:theme code="order.itemPrice" />:</span>
					<format:price priceData="${entry.erpPrice}" />
				</div>
				<div class="qty"><span><spring:theme code="basket.page.qty"/>:</span>${entry.quantity}</div>
				<div>
					<c:forEach items="${entry.product.baseOptions}" var="option">
						<c:if test="${not empty option.selected and option.selected.url eq entry.product.url}">
							<c:forEach items="${option.selected.variantOptionQualifiers}" var="selectedOption">
								<div>${fn:escapeXml(selectedOption.name)}: ${fn:escapeXml(selectedOption.value)}</div>
							</c:forEach>
						</c:if>
					</c:forEach>

					<c:if test="${ycommerce:doesPotentialPromotionExistForOrderEntryOrOrderEntryGroup(cartData, entry) && showPotentialPromotions}">
                        <c:forEach items="${cartData.potentialProductPromotions}" var="promotion">
                            <c:set var="displayed" value="false"/>
                            <c:forEach items="${promotion.consumedEntries}" var="consumedEntry">
                                <c:if test="${not displayed && ycommerce:isConsumedByEntry(consumedEntry,entry)}">
                                    <c:set var="displayed" value="true"/>
                                    <span class="promotion">${ycommerce:sanitizeHTML(promotion.description)}</span>
                                </c:if>
                            </c:forEach>
                        </c:forEach>
					</c:if>

					<c:if test="${ycommerce:doesAppliedPromotionExistForOrderEntryOrOrderEntryGroup(cartData, entry)}">
                        <c:forEach items="${cartData.appliedProductPromotions}" var="promotion">
                            <c:set var="displayed" value="false"/>
                            <c:forEach items="${promotion.consumedEntries}" var="consumedEntry">
                                <c:if test="${not displayed && ycommerce:isConsumedByEntry(consumedEntry,entry)}">
                                    <c:set var="displayed" value="true"/>
                                    <span class="promotion">${ycommerce:sanitizeHTML(promotion.description)}</span>
                                </c:if>
                            </c:forEach>
                        </c:forEach>
					</c:if>
					<common:configurationInfos entry="${entry}"/>
				</div>
				<c:if test="${entry.product.multidimensional}" >
					<a href="#" id="QuantityProductToggle" data-index="${loop.index}" class="showQuantityProductOverlay updateQuantityProduct-toggle">
						<span><spring:theme code="order.product.seeDetails"/></span>
					</a>
				</c:if>

				<spring:url value="/checkout/multi/getReadOnlyProductVariantMatrix" var="targetUrl" htmlEscape="false"/>
				<grid:gridWrapper entry="${entry}" index="${loop.index}" styleClass="display-none"
					targetUrl="${targetUrl}"/>
			</div>
		</li>
	</c:if>
</c:forEach>

</ul>
