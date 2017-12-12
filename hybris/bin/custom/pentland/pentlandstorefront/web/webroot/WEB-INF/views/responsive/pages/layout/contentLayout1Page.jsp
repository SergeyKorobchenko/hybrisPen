<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>

<template:page pageTitle="${pageTitle}">

	<div class="container">
		<div class="b-section--full-width">
			<cms:pageSlot position="Section1" var="feature">
				<cms:component component="${feature}" element="div" class=""/>
			</cms:pageSlot>

			<div class="row">
				<cms:pageSlot position="Section2A" var="feature" element="div" class="col-sm-12">
					<cms:component component="${feature}"/>
				</cms:pageSlot>
			</div>
			<div class="row">
				<cms:pageSlot position="Section2B" var="feature" element="div" class="col-sm-12">
					<cms:component component="${feature}"/>
				</cms:pageSlot>
			</div>

			<cms:pageSlot position="Section3" var="feature" element="div" >
				<cms:component component="${feature}"/>
			</cms:pageSlot>
		</div>
	</div>

</template:page>
