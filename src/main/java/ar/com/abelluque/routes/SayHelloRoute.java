package ar.com.abelluque.routes;

import org.springframework.stereotype.Component;

import ar.com.abelluque.builders.AbstractRouteBuilder;

@Component
public class SayHelloRoute extends AbstractRouteBuilder {
	
	@Override
	public void configure() {
		from("direct:sayhello").routeId("Say-hello-route")
			.log("Hello!!! -- Log Message")
		.end();
	}

}
