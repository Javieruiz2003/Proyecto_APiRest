package es.us.dad.mysql;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.MqttClientOptions;

public class RestClient extends AbstractVerticle {

    private RestClientUtil restClientUtil;
    
    int puertoBajoNivel = RestServer.puertohttp;
    int puertoAltoNivel = 8080;
    
    String ipPC = "172.20.10.3";
    
    String host = "http://localhost";		
    
	MqttClient mqttClient;
	
	Double UMBRAL = 30.0;

    @Override
    public void start(Promise<Void> startPromise) {
        WebClientOptions options = new WebClientOptions().setUserAgent("RestClientApp/2.0.2.1");
        WebClient client = WebClient.create(vertx, options);
        options.setKeepAlive(false);
        restClientUtil = new RestClientUtil(client);

        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        
		 ///////////////////////
    	/** ESTABLEZCO MQTT **/
       ///////////////////////
    	
    	mqttClient = MqttClient.create(vertx, new MqttClientOptions().setAutoKeepAlive(true));
		
		mqttClient.connect(1883, ipPC, s -> {

//			mqttClient.subscribe("twmp", MqttQoS.AT_LEAST_ONCE.value(), handler -> {
//				if (handler.succeeded()) {
//					System.out.println("Suscripción " + mqttClient.clientId());
//				}
//			});
		
		});
		

		 /////////////////
		/** ENDPOINTS **/
       /////////////////

        router.post("/api/business/sensorData").handler(this::handlePostSensorData);
        
        router.get("/api/business/sensorValues/:id_sensor/latest").handler(this::handleGet10LatestSensorValues);
        router.get("/api/business/actuatorStates/:id_actuador/latest").handler(this::handleGet10LatestActuatorStates);
        
        router.get("/api/business/group/:id_grupo/sensorValues/latest").handler(this::handleGetLatestSensorValuesByGroup);
        router.get("/api/business/group/:id_grupo/actuatorStates/latest").handler(this::handleGetLatestGroupActuatorStates);

        vertx.createHttpServer().requestHandler(router).listen(puertoAltoNivel, http -> {
            if (http.succeeded()) {
                startPromise.complete();
                System.out.println("API de alto nivel corriendo en el puerto: "+ puertoAltoNivel);
            } else {
                startPromise.fail(http.cause());
            }
        });
    }
    
	 /////////////////////
	/** MENSAJES MQTT **/
   /////////////////////
    
    private void sendMqttMessage(String topic, String message) {
        if (mqttClient != null && mqttClient.isConnected()) {
            mqttClient.publish(
                topic,
                Buffer.buffer(message),
                MqttQoS.AT_LEAST_ONCE,
                false,
                false
            );
        } else {
            System.err.println("MQTT client is not connected. Cannot send message.");
        }
    }
    
	 ///////////////////////
	/** MANEJADORES	API **/
   ///////////////////////
    
    private void handlePostSensorData(RoutingContext ctx) {
        SensorValue sensor = ctx.getBodyAsJson().mapTo(SensorValue.class);

        Promise<SensorValue> promise = Promise.promise();
        restClientUtil.postRequest(puertoBajoNivel, host, "api/sensorValue", sensor, SensorValue.class, promise);

        promise.future().onComplete(ar -> {
            if (ar.succeeded()) {
                // Lógica de comparación y envío MQTT
                double value = sensor.getValor();
                System.out.println(value);
                if (value > UMBRAL) {
                    sendMqttMessage("actuator/on", "Activar");
                } else {
                    sendMqttMessage("actuator/off", "Desactivar");
                }

                ctx.response().setStatusCode(201)
                    .putHeader("Content-Type", "application/json")
                    .end(Json.encodePrettily(ar.result()));
            } else {
                ctx.response().setStatusCode(500).end(ar.cause().toString());
            }
        });
    }


    	/* Últimos 10 valores SENSOR */
    
    private void handleGet10LatestSensorValues(RoutingContext ctx) {
        String id = ctx.pathParam("id_sensor");
        Promise<SensorValue[]> promise = Promise.promise();
        restClientUtil.getRequest(puertoBajoNivel, host, "api/business/sensorValues/" + id + "/latest", SensorValue[].class, promise);

        promise.future().onComplete(ar -> {
            if (ar.succeeded()) {
                ctx.response().putHeader("Content-Type", "application/json").end(Json.encodePrettily(ar.result()));
            } else {
                ctx.response().setStatusCode(500).end(ar.cause().toString());
            }
        });
    }

    	/* Últimos 10 valores ACTUADOR */
    
    private void handleGet10LatestActuatorStates(RoutingContext ctx) {
        String id = ctx.pathParam("id_actuador");
        Promise<ActuadorState[]> promise = Promise.promise();
        restClientUtil.getRequest(puertoBajoNivel, host, "api/business/actuatorStates/" + id + "/latest", ActuadorState[].class, promise);

        promise.future().onComplete(ar -> {
            if (ar.succeeded()) {
                ctx.response().putHeader("Content-Type", "application/json").end(Json.encodePrettily(ar.result()));
            } else {
                ctx.response().setStatusCode(500).end(ar.cause().toString());
            }
        });
    }

    	/*	Último valor de cada SENSOR del grupo */
    
    private void handleGetLatestSensorValuesByGroup(RoutingContext ctx) {
        String id = ctx.pathParam("id_grupo");
        Promise<SensorValue[]> promise = Promise.promise();
        restClientUtil.getRequest(puertoBajoNivel, host, "api/business/group/" + id + "/sensorValues/latest", SensorValue[].class, promise);

        promise.future().onComplete(ar -> {
            if (ar.succeeded()) {
                ctx.response().putHeader("Content-Type", "application/json").end(Json.encodePrettily(ar.result()));
            } else {
                ctx.response().setStatusCode(500).end(ar.cause().toString());
            }
        });
    }

    	/*	Último valor de cada ACTUADOR del grupo */
    
    private void handleGetLatestGroupActuatorStates(RoutingContext ctx) {
        String id = ctx.pathParam("id_grupo");
        Promise<ActuadorState[]> promise = Promise.promise();
        restClientUtil.getRequest(puertoBajoNivel, host, "api/business/group/" + id + "/actuatorStates/latest", ActuadorState[].class, promise);

        promise.future().onComplete(ar -> {
            if (ar.succeeded()) {
                ctx.response().putHeader("Content-Type", "application/json").end(Json.encodePrettily(ar.result()));
            } else {
                ctx.response().setStatusCode(500).end(ar.cause().toString());
            }
        });
    }
}
