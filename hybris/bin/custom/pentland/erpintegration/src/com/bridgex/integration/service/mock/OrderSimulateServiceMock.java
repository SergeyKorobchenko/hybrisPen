package com.bridgex.integration.service.mock;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.bridgex.integration.constants.ErpintegrationConstants;
import com.bridgex.integration.domain.ETReturnDto;
import com.bridgex.integration.domain.FutureStocksDto;
import com.bridgex.integration.domain.MaterialInfoDto;
import com.bridgex.integration.domain.MaterialOutputGridDto;
import com.bridgex.integration.domain.MultiBrandCartDto;
import com.bridgex.integration.domain.MultiBrandCartInput;
import com.bridgex.integration.domain.MultiBrandCartOutput;
import com.bridgex.integration.domain.MultiBrandCartResponse;
import com.bridgex.integration.domain.SizeDataDto;
import com.bridgex.integration.service.impl.OrderSimulateServiceImpl;

/**
 * @author Created by konstantin.pavlyukov on 10/30/2017.
 */
public class OrderSimulateServiceMock extends OrderSimulateServiceImpl {

  @Override
  public ResponseEntity<MultiBrandCartResponse> sendRequest(final MultiBrandCartDto requestDto, final Class responseClass) {
    final Random rn = new Random();
    final MultiBrandCartResponse response = new MultiBrandCartResponse();
    final MultiBrandCartOutput responseOutput = new MultiBrandCartOutput();

    final List<ETReturnDto> etReturnList = new ArrayList<>();
    final ETReturnDto etReturn = new ETReturnDto();
    etReturn.setType(ErpintegrationConstants.RESPONSE.ET_RETURN.SUCCESS_TYPE);
    etReturn.setNumber("007");
    etReturn.setMessage("Materials successfully returned");
    etReturnList.add(etReturn);
    response.setEtReturnList(etReturnList);

    final List<MaterialInfoDto> matList = new ArrayList<>();
    final List<MultiBrandCartInput> reqCartInputList = requestDto.getCartInput();
    int orderTotalPrice = 0;
    for (final MultiBrandCartInput cartInput : reqCartInputList) {
      final MaterialInfoDto materialInfoDto = new MaterialInfoDto();
      int unitPrice;
//      materialInfoDto.setUnitPrice(String.valueOf(unitPrice = (rn.nextInt(100) + 10)));
      materialInfoDto.setUnitPrice(String.valueOf(unitPrice = (rn.nextInt(100) + 10)));
      materialInfoDto.setMaterialNumber(cartInput.getMaterialNumber());

      final List<MaterialOutputGridDto> resMatGridList = new ArrayList<>();
      int qtySum = 0;
      int totalPrice = 0;
      for (final SizeDataDto sizeData : cartInput.getSizeData()) {
        final MaterialOutputGridDto resMatGrid = new MaterialOutputGridDto();
        resMatGrid.setAvailableQty(String.valueOf(rn.nextInt(200)));
        resMatGrid.setEan(sizeData.getEan());
        resMatGrid.setUserRequestedQty(sizeData.getQuantity());
        resMatGrid.setFutureStocksDtoList(createMockFutureList());
        resMatGridList.add(resMatGrid);
        qtySum += Integer.valueOf(sizeData.getQuantity());
      }
      totalPrice = unitPrice * qtySum;
      orderTotalPrice += totalPrice;
      materialInfoDto.setTotalPrice(String.valueOf(totalPrice));
      materialInfoDto.setMaterialOutputGridList(resMatGridList);

      matList.add(materialInfoDto);
    }
    responseOutput.setMaterialInfo(matList);
    responseOutput.setSubtotalPrice(String.valueOf(orderTotalPrice));
    responseOutput.setTotalPrice(String.valueOf(orderTotalPrice * 1.1));
    responseOutput.setTotalTaxPrice(String.valueOf(orderTotalPrice * 0.1));
    response.setMultiBrandCartOutput(responseOutput);

    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  private List<FutureStocksDto> createMockFutureList() {
    Date dt = new Date();
    Calendar c = Calendar.getInstance();
    c.setTime(dt);
    c.add(Calendar.DATE, 10);
    dt = c.getTime();
    final List<FutureStocksDto> result = new ArrayList<>();
    final FutureStocksDto future1 = new FutureStocksDto();
    future1.setFutureDate(dt);
    future1.setFutureQty("20");
    result.add(future1);
    c.add(Calendar.DATE, 10);
    dt = c.getTime();
    final FutureStocksDto future2 = new FutureStocksDto();
    future2.setFutureDate(dt);
    future2.setFutureQty("170");
    result.add(future2);
    return result;
  }

}
