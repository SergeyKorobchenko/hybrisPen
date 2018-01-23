<%@ page trimDirectiveWhitespaces="true"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<jsp:useBean id="accountSummaryInfoData" scope="request" type="com.bridgex.pentlandaccountsummaryaddon.data.AccountSummaryInfoData"/>

<div class="account-section-content">
    <div class="well well-lg well-tertiary">
        <div class="col-md-7 col-lg-8 account-summary-detail clearfix">
            <div class="col-sm-4 col-md-6 col-lg-4 item-wrapper">
                <div class="item-group">
                    <span class="item-label"><spring:theme code="text.company.accountsummary.businessunitid.label"/></span>
                    <span class="item-value">${fn:escapeXml(accountSummaryInfoData.id)}</span>
                </div>
                <div class="item-group">
                    <span class="item-label"><spring:theme code="text.company.accountsummary.b2bunit.label"/></span>
                    <span class="item-value">${fn:escapeXml(accountSummaryInfoData.name)}</span>
                </div>
                <div class="item-group">
                	<span class="item-label"><spring:theme code="text.company.accountsummary.address.label"/></span>
                    <c:if test="${not empty accountSummaryInfoData.address}">
                        <span class="item-value">
                            ${fn:escapeXml(accountSummaryInfoData.address.line1)},&nbsp;${fn:escapeXml(accountSummaryInfoData.address.line2)}<br/>
                            ${fn:escapeXml(accountSummaryInfoData.address.town)},&nbsp;${fn:escapeXml(accountSummaryInfoData.address.state)}<br/>
                            ${fn:escapeXml(accountSummaryInfoData.address.postalCode)}&nbsp;${fn:escapeXml(accountSummaryInfoData.address.country.name)}
                        </span>
                    </c:if>
                </div>
            </div>
            <div class="col-sm-4 col-md-6 col-lg-4 item-wrapper">
                <div class="item-group">
					<span class="item-label"><spring:theme code="text.company.accountsummary.creditrep.label"/></span>
					<span class="item-value">
					    <%--<c:forEach var="creditRepLine" items="${accountSummaryInfoData.creditReps}">--%>
                            <%--${fn:escapeXml(creditRepLine)}<br/>--%>
                        <%--</c:forEach>--%>
                        <spring:theme code="text.company.accountsummary.not.applicable"/>
					</span>
                </div>
                <div class="item-group">
                	<span class="item-label"><spring:theme code="text.company.accountsummary.creditline.label"/></span>
                    <span class="item-value">
					    <c:forEach var="creditLimitLine" items="${accountSummaryInfoData.formattedCreditLimits}">
                            ${fn:escapeXml(creditLimitLine.key)} &mdash; ${fn:escapeXml(creditLimitLine.value)}<br/>
                        </c:forEach>
					</span>
                </div>
            </div>

            <div class="col-sm-4 col-md-6 col-lg-4 item-wrapper">
                <div class="item-group">
                    <span class="item-label"><spring:theme code="text.company.accountsummary.currentbalance.label"/></span>
                    <span class="item-value">
                    	<c:choose>
	                    	<c:when test="${not empty accountSummaryInfoData.amountBalanceData.currentBalance}">
	                    		${fn:escapeXml(accountSummaryInfoData.amountBalanceData.currentBalance)}
	                   		</c:when>
	                   		<c:otherwise>
								<spring:theme code="text.company.accountsummary.not.applicable"/>
							</c:otherwise>
						</c:choose>
                    </span>
                </div>
                <div class="item-group">
                    <span class="item-label"><spring:theme code="text.company.accountsummary.openbalance.label"/></span>
                    <span class="item-value">
                    	<c:choose>
	                    	<c:when test="${not empty accountSummaryInfoData.amountBalanceData.openBalance}">
	                    		${fn:escapeXml(accountSummaryInfoData.amountBalanceData.openBalance)}
	                   		</c:when>
	                    	<c:otherwise>
								<spring:theme code="text.company.accountsummary.not.applicable"/>
							</c:otherwise>
						</c:choose>
                    </span>
                </div>
            </div>
        </div>

        <div class="col-md-5 col-lg-4 item-wrapper clearfix">
                            <span class="item-label">
                        <spring:theme code="text.company.accountsummary.days.label"/>
                    </span>
            <div class="framed">
                    <c:forEach items="${accountSummaryInfoData.amountBalanceData.dueBalance}" var="range">
                        <span class="item-label">
                                ${fn:escapeXml(range.key)}
                        </span>

                        <span class="item-value">
                             ${fn:escapeXml(range.value)}
                            </span>
                        </c:forEach>
            </div>
            <fmt:formatDate value="${firstWorkigDayOfMonth}" pattern="dd/MM/yyyy" var="firstDate"/>
            <span><spring:theme code="account.summary.balances.validity" arguments="${firstDate}"/></span>
        </div>
    </div>
</div>