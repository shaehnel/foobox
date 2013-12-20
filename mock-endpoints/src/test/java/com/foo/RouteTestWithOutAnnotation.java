package com.foo;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.UseAdviceWith;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;


@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="RouteTest-context.xml")
@UseAdviceWith(true)
public class RouteTestWithOutAnnotation {
	
	@Produce(uri="direct:route1") private ProducerTemplate producer;
	@EndpointInject(uri="mock:direct:route2") private MockEndpoint endpoint;
	@Autowired private ModelCamelContext context;
	
	/**
	 * When using a {@link AdviceWithRouteBuilder}, mocking the endpoints
	 * seems to work.
	 * <p/>
	 * The result is that the mock endpoint receives the message.
	 *
	 */
	@Test 
	public void test_succeeds() throws Exception {
		context.getRouteDefinition("route1").adviceWith(context, new AdviceWithRouteBuilder() {
			@Override public void configure() throws Exception {
				mockEndpointsAndSkip("*");				
			}
		});
		final Object body = new Integer(42);		
		endpoint.expectedBodiesReceived(body);
		context.start();
		producer.sendBody(body);
		endpoint.assertIsSatisfied(); // succeeds
	}
	
	

}
