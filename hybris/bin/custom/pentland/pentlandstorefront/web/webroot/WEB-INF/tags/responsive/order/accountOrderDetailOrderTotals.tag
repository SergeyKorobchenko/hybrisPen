<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="order" required="true" type="de.hybris.platform.commercefacades.order.data.AbstractOrderData" %>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/responsive/order" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<c:if test="${not empty order}">
    <div class="account-orderdetail">
        <div class="account-orderdetail__footer">
            <div class="row">
                <div class="col-sm-4 col-sm-offset-8">
                    <order:orderTotalsItem order="${order}"/>
                </div>
            </div>
        </div>
    </div>
</c:if>