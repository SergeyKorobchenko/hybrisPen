<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="cartData" required="true" type="de.hybris.platform.commercefacades.order.data.CartData" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<ul class="checkout-order-summary-list">
    <li class="checkout-shippingcheckout-order-summary-list-heading">
        <div class="title">
            <spring:theme code="checkout.multi.payment"/>
        </div>
        <div class="address">
            <c:if test="${not empty cartData.user.unit and not empty cartData.user.unit.customerType}">
                <spring:theme code="checkout.payment.${cartData.paymentType.code}.${cartData.user.unit.customerType}"/>
            </c:if>
        </div>
        <div class="title">
            <spring:theme code="checkout.multi.purchaseOrderNumber.label" htmlEscape="false"/>:&nbsp;
        </div>
        <div class="address">
            <c:choose>
                <c:when test="${not empty cartData.purchaseOrderNumber}">
                    ${fn:escapeXml(cartData.purchaseOrderNumber)}
                </c:when>
                <c:otherwise>
                    -
                </c:otherwise>
            </c:choose>
        </div>
    </li>
</ul>
