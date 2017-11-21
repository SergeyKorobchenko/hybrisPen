package com.bridgex.integration.service.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.bridgex.integration.constants.ErpintegrationConstants;
import com.bridgex.integration.domain.ETReturnDto;
import com.bridgex.integration.domain.MaterialInfoDto;
import com.bridgex.integration.domain.MaterialOutputGridDto;
import com.bridgex.integration.domain.MultiBrandCartDto;
import com.bridgex.integration.domain.MultiBrandCartInput;
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
    final List<ETReturnDto> etReturnList = new ArrayList<>();
    final ETReturnDto etReturn = new ETReturnDto();
    etReturn.setType(ErpintegrationConstants.RESPONSE.ET_RETURN.SUCCESS_TYPE);
    etReturn.setNumber("007");
    etReturn.setMessage("Materials successfully returned");
    etReturnList.add(etReturn);
    response.setEtReturnList(etReturnList);

    final List<MaterialInfoDto> matList = new ArrayList<>();
    final List<MultiBrandCartInput> reqCartInputList = requestDto.getCartInput();
    for (final MultiBrandCartInput cartInput : reqCartInputList) {
      final MaterialInfoDto materialInfoDto = new MaterialInfoDto();
      int unitPrice;
      materialInfoDto.setUnitPrice(String.valueOf(unitPrice = (rn.nextInt(100) + 10)));
      materialInfoDto.setMaterialNumber(cartInput.getMaterialNumber());

      final List<MaterialOutputGridDto> resMatGridList = new ArrayList<>();
      int qtySum = 0;
      for (final SizeDataDto sizeData : cartInput.getSizeData()) {
        final MaterialOutputGridDto resMatGrid = new MaterialOutputGridDto();
        resMatGrid.setAvailableQty(String.valueOf(qtySum += rn.nextInt(200)));
        resMatGrid.setEan(sizeData.getEan());
        resMatGridList.add(resMatGrid);
      }
      materialInfoDto.setTotalPrice(String.valueOf(unitPrice * qtySum));
      materialInfoDto.setMaterialOutputGridList(resMatGridList);

      matList.add(materialInfoDto);
    }
    response.setMaterialInfo(matList);

    return new ResponseEntity<>(response, HttpStatus.OK);
  }

}
