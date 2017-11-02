package com.bridgex.facades.order.converters.populator;

import java.util.ArrayList;
import java.util.List;

import com.bridgex.facades.order.data.OrderDetailsData;
import com.bridgex.facades.order.data.OrderItemData;
import com.bridgex.facades.order.data.ShipmentData;
import com.bridgex.integration.domain.OrderDetailsResponse;
import com.bridgex.integration.domain.OrderEntryDto;
import com.bridgex.integration.domain.ShipmentDto;
import com.bridgex.integration.domain.SizeVariantDto;

import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

/**
 * @author Goncharenko Mikhail, created on 02.11.2017.
 */
public class OrderDetailsPopulator implements Populator<OrderDetailsResponse, OrderDetailsData> {

  Converter<ShipmentDto,ShipmentData> shipmentConverter;

  @Override
  public void populate(OrderDetailsResponse source, OrderDetailsData target) throws ConversionException {
    target.setOrderNumber(source.getCode());
    target.setPONumber(source.getPurshaseOrderNumber());
    target.setOrderStatus(source.getStatus());
    target.setOrderCreationDate(source.getCreationTime());
    target.setRdd(source.getRdd());
    target.setOrderNet(source.getNetPrice());
    target.setOrderTax(source.getTaxPrice());
    target.setOrderTotal(source.getTotalPrice());

    populateItems(source, target);

    AddressData address = new AddressData();
    address.setCompanyName(source.getDeliveryAddressMarkFor());

    CountryData country = new CountryData();
    country.setName(source.getDeliveryAddressCountry());
    address.setCountry(country);

    address.setPostalCode(source.getDeliveryAddressPostcode());
    address.setLine1(source.getDeliveryAddressState());
    address.setTown(source.getDeliveryAddressCity());
    address.setLine2(source.getDeliveryAddressStreet());

    target.setShipTo(address);



  }

  private void populateItems(OrderDetailsResponse source, OrderDetailsData target) {
    List<OrderItemData> items = new ArrayList<>();
    for (OrderEntryDto dto : source.getOrderEntries()) {
      for(SizeVariantDto size : dto.getSizeVariants()) {
        OrderItemData item = new OrderItemData();
        item.setImage("url");
        item.setItemStatus(dto.getEntryStatus());
        item.setName(dto.getProduct());
        item.setNumber(dto.getEntryNumber());
        item.setQty(Integer.parseInt(dto.getQuantity()));
        item.setShipments(shipmentConverter.convertAll(size.getShipments()));
        items.add(item);
      }
    }
    target.setOrderItems(items);

  }
}
