<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart" %>

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
<spring:url value="/cart/delete" var="actionUrl1"  />
<a href="#" class="cart__head--link pull-right" data-toggle="modal" data-target="#myModal" data-backdrop="static" data-keyboard="false"><span class="glyphicon glyphicon-trash"></span><spring:theme code="basket.empty.cart" /></a>

<a href="#" class="cart__head--link js-save-cart-link pull-right"><span class="glyphicon glyphicon-plus"></span><spring:theme code="basket.save.cart" /></a>
    
<spring:url value="/cart/save" var="actionUrl" htmlEscape="false"/>
<cart:saveCartModal titleKey="text.save.cart.title" actionUrl="${actionUrl}" messageKey="basket.save.cart.info.msg"/>


<div id="myModal" class="modal fade" role="dialog" style="margin-top: 150px;">
  <div class="modal-dialog modal-sm">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title">Empty your basket</h4>
      
      <div class="modal-body">
        <p>Do you really want to clear the basket</p>
      </div>
      <div class="modal-footer">
      	<a href="${actionUrl1}" class="btn btn-primary btn-block mini-cart-checkout-button">Yes</a>
        <button type="button" class="btn btn-default btn-block mini-cart-checkout-button" data-dismiss="modal">No</button>
      </div>
    </div>
    </div>

  </div>
</div>