package de.thm.mni.http;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class CandidatesHandler {
  private Vertx vertx;
  // TODO: do something reasonable

  public CandidatesHandler(Vertx vertx) {
    this.vertx = vertx;
  }

  public Router getRouter() {
    var router = Router.router(vertx);

    router.get("/").handler(this::getCandidates);

    return router;
  }

  private void getCandidates(RoutingContext context) {
    var username = context.queryParam("forStudent");
    // TODO: do something reasonable
  }
}
