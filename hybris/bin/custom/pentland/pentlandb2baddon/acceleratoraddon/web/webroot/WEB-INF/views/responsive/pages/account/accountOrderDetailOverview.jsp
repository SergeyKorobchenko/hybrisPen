<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="b2b-order" tagdir="/WEB-INF/tags/addons/pentlandb2baddon/responsive/order" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="well-lg well well-tertiary">
	<ycommerce:testId code="orderDetail_overview_section">
		<b2b-order:accountOrderDetailsOverview order="${orderData}"/>
	</ycommerce:testId>
</div>

<div class="well-lg well well-tertiary">
	<ycommerce:testId code="orderDetail_soldTo_section">
		<b2b-order:accountOrderDetailsSoldTo order="${orderData}"/>
	</ycommerce:testId>
</div>