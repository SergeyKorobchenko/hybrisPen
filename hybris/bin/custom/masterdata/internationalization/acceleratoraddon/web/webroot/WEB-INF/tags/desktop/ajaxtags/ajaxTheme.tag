<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>

<%@ attribute name="code" required="true" type="java.lang.String" %>
<%@ attribute name="arguments" required="false" type="java.lang.Object" %>
<%@ attribute name="unit" required="false" type="java.lang.Boolean" %>
<%@ attribute name="textOnly" required="false" type="java.lang.Boolean" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:choose>
    <c:when test="${request.scheme eq 'http'}">
        <span class="ajax_message" data-code="${code}" data-arguments="${arguments}" data-unit="${unit}" data-text_only="${textOnly}"></span>
    </c:when>
    <c:otherwise>
        <span><spring:theme code="${code}" arguments="${arguments}"/></span>
    </c:otherwise>
</c:choose>