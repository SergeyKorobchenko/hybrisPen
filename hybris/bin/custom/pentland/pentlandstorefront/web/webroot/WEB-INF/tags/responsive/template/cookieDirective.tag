<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${displayCookieDirective}">
    <div class="cookie-disclaimer">
        <div class="container">
            <div class="b-section--full-width">
                <div class="cookie-close accept-cookie"><span class="glyphicon glyphicon-remove"></span></div>

                <h4><spring:theme code="cookie.directive.header"/></h4>

                <p class="text-justify">
                    <c:url var="policyLink" value="/termsofuse#2"/>
                    <spring:theme code="cookie.directive.message" arguments="${policyLink}"/>
                </p>
                <%--<button type="button" class="btn btn-success accept-cookie">Continue</button>--%>
            </div>
        </div>
    </div>
</c:if>

