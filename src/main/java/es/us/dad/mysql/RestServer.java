package es.us.dad.mysql;


import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.Json;
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

	public static int puertohttp = 8082;
    
    
	private MySQLPool mySqlClient;
  
    private final Gson gson = new Gson();
	

    @Override
    public void start(Promise<Void> startFuture) {
    	
		 //////////////////////////////
		/** CONFIGURACIÓN de MySQL **/
       //////////////////////////////
        																					//localhost
        MySQLConnectOptions connectOptions = new MySQLConnectOptions().setPort(3306).setHost("localhost")
            .setDatabase("proyectodad").setUser("BBDD_dad").setPassword("dad");

        PoolOptions poolOptions = new PoolOptions().setMaxSize(5);
        mySqlClient = MySQLPool.pool(vertx, connectOptions, poolOptions);

        // Crear el Router para manejar las rutas REST
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create()); // Para leer JSON del body

		 /////////////////
		/** ENDPOINTS **/
       /////////////////
        
        // Endpoints para Sensores
        router.get("/api/sensores").handler(this::getAllSensores);
        router.get("/api/sensores/:id").handler(this::getSensorById);
        router.put("/api/sensores/:id").handler(this::updateSensor);
        router.post("/api/sensores").handler(this::addSensor);
        router.delete("/api/sensores/:id").handler(this::deleteSensor);
        
        // Endpoints para actuador
        router.get("/api/actuadores").handler(this::getAllActuador);
        router.get("/api/actuadores/:id").handler(this::getActuadorById);
        router.post("/api/actuadores").handler(this::addActuador);
        router.put("/api/actuadores/:id").handler(this::updateActuador);
        router.delete("/api/actuadores/:id").handler(this::deleteActuador); //
        
        // Endpoints para dispositivo
        router.get("/api/dispositivos").handler(this::getAllDispositivos);
        router.get("/api/dispositivos/:id").handler(this::getDispositivoById);
        router.put("/api/dispositivos/:id").handler(this::updateDispositivo);
        router.post("/api/dispositivos").handler(this::addDispositivo);
        router.delete("/api/dispositivos/:id").handler(this::deleteDispositivo);
        
        // Endpoints para grupo
        router.get("/api/grupos").handler(this::getAllGrupos);
        router.get("/api/grupos/:id").handler(this::getGrupoById);
        router.put("/api/grupos/:id").handler(this::updateGrupo);
        router.post("/api/grupos").handler(this::addGrupo);
        router.delete("/api/grupos/:id").handler(this::deleteGrupo);
        
        // Endpoints para sensorValue
        router.get("/api/sensorValue/:id").handler(this::getSensorValueById);
        router.post("/api/sensorValue").handler(this::addSensorValue);
        //últimos 10  valores
        router.get("/api/business/sensorValues/:id/latest").handler(this::get10LatestSensorValues);
        
        
        router.get("/api/business/group/:id/sensorValues/latest").handler(this::getLatestSensorValuesByGroup);
        
        
        // Endpoints para actuatorState
        router.get("/api/actuatorState/:id").handler(this::getActuatorStateById);
        router.post("/api/actuatorState").handler(this::addActuatorState);
        //últimos 10 valores
        router.get("/api/business/actuatorStates/:id/latest").handler(this::get10LatestActuatorStates);	//
        
        router.get("/api/business/group/:id/actuatorStates/latest").handler(this::getLatestActuatorStatesByGroup);

        
        
        /**
         
        Consultar el grupo del sensor para publicar en el canal MQTT correcto.
        
        Para enviar un comando MQTT correctamente, el sistema necesita:
        	1. Dado un sensor ID, encontrar a qué dispositivo pertenece
        	2. Dado el dispositivo ID, encontrar a qué grupo pertenece
        Determina el grupo asociado al sensor 
		router.get("/api/sensors/:id/grupo").handler(this::getSensorGroupInfo);
        
        **/

        // Iniciar el servidor HTTP
        vertx.createHttpServer().requestHandler(router::handle).listen(puertohttp, result -> {
			if (result.succeeded()) {
				startFuture.complete();
                System.out.println("API de bajo nivel corriendo en el puerto: "+ puertohttp);

			} else {
				startFuture.fail(result.cause());
			}
        });
    }

	 //////////////////////////////////
	/** MÉTODOS PARA MANEJAR RUTAS **/
   //////////////////////////////////
    
    	/** SENSORES **/
    
    private void getAllSensores(RoutingContext ctx) {
        mySqlClient.query("SELECT * FROM proyectodad.sensor;")
            .execute(res -> {
                if (res.succeeded()) {
                    RowSet<Row> resultSet = res.result();
                    JsonArray sensores = new JsonArray();
                    for (Row row : resultSet) {
                        Sensor sensor = new Sensor(row.getInteger("id"),row.getString("nombre"),
                            row.getString("tipo"),row.getString("identificador"),
                            row.getInteger("id_dispositivo")
                        );
                        sensores.add(JsonObject.mapFrom(sensor)); // Convierte el objeto Sensor a JSON
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
    
    private void getSensorById(RoutingContext ctx) {
        String id = ctx.pathParam("id"); // Obtiene el ID del path
        mySqlClient.preparedQuery("SELECT * FROM proyectodad.sensor WHERE id = ?;")
            .execute(Tuple.of(id), res -> {
                if (res.succeeded()) {
                    RowSet<Row> resultSet = res.result();
                    if (resultSet.size() == 0) {
                        // Si no se encuentra el sensor, retorna 404
                        ctx.response()
                            .setStatusCode(404)
                            .end("Sensor no encontrado");
                    } else {
                        // Convierte la fila a un objeto Sensor y luego a JSON
                        Row row = resultSet.iterator().next();
                        Sensor sensor = new Sensor(row.getInteger("id"),row.getString("nombre"),
                            row.getString("tipo"),row.getString("identificador"),row.getInteger("id_dispositivo"));
                        ctx.response()
                            .setStatusCode(200)
                            .putHeader("Content-Type", "application/json")
                            .end(JsonObject.mapFrom(sensor).encode()); // Devuelve el JSON
                    }
                } else {
                    ctx.response()
                        .setStatusCode(500)
                        .end("Error al consultar el sensor: " + res.cause().getMessage());
                }
            });
    }

    private void updateSensor(RoutingContext ctx) {
 
            String id = ctx.pathParam("id");
            Sensor sensor = gson.fromJson(ctx.getBodyAsString(), Sensor.class);
            
            mySqlClient.preparedQuery(
                "UPDATE sensor SET nombre = ?, tipo = ?, identificador = ?, id_dispositivo = ? WHERE id = ?")
                .execute(Tuple.of(sensor.getNombre(),sensor.getTipo(),sensor.getIdentificador(),
                		sensor.getid_dispositivo(),id),
                res -> {
                    if (res.succeeded()) {
                        ctx.response()
                            .setStatusCode(200)
                            .putHeader("Content-Type", "application/json")
                            .end(JsonObject.mapFrom(sensor).encode());
                    } else {
                        ctx.response()
                            .setStatusCode(500)
                            .putHeader("Content-Type", "application/json")
                            .end(new JsonObject()
                                .put("error", res.cause().getMessage())
                                .encode());
                    }
                });
        
    }

    private void addSensor(RoutingContext ctx) {
        try {
            Sensor sensor = gson.fromJson(ctx.getBodyAsString(), Sensor.class);
            
            mySqlClient.preparedQuery(
                "INSERT INTO sensor (nombre, tipo, identificador, id_dispositivo) VALUES (?, ?, ?, ?)")
                .execute(Tuple.of(sensor.getNombre(),sensor.getTipo(),sensor.getIdentificador(),sensor.getid_dispositivo()),
                res -> {
                    if (res.succeeded()) {
                        ctx.response()
                            .setStatusCode(201)
                            .putHeader("Content-Type", "application/json")
                            .end(JsonObject.mapFrom(sensor).encode());
                    } else {
                        ctx.response()
                            .setStatusCode(500)
                            .putHeader("Content-Type", "application/json")
                            .end(new JsonObject()
                                .put("error", res.cause().getMessage())
                                .encode());
                    }
                });
        } catch (Exception e) {
            ctx.response()
                .setStatusCode(400)
                .putHeader("Content-Type", "application/json")
                .end(new JsonObject()
                    .put("error", "JSON inválido: " + e.getMessage())
                    .encode());
        }
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
    	
    	/** ACTUADORES **/
    
    private void getAllActuador(RoutingContext ctx) {
        mySqlClient.query("SELECT * FROM proyectodad.actuador;")
            .execute(res -> {
            	 if (res.succeeded()) {
	                    RowSet<Row> resultSet = res.result();
	                    JsonArray actuadores = new JsonArray();
	                    for (Row row : resultSet) {
	                        Actuador actuador = new Actuador(row.getInteger("id"),row.getString("nombre"),
	                            row.getString("tipo"),row.getString("identificador"),
	                            row.getInteger("id_dispositivo")
	                        );
	                        actuadores.add(JsonObject.mapFrom(actuador)); // Convierte el objeto Sensor a JSON
	                    }
	                    ctx.response()
	                        .setStatusCode(200)
	                        .putHeader("Content-Type", "application/json")
	                        .end(actuadores.encode());
	                } else {
	                    ctx.response()
	                        .setStatusCode(500)
	                        .end("Error al consultar sensores: " + res.cause().getMessage());
	                }
	            });
    }

    private void getActuadorById(RoutingContext ctx) {
        String id = ctx.pathParam("id"); // Obtiene el ID del path
        mySqlClient.preparedQuery("SELECT * FROM proyectodad.actuador WHERE id = ?;")
            .execute(Tuple.of(id), res -> {
                if (res.succeeded()) {
                    RowSet<Row> resultSet = res.result();
                    if (resultSet.size() == 0) {
                        // Si no se encuentra el actuador, retorna 404
                        ctx.response()
                            .setStatusCode(404)
                            .end("Actuador no encontrado");
                    } else {
                        // Convierte la fila a un objeto Sensor y luego a JSON
                        Row row = resultSet.iterator().next();
                        Actuador actuador = new Actuador(
                            row.getInteger("id"),row.getString("nombre"),
                            row.getString("tipo"),row.getString("identificador"),
                            row.getInteger("id_dispositivo")
                        );
                        ctx.response()
                            .setStatusCode(200)
                            .putHeader("Content-Type", "application/json")
                            .end(JsonObject.mapFrom(actuador).encode()); // Devuelve el JSON
                    }
                } else {
                    ctx.response()
                        .setStatusCode(500)
                        .end("Error al consultar el actuador: " + res.cause().getMessage());
                }
            });
    }

    private void updateActuador(RoutingContext ctx) {

            String id = ctx.pathParam("id");
            Actuador actuador = gson.fromJson(ctx.getBodyAsString(), Actuador.class);
            
            mySqlClient.preparedQuery(
                "UPDATE actuador SET nombre = ?, tipo = ?, identificador = ?, id_dispositivo = ? WHERE id = ?")
                .execute(Tuple.of(
                    actuador.getNombre(),
                    actuador.getTipo(),
                    actuador.getIdentificador(),
                    actuador.getid_dispositivo(),
                    id),
                res -> {
                    if (res.succeeded()) {
                        ctx.response()
                            .setStatusCode(200)
                            .putHeader("Content-Type", "application/json")
                            .end(JsonObject.mapFrom(actuador).encode());
                    } else {
                        ctx.response()
                            .setStatusCode(500)
                            .putHeader("Content-Type", "application/json")
                            .end(new JsonObject()
                                .put("error", res.cause().getMessage())
                                .encode());
                    }
                });
        
    }

    private void addActuador(RoutingContext ctx) {
        try {
            Actuador actuador = gson.fromJson(ctx.getBodyAsString(), Actuador.class);
            
            mySqlClient.preparedQuery(
                "INSERT INTO actuador (nombre, tipo, identificador, id_dispositivo) VALUES (?, ?, ?, ?)")
                .execute(Tuple.of(
                    actuador.getNombre(),
                    actuador.getTipo(),
                    actuador.getIdentificador(),
                    actuador.getid_dispositivo()),
                res -> {
                    if (res.succeeded()) {
                        ctx.response()
                            .setStatusCode(201)
                            .putHeader("Content-Type", "application/json")
                            .end(JsonObject.mapFrom(actuador).encode());
                    } else {
                        ctx.response()
                            .setStatusCode(500)
                            .putHeader("Content-Type", "application/json")
                            .end(new JsonObject()
                                .put("error", res.cause().getMessage())
                                .encode());
                    }
                });
        } catch (Exception e) {
            ctx.response()
                .setStatusCode(400)
                .putHeader("Content-Type", "application/json")
                .end(new JsonObject()
                    .put("error", "JSON inválido: " + e.getMessage())
                    .encode());
        }
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
    
    	/** DISPOSITIVOS **/
    
    private void getAllDispositivos(RoutingContext ctx) {
        mySqlClient.query("SELECT * FROM proyectodad.dispositivo;")
            .execute(res -> {
                if (res.succeeded()) {
                    RowSet<Row> resultSet = res.result();
                    JsonArray dispositivos = new JsonArray();
                    for (Row row : resultSet) {
                        Dispositivo dispositivo = new Dispositivo(row.getInteger("id"),row.getString("nombre"),   
                            row.getInteger("id_grupo"));
                        dispositivos.add(JsonObject.mapFrom(dispositivo)); // Convierte el objeto Dispositivo a JSON
                    }
                    ctx.response()
                        .setStatusCode(200)
                        .putHeader("Content-Type", "application/json")
                        .end(dispositivos.encode());
                } else {
                    ctx.response()
                        .setStatusCode(500)
                        .end("Error al consultar sensores: " + res.cause().getMessage());
                }
            });
    }

    private void getDispositivoById(RoutingContext ctx) {
        String id = ctx.pathParam("id"); // Obtiene el ID del path
        mySqlClient.preparedQuery("SELECT * FROM proyectodad.dispositivo WHERE id = ?;")
            .execute(Tuple.of(id), res -> {
                if (res.succeeded()) {
                    RowSet<Row> resultSet = res.result();
                    if (resultSet.size() == 0) {
                        // Si no se encuentra el sensor, retorna 404
                        ctx.response()
                            .setStatusCode(404)
                            .end("Dispositivo no encontrado");
                    } else {
                        // Convierte la fila a un objeto Sensor y luego a JSON
                        Row row = resultSet.iterator().next();
                        Dispositivo dispositivo = new Dispositivo(row.getInteger("id"),row.getString("nombre"),   
                                row.getInteger("id_grupo"));
                        ctx.response()
                            .setStatusCode(200)
                            .putHeader("Content-Type", "application/json")
                            .end(JsonObject.mapFrom(dispositivo).encode()); // Devuelve el JSON
                    }
                } else {
                    ctx.response()
                        .setStatusCode(500)
                        .end("Error al consultar el dispositivo: " + res.cause().getMessage());
                }
            });
    }
    
    private void updateDispositivo(RoutingContext ctx) {

            String id = ctx.pathParam("id");
            Dispositivo dispositivo = gson.fromJson(ctx.getBodyAsString(), Dispositivo.class);
            
            mySqlClient.preparedQuery(
                "UPDATE dispositivo SET nombre = ?, id_grupo = ? WHERE id = ?")
                .execute(Tuple.of(
                    dispositivo.getNombre(),
                    dispositivo.getId_grupo(),
                    id),
                res -> {
                    if (res.succeeded()) {
                        ctx.response()
                            .setStatusCode(200)
                            .putHeader("Content-Type", "application/json")
                            .end(JsonObject.mapFrom(dispositivo).encode());
                    } else {
                        ctx.response()
                            .setStatusCode(500)
                            .putHeader("Content-Type", "application/json")
                            .end(new JsonObject()
                                .put("error", res.cause().getMessage())
                                .encode());
                    }
                });
        
    }

    private void addDispositivo(RoutingContext ctx) {
        try {
            Dispositivo dispositivo = gson.fromJson(ctx.getBodyAsString(), Dispositivo.class);
            
            mySqlClient.preparedQuery(
                "INSERT INTO dispositivo (nombre, id_grupo) VALUES (?, ?)")
                .execute(Tuple.of(
                    dispositivo.getNombre(),
                    dispositivo.getId_grupo()),
                res -> {
                    if (res.succeeded()) {
                        ctx.response()
                            .setStatusCode(201)
                            .putHeader("Content-Type", "application/json")
                            .end(JsonObject.mapFrom(dispositivo).encode());
                    } else {
                        ctx.response()
                            .setStatusCode(500)
                            .putHeader("Content-Type", "application/json")
                            .end(new JsonObject()
                                .put("error", res.cause().getMessage())
                                .encode());
                    }
                });
        } catch (Exception e) {
            ctx.response()
                .setStatusCode(400)
                .putHeader("Content-Type", "application/json")
                .end(new JsonObject()
                    .put("error", "JSON inválido: " + e.getMessage())
                    .encode());
        }
    }

    private void deleteDispositivo(RoutingContext ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        mySqlClient.preparedQuery("DELETE FROM proyectodad.dispositivo WHERE id = ?;")
            .execute(Tuple.of(id), res -> {
                if (res.succeeded()) {
                    ctx.response()
                        .setStatusCode(200)
                        .end("Dispositivo eliminado");
                } else {
                    ctx.response()
                        .setStatusCode(500)
                        .end("Error al eliminar: " + res.cause().getMessage());
                }
            });
    }

    	/** GRUPOS **/
    
    private void getAllGrupos(RoutingContext ctx) {
        mySqlClient.query("SELECT * FROM proyectodad.grupo;")
            .execute(res -> {
                if (res.succeeded()) {
                    RowSet<Row> resultSet = res.result();
                    JsonArray grupos = new JsonArray();
                    for (Row row : resultSet) {
                        Grupo grupo = new Grupo(row.getInteger("id"),row.getString("canal_mqtt"),
                            row.getString("nombre"));
                        grupos.add(JsonObject.mapFrom(grupo)); // Convierte el objeto Grupo a JSON
                    }
                    ctx.response()
                        .setStatusCode(200)
                        .putHeader("Content-Type", "application/json")
                        .end(grupos.encode());
                } else {
                    ctx.response()
                        .setStatusCode(500)
                        .end("Error al consultar grupos: " + res.cause().getMessage());
                }
            });
    }

    private void getGrupoById(RoutingContext ctx) {
        String id = ctx.pathParam("id"); // Obtiene el ID del path
        mySqlClient.preparedQuery("SELECT * FROM proyectodad.grupo WHERE id = ?;")
            .execute(Tuple.of(id), res -> {
                if (res.succeeded()) {
                    RowSet<Row> resultSet = res.result();
                    if (resultSet.size() == 0) {
                        // Si no se encuentra el grupo, retorna 404
                        ctx.response()
                            .setStatusCode(404)
                            .end("Sensor no encontrado");
                    } else {
                        // Convierte la fila a un objeto Grupo y luego a JSON
                        Row row = resultSet.iterator().next();
                        Grupo grupo = new Grupo(row.getInteger("id"),row.getString("canal_mqtt"),
                                row.getString("nombre"));
                        ctx.response()
                            .setStatusCode(200)
                            .putHeader("Content-Type", "application/json")
                            .end(JsonObject.mapFrom(grupo).encode()); // Devuelve el JSON
                    }
                } else {
                    ctx.response()
                        .setStatusCode(500)
                        .end("Error al consultar el grupo: " + res.cause().getMessage());
                }
            });
    }

   
    private void updateGrupo(RoutingContext routing) { 
    	
        String idGrupo = routing.request().getParam("id");

        Grupo grupo = gson.fromJson(routing.getBodyAsString(), Grupo.class);
        
        mySqlClient.preparedQuery("UPDATE grupo SET canal_mqtt = ?, nombre = ? WHERE id = ?")
        .execute(Tuple.of(grupo.getCanal_mqtt(), grupo.getNombre(), idGrupo),
                res -> {
                    if (res.succeeded()) {
                        routing.response()
                            .setStatusCode(200)
                            .putHeader("Content-Type", "application/json")
                            .end(JsonObject.mapFrom(grupo).encode());
                    } else {
                        routing.response()
                            .setStatusCode(500)
                            .putHeader("Content-Type", "application/json")
                            .end(new JsonObject()
                                .put("error", res.cause().getMessage())
                                .encode());
                    }
                });
    }
  
    private void addGrupo(RoutingContext routing) {
        try {
         
            Grupo sensor = gson.fromJson(routing.getBodyAsString(), Grupo.class);

            mySqlClient.preparedQuery(
            		"INSERT INTO proyectodad.grupo (id,canal_mqtt,nombre) VALUES (?, ?, ?)"
            ).execute(
                Tuple.of(sensor.getId(),sensor.getCanal_mqtt(), sensor.getNombre()),
                res -> {
                    if (res.succeeded()) {
                      
                        routing.response()
                            .setStatusCode(201)
                            .putHeader("Content-Type", "application/json")
                            .end(JsonObject.mapFrom(sensor).encode());
                    } else {
                        routing.response()
                            .setStatusCode(500)
                            .end("Error al insertar: " + res.cause().getMessage());
                    }
                }
            );
        } catch (Exception e) {
            routing.response()
                .setStatusCode(400)
                .end("JSON inválido: " + e.getMessage());
        }
    }
 

    private void deleteGrupo(RoutingContext ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        mySqlClient.preparedQuery("DELETE FROM proyectodad.grupo WHERE id = ?;")
            .execute(Tuple.of(id), res -> {
                if (res.succeeded()) {
                    ctx.response()
                        .setStatusCode(200)
                        .end("Grupo eliminado");
                } else {
                    ctx.response()
                        .setStatusCode(500)
                        .end("Error al eliminar: " + res.cause().getMessage());
                }
            });
    }
    
    	/** SENSORVALUE **/
    
    private void getSensorValueById(RoutingContext ctx) {
        String id = ctx.pathParam("id"); // Obtiene el ID del path
        mySqlClient.preparedQuery("SELECT * FROM proyectodad.sensorValue WHERE id = ?;")
            .execute(Tuple.of(id), res -> {
                if (res.succeeded()) {
                    RowSet<Row> resultSet = res.result();
                    if (resultSet.size() == 0) {
                        // Si no se encuentra el sensorValue, retorna 404
                        ctx.response()
                            .setStatusCode(404)
                            .end("Sensor no encontrado");
                    } else {
                        // Convierte la fila a un objeto sensorValue y luego a JSON
                        Row row = resultSet.iterator().next();
                        SensorValue sensorValue = new SensorValue(row.getInteger("id"),row.getInteger("id_sensor"),
                                row.getFloat("valor"),row.getLong("timeStamp") );
                        ctx.response()
                            .setStatusCode(200)
                            .putHeader("Content-Type", "application/json")
                            .end(JsonObject.mapFrom(sensorValue).encode()); // Devuelve el JSON
                    }
                } else {
                    ctx.response()
                        .setStatusCode(500)
                        .end("Error al consultar el sensorValue: " + res.cause().getMessage());
                }
            });
    }

   
    
    private void addSensorValue(RoutingContext routing) {
        try {
    
            SensorValue sensor = gson.fromJson(routing.getBodyAsString(), SensorValue.class);

            mySqlClient.preparedQuery(
                "INSERT INTO proyectodad.sensorValue (id,id_sensor, valor, timeStamp) VALUES (?, ?, ?, ?)"
            ).execute(
                Tuple.of(sensor.getId(),sensor.getId_sensor(), sensor.getValor(), sensor.getTimeStamp()),
                res -> {
                    if (res.succeeded()) {
                      
                        routing.response()
                            .setStatusCode(201)
                            .putHeader("Content-Type", "application/json")
                            .end(JsonObject.mapFrom(sensor).encode());
                    } else {
                        routing.response()
                            .setStatusCode(500)
                            .end("Error al insertar: " + res.cause().getMessage());
                    }
                }
            );
        } catch (Exception e) {
            routing.response()
                .setStatusCode(400)
                .end("JSON inválido: " + e.getMessage());
        }
    }
    
    		/** CONSULTAS ESPECIFICAS API REST **/
 
    private void get10LatestSensorValues(RoutingContext ctx) {
        int sensorId = Integer.parseInt(ctx.pathParam("id"));

        mySqlClient.preparedQuery("SELECT * FROM sensorValue WHERE id_sensor = ? ORDER BY timeStamp DESC LIMIT 10")
            .execute(Tuple.of(sensorId), res -> {
                if (res.succeeded() && res.result().size() > 0) {
                    List<SensorValue> values = new ArrayList<>();
                    for (Row row : res.result()) {
                        values.add(new SensorValue(
                            row.getInteger("id"),
                            row.getInteger("id_sensor"),
                            row.getFloat("valor"),
                            row.getLong("timeStamp")
                        ));
                    }
                    ctx.response().setStatusCode(200)
                        .putHeader("Content-Type", "application/json")
                        .end(Json.encodePrettily(values));
                } else {
                    ctx.response().setStatusCode(404)
                        .end(new JsonObject().put("error", "No values found").encode());
                }
            });
    }

 
	private void getLatestSensorValuesByGroup(RoutingContext ctx) {
	    int groupId = Integer.parseInt(ctx.pathParam("id"));

	    String query = 

	    		"SELECT sv.* "
	    		+ "FROM sensorValue sv "
	    		+ "INNER JOIN ("
	    			+ "SELECT sv.id_sensor, MAX(sv.timeStamp) as max_ts "
	    			+ "FROM sensorValue sv "
	    			+ "JOIN sensor s ON sv.id_sensor = s.id "
	    			+ "JOIN dispositivo d ON s.id_dispositivo = d.id "
	    			+ "WHERE d.id_grupo = ? "
	    			+ "GROUP BY sv.id_sensor"
    			+ ") grouped "
    			+ "ON sv.id_sensor = grouped.id_sensor AND sv.timeStamp = grouped.max_ts";

	    mySqlClient.preparedQuery(query)
	        .execute(Tuple.of(groupId), res -> {
	            if (res.succeeded()) {
	                List<SensorValue> values = new ArrayList<>();
	                for (Row row : res.result()) {
	                    values.add(new SensorValue(
	                        row.getInteger("id"),
	                        row.getInteger("id_sensor"),
	                        row.getFloat("valor"),
	                        row.getLong("timeStamp")));
	                }
	                ctx.response().setStatusCode(200)
	                    .putHeader("Content-Type", "application/json")
	                    .end(Json.encodePrettily(values));
	            } else {
	                ctx.response().setStatusCode(500)
	                    .end(new JsonObject().put("error", "DB Error").encode());
	                
	            }
	        });
	}

	
		/**ACTUATORSTATE**/
    
    private void getActuatorStateById(RoutingContext ctx) {
        String id = ctx.pathParam("id"); // Obtiene el ID del path
        mySqlClient.preparedQuery("SELECT * FROM proyectodad.actuatorState WHERE id = ?;")
            .execute(Tuple.of(id), res -> {
                if (res.succeeded()) {
                    RowSet<Row> resultSet = res.result();
                    if (resultSet.size() == 0) {
                        // Si no se encuentra el actuatorState, retorna 404
                        ctx.response()
                            .setStatusCode(404)
                            .end("Sensor no encontrado");
                    } else {
                        // Convierte la fila a un objeto sensorValue y luego a JSON
                        Row row = resultSet.iterator().next();
                        ActuadorState actuatorState = new ActuadorState(row.getInteger("id"),row.getInteger("id_actuador"),
                                row.getBoolean("estado"),row.getLong("timeStamp") );
                        ctx.response()
                            .setStatusCode(200)
                            .putHeader("Content-Type", "application/json")
                            .end(JsonObject.mapFrom(actuatorState).encode()); // Devuelve el JSON
                    }
                } else {
                    ctx.response()
                        .setStatusCode(500)
                        .end("Error al consultar el actuatorState: " + res.cause().getMessage());
                }
            });
    }
    
    private void addActuatorState(RoutingContext ctx) {
        try {
            ActuadorState actuatorState = gson.fromJson(ctx.getBodyAsString(), ActuadorState.class);
            
            mySqlClient.preparedQuery(
                "INSERT INTO actuatorState (id_actuador, estado, timeStamp) VALUES (?, ?, ?)")
                .execute(Tuple.of(actuatorState.getId_actuador(),actuatorState.getEstado(),
                    actuatorState.getTimeStamp()),
                res -> {
                    if (res.succeeded()) {
                        ctx.response()
                            .setStatusCode(201)
                            .putHeader("Content-Type", "application/json")
                            .end(JsonObject.mapFrom(actuatorState).encode());
                    } else {
                        ctx.response()
                            .setStatusCode(500)
                            .putHeader("Content-Type", "application/json")
                            .end(new JsonObject()
                                .put("error", res.cause().getMessage())
                                .encode());
                    }
                });
        } catch (Exception e) {
            ctx.response()
                .setStatusCode(400)
                .putHeader("Content-Type", "application/json")
                .end(new JsonObject()
                    .put("error", "JSON inválido: " + e.getMessage())
                    .encode());
        }
    }
    	
    		/** CONSULTAS ESPECIFICAS API REST*/

    private void get10LatestActuatorStates(RoutingContext ctx) {
    	int actuadorId = Integer.parseInt(ctx.pathParam("id"));

    	mySqlClient.preparedQuery("SELECT * FROM actuatorState WHERE id_actuador = ? ORDER BY timeStamp DESC LIMIT 10")
    	.execute(Tuple.of(actuadorId), res -> {
    		if (res.succeeded() && res.result().size() > 0) {
    			List<ActuadorState> values = new ArrayList<>();
    			for (Row row : res.result()) {
    				values.add(new ActuadorState(
    						row.getInteger("id"),
    						row.getInteger("id_actuador"),
    						row.getBoolean("estado"),
    						row.getLong("timeStamp")
    						));
    			}
    			ctx.response().setStatusCode(200)
    			.putHeader("Content-Type", "application/json")
    			.end(Json.encodePrettily(values));
    		} else {
    			ctx.response().setStatusCode(404)
    			.end(new JsonObject().put("error", "No values found").encode());
    		}
    	});
    }
    private void getLatestActuatorStatesByGroup(RoutingContext ctx) {
        int groupId = Integer.parseInt(ctx.pathParam("id"));

        String query = 
            "SELECT ast.* " +
            "FROM actuatorState ast " +
            "INNER JOIN ( " +
                "SELECT ast.id_actuador, MAX(ast.timeStamp) as max_ts " +
                "FROM actuatorState ast " +
                "JOIN actuador a ON ast.id_actuador = a.id " +
                "JOIN dispositivo d ON a.id_dispositivo = d.id " +
                "WHERE d.id_grupo = ? " +
                "GROUP BY ast.id_actuador " +
            ") grouped " +
            "ON ast.id_actuador = grouped.id_actuador AND ast.timeStamp = grouped.max_ts";

        mySqlClient.preparedQuery(query)
            .execute(Tuple.of(groupId), res -> {
                if (res.succeeded()) {
                    List<ActuadorState> values = new ArrayList<>();
                    for (Row row : res.result()) {
                        values.add(new ActuadorState(
                            row.getInteger("id"),
                            row.getInteger("id_actuador"),
                            row.getBoolean("estado"),
                            row.getLong("timeStamp")
                        ));
                    }
                    ctx.response().setStatusCode(200)
                        .putHeader("Content-Type", "application/json")
                        .end(Json.encodePrettily(values));
                } else {
                    ctx.response().setStatusCode(500)
                        .end(new JsonObject().put("error", "DB Error").encode());
                }
            });
    }  
}