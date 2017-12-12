<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>

<c:if test="${fn:length(breadcrumbs) > 0}">
	<div class="breadcrumb-section">
		<%--<div class="container">--%>
			<div class="b-section--full-width">
				<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
			</div>
		<%--</div>--%>
	</div>
</c:if>