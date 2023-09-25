package com.temperaturesensors2.tempo;

import io.vertx.core.Vertx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TempoApplication {

	public static void main(String[] args) {

		System.out.println("Hello Bankier Olegs 2!");

		SpringApplication.run(TempoApplication.class, args);

		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new SensorVerticle());
	}

}
