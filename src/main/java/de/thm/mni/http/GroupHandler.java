package de.thm.mni.http;

import de.thm.mni.model.Group;
import de.thm.mni.model.Student;
import de.thm.mni.model.Tutor;
import de.thm.mni.store.GroupStore;
import de.thm.mni.store.StudentStore;
import de.thm.mni.store.TutorStore;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.util.Set;

public class GroupHandler {
  private Vertx vertx;
  private final StudentStore studentStore;
  private final GroupStore groupStore;
  // TODO: do something reasonable

  public GroupHandler(Vertx vertx) {
    this.vertx = vertx;
    this.studentStore = StudentStore.getStore();
    this.groupStore = GroupStore.getStore();
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
    int beispiel = 0;
    JsonArray array = context.getBodyAsJsonArray();
    String valueUsername = array.getString(beispiel);
    var response = context.response();

  }

  private void getAll(RoutingContext context) {
    // TODO: do something reasonable
  }

}
