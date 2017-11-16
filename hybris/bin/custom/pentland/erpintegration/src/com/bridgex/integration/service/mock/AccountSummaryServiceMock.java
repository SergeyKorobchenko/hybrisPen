package com.bridgex.integration.service.mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.bridgex.integration.constants.ErpintegrationConstants;
import com.bridgex.integration.domain.AccountSummaryDetailsDto;
import com.bridgex.integration.domain.AccountSummaryDto;
import com.bridgex.integration.domain.AccountSummaryResponse;
import com.bridgex.integration.domain.ETReturnDto;
import com.bridgex.integration.service.impl.AbstractIntegrationService;
import com.bridgex.integration.service.impl.AccountSummaryServiceImpl;

/**
 * @author Goncharenko Mikhail, created on 09.11.2017.
 */
public class AccountSummaryServiceMock extends AccountSummaryServiceImpl {

  @Override
  public String getServiceName() {
    return "int_sync_02";
  }

  @Override
  public ResponseEntity<AccountSummaryResponse> sendRequest(AccountSummaryDto request, Class responseClass) {
    AccountSummaryResponse responseBody = new AccountSummaryResponse();
    responseBody.setSapCustomerName("AMAZON EU S.A.R.L.");
    responseBody.setSapCustomerId("0000107660");
    responseBody.setStreetLine1("UK Branch");
    responseBody.setStreetLine2("60 Holborn Viaduct.");
    responseBody.setStreetLine3("");
    responseBody.setCity("London");
    responseBody.setCountry("GB");
    responseBody.setHouseNumber("");
    responseBody.setEtReturn(Collections.singletonList(new ETReturnDto(ErpintegrationConstants.RESPONSE.ET_RETURN.SUCCESS_TYPE)));
    responseBody.setPostalCode("223610");
    responseBody.setRegion("LO");

    responseBody.setDetails(generateDetails());

    return new ResponseEntity<AccountSummaryResponse>(responseBody, HttpStatus.OK);
  }

  private List<AccountSummaryDetailsDto> generateDetails() {
    AccountSummaryDetailsDto dto = new AccountSummaryDetailsDto();
    dto.setBrand("8001");
    dto.setBrandName("Speedo");
    dto.setCreditLimit("950000.00");
    dto.setCreditRep("Speedo Finance");
    dto.setCurrentBalance("342.07");
    dto.setOpenBalance("428428.22");
    dto.setPastDueBalance("4680447.11");
    dto.setCurrency("GBP");
    dto.setDays1to30("4000.00");
    dto.setDays31to60("0.00");
    dto.setDays61to90("1232.00");
    dto.setDaysOver90("494.10");

    AccountSummaryDetailsDto dto2 = new AccountSummaryDetailsDto();
    dto2.setBrand("8001");
    dto2.setBrandName("Mitre");
    dto2.setCreditLimit("950000.00");
    dto2.setCreditRep("Mitre Finance");
    dto2.setCurrentBalance("342.07");
    dto2.setOpenBalance("428428.22");
    dto2.setPastDueBalance("4680447.11");
    dto2.setCurrency("GBP");
    dto2.setDays1to30("4000.00");
    dto2.setDays31to60("0.00");
    dto2.setDays61to90("1232.00");
    dto2.setDaysOver90("494.10");

    List<AccountSummaryDetailsDto> list = new ArrayList<>();
    list.add(dto);
    list.add(dto2);
    return list;
  }

}
