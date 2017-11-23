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
                    <ycommerce:testId code="orderDetail_overviewOrderStatus_label">
                        <span class="item-label"><spring:theme code="text.account.orderHistory.orderStatus"/></span>
                        <c:if test="${not empty orderData.statusDisplay}">
                            <span class="item-value"><spring:theme code="text.account.order.status.display.${fn:escapeXml(orderData.statusDisplay)}"/></span>
                        </c:if>
                    </ycommerce:testId>
                </div>
                <div class="item-group">
                    <ycommerce:testId code="orderDetail_orderType_label">
                        <span class="item-label"><spring:theme code="text.account.orderHistory.orderType"/></span>
                        <span class="item-value">${fn:escapeXml(order.orderType)}</span>
                    </ycommerce:testId>
                </div>
            </div>
            <div class="col-sm-3 col-sm-offset-2 item-wrapper">
                <div class="item-group">
                    <ycommerce:testId code="orderDetail_creationDate_label">
                        <span class="item-label"><spring:theme code="text.account.orderhistory.dateplaced"/></span>
                        <span class="item-value"><fmt:formatDate value="${order.created}" dateStyle="medium" timeStyle="medium" type="both"/></span>
                    </ycommerce:testId>
                </div>
                <div class="item-group">
                    <ycommerce:testId code="orderDetail_placedBy_label">
                        <span class="item-label"><spring:theme code="text.account.order.user"/></span>
                        <span class="item-value">${fn:escapeXml(order.user.name)}</span>
                    </ycommerce:testId>
                </div>
                <div class="item-group">
                    <ycommerce:testId code="orderDetail_RDD_label">
                        <span class="item-label"><spring:theme code="text.account.orderHistory.rdd"/></span>
                        <span class="item-value"><fmt:formatDate value="${order.rdd}" dateStyle="medium" type="date"/></span>
                    </ycommerce:testId>
                </div>
            </div>
            <div class="col-sm-2 item-wrapper">
                <div class="item-group">
                    <ycommerce:testId code="orderDetail_overviewOrderTotal_label">
                        <span class="item-label"><spring:theme code="text.summary.order.total"/></span>
                        <span class="item-value"><format:price priceData="${order.totalPriceWithTax}"/></span>
                    </ycommerce:testId>
                </div>
                <div class="item-group">
                    <ycommerce:testId code="orderDetail_overviewTotalQuantity_label">
                        <span class="item-label"><spring:theme code="text.summary.order.totalQty"/></span>
                        <span class="item-value">${fn:escapeXml(order.totalUnitCount)}</span>
                    </ycommerce:testId>
                </div>
                <div class="item-group">
                    <ycommerce:testId code="orderDetail_overviewBrand_label">
                        <span class="item-label"><spring:theme code="text.summary.order.brand"/></span>
                        <span class="item-value">${fn:escapeXml(order.brand)}</span>
                    </ycommerce:testId>
                </div>
            </div>
            <div class="col-sm-2 item-action">
                <c:set var="orderCode" value="${orderData.code}" scope="request"/>
                <action:actions element="div" parentComponent="${component}"/>
            </div>
        </div>
    </div>


</div>

