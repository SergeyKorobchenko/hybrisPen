<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<spring:htmlEscape defaultHtmlEscape="true" />
<br/>
<br/>
<br/>
<c:url value="/cart/update-all" var="updateAllCartFormAction" />
<form:form method="post" id="updateAllCartForm" action="${updateAllCartFormAction}" commandName="b2bCartForm">
	<div class="row">
		<div class="col-xs-12 col-md-2">
			<strong><spring:theme code="text.cart.rdd"/></strong>
			<div class="form-group">
				<div class="input-group date" id="cartrdddatetimepicker">
					<form:input type="text" class="form-control" path="requestedDeliveryDate" data-value="${b2bCartForm.requestedDeliveryDate}" />
					<span class="input-group-addon">
						<span class="glyphicon glyphicon-calendar"></span>
					</span>
				</div>
			</div>
		</div>
		<div class="col-xs-12 col-md-2">
			<strong><spring:theme code="text.cart.purchaseOrderNumber"/></strong>
			<div>
				<form:input type="text" class="form-control" style="width: 99%;" path="purchaseOrderNumber" />
			</div>
		</div>
		<div class="col-xs-12 col-md-8">
			<strong><spring:theme code="text.cart.customerNotes"/></strong>
			<div>
				<form:textarea class="form-control" style="width: 400px; min-height: 100px;" path="customerNotes" />
			</div>
		</div>
	</div>
</form:form>