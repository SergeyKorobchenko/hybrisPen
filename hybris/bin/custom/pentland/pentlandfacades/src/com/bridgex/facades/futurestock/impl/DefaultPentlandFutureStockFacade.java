package com.bridgex.facades.futurestock.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.spockframework.util.Nullable;
import org.springframework.beans.factory.annotation.Required;

import com.bridgex.core.product.OrderSimulationService;
import com.bridgex.facades.constants.PentlandfacadesConstants;
import com.bridgex.facades.product.PentlandProductFacade;
import com.bridgex.integration.domain.FutureStocksDto;
import com.bridgex.integration.domain.MaterialInfoDto;
import com.bridgex.integration.domain.MaterialOutputGridDto;
import com.bridgex.integration.domain.MultiBrandCartDto;
import com.bridgex.integration.domain.MultiBrandCartOutput;
import com.bridgex.integration.domain.MultiBrandCartResponse;

import de.hybris.platform.acceleratorfacades.futurestock.impl.DefaultFutureStockFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.FutureStockData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.StockData;

/**
 * @author Created by konstantin.pavlyukov@masterdata.ru on 11/23/2017.
 */
public class DefaultPentlandFutureStockFacade extends DefaultFutureStockFacade {

  private PentlandProductFacade pentlandProductFacade;
  private OrderSimulationService orderSimulationService;

  private SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);

  @Override
  @Nullable
  public Map<String, List<FutureStockData>> getFutureAvailabilityForSelectedVariants(final String productCode,
                                                                                     final List<String> skus)
  {
    Map<String, List<FutureStockData>> result = new HashMap<>();
    final ProductData productData = getPentlandProductFacade().getProductForCodeAndOptions(productCode,
                                                                              Arrays.asList(ProductOption.BASIC, ProductOption.VARIANT_MATRIX_BASE));
    if (CollectionUtils.isEmpty(productData.getVariantMatrix())) {
      return null;
    }
    final MultiBrandCartDto request = getPentlandProductFacade().createOrderFormRequest(productData.getVariantMatrix().get(0).getElements(), new Date());
    final MultiBrandCartResponse response = getOrderSimulationService().simulateOrder(request);

    if (!getOrderSimulationService().successResponse(response)) {
      return null;
    }
    final MultiBrandCartOutput multiBrandCartOutput = response.getMultiBrandCartOutput();
    if (multiBrandCartOutput == null) {
      return null;
    }
    final List<MaterialInfoDto> matListResp = multiBrandCartOutput.getMaterialInfo();
    if (CollectionUtils.isEmpty(matListResp)) {
      return null;
    }

    matListResp.forEach(material -> {
      final List<MaterialOutputGridDto> grid = material.getMaterialOutputGridList();
      if (CollectionUtils.isNotEmpty(grid)) {
        grid.forEach(size -> {
          final List<FutureStocksDto> futures = size.getFutureStocksDtoList();
          if (CollectionUtils.isNotEmpty(futures)) {
            final List<FutureStockData> futureDataList = new ArrayList<>();
            futures.forEach(f -> {
              futureDataList.add(populateFutureData(f));
            });
            result.put(size.getEan(), futureDataList);
          }
        });
      }
    });

    return result;
  }

  protected FutureStockData populateFutureData(final FutureStocksDto dto) {
    final FutureStockData futureStockData = new FutureStockData();
    futureStockData.setDate(dto.getFutureDate());
    futureStockData.setFormattedDate(sdf.format(dto.getFutureDate()));
    final StockData stock = new StockData();
    stock.setStockLevel((long)Double.parseDouble(dto.getFutureQty()));
    stock.setStockThreshold(PentlandfacadesConstants.DEFAULT_STOCK_THRESHOLD);
    futureStockData.setStock(stock);
    return futureStockData;

  }

  public PentlandProductFacade getPentlandProductFacade() {
    return pentlandProductFacade;
  }

  @Required
  public void setPentlandProductFacade(PentlandProductFacade pentlandProductFacade) {
    this.pentlandProductFacade = pentlandProductFacade;
  }

  public OrderSimulationService getOrderSimulationService() {
    return orderSimulationService;
  }

  @Required
  public void setOrderSimulationService(OrderSimulationService orderSimulationService) {
    this.orderSimulationService = orderSimulationService;
  }
}
