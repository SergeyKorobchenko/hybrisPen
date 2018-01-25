<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="order" required="true" type="de.hybris.platform.commercefacades.order.data.OrderData" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="action" tagdir="/WEB-INF/tags/responsive/action" %>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/responsive/order" %>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="row">
    <div class="col-sm-12 col-md-12 col-no-padding">
        <div class="row">
            <div class="col-sm-3 item-wrapper">
                <div class="item-group">
                    <ycommerce:testId code="orderDetail_soldTo_label">
                        <span class="item-label"><spring:theme code="text.account.orderHistory.soldTo"/></span>
                        <span class="item-value">${fn:escapeXml(order.soldTo)}<br>${fn:escapeXml(order.soldToName)}</span>
                    </ycommerce:testId>
                </div>
            </div>
            <div class="col-sm-3 col-sm-offset-2 item-wrapper">
                <div class="item-group">
                    <ycommerce:testId code="orderDetail_shipTo_label">
                        <span class="item-label"><spring:theme code="text.account.orderHistory.shipTo"/></span>
                        <span class="item-value"><order:addressItem address="${order.deliveryAddress}" storeAddress="true"/></span>
                    </ycommerce:testId>
                </div>
            </div>
            <div class="col-sm-2 item-wrapper">
                <div class="item-group">
                    <ycommerce:testId code="orderDetail_markFor_label">
                        <span class="item-label"><spring:theme code="text.account.orderHistory.markFor"/></span>
                        <span class="item-value">${fn:escapeXml(order.markFor)}<br>${fn:escapeXml(order.markForName)}</span>
                    </ycommerce:testId>
                </div>
            </div>
        </div>
    </div>


</div>