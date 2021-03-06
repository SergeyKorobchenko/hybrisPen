<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<c:url value="/cart/update-all" var="updateAllCartFormAction" />

<form:form method="post" id="updateAllCartForm" action="${updateAllCartFormAction}" commandName="b2bCartForm" data-page-type="${pageType}">
	<div class="row align-items-end">
		<div class="col-xs-12 col-md-2">
			<label><spring:theme code="text.cart.rdd"/></label>
			<div class="form-group">
				<div class="input-group date" id="cartrdddatetimepicker">
                    <fmt:formatDate value="${b2bCartForm.requestedDeliveryDate}" pattern="yyyy-MM-dd" var="initialDate"/>
					<form:input type="text" class="form-control" path="requestedDeliveryDate" data-mindate="${b2bCartForm.minDate}"
								data-disableddates="${b2bCartForm.bankHolidays}" data-initial="${initialDate}"/>
					<span class="input-group-addon">
						<span class="glyphicon glyphicon-calendar"></span>
					</span>
				</div>
				<span style="display:none;" class="help-block" id="requestedDeliveryDateError"><spring:theme code="basket.error.checkout.empty.rdd"/></span>
			</div>
		</div>

		<div class="col-xs-12 col-md-2">
			<label><spring:theme code="text.cart.purchaseOrderNumber"/></label>
			<div class="form-group">
				<div>
					<form:input type="text" class="form-control" style="width: 100%;" path="purchaseOrderNumber" data-initial="${b2bCartForm.purchaseOrderNumber}" maxlength="20"/>
				</div>
				<span style="display:none;" class="help-block" id="purchaseOrderNumberError"><spring:theme code="basket.error.checkout.empty.ponumber"/></span>
			</div>
		</div>

		<div class="col-xs-12 col-md-8">
			<label><spring:theme code="text.cart.customerNotes"/></label>
			<div>
				<form:textarea class="form-control" style="min-height: 42px; height: 42px;" path="customerNotes" data-initial="${b2bCartForm.customerNotes}"/>
			</div>
		</div>
	</div>
</form:form>