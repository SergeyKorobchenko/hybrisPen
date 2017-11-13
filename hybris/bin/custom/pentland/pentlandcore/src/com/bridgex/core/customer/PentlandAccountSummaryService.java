package com.bridgex.core.customer;

import java.util.List;

import com.bridgex.integration.domain.AccountSummaryDto;
import com.bridgex.integration.domain.AccountSummaryResponse;

/**
 * @author Goncharenko Mikhail, created on 10.11.2017.
 */
public interface PentlandAccountSummaryService {

  AccountSummaryResponse getAccountSummaryInfo(String sapCustomerId, List<String> sapBrand);

}
