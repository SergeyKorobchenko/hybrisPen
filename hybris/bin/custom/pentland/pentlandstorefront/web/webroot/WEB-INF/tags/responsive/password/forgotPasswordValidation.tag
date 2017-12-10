<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring"  uri="http://www.springframework.org/tags"%>

<spring:htmlEscape defaultHtmlEscape="true" />

<p class="positive forgotten-password" id="validEmail" tabindex="0">
	<spring:theme code="account.confirmation.forgotten.password.link.sent"/>
</p>
