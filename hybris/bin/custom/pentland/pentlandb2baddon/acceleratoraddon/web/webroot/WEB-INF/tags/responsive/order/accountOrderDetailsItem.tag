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
    <div class="col-xs-12">
        <ycommerce:testId code="orderDetail_itemNumber_label">
            <span class="item-value"><spring:theme code="text.account.orderHistory.item.number"/>: ${fn:escapeXml(item.number)}</span>
        </ycommerce:testId>
        <ycommerce:testId code="orderDetail_itemName_label">
            <span class="item-value">${fn:escapeXml(item.name)}</span>
        </ycommerce:testId>
        <ycommerce:testId code="orderDetail_itemQuantity_label">
            <span class="item-label"><spring:theme code="text.account.orderHistory.item.qty"/>: ${fn:escapeXml(item.qty)}</span>
        </ycommerce:testId>
        <ycommerce:testId code="orderDetail_itemStatus_label">
            <span class="item-label"><spring:theme code="text.account.orderHistory.item.status"/>: ${fn:escapeXml(item.itemStatus)}</span>
        </ycommerce:testId>
        <ycommerce:testId code="orderDetail_shipments_label">
            <span class="item-label"><spring:theme code="text.account.orderHistory.item.shipments"/>:</span>
            <span class="item-info">
                        <span class="item-value"><spring:theme code="text.account.orderHistory.item.shipdate"/>
                        <c:choose>
                            <c:when test="${item.shipDate != null}"> :<fmt:formatDate value="${item.shipDate}"/> |</c:when>
                            <c:otherwise>: <spring:theme code="text.account.orderHistory.item.notassigned"/> |</c:otherwise>
                        </c:choose>
                        </span>
                        <span class="item-value"> <spring:theme code="text.account.orderHistory.item.qty"/>: ${item.shippedQty} |</span>
                        <span class="item-value"> <spring:theme code="text.account.orderHistory.item.status"/>: ${item.itemStatus}</span>
                    </span>
        </ycommerce:testId>
        <ycommerce:testId code="orderDetail_ItemPrice_label">
            <div class="text-right">
                <span class="price-info"><format:price priceData="${item.totalPrice}"/></span>
            </div>
        </ycommerce:testId>
    </div>
</div>





