<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="b2b-order" tagdir="/WEB-INF/tags/addons/pentlandb2baddon/responsive/order" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:choose>
	<c:when test="${pageType eq 'ORDERCONFIRMATION'}">
		<c:choose>
			<c:when test="${empty sapOrders}">
				<div class="well well-tertiary well-single-headline">
					<div class="well-headline">
						<spring:theme code="checkout.multi.order.summary"/>&nbsp;${orderCode}
					</div>
				</div>

				<div class="well well-tertiary well-lg">
					<ycommerce:testId code="orderDetail_overview_section">
						<b2b-order:summaryOrderDetailsOverview order="${orderData}"/>
					</ycommerce:testId>
				</div>
			</c:when>
			<c:otherwise>
				<c:forEach var="order" items="${sapOrders}">
					<div class="well well-tertiary well-single-headline">
						<div class="well-headline">
							<spring:theme code="checkout.multi.order.summary"/>&nbsp;${order.code}
						</div>
					</div>

					<div class="well well-tertiary well-lg margin-bottom-25">
						<ycommerce:testId code="orderDetail_overview_section">
							<b2b-order:summaryOrderDetailsOverview order="${order}"/>
						</ycommerce:testId>
					</div>
				</c:forEach>
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:otherwise>
		<div class="well-lg well well-tertiary">
			<ycommerce:testId code="orderDetail_overview_section">
				<b2b-order:accountOrderDetailsOverview order="${orderData}"/>
			</ycommerce:testId>
		</div>
		<c:if test="${not empty orderData.placedBy}">
			<div class="alert alert-info order-placedby">
				<c:choose>
					<c:when test="${not empty agent}">
						<spring:theme code="text.account.order.placedBy" arguments="${orderData.placedBy}"/>
					</c:when>
					<c:otherwise>
						<spring:theme code="text.account.order.placedByText"/>
					</c:otherwise>
				</c:choose>
			</div>
		</c:if>
	</c:otherwise>
</c:choose>