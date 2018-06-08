/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.bridgex.facades.process.email.context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import com.bridgex.core.integration.PentlandIntegrationService;
import com.bridgex.core.order.PentlandCartService;
import com.bridgex.facades.order.PentlandOrderFacade;
import com.bridgex.integration.domain.OrderDetailsDto;
import com.bridgex.integration.domain.OrderDetailsResponse;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.coupon.data.CouponData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.enums.ExportStatus;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;


/**
 * Velocity context for a order notification email.
 */
public class OrderNotificationEmailContext extends AbstractEmailContext<OrderProcessModel>
{
	private Converter<OrderModel, OrderData> orderConverter;
	private OrderData                        orderData;
	private List<CouponData>                 giftCoupons;
	private SessionService                   sessionService;
	private UserService                      userService;
	private PentlandIntegrationService<OrderDetailsDto,OrderDetailsResponse> orderDetailsService;
	private PentlandOrderFacade orderFacade;
	private ProductFacade productFacade;
	
	@Override
	public void init(final OrderProcessModel orderProcessModel, final EmailPageModel emailPageModel)
	{
		super.init(orderProcessModel, emailPageModel);
		CustomerModel customer = getCustomer(orderProcessModel);
		put("customer", customer);

		sessionService.executeInLocalView(new SessionExecutionBody() {
			@Override
			public Object execute() {
				userService.setCurrentUser(customer);
				OrderModel order = orderProcessModel.getOrder();
				orderData = getOrderConverter().convert(order);
				for (OrderEntryData orderEntryData : orderData.getEntries()) {
					ProductData productForCodeAndOptions = getProductFacade().getProductForCodeAndOptions(orderEntryData.getProduct().getCode(),Arrays.asList(ProductOption.BRAND));
					orderEntryData.setProduct(productForCodeAndOptions);
				}
				if(ExportStatus.EXPORTED.equals(order.getExportStatus()) && CollectionUtils.isNotEmpty(order.getByBrandOrderList())) {
					orderData.setSubOrders(getOrderConverter().convertAll(order.getByBrandOrderList()));
					setSapEntriesFromSourceOrder(orderData);
				}else{
					orderData.setSubOrders(Collections.singletonList(orderData));
				}
				return super.execute();
			}
		});


		giftCoupons = orderData.getAppliedOrderPromotions().stream()
				.filter(x -> CollectionUtils.isNotEmpty(x.getGiveAwayCouponCodes())).flatMap(p -> p.getGiveAwayCouponCodes().stream())
				.collect(Collectors.toList());
	}
	
	 private void setSapEntriesFromSourceOrder(OrderData orderData)
	 {
		 List<OrderData> orderByBrand=new ArrayList<>();
			for (OrderData sapOrder : orderData.getSubOrders())
			{
				List<OrderEntryData> entries = orderData.getEntries().stream().filter(e->e.getProduct().getBrandName().equals(sapOrder.getBrand())).collect(Collectors.<OrderEntryData>toList());
				sapOrder.setEntries(entries);
				orderByBrand.add(sapOrder);
			}
			orderData.setSubOrders(orderByBrand);
	 }
	 
	@Override
	protected BaseSiteModel getSite(final OrderProcessModel orderProcessModel)
	{
		return orderProcessModel.getOrder().getSite();
	}

	@Override
	protected CustomerModel getCustomer(final OrderProcessModel orderProcessModel)
	{
		return (CustomerModel) orderProcessModel.getOrder().getUser();
	}

	protected Converter<OrderModel, OrderData> getOrderConverter()
	{
		return orderConverter;
	}

	@Required
	public void setOrderConverter(final Converter<OrderModel, OrderData> orderConverter)
	{
		this.orderConverter = orderConverter;
	}

	public OrderData getOrder()
	{
		return orderData;
	}

	@Override
	protected LanguageModel getEmailLanguage(final OrderProcessModel orderProcessModel)
	{
		return orderProcessModel.getOrder().getLanguage();
	}

	public List<CouponData> getCoupons()
	{
		return giftCoupons;
	}

	@Required
	public void setSessionService(SessionService sessionService) {
		this.sessionService = sessionService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	public void setOrderDetailsService(
			PentlandIntegrationService<OrderDetailsDto, OrderDetailsResponse> orderDetailsService) {
		this.orderDetailsService = orderDetailsService;
	}

	public PentlandOrderFacade getOrderFacade() {
		return orderFacade;
	}
	
	@Required
	public void setOrderFacade(PentlandOrderFacade orderFacade) {
		this.orderFacade = orderFacade;
	}

	public ProductFacade getProductFacade() {
		return productFacade;
	}

	@Required
	public void setProductFacade(ProductFacade productFacade) {
		this.productFacade = productFacade;
	}
	
	
	

}
