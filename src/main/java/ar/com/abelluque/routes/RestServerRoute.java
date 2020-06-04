package ar.com.abelluque.routes;

import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

import ar.com.abelluque.builders.AbstractRouteBuilder;

@Component
public class RestServerRoute extends AbstractRouteBuilder {

	@Override
	public void configure() {
		
		restConfiguration().contextPath("/camel-gateway").apiContextPath("/api-doc")
				.apiProperty("api.title", "Camel REST Gateway").apiProperty("api.version", "1.0")
				.apiProperty("cors", "true")
				.apiProperty("api.specification.contentType.json", "application/vnd.oai.openapi+json;version=2.0")
				.apiProperty("api.specification.contentType.yaml", "application/vnd.oai.openapi;version=2.0")
				.apiContextRouteId("doc-api").component("servlet").bindingMode(RestBindingMode.json);

		rest("/sayhello").description("Say Hello REST service")
				.get("/").description("Say Hello").route()
				.routeId("say-api")
				.to("direct:sayhello")
				.endRest();

	}

}
