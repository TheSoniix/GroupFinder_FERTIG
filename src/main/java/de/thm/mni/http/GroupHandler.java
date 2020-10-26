package de.thm.mni.http;

import de.thm.mni.store.GroupStore;
import de.thm.mni.store.StudentStore;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class GroupHandler {
  private Vertx vertx;
  private final StudentStore studentStore;
  private final GroupStore groupStore;
  // TODO: do something reasonable

  public GroupHandler(Vertx vertx) {
    this.vertx = vertx;
    this.studentStore = StudentStore.getStore();
  }

  public Router getRouter() {
    var router = Router.router(vertx);

    router.get("/").handler(this::getAll);
    router.post("/").handler(this::registerGroup);
    router.delete("/:gid").handler(this::deleteGroup);

    return router;
  }

  private void deleteGroup(RoutingContext context) {
    var groupId = context.pathParam("gid");
    // TODO: do something reasonable
  }

  private void registerGroup(RoutingContext context) {
    /* TODO: do something reasonable
          - reqeuest an Array of usernames
          - response a goup with an id, a tutor for the group and groupmembers

     */
    var response = context.response();

    try {
      JsonArray array = context.getBodyAsJsonArray();

      for (int i = 0; i < array.size(); i++) {
        String value = array.getString(i);
        if (studentStore.find(value).isPresent()) {
        groupStore.store(StudentStore.final);
        }
      }
    } catch (IllegalArgumentException | NullPointerException | ClassCastException ex) {
      response.setStatusCode(406).end("Invalid Content");
    }

  }

  private void getAll(RoutingContext context) {
    // TODO: do something reasonable
  }
}
