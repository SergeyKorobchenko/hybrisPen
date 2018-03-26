<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true" %>
<%@ attribute name="pageTitle" required="false" rtexprvalue="true" %>
<%@ attribute name="pageCss" required="false" fragment="true" %>
<%@ attribute name="pageScripts" required="false" fragment="true" %>
<%@ attribute name="hideHeaderLinks" required="false" %>

<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="header" tagdir="/WEB-INF/tags/responsive/common/header" %>
<%@ taglib prefix="footer" tagdir="/WEB-INF/tags/responsive/common/footer" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common" %>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<spring:htmlEscape defaultHtmlEscape="true"/>

<template:master pageTitle="${pageTitle}">

	<jsp:attribute name="pageCss">
		<jsp:invoke fragment="pageCss"/>
	</jsp:attribute>

    <jsp:attribute name="pageScripts">
		<jsp:invoke fragment="pageScripts"/>
	</jsp:attribute>

    <jsp:body>
        <div class="branding-mobile hidden-md hidden-lg">
            <div class="container">
                <div class="b-section--full-width">
                    <div class="js-mobile-logo">
                            <%--populated by JS acc.navigation--%>
                    </div>
                </div>
            </div>
        </div>

        <main data-currency-iso-code="${fn:escapeXml(currentCurrency.isocode)}">
            <spring:theme code="text.skipToContent" var="skipToContent"/>
            <a href="#skip-to-content" class="skiptocontent" data-role="none">${fn:escapeXml(skipToContent)}</a>
            <spring:theme code="text.skipToNavigation" var="skipToNavigation"/>
            <a href="#skiptonavigation" class="skiptonavigation" data-role="none">${fn:escapeXml(skipToNavigation)}</a>

            <header:header hideHeaderLinks="${hideHeaderLinks}"/>

            <a id="skip-to-content"></a>

            <div>
                <common:globalMessages/>

                <cart:cartRestoration/>

                <jsp:doBody/>
            </div>
            
     <!-- After Session timeout popup will appear and click on close button then it redirects to login page... Connect-46 --> 
      
<script>
			    var secondsBeforeExpire = ${pageContext.session.maxInactiveInterval};
			    var timeToDecide = 0; 
			    setTimeout(function() {
			    	$('#myModal-timeout').modal('show');  
			    	$('#myModal-timeout').on('hidden.bs.modal', function () {
			    			 window.location="${request.contextPath}/login";
			    		 		});	   	
			    }, (secondsBeforeExpire - timeToDecide) * 1000);
			</script>

		<div class="modal fade" id="myModal-timeout" tabindex="-1" role="dialog" aria-hidden="true" data-backdrop="static" >
		  <div class="modal-dialog">
		    <div class="modal-content ">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>  
		        <h2 class="modal-title custom-modal-title">
		        	<div class="box-head">
		        		<div class="title_head">
		        			<span class="style-icons-font sty-normal">
		        				<i class="fa fa-info-circle" aria-hidden="true"></i>
		        			</span>
		        			<span class="lb_head"><span class="lb_show">Information</span></span>
		        		</div>
		        	</div>
		        </h2>   
		      </div>
		      <div class="modal-body">
		        <p>Your session has expired. You will now be redirected to the login page.</p>
		      </div>
		      <div class="modal-footer">
		         <button class="btn btn-default sty-btn-main" data-dismiss="modal">Close</button> 
		       </div>
		    </div>
		  </div>
		</div>
            <footer:footer/>
        </main>

    </jsp:body>

</template:master>
