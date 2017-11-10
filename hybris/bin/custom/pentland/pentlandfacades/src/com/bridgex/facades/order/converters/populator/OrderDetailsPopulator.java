package com.bridgex.facades.order.converters.populator;

import static org.apache.commons.lang3.time.DateUtils.ceiling;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.BiFunction;

import org.springframework.beans.factory.annotation.Required;

import com.bridgex.facades.order.data.OrderItemData;
import com.bridgex.facades.order.data.ShipmentData;
import com.bridgex.integration.domain.OrderDetailsResponse;
import com.bridgex.integration.domain.OrderEntryDto;
import com.bridgex.integration.domain.SizeVariantDto;

import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
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
public class OrderDetailsPopulator implements Populator<OrderDetailsResponse, OrderData> {

  private ProductService productService;
  private PriceDataFactory priceDataFactory;

  private BiFunction<ShipmentData, ShipmentData, ShipmentData> shipmentDataMerger = (shipment1,shipment2) -> {
    shipment1.setQty(shipment1.getQty() + shipment2.getQty());
    return shipment1;
  };

  @Override
  public void populate(OrderDetailsResponse source, OrderData target) throws ConversionException {
    target.setCode(source.getCode());
    target.setPurchaseOrderNumber(source.getPurshaseOrderNumber());
    target.setStatusDisplay(source.getStatus());
    target.setCreated(source.getCreationTime());
    target.setRdd(source.getRdd());
    target.setSubTotal(populatePriceData(source.getNetPrice(), source.getCurrency()));
    target.setTotalTax(populatePriceData(source.getTaxPrice(), source.getCurrency()));
    target.setTotalPriceWithTax(populatePriceData(source.getTotalPrice(), source.getCurrency()));
    target.setTotalUnitCount(Double.valueOf(source.getTotalQuantity()).intValue());

    target.setSoldTo(source.getCustomerName() + " " + source.getCustomerId());
    target.setMarkFor(source.getDeliveryAddressMarkForId() + " " + source.getDeliveryAddressMarkForName());

    populateItems(source, target);
    populateAddress(source, target);
  }

  private PriceData populatePriceData(String price, String currency) {
    return priceDataFactory.create(PriceDataType.BUY, BigDecimal.valueOf(Double.parseDouble(price)), currency);
  }

  private void populateItems(OrderDetailsResponse source, OrderData target) {
    List<OrderItemData> items = new ArrayList<>();

    for (OrderEntryDto dto : source.getOrderEntries()) {
      OrderItemData item = new OrderItemData();
      item.setNumber(dto.getEntryNumber());
      item.setItemStatus(dto.getEntryStatus());
      item.setTotalPrice(populatePriceData(dto.getPrice(), source.getCurrency()));
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
      shipments.merge(ceiling(size.getShipDate(), Calendar.DAY_OF_YEAR), shipment, shipmentDataMerger);
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

  private void populateAddress(OrderDetailsResponse source, OrderData target) {
    AddressData address = new AddressData();

    CountryData country = new CountryData();
    country.setName(source.getDeliveryAddressCountry());
    address.setCountry(country);

    address.setPostalCode(source.getDeliveryAddressPostcode());
    address.setState(source.getDeliveryAddressState());
    address.setTown(source.getDeliveryAddressCity());
    address.setLine1(source.getDeliveryAddressStreet());

    target.setDeliveryAddress(address);
  }

  @Required
  public void setProductService(ProductService productService) {
    this.productService = productService;
  }

  @Required
  public void setPriceDataFactory(PriceDataFactory priceDataFactory) {
    this.priceDataFactory = priceDataFactory;
  }
}
