package com.bridgex.facades.order.converters.populator;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.BiFunction;

import org.apache.commons.lang.StringUtils;
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
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

/**
 * @author Goncharenko Mikhail, created on 02.11.2017.
 */
public class OrderDetailsPopulator implements Populator<OrderDetailsResponse, OrderData> {

  private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OrderDetailsPopulator.class);

  private ProductService productService;
  private PriceDataFactory priceDataFactory;
  private SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

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

    target.setSoldTo(source.getCustomerId());
    target.setSoldToName(source.getCustomerName());
    target.setMarkFor(source.getDeliveryAddressMarkForId());
    target.setMarkForName(source.getDeliveryAddressMarkForName());

    populateItems(source, target);
    populateAddress(source, target);
  }

    private void populateItems(OrderDetailsResponse source, OrderData target) {
    List<OrderItemData> items = new ArrayList<>();

    for (OrderEntryDto dto : source.getOrderEntries()) {
      OrderItemData item = new OrderItemData();
      item.setNumber(dto.getEntryNumber());
      item.setItemStatus(dto.getEntryStatus());
      item.setTotalPrice(populatePriceData(dto.getNetPrice(), source.getCurrency()));
      item.setQty(getInt(dto.getQuantity()));
      item.setShippedQty(getInt(dto.getShippedQuantity()));
      item.setShipDate(parseShipDate(dto.getShippedDate()));
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

      Date shipDate = parseShipDate(size);

      shipment.setShipDate(shipDate);
      shipment.setQty(getInt(size.getShipQty()));
      shipments.merge(shipDate, shipment, shipmentDataMerger);
    }
    item.setShipments(shipments);
  }

  private Date parseShipDate(SizeVariantDto size) {
    Date shipDate = null;
    if (size.getShipDate() != null) {
      try {
        shipDate = formatter.parse(size.getShipDate());
      } catch (ParseException e) {
        LOG.error("Unable to parse shipment date. Change to N/A");
      }
    }
    return shipDate;
  }

  private Date parseShipDate(String date) {
    Date shipDate = null;
    if (date != null && !"00000000".equals(date)) {
      try {
        shipDate = formatter.parse(date);
      } catch (ParseException e) {
        LOG.error("Unable to parse shipment date. Change to N/A");
      }
    }
    return shipDate;
  }

  private void populateNameAndImage(OrderEntryDto dto, OrderItemData item) {
    try {
      ProductModel product = productService.getProductForCode(dto.getProduct());
      if(StringUtils.isNotEmpty(product.getSapName())) {
        item.setName(product.getSapName());
      }else{
        item.setName(product.getName());
      }
      } catch (UnknownIdentifierException e) {
        LOG.warn("Error while loading product: " + e.getMessage());
        item.setName(StringUtils.EMPTY);
    }
  }

  private void populateAddress(OrderDetailsResponse source, OrderData target) {
    AddressData address = new AddressData();

    CountryData country = new CountryData();
    country.setName(source.getDeliveryAddressCountry());
    address.setCountry(country);
    address.setId(source.getDeliveryAddressId());
    address.setPostalCode(source.getDeliveryAddressPostcode());
    address.setState(source.getDeliveryAddressState());
    address.setTown(source.getDeliveryAddressCity());
    address.setLine1(source.getDeliveryAddressStreet());
    address.setDisplayName(source.getDeliveryAddressName());
    target.setDeliveryAddress(address);
  }

  private PriceData populatePriceData(String price, String currency) {
    return priceDataFactory.create(PriceDataType.BUY, BigDecimal.valueOf(getDouble(price)), currency);
  }

  private double getDouble(String price) {
    try {
      return Double.parseDouble(Optional.ofNullable(price).orElse("0"));
    } catch (NumberFormatException e) {
      return 0D;
    }
  }

  private int getInt(String value) {
    try {
      return Double.valueOf((Optional.ofNullable(value).orElse("0"))).intValue();
    } catch (NumberFormatException e) {
      return 0;
    }
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
