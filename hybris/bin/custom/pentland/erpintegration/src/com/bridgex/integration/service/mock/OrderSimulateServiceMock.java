package com.bridgex.integration.service.mock;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.bridgex.integration.constants.ErpintegrationConstants;
import com.bridgex.integration.domain.ETReturnDto;
import com.bridgex.integration.domain.MaterialInfoDto;
import com.bridgex.integration.domain.MultiBrandCartDto;
import com.bridgex.integration.domain.MultiBrandCartResponse;
import com.bridgex.integration.service.impl.OrderSimulateServiceImpl;

/**
 * @author Created by konstantin.pavlyukov on 10/30/2017.
 */
public class OrderSimulateServiceMock extends OrderSimulateServiceImpl {

  @Override
  public ResponseEntity<MultiBrandCartResponse> sendRequest(final MultiBrandCartDto requestDto, final Class responseClass) {
    final MultiBrandCartResponse response = new MultiBrandCartResponse();
    final List<ETReturnDto> etReturnList = new ArrayList<>();
    final ETReturnDto etReturn = new ETReturnDto();
    etReturn.setType(ErpintegrationConstants.RESPONSE.ET_RETURN.SUCCESS_TYPE);
    etReturn.setNumber("007");
    etReturn.setMessage("Materials successfully returned");
    etReturnList.add(etReturn);
    response.setEtReturn(etReturnList);

    final List<MaterialInfoDto> matList = new ArrayList<>();
    final MaterialInfoDto materialInfoDto = new MaterialInfoDto();
    materialInfoDto.setTotalPrice("26");
    materialInfoDto.setMaterialNumber(requestDto.getCartInput().get(0).getMaterialNumber());
    matList.add(materialInfoDto);
    response.setMaterialInfo(matList);

    return new ResponseEntity<>(response, HttpStatus.OK);
  }

}
