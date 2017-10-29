package com.bridgex.integration.service;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import javax.annotation.Resource;
import javax.validation.constraints.AssertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import com.bridgex.integration.domain.MultiBrandCartDto;
import com.bridgex.integration.domain.MultiBrandCartResponse;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.util.Config;

/**
 * @author Created by konstantin.pavlyukov on 10/29/2017.
 */
@UnitTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/test/erpintegration-test-spring.xml")
public class OrderSimulateServiceTest {

  @Resource
  IntegrationService<MultiBrandCartDto, MultiBrandCartResponse> orderSimulateService;

  MockRestServiceServer mockServer;

  @Resource
  RestTemplate restTemplate;

  @Before
  public void setUp() {
    mockServer = MockRestServiceServer.createServer(restTemplate);
  }

  @Test
  public void orderSimulateServiceSendRequestTest() throws Exception{
    Registry.activateMasterTenant();

    mockServer.expect(requestTo(Config.getString("erp.integration.int_sync_04.url", "")))
              .andExpect(method(HttpMethod.POST))
              .andRespond(withSuccess("{\"ET_RETURN\":{\"TYPE\":\"TEST\"}}", MediaType.APPLICATION_JSON));

    MultiBrandCartDto dto = new MultiBrandCartDto();
    dto.setDocType("C");
    dto.setServiceConsumer("Hybris_B2B");
    ResponseEntity<MultiBrandCartResponse> result = orderSimulateService.sendRequest(dto, MultiBrandCartResponse.class);
    MultiBrandCartResponse body = result.getBody();
    assertEquals(body.getEtReturn().getType(),"TEST");
  }


}
