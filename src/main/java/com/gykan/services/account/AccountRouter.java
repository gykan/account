package com.gykan.services.account;

import com.gykan.services.common.model.Account;
import org.apache.camel.Exchange;
import org.apache.camel.ShutdownRunningTask;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.spring.boot.FatJarRouter;
import org.apache.camel.zipkin.starter.CamelZipkin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@CamelZipkin
public class AccountRouter extends FatJarRouter {

	@Value("${port}")
	private int port;
		
	@Override
	public void configure() throws Exception {
		String consulUrl = System.getProperty("consul");

		if (consulUrl == null) {
			consulUrl = "http://127.0.0.1:8500";
		}

		restConfiguration()
			.component("netty4-http")
			.bindingMode(RestBindingMode.json)
			.port(port);
		
		from("direct:start").routeId("account-consul").marshal().json(JsonLibrary.Jackson)
			.setHeader(Exchange.HTTP_METHOD, constant("PUT"))
			.setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
			.to(consulUrl + "/v1/agent/service/register");
		from("direct:stop").shutdownRunningTask(ShutdownRunningTask.CompleteAllTasks)
			.toD(consulUrl + "/v1/agent/service/deregister/${header.id}");
		
		rest("/account")
			.get("/{id}")
				.to("bean:accountService?method=findById(${header.id})")
			.get("/customer/{customerId}")
				.to("bean:accountService?method=findByCustomerId(${header.customerId})")
			.get("/")
				.to("bean:accountService?method=findAll")
			.post("/").consumes("application/json").type(Account.class)
				.to("bean:accountService?method=add(${body})");
		
	}

}
