<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="order" required="true" type="de.hybris.platform.commercefacades.order.data.OrderData" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="action" tagdir="/WEB-INF/tags/responsive/action" %>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="row">
    <div class="col-sm-12 col-md-12 col-no-padding">
        <div class="row">
            <div class="col-sm-3 item-wrapper">
                <div class="item-group">
                    <ycommerce:testId code="orderDetail_overviewOrderID_label">
                        <span class="item-label"><spring:theme code="text.account.orderHistory.orderNumber"/></span>
                        <span class="item-value">${fn:escapeXml(order.code)}</span>
                    </ycommerce:testId>
                </div>
                <div class="item-group">
                    <ycommerce:testId code="orderDetail_PONumber_label">
                        <span class="item-label"><spring:theme code="text.account.orderHistory.POnumber"/></span>
                        <span class="item-value">${fn:escapeXml(order.purchaseOrderNumber)}</span>
                    </ycommerce:testId>
                </div>
                <div class="item-group">
                    <ycommerce:testId code="orderDetail_overviewOrderStatus_label">
                        <span class="item-label"><spring:theme code="text.account.orderHistory.orderStatus"/></span>
                        <c:if test="${not empty orderData.statusDisplay}">
                            <span class="item-value"><spring:theme code="text.account.order.status.display.${fn:escapeXml(orderData.statusDisplay)}"/></span>
                        </c:if>
                    </ycommerce:testId>
                </div>
            </div>
            <div class="col-sm-3 item-wrapper">
                <div class="item-group">
                    <ycommerce:testId code="orderDetail_creationDate_label">
                        <span class="item-label"><spring:theme code="text.account.orderHistory.creationDate"/></span>
                        <span class="item-value"><fmt:formatDate value="${order.created}" dateStyle="medium" type="date"/></span>
                    </ycommerce:testId>
                </div>
                <div class="item-group">
                    <ycommerce:testId code="orderDetail_RDD_label">
                        <span class="item-label"><spring:theme code="text.account.orderHistory.rdd"/></span>
                        <span class="item-value"><fmt:formatDate value="${order.rdd}" dateStyle="medium" type="date"/></span>
                    </ycommerce:testId>
                </div>
                <div class="item-group">
                    <ycommerce:testId code="orderDetail_overviewTotalQuantity_label">
                        <span class="item-label"><spring:theme code="text.account.orderHistory.totalQty"/></span>
                        <span class="item-value">${fn:escapeXml(order.totalUnitCount)}</span>
                    </ycommerce:testId>
                </div>
            </div>
            <div class="col-sm-3 item-wrapper">
                <div class="item-group">
                    <ycommerce:testId code="orderDetail_overviewOrderNet_label">
                        <span class="item-label"><spring:theme code="text.account.order.net"/></span>
                        <span class="item-value"><format:price priceData="${order.subTotal}"/></span>
                    </ycommerce:testId>
                </div>
                <div class="item-group">
                    <ycommerce:testId code="orderDetail_overviewOrderTax_label">
                        <span class="item-label"><spring:theme code="text.account.order.tax"/></span>
                        <span class="item-value"><format:price priceData="${order.totalTax}"/></span>
                    </ycommerce:testId>
                </div>
                <div class="item-group">
                    <ycommerce:testId code="orderDetail_overviewOrderTotal_label">
                        <span class="item-label"><spring:theme code="text.account.order.total"/></span>
                        <span class="item-value"><format:price priceData="${order.totalPriceWithTax}"/></span>
                    </ycommerce:testId>
                </div>
            </div>
            <div class="col-sm-3 item-action">
                <c:set var="orderCode" value="${orderData.code}" scope="request"/>
                <action:actions element="div" parentComponent="${component}"/>
            </div>
        </div>
    </div>


</div>

