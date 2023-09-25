package com.temperaturesensors2.tempo;

import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class SensorVerticle extends AbstractVerticle {
	
	Logger logger = LoggerFactory.getLogger(SensorVerticle.class);

	private final String uuid = UUID.randomUUID().toString();
	private double temperature = 21.0;
	private final Random random = new Random();

	@Override
	public void start(Promise<Void> startPromise) {
		vertx.setPeriodic(2000, this::updateTemperature);
		startPromise.complete();
	}

	private void updateTemperature(Long id) {
		temperature = temperature + (random.nextGaussian() / 2.0d);
		logger.info("Logger temperature updated: {}", temperature);
	}

}
