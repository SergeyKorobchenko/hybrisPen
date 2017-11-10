package com.bridgex.integration.service;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import com.bridgex.integration.domain.OrderDetailsDto;
import com.bridgex.integration.domain.OrderDetailsResponse;

import de.hybris.platform.core.Registry;
import de.hybris.platform.util.Config;

/**
 * @author Goncharenko Mikhail, created on 09.11.2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/test/erpintegration-test-spring.xml")
public class OrderDetailsServiceTest {

  @Resource
  IntegrationService<OrderDetailsDto, OrderDetailsResponse> orderDetailsService;

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

    mockServer.expect(requestTo(Config.getString("erp.integration.int_sync_06.url", "")))
              .andExpect(method(HttpMethod.POST))
              .andRespond(withSuccess("{\"ET_RETURN\":{\"TYPE\":\"TEST\",\"TTTT\":\"1111\"}}", MediaType.APPLICATION_JSON));

    OrderDetailsDto dto = new OrderDetailsDto();
    dto.setOrderCode("00000001");
    dto.setServiceConsumer("Hybris_B2B");
    dto.setCustomerViewFlag("X");
    dto.setLanguage("EN");
    dto.setOrderType("C");
    ResponseEntity<OrderDetailsResponse> result = orderDetailsService.sendRequest(dto, OrderDetailsResponse.class);
    OrderDetailsResponse body = result.getBody();
    assertEquals(body.getEtReturn().getType(),"TEST");
  }


}