<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:if test="${displayCookieDirective}">
    <div class="cookie-disclaimer">
        <div class="cookie-close accept-cookie"><i class="fa fa-times"></i></div>
        <div class="container">
            <p class="text-justify">
            <h4><spring:theme code="cookie.directive.header"/></h4>
            <c:url var="policyLink" value="/termsofuse#2"/>
            <spring:theme code="cookie.directive.message" arguments="${policyLink}"/>
            </p>
                <%--<button type="button" class="btn btn-success accept-cookie">Continue</button>--%>
        </div>
    </div>
</c:if>

