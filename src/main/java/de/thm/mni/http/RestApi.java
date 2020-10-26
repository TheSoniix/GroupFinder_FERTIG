package de.thm.mni.http;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class RestApi {
  private Vertx vertx;

  public RestApi(Vertx vertx) {
    this.vertx = vertx;
  }

  public Router getRouter() {
    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());

    router.mountSubRouter("/users", new UserHandler(vertx).getRouter());
    router.mountSubRouter("/candidates", new CandidatesHandler(vertx).getRouter());
    router.mountSubRouter("/groups", new GroupHandler(vertx).getRouter());
    
    return router;
  }
}
