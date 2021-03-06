<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:htmlEscape defaultHtmlEscape="true"/>

<spring:url value="/cart/export" var="exportUrl" htmlEscape="false"/>
<spring:url value="/cart/images" var="imagesExportUrl" htmlEscape="false"/>

<div class=" col-xs-12 col-md-6 pull-left">
    <a href="${exportUrl}" class="export__cart--link">
        <span class="glyphicon glyphicon-download-alt"></span>
        <spring:theme code="basket.export.csv.file"/>
    </a>
    <a href="${imagesExportUrl}" class="export__images--link">
        <span class="glyphicon glyphicon-download-alt"></span>
        <spring:theme code="basket.export.images.file"/>
    </a>
</div>