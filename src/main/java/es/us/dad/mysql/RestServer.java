package es.us.dad.mysql;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;

public class RestServer extends AbstractVerticle {

    private MySQLPool mySqlClient;

    @Override
    public void start(Promise<Void> startFuture) {
        // Configuración de MySQL
        MySQLConnectOptions connectOptions = new MySQLConnectOptions().setPort(3306).setHost("localhost")
            .setDatabase("proyectodad").setUser("BBDD_dad").setPassword("dad");

        PoolOptions poolOptions = new PoolOptions().setMaxSize(5);
        mySqlClient = MySQLPool.pool(vertx, connectOptions, poolOptions);

        // Crear el Router para manejar las rutas REST
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create()); // Para leer JSON del body

        // Endpoints para Sensores
        router.get("/api/sensores").handler(this::getAllSensores);
        router.get("/api/sensores/:nombre").handler(this::getSensorByName);
        router.post("/api/sensores").handler(this::addSensor);
        router.delete("/api/sensores/:id").handler(this::deleteSensor);
        
        
        // Endpoints para actuador
        router.get("/api/actuadores").handler(this::getAllActuador);
        router.get("/api/actuadores/:nombre").handler(this::getActuadorByName);
        router.post("/api/actuadores").handler(this::addActuador);
        router.delete("/api/actuadores/:id").handler(this::deleteActuador);
        
      

