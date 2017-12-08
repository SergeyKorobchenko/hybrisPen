<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true" %>
<%@ attribute name="pageTitle" required="false" rtexprvalue="true" %>
<%@ attribute name="pageCss" required="false" fragment="true" %>
<%@ attribute name="pageScripts" required="false" fragment="true" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="spheader" tagdir="/WEB-INF/tags/addons/secureportaladdon/responsive/common/spheader" %>
<%@ taglib prefix="footer" tagdir="/WEB-INF/tags/responsive/common/footer" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>

<spring:htmlEscape defaultHtmlEscape="true"/>
<template:master pageTitle="${pageTitle}">
    <jsp:attribute name="pageCss">
        <jsp:invoke fragment="pageCss"/>
    </jsp:attribute>

    <jsp:attribute name="pageScripts">
        <jsp:invoke fragment="pageScripts"/>
    </jsp:attribute>

    <jsp:body>
        <spheader:header/>
        <a id="skip-to-content"></a>
        <div class="container">
            <common:globalMessages/>
            <jsp:doBody/>
         </div>
        <div class="c-brand-banner">
            <div class="container-fluid--unlimited b-margin-top">
                <div class="row">
                    <cms:pageSlot position="BrandImageSlot" var="feature" element="div" class="col-md-12">
                        <cms:component component="${feature}"/>
                    </cms:pageSlot>
                </div>
            </div>
        </div>
         <footer:footer/>
    </jsp:body>
</template:master>