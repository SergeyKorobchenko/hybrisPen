<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%-- Temporary attribute for only MVP1, helps to disable unused components, must be deleted in future --%>
<%@ attribute name="isMVP1" required="false" %>

<c:if test="${empty isMVP1}">
	<spring:htmlEscape defaultHtmlEscape="true" />

	<spring:url value="/cart/export" var="exportUrl" htmlEscape="false"/>
	<div class=" col-xs-12 col-md-3 pull-left">
		<a href="${exportUrl}" class="export__cart--link">
			<spring:theme code="basket.export.csv.file" />
		</a>
	</div>
</c:if>