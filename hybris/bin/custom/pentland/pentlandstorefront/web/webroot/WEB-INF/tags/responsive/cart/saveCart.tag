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

<button href="#" class="cart__head--link js-save-cart-link btn btn-primary pull-right">
    <spring:theme code="basket.save.cart" />
</button>
    
<spring:url value="/cart/save" var="actionUrl" htmlEscape="false"/>
<cart:saveCartModal titleKey="text.save.cart.title" actionUrl="${actionUrl}" messageKey="basket.save.cart.info.msg"/>