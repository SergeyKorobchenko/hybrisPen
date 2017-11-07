package com.bridgex.facades.order.converters.populator;

import java.util.*;
import java.util.function.BiFunction;

import org.springframework.beans.factory.annotation.Required;

import com.bridgex.facades.order.data.OrderDetailsData;
import com.bridgex.facades.order.data.OrderItemData;
import com.bridgex.facades.order.data.ShipmentData;
import com.bridgex.integration.domain.OrderDetailsResponse;
import com.bridgex.integration.domain.OrderEntryDto;
import com.bridgex.integration.domain.SizeVariantDto;

import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

/**
 * @author Goncharenko Mikhail, created on 02.11.2017.
 */
public class OrderDetailsPopulator implements Populator<OrderDetailsResponse, OrderDetailsData> {

  private ProductService productService;

  private BiFunction<ShipmentData, ShipmentData, ShipmentData> shipmentDataMerger = (shipment1,shipment2) -> {
    shipment1.setQty(shipment1.getQty() + shipment2.getQty());
    return shipment1;
  };

  @Override
  public void populate(OrderDetailsResponse source, OrderDetailsData target) throws ConversionException {
    target.setOrderNumber(source.getCode());
    target.setPONumber(source.getPurshaseOrderNumber());
    target.setOrderStatus(source.getStatus());
    target.setOrderCreationDate(source.getCreationTime());
    target.setRdd(source.getRdd());
    target.setOrderNet(Double.valueOf(source.getNetPrice()));
    target.setOrderTax(Double.valueOf(source.getTaxPrice()));
    target.setOrderTotal(Double.valueOf(source.getTotalPrice()));
    target.setSoldTo(source.getCustomerId() + " " + source.getCustomerName());
    target.setTotalQty(Double.valueOf(source.getTotalQuantity()).intValue());

    populateItems(source, target);
    populateAddress(source, target);
  }

  private void populateItems(OrderDetailsResponse source, OrderDetailsData target) {
    List<OrderItemData> items = new ArrayList<>();

    for (OrderEntryDto dto : source.getOrderEntries()) {
      OrderItemData item = new OrderItemData();
      item.setNumber(dto.getEntryNumber());
      item.setItemStatus(dto.getEntryStatus());
      item.setQty(Double.valueOf(dto.getQuantity()).intValue());
      populateNameAndImage(dto, item);
      populateShipments(dto, item);
      items.add(item);
    }

    target.setOrderItems(items);
  }

  private void populateShipments(OrderEntryDto dto, OrderItemData item) {
    Map<Date, ShipmentData> shipments = new HashMap<>();
    for(SizeVariantDto size : dto.getSizeVariants()) {
      ShipmentData shipment = new ShipmentData();
      shipment.setShipmentStatus(size.getShipStatus());
      shipment.setShipDate(size.getShipDate());
      shipment.setQty(Double.valueOf(size.getShipQty()).intValue());
      shipments.merge(size.getShipDate(), shipment, shipmentDataMerger);

    }
    item.setShipments(shipments);
  }

  private void populateNameAndImage(OrderEntryDto dto, OrderItemData item) {
    ProductModel product = productService.getProductForCode(dto.getProduct());
    item.setName(product.getName());
    Optional.of(product.getPicture())
            .map(MediaModel::getURL)
            .ifPresent(item::setImageUrl);
  }

  private void populateAddress(OrderDetailsResponse source, OrderDetailsData target) {
    AddressData address = new AddressData();
    address.setCompanyName(source.getDeliveryAddressMarkFor());

    CountryData country = new CountryData();
    country.setName(source.getDeliveryAddressCountry());
    address.setCountry(country);

    address.setPostalCode(source.getDeliveryAddressPostcode());
    address.setState(source.getDeliveryAddressState());
    address.setTown(source.getDeliveryAddressCity());
    address.setLine1(source.getDeliveryAddressStreet());

    target.setShipTo(address);
  }

  @Required
  public void setProductService(ProductService productService) {
    this.productService = productService;
  }
}
