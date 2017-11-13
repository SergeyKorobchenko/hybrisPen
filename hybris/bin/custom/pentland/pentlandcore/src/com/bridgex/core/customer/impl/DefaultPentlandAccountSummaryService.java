package com.bridgex.core.customer.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.bridgex.core.customer.PentlandAccountSummaryService;
import com.bridgex.integration.domain.AccountSummaryDto;
import com.bridgex.integration.domain.AccountSummaryResponse;
import com.bridgex.integration.domain.BrandDto;
import com.bridgex.integration.service.IntegrationService;

import de.hybris.platform.servicelayer.i18n.CommonI18NService;

/**
 * @author Goncharenko Mikhail, created on 13.11.2017.
 */
public class DefaultPentlandAccountSummaryService implements PentlandAccountSummaryService {

  public static final String SERVICE_CUSTOMER = "B2B_Hybris";
  private CommonI18NService  commonI18NService;
  private IntegrationService<AccountSummaryDto,AccountSummaryResponse> integrationService;


  public AccountSummaryResponse requestAccountSummary(AccountSummaryDto request) {
    return integrationService.sendRequest(request, AccountSummaryResponse.class).getBody();
  }

  @Override
  public AccountSummaryResponse getAccountSummaryInfo(String sapCustomerId, List<String> sapBrand) {
    return requestAccountSummary(createAccountSummaryRequest(sapCustomerId, sapBrand));
  }

  private AccountSummaryDto createAccountSummaryRequest(String sapCustomerId, List<String> sapBrand) {

    AccountSummaryDto dto = new AccountSummaryDto();
    dto.setSapCustomerId(sapCustomerId);
    dto.setLanguage(commonI18NService.getCurrentLanguage().getIsocode().toUpperCase());
    dto.setServiceCustomer(SERVICE_CUSTOMER);

    List<BrandDto> brands = sapBrand.stream().map(br -> {
      BrandDto brand = new BrandDto();
      brand.setBrand(br);
      return brand;
    }).collect(Collectors.toList());

    dto.setBrands(brands);

    return dto;
  }

}
