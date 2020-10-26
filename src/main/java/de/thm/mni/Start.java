package de.thm.mni;

import de.thm.mni.http.RestApi;
import de.thm.mni.model.Student;
import io.vertx.core.Vertx;

public class Start {
  private static final int SERVER_PORT = 8888;

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    var server = vertx.createHttpServer();
    var restApi = new RestApi(vertx);

    server.requestHandler(restApi.getRouter()).listen(SERVER_PORT, http -> {
      if (http.succeeded()) {
        System.out.println("HTTP server started on port 8888");
      } else {
        System.err.println(http.cause());
      }
    });
  }
}
