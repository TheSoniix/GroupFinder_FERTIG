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
    boolean allUserAvailable = false;

    try {
      JsonArray array = context.getBodyAsJsonArray();


      // getGroupMember() verwenden!

      for (int i = 0; i < array.size(); i++) {
        String valueUsername = array.getString(i);
        var userStudent = studentStore.find(valueUsername);
        if (userStudent.isPresent()) {
          allUserAvailable = true;
        } else {
          allUserAvailable = false;
          response.setStatusCode(409).end("Not every User is available");
        }
      }

      if (allUserAvailable) {
        var group = new Group(
          sortBestTutor(),
          getGroupMember()
        );
      }
    } catch (IllegalArgumentException | NullPointerException | ClassCastException ex) {
      response.setStatusCode(406).end("Invalid Content");
    }

  }

  private void getAll(RoutingContext context) {
    // TODO: do something reasonable
  }

  private Tutor sortBestTutor() {
    //besten Tutor für die Gruppe!
    //Tutoren ohne Kapazität aussortieren bsp. neue Methode
    //Zuweisen, falls es nur noch einen Tutor gibt
    //Ansonsten, Stärke der Gruppe (getGroupMember() sortiert andere Student aus) mit Kompetenzen vergleichen und nach Matching sortieren
    //return besten Tutor

    for (int i= 0; i < TutorStore.getStore().getSize(); i++) {
      var objT = aktueller Tutor;
      objT.getKompetänzen;

      for (int j = 0; j < StudentStore.getStore().getSize(); i++)   {
        var objS = aktueller Student;
        objS.getStrength

          obT mit objS vergleichen;
      }
    }
    return Tutor;
  }

  private Set<Student> getGroupMember() {

    return
  }
}
