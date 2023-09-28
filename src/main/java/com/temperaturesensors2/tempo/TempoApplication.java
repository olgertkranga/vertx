package com.temperaturesensors2.tempo;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TempoApplication {
	
	private static final Logger logger = LoggerFactory.getLogger(TempoApplication.class);

	public static void main(String[] args) {
		
		//curl -H "X-Header: value" http://localhost:8080/data
		//https://www.youtube.com/watch?v=VQpF6oTCxGI
		//39:53
		//48:20

		System.out.println("Hello Bankier Olegs!");
		SpringApplication.run(TempoApplication.class, args);
		
		Vertx.clusteredVertx(new VertxOptions())
		.onSuccess(vertx -> {
			vertx.deployVerticle(new SensorVerticle());
		})
		.onFailure(failure -> {
			logger.error("Woops", failure);
		});
		
		//Vertx vertx = Vertx.vertx();
		////vertx.deployVerticle(new SensorVerticle());
		//vertx.deployVerticle("SensorVerticle", new DeploymentOptions().setInstances(1));
		//vertx.eventBus().<JsonObject>consumer("temperature.updates", message -> {
		//	logger.info(">>> {}", message.body().encodePrettily());
		//});
	}

}
