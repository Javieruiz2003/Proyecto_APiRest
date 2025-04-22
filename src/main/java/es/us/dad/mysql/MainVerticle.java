package es.us.dad.mysql;




import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;

public class MainVerticle extends AbstractVerticle {


	
	MySQLPool mySqlClient;

	@Override
	public void start(Promise<Void> startFuture) {
		MySQLConnectOptions connectOptions = new MySQLConnectOptions().setPort(3306).setHost("localhost")
				.setDatabase("proyectodad").setUser("BBDD_dad").setPassword("dad");

		PoolOptions poolOptions = new PoolOptions().setMaxSize(5);

		mySqlClient = MySQLPool.pool(vertx, connectOptions, poolOptions);

		getAll();

		getAllWithConnection();

		
		getByName("sensor2");	
		
		Sensor nuevoSensor = new Sensor(null, "Sensor1", "Temperatura", 123, 1);
		addSensor(nuevoSensor, res -> {
		    if (res.succeeded()) {
		        System.out.println("Operación completada con éxito");
		    } else {
		        System.out.println("Error en la operación");
		    }
		});
		
		deleteSensor(3, res -> {  // Eliminar sensor con ID=3
		    if (res.succeeded()) {
		        System.out.println("Eliminación exitosa");
		    } else {
		        System.out.println("Fallo al eliminar: " + res.cause());
		    }
		});
		

	}

	
	//Lee todos los sensores
	
	private void getAll() {
		mySqlClient.query("SELECT * FROM proyectodad.sensor;", res -> {
			if (res.succeeded()) {
				// Get the result set
				RowSet<Row> resultSet = res.result();
				System.out.println(resultSet.size());
				JsonArray result = new JsonArray();
				for (Row elem : resultSet) {
					result.add(JsonObject.mapFrom(new Sensor(elem.getInteger("id"),
							elem.getString("nombre"), elem.getString("tipo"),
							elem.getInteger("identificador"),elem.getInteger("id_dispositivo"))));
				}
				System.out.println(result.toString());
			} else {
				System.out.println("Error: " + res.cause().getLocalizedMessage());
			}
		});
	}

	private void getAllWithConnection() {
		mySqlClient.getConnection(connection -> {
			if (connection.succeeded()) {
				connection.result().query("SELECT * FROM proyectodad.sensor;", res -> {
					if (res.succeeded()) {
						// Get the result set
						RowSet<Row> resultSet = res.result();
						System.out.println(resultSet.size());
						JsonArray result = new JsonArray();
						for (Row elem : resultSet) {
							result.add(JsonObject
									.mapFrom(new Sensor(elem.getInteger("id"),
											elem.getString("nombre"), elem.getString("tipo"),
											elem.getInteger("identificador"),elem.getInteger("id_dispositivo"))));
						}
						System.out.println(result.toString());
					} else {
						System.out.println("Error: " + res.cause().getLocalizedMessage());
					}
					connection.result().close();
				});
			} else {
				System.out.println(connection.cause().toString());
			}
		});
	}

	private void getByName(String nombre) {
		mySqlClient.getConnection(connection -> {
			if (connection.succeeded()) {
				connection.result().preparedQuery("SELECT * FROM proyectodad.sensor WHERE nombre = ?",
						Tuple.of(nombre), res -> {
							if (res.succeeded()) {
								// Get the result set
								RowSet<Row> resultSet = res.result();
								System.out.println(resultSet.size());
								JsonArray result = new JsonArray();
								for (Row elem : resultSet) {
									result.add(JsonObject.mapFrom(new Sensor(elem.getInteger("id"),
											elem.getString("nombre"), elem.getString("tipo"),
											elem.getInteger("identificador"),elem.getInteger("id_dispositivo"))));
								}
								System.out.println(result.toString());
							} else {
								System.out.println("Error: " + res.cause().getLocalizedMessage());
							}
							connection.result().close();
						});
			} else {
				System.out.println(connection.cause().toString());
			}
		});
	}

	private void addSensor(Sensor sensor, Handler<AsyncResult<Void>> handler) {
	    String query = "INSERT INTO proyectodad.sensor (nombre, tipo, identificador, id_dispositivo) " +
	                   "VALUES (?, ?, ?, ?)";
	    
	    Tuple params = Tuple.of(sensor.getNombre(),sensor.getTipo(),sensor.getIdentificador(),
	    		sensor.getId_device());
	    
	    mySqlClient.preparedQuery(query, params, res -> {
	        if (res.succeeded()) {
	            System.out.println("Sensor añadido correctamente");
	            handler.handle(Future.succeededFuture());
	        } else {
	            System.out.println("Error al añadir sensor: " + res.cause().getLocalizedMessage());
	            handler.handle(Future.failedFuture(res.cause()));
	        }
	    });
	}


	private void deleteSensor(int sensorId, Handler<AsyncResult<Void>> handler) {
	    String query = "DELETE FROM proyectodad.sensor WHERE id = ?";
	    
	    mySqlClient.preparedQuery(query, Tuple.of(sensorId), res -> {
	        if (res.succeeded()) {
	            RowSet<Row> resultSet = res.result();
	            if (resultSet.rowCount() > 0) {
	                System.out.println("Sensor eliminado (ID: " + sensorId + ")");
	                handler.handle(Future.succeededFuture());
	            } else {
	                System.out.println("No se encontró el sensor con ID: " + sensorId);
	                handler.handle(Future.failedFuture("Sensor no encontrado"));
	            }
	        } else {
	            System.out.println("Error al eliminar: " + res.cause().getMessage());
	            handler.handle(Future.failedFuture(res.cause()));
	        }
	    });
	}

	@Override
	public void stop(Future<Void> stopFuture) throws Exception {
		super.stop(stopFuture);
	}

}



