<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<%--<div class="slider_component simple-banner">
	<div id="homepage_slider" class="svw">
		<ul>
			<c:forEach items="${banners}" var="banner" varStatus="status">
				<c:if test="${ycommerce:evaluateRestrictions(banner)}">
					<c:url value="${banner.urlLink}" var="encodedUrl" />
					<li><a tabindex="-1" href="${encodedUrl}"<c:if test="${banner.external}"> target="_blank"</c:if>><img src="${banner.media.url}" alt="${not empty banner.headline ? banner.headline : banner.media.altText}" title="${not empty banner.headline ? banner.headline : banner.media.altText}"/></a></li>
				</c:if>
			</c:forEach>
		</ul>
	</div>
</div>--%>


<%--
<div id="myCarousel" class="carousel slide" data-ride="carousel">
	<!-- Indicators -->
	<ol class="carousel-indicators">
		<li data-target="#myCarousel" data-slide-to="0" class="active"></li>
		<li data-target="#myCarousel" data-slide-to="1" class=""></li>
		<li data-target="#myCarousel" data-slide-to="2" class=""></li>
	</ol>
	<div class="carousel-inner">
		<div class="item active"><img src="http://lorempixel.com/1200/400/sports" data-media='{1200:http://lorempixel.com/1200/400/sports}' style="width:100%" alt="First slide">
			<div class="container">
				<div class="carousel-caption">
					<h1>Slide 1</h1>
					<p>Aenean a rutrum nulla. Vestibulum a arcu at nisi tristique pretium.</p>
					<p><a class="btn btn-lg btn-primary" href="#" role="button">Sign up today</a></p>
				</div>
			</div>
		</div>
		<div class="item"><img src="http://lorempixel.com/1200/400/people" style="width:100%" data-src="" data-media='{1200:http://lorempixel.com/1200/400/people}' alt="Second    slide">
			<div class="container">
				<div class="carousel-caption">
					<h1>Slide 2</h1>
					<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae egestas purus. </p>
					<p><a class="btn btn-lg btn-primary" href="#" role="button">Learn more</a></p>
				</div>
			</div>
		</div>
		<div class="item"><img src="http://lorempixel.com/1200/400/abstract" style="width:100%" data-src="" data-media='{1200:http://lorempixel.com/1200/400/abstract}' alt="Third slide">
			<div class="container">
				<div class="carousel-caption">
					<h1>Slide 3</h1>
					<p>Donec sit amet mi imperdiet mauris viverra accumsan ut at libero.</p>
					<p><a class="btn btn-lg btn-primary" href="#" role="button">Browse gallery</a></p>
				</div>
			</div>
		</div>
	</div>
	<a class="left carousel-control" href="#myCarousel" data-slide="prev"><span class="glyphicon glyphicon-chevron-left"></span></a>
	<a class="right carousel-control" href="#myCarousel" data-slide="next"><span class="glyphicon glyphicon-chevron-right"></span></a>
</div>--%>
