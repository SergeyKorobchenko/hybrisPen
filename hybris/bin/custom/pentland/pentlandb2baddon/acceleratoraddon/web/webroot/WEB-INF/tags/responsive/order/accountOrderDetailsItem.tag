<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="order" required="true" type="de.hybris.platform.commercefacades.order.data.OrderData" %>
<%@ attribute name="item" required="true" type="com.bridgex.facades.order.data.OrderItemData" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="row">
    <div class="col-xs-2">
        <img class="img-thumbnail" src="${item.imageUrl}" title="${item.name}"/>
    </div>
    <div class="col-xs-8">
        <div class="">
            <ycommerce:testId code="orderDetail_itemNumber_label">
                <span class="item-value">${fn:escapeXml(item.number)}</span>
            </ycommerce:testId>
            <ycommerce:testId code="orderDetail_itemName_label">
                <span class="item-value">${fn:escapeXml(item.name)}</span>
            </ycommerce:testId>
            <ycommerce:testId code="orderDetail_itemQuantity_label">
                <span class="item-label"><spring:theme code="text.account.orderHistory.item.qty"/>:</span>
                <span class="item-value">${fn:escapeXml(item.qty)}</span>
            </ycommerce:testId>
            <ycommerce:testId code="orderDetail_itemStatus_label">
                <span class="item-label"><spring:theme code="text.account.orderHistory.item.status"/>:</span>
                <span class="item-value">${fn:escapeXml(item.itemStatus)}</span>
            </ycommerce:testId>
            <ycommerce:testId code="orderDetail_shipments_label">
                <span class="item-label"><spring:theme code="text.account.orderHistory.item.shipments"/>:</span>
                <c:forEach items="${item.shipments}" var="shipment">
                    <span class="item-info">
                        <span class="text-info"><spring:theme code="text.account.orderHistory.item.shipdate"/>: <fmt:formatDate value="${shipment.key}" /></span>
                        <span class="text-info"><spring:theme code="text.account.orderHistory.item.status"/>: ${shipment.value.shipmentStatus} </span>
                        <span class="text-info"><spring:theme code="text.account.orderHistory.item.qty"/>: ${shipment.value.qty} </span>
                    </span>
                </c:forEach>
            </ycommerce:testId>
        </div>
    </div>
    <div class="col-xs-2 text-right">
        <format:price priceData="${item.totalPrice}"/>
    </div>
</div>





