package de.thm.mni.http;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class RestApi {
  private Vertx vertx;

  /**
   * A constructor of the class RestApi to initialize vertx.
   *
   * @param vertx An instance to be able to communicate with Vertx.
   */
  public RestApi(Vertx vertx) {
    this.vertx = vertx;
  }

  /**
   * Method for specifying the different routes and assigning them to a method of the instances of the handler classes.
   *
   * @return the route that was called.
   */
  public Router getRouter() {
    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());

    router.mountSubRouter("/users", new UserHandler(vertx).getRouter());
    router.mountSubRouter("/candidates", new CandidatesHandler(vertx).getRouter());
    router.mountSubRouter("/groups", new GroupHandler(vertx).getRouter());

    return router;
  }
}
