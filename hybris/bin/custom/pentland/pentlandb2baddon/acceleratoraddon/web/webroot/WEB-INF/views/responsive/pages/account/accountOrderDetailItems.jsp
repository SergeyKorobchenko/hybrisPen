<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="b2b-order" tagdir="/WEB-INF/tags/addons/pentlandb2baddon/responsive/order" %>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/responsive/order" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="account-orderdetail account-consignment">
    <ycommerce:testId code="orderDetail_itemList_section">
        <table class="orderListTable">
            <tr>
                <c:forEach items="${orderData.orderItems}" var="item">
                    <td>
                        <div class="fulfilment-states-${fn:escapeXml(item.itemStatus)}">
                           <b2b-order:accountOrderDetailsItem order="${orderData}" item="${item}"/>
                        </div>
                    </td>
                </c:forEach>
            </tr>
        </table>
    </ycommerce:testId>
</div>