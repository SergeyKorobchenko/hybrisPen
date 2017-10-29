package com.bridgex.integration.service;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.bridgex.integration.domain.MultiBrandCartDto;
import com.bridgex.integration.domain.MultiBrandCartResponse;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.Registry;

/**
 * @author Created by konstantin.pavlyukov on 10/29/2017.
 */
@UnitTest
@ContextConfiguration("classpath:/test/erpintegration-test-spring.xml")
@ActiveProfiles("devlocal")
public class OrderSimulateServiceTest extends AbstractJUnit4SpringContextTests {

  @Resource
  IntegrationService<MultiBrandCartDto> orderSimulateService;

  @Test
  public void orderSimulateServiceSendRequestTest() {
    Registry.activateMasterTenant();
    MultiBrandCartDto dto = new MultiBrandCartDto();
    dto.setDocType("C");
    dto.setServiceConsumer("Hybris_B2B");
    ResponseEntity result = orderSimulateService.sendRequest(dto, MultiBrandCartResponse.class);

  }
}
