<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="footer" tagdir="/WEB-INF/tags/responsive/common/footer" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:if test="${component.visible}">
    <div class="container-fluid">
        <div class="footer__top">
            <div class="row">
                <c:choose>
                    <c:when test="${showLanguageCurrency}">
                        <div class="footer__left col-xs-12 col-sm-12 col-md-9">
                    </c:when>
                    <c:otherwise>
                        <div class="footer__left col-xs-12 col-sm-12 col-md-12">
                    </c:otherwise>
                </c:choose>
                    <div class="row">
                        <c:forEach items="${component.navigationNode.children}" var="childLevel1">
                            <c:forEach items="${childLevel1.children}" step="${component.wrapAfter}" varStatus="i">
                                <div class="footer__nav--container col-xs-12 col-sm-3">
                                    <c:if test="${component.wrapAfter > i.index}">
                                        <h4 class="footer__nav--title">${fn:escapeXml(childLevel1.title)}</h4>
                                    </c:if>
                                    <ul class="footer__nav--links">
                                        <c:forEach items="${childLevel1.children}" var="childLevel2" begin="${i.index}"
                                                   end="${i.index + component.wrapAfter - 1}">
                                            <c:forEach items="${childLevel2.entries}" var="childlink">
                                                <cms:component component="${childlink.item}" evaluateRestriction="true"
                                                               element="li" class="footer__link"/>
                                            </c:forEach>
                                        </c:forEach>
                                    </ul>
                                </div>
                            </c:forEach>
                        </c:forEach>
                    </div>
                </div>

                <c:if test="${showLanguageCurrency}">
                    <div class="footer__right col-xs-12 col-md-3">
                        <div class="row">
                            <div class="col-xs-6 col-md-6 footer__dropdown">
                                <footer:languageSelector languages="${languages}" currentLanguage="${currentLanguage}"/>
                            </div>
                            <div class="col-xs-6 col-md-6 footer__dropdown">
                                <footer:currencySelector currencies="${currencies}" currentCurrency="${currentCurrency}"/>
                            </div>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>
    </div>

    <div class="footer__bottom">
        <div class="footer__copyright">
            <div class="container">${fn:escapeXml(notice)}</div>
        </div>
    </div>
</c:if>