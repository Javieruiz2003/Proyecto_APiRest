package es.us.dad.mysql;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;

public class RestClient extends AbstractVerticle {

	public RestClientUtil restClientUtil;

	public void start(Promise<Void> startFuture) {
		WebClientOptions options = new WebClientOptions().setUserAgent("RestClientApp/2.0.2.1");
		options.setKeepAlive(false);
		restClientUtil = new RestClientUtil(WebClient.create(vertx, options));

		/* --------------- GET many request --------------- */

		Promise<Actuador[]> resList = Promise.promise();
		resList.future().onComplete(complete -> {
			if (complete.succeeded()) {
				System.out.println("GetAll:");
				Stream.of(complete.result()).forEach(elem -> {
					System.out.println(elem.toString());
				});
			} else {
				System.out.println(complete.cause().toString());
			}
		});

		//Cambiar el puerto cuando se cambie en otro sitio
		restClientUtil.getRequest(8068, "http://localhost", "api/sensores", 
				Actuador[].class, resList);

		/* --------------- GET one request --------------- */

		Promise<Actuador> res = Promise.promise();
		res.future().onComplete(complete -> {
			if (complete.succeeded()) {
				System.out.println("GetOne");
				System.out.println(complete.result().toString());
			} else {
				System.out.println(complete.cause().toString());
			}
		});

		restClientUtil.getRequest(8068, "http://localhost", "api/sensores/1", 
				Actuador.class, res);


		// --------------- POST request --------------- 

		Promise<SensorValue> resPost = Promise.promise();
		resPost.future().onComplete(complete -> {
			if (complete.succeeded()) {
				System.out.println("Post SensorValue");
				System.out.println(complete.result().toString());
			} else {
				System.out.println(complete.cause().toString());
			}
		});

			
	
		restClientUtil.postRequest(8068, "http://localhost", "api/sensorValue1",
				new SensorValue(44,2, (float)3, (long)0),
				SensorValue.class, resPost);
		
	
		// --------------- POST request --------------- 

		Promise<ActuadorState> postActuadorState = Promise.promise();
		resPost.future().onComplete(complete -> {
			if (complete.succeeded()) {
				System.out.println("Post ActuadorState");
				System.out.println(complete.result().toString());
			} else {
				System.out.println(complete.cause().toString());
			}
		});

		restClientUtil.postRequest(8068, "http://localhost", "api/actuatorState",
				new ActuadorState(44, 2, true, (long) 0), ActuadorState.class, postActuadorState);
				
			
			
		/* --------------- LAUNCH local server --------------- */
		vertx.deployVerticle(RestServer.class.getName(), deploy -> {
			if (deploy.succeeded()) {
				System.out.println("Verticle deployed");
			}else {
				System.out.println("Error deploying verticle");
			}
		});

	}

}
