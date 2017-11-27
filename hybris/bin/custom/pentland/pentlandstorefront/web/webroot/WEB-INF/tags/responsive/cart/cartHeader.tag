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
					<form:input type="text" class="form-control" path="requestedDeliveryDate" data-mindate="${b2bCartForm.minDate}"
								data-disableddates="${b2bCartForm.bankHolidays}" />
					<span class="input-group-addon">
						<span class="glyphicon glyphicon-calendar"></span>
					</span>
				</div>
				<span style="display:none;" class="help-block" id="requestedDeliveryDateError"><spring:theme code="basket.error.checkout.empty.rdd"/></span>
			</div>
		</div>
		<div class="col-xs-12 col-md-2">
			<strong><spring:theme code="text.cart.purchaseOrderNumber"/></strong>
			<div class="form-group">
				<div>
					<form:input type="text" class="form-control" style="width: 99%;" path="purchaseOrderNumber" />
				</div>
				<span style="display:none;" class="help-block" id="purchaseOrderNumberError"><spring:theme code="basket.error.checkout.empty.ponumber"/></span>
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