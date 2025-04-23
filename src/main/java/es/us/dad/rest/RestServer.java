package es.us.dad.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class RestServer extends AbstractVerticle {

	/************************/
	/***** INICIO REST ********/
	/************************/

	private Map<Integer, SensorTemperaturaEntity> temperaturas = new HashMap<Integer, SensorTemperaturaEntity>();
	private Map<Integer, ActVenEntity> ventiladores = new HashMap<Integer, ActVenEntity>();
	private Gson gson;

	public void start(Promise<Void> startFuture) {
		// Instantiating a Gson serialize object using specific date format
		gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		// Defining the router object
		Router router = Router.router(vertx);
		// Handling any server startup result
		vertx.createHttpServer().requestHandler(router::handle).listen(8080, result -> {
			if (result.succeeded()) {
				startFuture.complete();
			} else {
				startFuture.fail(result.cause());
			}
		});

		// Defining URI paths for each method in RESTful interface, including body
		// SensorTemperatura by /api/users* or /api/users/*
		router.route("/api/temperaturas*").handler(BodyHandler.create());
		router.get("/api/temperaturas").handler(this::getAllWithParamsT);
		router.get("/api/temperaturas/temperatura/allt").handler(this::getAllT);
		router.get("/api/temperaturas/:idtemp").handler(this::getOneT);
		router.post("/api/temperaturas").handler(this::addOneT);
		router.delete("/api/temperaturas/:idtemp").handler(this::deleteOneT);
		router.put("/api/temperaturas/:idtemp").handler(this::putOneT);
		
		// Actuador Ventilador
		router.route("/api/ventiladores").handler(BodyHandler.create());
		router.get("/api/ventiladores").handler(this::getAllWithParamsV);
		router.get("/api/ventiladores/ventilador/allv").handler(this::getAllV);
		router.get("/api/ventiladores/:idVent").handler(this::getOneV);
		router.post("/api/ventiladores").handler(this::addOneV);
		router.delete("/api/ventiladores/:idVent").handler(this::deleteOneV);
		router.put("/api/ventiladores/:idVent").handler(this::putOneV);


	}

	@Override
	public void stop(Promise<Void> stopPromise) throws Exception {
		try {
			temperaturas.clear();
			stopPromise.complete();
		} catch (Exception e) {
			stopPromise.fail(e);
		}
		super.stop(stopPromise);
	}

	/************************/
	/***** TEMPERATURA ********/
	/************************/

	private void getAllT(RoutingContext routingContext) {
		routingContext.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(200)
				.end(gson.toJson(new TemperaturaEntityListWrapper(temperaturas.values())));
	}

	private void getAllWithParamsT(RoutingContext routingContext) {

		final String temperatura = routingContext.queryParams().contains("temperatura")
				? routingContext.queryParam("temperatura").get(0)
				: null;

		final String timestampt = routingContext.queryParams().contains("timestampt")
				? routingContext.queryParam("timestampt").get(0)
				: null;

		Double temperaturadouble = Double.parseDouble(temperatura);
		Long timestamptlong = Long.parseLong(timestampt);

		routingContext.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(200)
				.end(gson.toJson(new TemperaturaEntityListWrapper(temperaturas.values().stream().filter(elem -> {
					boolean res = true;
					res = res && (temperaturadouble != null ? elem.getTemperatura().equals(temperaturadouble) : true);
					res = res && (timestamptlong != null ? elem.getTimestampt().equals(timestamptlong) : true);
					return res;
				}).collect(Collectors.toList()))));
	}

	private void getOneT(RoutingContext routingContext) {
		int id = 0;
		try {
			id = Integer.parseInt(routingContext.request().getParam("idtemp"));

			if (temperaturas.containsKey(id)) {
				SensorTemperaturaEntity ds = temperaturas.get(id);
				routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
						.setStatusCode(200).end(ds != null ? gson.toJson(ds) : "");
			} else {
				routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
						.setStatusCode(204).end();
			}
		} catch (Exception e) {
			routingContext.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(204)
					.end();
		}
	}

	private void addOneT(RoutingContext routingContext) {
		final SensorTemperaturaEntity temp = gson.fromJson(routingContext.getBodyAsString(),
				SensorTemperaturaEntity.class);
		temperaturas.put(temp.getidTemp(), temp);
		routingContext.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
				.end(gson.toJson(temp));
	}

	private void deleteOneT(RoutingContext routingContext) {
		int id = Integer.parseInt(routingContext.request().getParam("idtemp"));
		if (temperaturas.containsKey(id)) {
			SensorTemperaturaEntity temp = temperaturas.get(id);
			temperaturas.remove(id);
			routingContext.response().setStatusCode(200).putHeader("content-type", "application/json; charset=utf-8")
					.end(gson.toJson(temperaturas));
		} else {
			routingContext.response().setStatusCode(204).putHeader("content-type", "application/json; charset=utf-8")
					.end();
		}
	}

	private void putOneT(RoutingContext routingContext) {
		int id = Integer.parseInt(routingContext.request().getParam("idtemp"));
		SensorTemperaturaEntity ds = temperaturas.get(id);
		final SensorTemperaturaEntity element = gson.fromJson(routingContext.getBodyAsString(),
				SensorTemperaturaEntity.class);
		ds.setTemperatura(element.getTemperatura());
		ds.setTimestampt(element.getTimestampt());
		temperaturas.put(ds.getidTemp(), ds);
		routingContext.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
				.end(gson.toJson(element));
	}
	
	/************************/
	/***** VENTILADOR ********/
	/************************/
	private void getAllV(RoutingContext routingContext) {
	    routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
	        .setStatusCode(200)
	        .end(gson.toJson(new ActVenListWrapper(ventiladores.values())));
	}

	private void getAllWithParamsV(RoutingContext routingContext) {
	    final String onoff = routingContext.queryParams().contains("onoff") ?
	        routingContext.queryParam("onoff").get(0) : null;

	    final String timestampf = routingContext.queryParams().contains("timestampf") ?
	        routingContext.queryParam("timestampf").get(0) : null;

	    Integer onoffInt = (onoff != null) ? Integer.parseInt(onoff) : null;
	    Long timestampLong = (timestampf != null) ? Long.parseLong(timestampf) : null;

	    routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
	        .setStatusCode(200)
	        .end(gson.toJson(new ActVenListWrapper(ventiladores.values().stream().filter(elem -> {
	            boolean res = true;
	            res = res && (onoffInt != null ? elem.getOnoff().equals(onoffInt) : true);
	            res = res && (timestampLong != null ? elem.getTimestampf().equals(timestampLong) : true);
	            return res;
	        }).collect(Collectors.toList()))));
	}

	private void getOneV(RoutingContext routingContext) {
	    try {
	        int id = Integer.parseInt(routingContext.request().getParam("idVent"));
	        if (ventiladores.containsKey(id)) {
	            ActVenEntity ven = ventiladores.get(id);
	            routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
	                .setStatusCode(200).end(gson.toJson(ven));
	        } else {
	            routingContext.response().setStatusCode(204).end();
	        }
	    } catch (Exception e) {
	        routingContext.response().setStatusCode(400).end();
	    }
	}

	private void addOneV(RoutingContext routingContext) {
	    final ActVenEntity ven = gson.fromJson(routingContext.getBodyAsString(), ActVenEntity.class);
	    ventiladores.put(ven.getIdVent(), ven);
	    routingContext.response().setStatusCode(201)
	        .putHeader("content-type", "application/json; charset=utf-8")
	        .end(gson.toJson(ven));
	}

	private void putOneV(RoutingContext routingContext) {
	    int id = Integer.parseInt(routingContext.request().getParam("idVent"));
	    ActVenEntity ven = ventiladores.get(id);
	    final ActVenEntity updated = gson.fromJson(routingContext.getBodyAsString(), ActVenEntity.class);
	    if (ven != null) {
	        ven.setOnoff(updated.getOnoff());
	        ven.setTimestampf(updated.getTimestampf());
	        ven.setIdGroup(updated.getIdGroup());
	        ven.setIdPlaca(updated.getIdPlaca());
	        ventiladores.put(id, ven);
	        routingContext.response().setStatusCode(201)
	            .putHeader("content-type", "application/json; charset=utf-8")
	            .end(gson.toJson(ven));
	    } else {
	        routingContext.response().setStatusCode(404).end();
	    }
	}

	private void deleteOneV(RoutingContext routingContext) {
	    int id = Integer.parseInt(routingContext.request().getParam("idVent"));
	    if (ventiladores.containsKey(id)) {
	        ActVenEntity removed = ventiladores.remove(id);
	        routingContext.response().setStatusCode(200)
	            .putHeader("content-type", "application/json; charset=utf-8")
	            .end(gson.toJson(removed));
	    } else {
	        routingContext.response().setStatusCode(204).end();
	    }
	}
	/************************/
	/***** FINALIZO REST ******/
	/************************/

}
