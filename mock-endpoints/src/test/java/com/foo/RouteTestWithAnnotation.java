package com.foo;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.MockEndpointsAndSkip;
import org.apache.camel.test.spring.UseAdviceWith;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;


@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="RouteTest-context.xml")
@MockEndpointsAndSkip("direct:*")
@UseAdviceWith(true)
public class RouteTestWithAnnotation {
	
	@Produce(uri="direct:route1") private ProducerTemplate producer;
	@EndpointInject(uri="mock:direct:route2") private MockEndpoint endpoint;
	@Autowired private ModelCamelContext context;
	
	/**
	 * The test fails although it should not.
	 * {@link @MockEndpointsAndSkip} should mock all endpoints including route2 in the route definition, 
	 * leading to the mock endpoint to receive the body object.
	 * <p/>
	 * The result is however that the mock endpoint does not receive the message.
	 *
	 */
	@Test 
	public void test_fails() throws Exception {
		final Object body = new Integer(42);
		endpoint.expectedBodiesReceived(body);
		context.start();
		producer.sendBody(body);
		endpoint.assertIsSatisfied(); // fails
	}

}
