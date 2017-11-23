<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<div class="row">
    <div class="container-lg col-md-10">
        <p class="lead"><spring:theme code="text.account.hidePrices.notice"/></p>
    </div>
</div>
<div class="row">
    <div class="container-lg col-md-10">
        <div class="account-section-content">
            <div class="account-section-form">
                <form id="showHidePrices" >
                    <div class="radio">
                        <label><input type="radio" name="showHidePrices"><spring:theme code="text.account.hidePrices.show"/></label>
                    </div>
                    <div class="radio">
                        <label><input type="radio" name="showHidePrices"><spring:theme code="text.account.hidePrices.hide"/></label>
                    </div>
                    <button type="submit" class="btn btn-primary">
                        <spring:theme code="text.button.save" text="Cancel" />
                    </button>
                </form>
            </div>
        </div>
    </div>
</div>