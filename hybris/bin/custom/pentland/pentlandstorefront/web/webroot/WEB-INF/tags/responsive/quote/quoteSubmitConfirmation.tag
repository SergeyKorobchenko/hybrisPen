<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="quoteData" required="true" type="de.hybris.platform.commercefacades.quote.data.QuoteData"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<%--
    ~ /*
    ~  * [y] hybris Platform
    ~  *
    ~  * Copyright (c) 2000-2017 SAP SE or an SAP affiliate company.
    ~  * All rights reserved.
    ~  *
    ~  * This software is the confidential and proprietary information of SAP
    ~  * ("Confidential Information"). You shall not disclose such Confidential
    ~  * Information and shall use it only in accordance with the terms of the
    ~  * license agreement you entered into with SAP.
    ~  *
    ~  */
--%>

<spring:htmlEscape defaultHtmlEscape="true" />
<spring:url value="/quote/{/quoteCode}/submit/" var="submitQuoteUrl" htmlEscape="false">
    <spring:param name="quoteCode" value="${quoteData.code}"/>
</spring:url>

<div style="display:none">
    <spring:theme code="${ycommerce:isQuoteUserSalesRep() ? 'text.quote.conteroffer.confirmation.modal.title' : 'text.quote.submit.confirmation.modal.title'}"  arguments="${quoteData.code}"
                  var="submitConfirmationModalTitle"/>
    <div id="js-quote-submit-modal" data-submit-confirmation-modal-title="${submitConfirmationModalTitle}">
        <div class="modal__top">
            <label class="modal__top--label">
                <spring:theme code="text.quote.name.label"/>
            </label>
            <p class="modal__top--text js-modal-quote-name">${fn:escapeXml(quoteData.name)}</p>
            <label class="modal__top--label">
                <spring:theme code="text.quote.description.label"/>
            </label>
            <p class="modal__top--text js-modal-quote-description">${fn:escapeXml(quoteData.description)}</p>
        </div>
        <div class="modal__bottom">
            <p class="modal__bottom--text modal__text--bold"><spring:theme code="${ycommerce:isQuoteUserSalesRep() ? 'text.quote.conteroffer.possible.checkout.message' : 'text.quote.submit.not.modifiable.message'}"/></p>
            <p class="modal__bottom--text modal__text--bold"><spring:theme code="${ycommerce:isQuoteUserSalesRep() ? 'text.quote.conteroffer.confirmation.message' : 'text.quote.submit.confirmation.message'}"/></p>
        </div>

        <form:form action="${submitQuoteUrl}" id="quoteSubmitForm">
            <button type="submit" class="btn btn-primary btn-block" id="submitYesButton">
                <spring:theme code="text.quote.yes.button.label"/>
            </button>
            <button type="button" class="btn btn-default btn-block" id="submitNoButton">
                <spring:theme code="text.quote.no.button.label"/>
            </button>
        </form:form>
    </div>
</div>