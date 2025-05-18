package es.us.dad.mysql;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class StartVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) {

        // Desplegar RestServer
        vertx.deployVerticle(new RestServer(), serverRes -> {
            if (serverRes.succeeded()) {
                System.out.println("✅ RestServer desplegado correctamente");

                // Luego desplegar RestClient
                vertx.deployVerticle(new RestClient(), clientRes -> {
                    if (clientRes.succeeded()) {
                        System.out.println("✅ RestClient desplegado correctamente");
                        startPromise.complete();
                    } else {
                        System.err.println("❌ Error al desplegar RestClient: " + clientRes.cause());
                        startPromise.fail(clientRes.cause());
                    }
                });

            } else {
                System.err.println("❌ Error al desplegar RestServer: " + serverRes.cause());
                startPromise.fail(serverRes.cause());
            }
        });
    }
}

