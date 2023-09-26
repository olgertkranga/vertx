package com.temperaturesensors2.tempo;

import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.PayloadApplicationEvent;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;

public class SensorVerticle extends AbstractVerticle {

	private static final Logger logger = LoggerFactory.getLogger(SensorVerticle.class);
	private static final int httpPort = Integer.parseInt(System.getenv().getOrDefault("HTTP_PORT", "8080"));

	private final String uuid = UUID.randomUUID().toString();
	private double temperature = 21.0;
	private final Random random = new Random();
	
	private MySQLPool mySQLPool;

	@Override
	public void start(Promise<Void> startPromise) {
		mySQLPool = MySQLPool.pool(vertx, new MySQLConnectOptions()
				.setHost("localhost")
				.setUser("root")
				.setDatabase("ecommerce")
				.setPassword("root"), new PoolOptions()
				);		
		
		//vertx.eventBus().consumer("cars");
		
		Router router = Router.router(vertx);
		router.get("/cars").handler(this::getAllData);
		
		//vertx.setPeriodic(2000, this::updateTemperature);
		//Router router = Router.router(vertx);
		//router.get("/data").handler(this::getData);

		vertx.createHttpServer()
		.requestHandler(router)
		.listen(httpPort)
		.onSuccess(ok -> {
			logger.info("http server running: http://127.0.0.1:{}", httpPort);
		}).onFailure(startPromise::fail);
	}
	
	private void getAllData(RoutingContext context) {
		logger.info("Processing HTTP request from {}", context.request().remoteAddress());
		String query = "select name, model, color from ecommerce.cars";
		mySQLPool.preparedQuery(query)
		.execute()
		.onSuccess(rows -> {
			JsonArray array = new JsonArray();
			for (Row row : rows) {
				array.add(new JsonObject()
						.put("name", row.getString("name"))
						.put("model", row.getString("model"))
						.put("color", row.getString("color"))
				);
			}
			context.response()
				.putHeader("Content-Type", "application/json")
				.setStatusCode(200)
				.end(array.encode());
		})
		.onFailure(failure -> {
			logger.info("Woops", failure);
			context.fail(500);
		});
		
	}

	private void getData(RoutingContext context) {
		logger.info("Processing HTTP request from {}", context.request().remoteAddress());

		JsonObject payload = new JsonObject()
				.put("uuid", uuid)
				.put("temperature", temperature)
				.put("timestamp", System.currentTimeMillis());
		context.response()
		.putHeader("Context-Type", "application/json")
		.setStatusCode(200)
		.end(payload.encode());
		//JsonObject payload = new JsonObject()
		//		.put("uuid", uuid)
		//		.put("temperature", temperature)
		//		.put("timestamp", System.currentTimeMillis());
		//context.response()
		//.putHeader("Context-Type", "application/json")
		//.setStatusCode(200)
		//.end(payload.encode());
	}

	private void updateTemperature(Long id) {
		temperature = temperature + (random.nextGaussian() / 2.0d);
		//logger.info("Logger temperature updated: {}", temperature);
	}

}