        // Iniciar el servidor HTTP
        vertx.createHttpServer().requestHandler(router::handle).listen(8021, result -> {
			if (result.succeeded()) {
				startFuture.complete();
			} else {
				startFuture.fail(result.cause());
			}
            });
    }

    // ===== MÉTODOS PARA MANEJAR RUTAS =====

    private void getAllSensores(RoutingContext ctx) {
        mySqlClient.query("SELECT * FROM proyectodad.sensor;")
            .execute(res -> {
                if (res.succeeded()) {
                    RowSet<Row> resultSet = res.result();
                    JsonArray sensores = new JsonArray();
                    for (Row row : resultSet) {
                        sensores.add(new JsonObject().put("id", row.getInteger("id"))
                        		.put("nombre", row.getString("nombre")).put("tipo", row.getString("tipo"))
                        		.put("identificador", row.getString("identificador"))
                        		.put("id_dispositivo", row.getInteger("id_dispositivo")));
                    }
                    ctx.response()
                        .setStatusCode(200)
                        .putHeader("Content-Type", "application/json")
                        .end(sensores.encode());
                } else {
                    ctx.response()
                        .setStatusCode(500)
                        .end("Error al consultar sensores: " + res.cause().getMessage());
                }
            });
    }

    private void getSensorByName(RoutingContext ctx) {
        String nombre = ctx.pathParam("nombre");
        mySqlClient.preparedQuery("SELECT * FROM proyectodad.sensor WHERE nombre = ?;")
            .execute(Tuple.of(nombre), res -> {
                if (res.succeeded()) {
                    RowSet<Row> resultSet = res.result();
                    if (resultSet.size() > 0) {
                        Row row = resultSet.iterator().next();
                        JsonObject sensor = new JsonObject().put("id", row.getInteger("id"))
                        		.put("nombre", row.getString("nombre")).put("tipo", row.getString("tipo"))
                        		.put("identificador", row.getString("identificador")).put("id_dispositivo", row.getInteger("id_dispositivo"));
                        ctx.response().setStatusCode(200)
                            .putHeader("Content-Type", "application/json")
                            .end(sensor.encode());
                    } else {
                        ctx.response()
                            .setStatusCode(404)
                            .end("Sensor no encontrado");
                    }
                } else {
                    ctx.response()
                        .setStatusCode(500)
                        .end("Error en la consulta: " + res.cause().getMessage());
                }
            });
    }

    private void addSensor(RoutingContext ctx) {
        JsonObject body = ctx.getBodyAsJson();
        mySqlClient.preparedQuery(
            "INSERT INTO proyectodad.sensor (nombre, tipo, identificador, id_dispositivo) VALUES (?, ?, ?, ?);")
            .execute(Tuple.of(
                body.getString("nombre"),
                body.getString("tipo"),
                body.getString("identificador"),
                body.getInteger("id_dispositivo")),
            res -> {
                if (res.succeeded()) {
                    ctx.response()
                        .setStatusCode(201)
                        .end("Sensor creado correctamente");
                } else {
                    ctx.response()
                        .setStatusCode(500)
                        .end("Error al crear sensor: " + res.cause().getMessage());
                }
            });
    }

    private void deleteSensor(RoutingContext ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        mySqlClient.preparedQuery("DELETE FROM proyectodad.sensor WHERE id = ?;")
            .execute(Tuple.of(id), res -> {
                if (res.succeeded()) {
                    ctx.response()
                        .setStatusCode(200)
                        .end("Sensor eliminado");
                } else {
                    ctx.response()
                        .setStatusCode(500)
                        .end("Error al eliminar: " + res.cause().getMessage());
                }
            });
    }
    
    
    private void getAllActuador(RoutingContext ctx) {
        mySqlClient.query("SELECT * FROM proyectodad.actuador;")
            .execute(res -> {
                if (res.succeeded()) {
                    RowSet<Row> resultSet = res.result();
                    JsonArray sensores = new JsonArray();
                    for (Row row : resultSet) {
                        sensores.add(new JsonObject().put("id", row.getInteger("id"))
                        		.put("nombre", row.getString("nombre")).put("tipo", row.getString("tipo"))
                        		.put("identificador", row.getString("identificador"))
                        		.put("id_dispositivo", row.getInteger("id_dispositivo")));
                    }
                    ctx.response()
                        .setStatusCode(200)
                        .putHeader("Content-Type", "application/json")
                        .end(sensores.encode());
                } else {
                    ctx.response()
                        .setStatusCode(500)
                        .end("Error al consultar actuadores: " + res.cause().getMessage());
                }
            });
    }

    private void getActuadorByName(RoutingContext ctx) {
        String nombre = ctx.pathParam("nombre");
        mySqlClient.preparedQuery("SELECT * FROM proyectodad.sensor WHERE nombre = ?;")
            .execute(Tuple.of(nombre), res -> {
                if (res.succeeded()) {
                    RowSet<Row> resultSet = res.result();
                    if (resultSet.size() > 0) {
                        Row row = resultSet.iterator().next();
                        JsonObject sensor = new JsonObject().put("id", row.getInteger("id"))
                        		.put("nombre", row.getString("nombre")).put("tipo", row.getString("tipo"))
                        		.put("identificador", row.getString("identificador")).put("id_dispositivo", row.getInteger("id_dispositivo"));
                        ctx.response().setStatusCode(200)
                            .putHeader("Content-Type", "application/json")
                            .end(sensor.encode());
                    } else {
                        ctx.response()
                            .setStatusCode(404)
                            .end("Actuador no encontrado");
                    }
                } else {
                    ctx.response()
                        .setStatusCode(500)
                        .end("Error en la consulta: " + res.cause().getMessage());
                }
            });
    }

    private void addActuador(RoutingContext ctx) {
        JsonObject body = ctx.getBodyAsJson();
        mySqlClient.preparedQuery(
            "INSERT INTO proyectodad.actuador (nombre, tipo, identificador, id_dispositivo) VALUES (?, ?, ?, ?);")
            .execute(Tuple.of(
                body.getString("nombre"),
                body.getString("tipo"),
                body.getString("identificador"),
                body.getInteger("id_dispositivo")),
            res -> {
                if (res.succeeded()) {
                    ctx.response()
                        .setStatusCode(201)
                        .end("Actuador creado correctamente");
                } else {
                    ctx.response()
                        .setStatusCode(500)
                        .end("Error al crear sensor: " + res.cause().getMessage());
                }
            });
    }

    private void deleteActuador(RoutingContext ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        mySqlClient.preparedQuery("DELETE FROM proyectodad.actuador WHERE id = ?;")
            .execute(Tuple.of(id), res -> {
                if (res.succeeded()) {
                    ctx.response()
                        .setStatusCode(200)
                        .end("Actuador eliminado");
                } else {
                    ctx.response()
                        .setStatusCode(500)
                        .end("Error al eliminar: " + res.cause().getMessage());
                }
            });
    }
}