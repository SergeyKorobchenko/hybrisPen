package com.bridgex.core.customer.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.expression.AccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.client.ResourceAccessException;

import com.bridgex.core.customer.PentlandAccountSummaryService;
import com.bridgex.integration.constants.ErpintegrationConstants;
import com.bridgex.integration.domain.AccountSummaryDto;
import com.bridgex.integration.domain.AccountSummaryResponse;
import com.bridgex.integration.domain.BrandDto;
import com.bridgex.integration.service.IntegrationService;

import de.hybris.platform.servicelayer.i18n.CommonI18NService;

/**
 * @author Goncharenko Mikhail, created on 13.11.2017.
 */
public class DefaultPentlandAccountSummaryService implements PentlandAccountSummaryService {

  private CommonI18NService commonI18NService;
  private IntegrationService<AccountSummaryDto,AccountSummaryResponse> integrationService;

  public AccountSummaryResponse requestAccountSummary(AccountSummaryDto request) {
    AccountSummaryResponse response = integrationService.sendRequest(request, AccountSummaryResponse.class).getBody();
    checkRequestSuccess(response);
    return response;
  }

  private void checkRequestSuccess(AccountSummaryResponse response) {
    if (response.getEtReturn() != null) {
      if (response.getEtReturn().getType().equals(ErpintegrationConstants.RESPONSE.ET_RETURN.ERROR_TYPE)) {
        throw new ResourceAccessException("ERP request failed with response: " + response.getEtReturn().getMessage());
      }
    }
  }

  @Override
  public AccountSummaryResponse getAccountSummaryInfo(String sapCustomerId, List<String> sapBrand) {
    return requestAccountSummary(createAccountSummaryRequest(sapCustomerId, sapBrand));
  }

  private AccountSummaryDto createAccountSummaryRequest(String sapCustomerId, List<String> sapBrand) {

    AccountSummaryDto dto = new AccountSummaryDto();
    dto.setSapCustomerId(sapCustomerId);
    dto.setLanguage(commonI18NService.getCurrentLanguage().getIsocode().toUpperCase());

    List<BrandDto> brands = sapBrand.stream().map(br -> {
      BrandDto brand = new BrandDto();
      brand.setBrand(br);
      return brand;
    }).collect(Collectors.toList());

    dto.setBrands(brands);

    return dto;
  }

  public CommonI18NService getCommonI18NService() {
    return commonI18NService;
  }

  public void setCommonI18NService(CommonI18NService commonI18NService) {
    this.commonI18NService = commonI18NService;
  }

  public IntegrationService<AccountSummaryDto, AccountSummaryResponse> getIntegrationService() {
    return integrationService;
  }

  public void setIntegrationService(IntegrationService<AccountSummaryDto, AccountSummaryResponse> integrationService) {
    this.integrationService = integrationService;
  }
}
