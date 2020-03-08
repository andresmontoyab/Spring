package com.jpa.hibernate.springhibernatedepth;

import com.jpa.hibernate.springhibernatedepth.controller.Controller;
import com.jpa.hibernate.springhibernatedepth.service.ServiceMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.support.RestGatewaySupport;

import static org.junit.Assert.*;
import static org.springframework.test.web.client.ExpectedCount.manyTimes;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.ExpectedCount.times;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@ContextConfiguration
@SpringBootTest(classes = SpringHibernateDepthApplication.class)
public class ServiceMockTest {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ServiceMock serviceMock;

    private MockRestServiceServer mockServer;

    @Before
    public void setUp() {
        //RestGatewaySupport gateway = new RestGatewaySupport();
        //gateway.setRestTemplate(restTemplate);
        //mockServer = MockRestServiceServer.createServer(gateway);
        MockRestServiceServer.MockRestServiceServerBuilder builder =
                MockRestServiceServer.bindTo(restTemplate);
        builder.ignoreExpectOrder(true);
        mockServer = builder.build();
    }

    @Test
    public void testGetRootResourceOnce() {
        mockServer.expect(times(1), requestTo("https://jsonplaceholder.typicode.com/todos/2"))
                .andRespond(withSuccess("{message : 'under construction'}", MediaType.APPLICATION_JSON));

        mockServer.expect(times(1), requestTo("https://jsonplaceholder.typicode.com/todos/1"))
                .andRespond(withSuccess("{message : 'under construction'}", MediaType.APPLICATION_JSON));

        mockServer.expect(times(1), requestTo("https://jsonplaceholder.typicode.com/comments"))
                .andRespond(withSuccess("{message : 'under commets'}", MediaType.APPLICATION_JSON));

        serviceMock.getRootResource();

        mockServer.verify();
    }

}