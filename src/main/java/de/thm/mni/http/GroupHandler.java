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

import java.util.HashSet;
import java.util.Set;

import static java.lang.Integer.parseInt;

public class GroupHandler {
  private Vertx vertx;
  private final StudentStore studentStore;
  private final GroupStore groupStore;
  private final TutorStore tutorStore;
  // TODO: do something reasonable

  public GroupHandler(Vertx vertx) {
    this.vertx = vertx;
    this.studentStore = StudentStore.getStore();
    this.tutorStore = TutorStore.getStore();
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
    Integer groupId = parseInt(context.pathParam("gid"));
    var response = context.response();
    var group = groupStore.find(groupId);

    if (group.isPresent()) {
      groupStore.delete(group.get());
      response.setStatusCode(200).end("Group: \"" + group  + "\" succesfully deleted!");
    } else {
      response.setStatusCode(409).end("Group: \"" + group + "\" does not exists!");
    }
    context.response().end();
  }

  private void registerGroup(RoutingContext context) {
    var response = context.response();

    JsonArray array = context.getBodyAsJsonArray();
    Set<Student> groupMembers = new HashSet<>();
    Set<String> groupStrengths = new HashSet<>();
    Tutor bestTutor = null;
    int bestSimilarities = 0;
    int currSimilarities = 0;

    try {
      //GruppenListe  !!CHECKEN OB DER STUDENT SCHON EIN GRUPPENMITGLIED IST!!
      for (int i = 0; i < array.size(); i++) {
        var studentFromArray = studentStore.find(array.getString(i));

        if (studentFromArray.isPresent() && !studentFromArray.get().getAlreadyMember()) {
          groupMembers.add(studentFromArray.get());
          studentFromArray.get().setAlreadyMember(true);
        } else {
          response.setStatusCode(409).end("The user: \"" + studentFromArray + "\" does not exists or is already member of a group!");
        }
      }

      //GruppenStärke
      for (Student currGroupMember : groupMembers) {
        groupStrengths.addAll(currGroupMember.getStrengths());
      }

      //Tutoren Kompetänzen mit GruppenStärke vergleichen
      for (Tutor currTutor : tutorStore.getAll()) {
        if (currTutor.getCapacity() != 0) {
          currSimilarities = 0;

          for (String i : currTutor.getCompetencies()) {
            for (String j : groupStrengths) {
              if (i.equals(j)) {
                currSimilarities++;

                if (currSimilarities > bestSimilarities) {
                  bestSimilarities = currSimilarities;
                  bestTutor = currTutor;
                }
              }
            }
          }
        }
      }
      assert bestTutor != null;
      bestTutor.setCapacity(bestTutor.getCapacity() - 1);

      //Gruppe erstellen
      var group = new Group(bestTutor, groupMembers);
      groupStore.store(group);
      response.setStatusCode(200).end("Succesfully Created Group:" + group);

    } catch (IllegalArgumentException | NullPointerException | ClassCastException ex) {
      response.setStatusCode(406).end("Invalid Content");
    }




  }

  private void getAll(RoutingContext context) {
    // TODO: do something reasonable
    var response = context.response();

    if (groupStore.getSize() == 0)  {
      response.setStatusCode(409).end("No Groups existing!");
    } else {
      response.setStatusCode(200).end("Liste der Gruppen" + groupStore.getAll().toString());
    }
  }

}
