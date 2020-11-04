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
      response.setStatusCode(200).end("Group: \"" + group + "\" succesfully deleted!");
    } else {
      response.setStatusCode(404).end("Group: \"" + group + "\" does not exists!");
    }
    context.response().end();
  }

  private void registerGroup(RoutingContext context) {
    JsonArray usernameArray = context.getBodyAsJsonArray();
    var response = context.response();

    try {
      var groupSet = createGroupList(usernameArray, context);
      var bestTutor = findBestTutor(groupSet);

      if (bestTutor == null) {
        response.setStatusCode(409).end("Es steht kein Tutor zur Verfügung!");
      } else if (groupSet != null) {
        bestTutor.setCapacity(bestTutor.getCapacity() - 1);
        var group = new Group(bestTutor, groupSet);
        groupStore.store(group);
        response.setStatusCode(200).end("Succesfully Created Group:" + group);
      }
    } catch (IllegalArgumentException | NullPointerException | ClassCastException ex) {
      response.setStatusCode(406).end("Invalid Content");
    }

  }

  private Set<Student> createGroupList(JsonArray arr, RoutingContext context) {
    Set<Student> groupSet = new HashSet<>();
    var response = context.response();
    for (int i = 0; i < arr.size(); i++) {
      var student = studentStore.find(arr.getString(i));
      var studentGroupcheck = groupStore.searchStudent(arr.getString(i));

      if (student.isEmpty()) {
        response.setStatusCode(404).end("Der Student " + arr.getString(i) + " existiert nicht!");
        return null;
      } else if (studentGroupcheck) {
        response.setStatusCode(409).end("Der Student " + arr.getString(i) + " ist bereits in einer Gruppe!");
        return null;
      } else if (groupSet.contains(student.get())) {
        response.setStatusCode(409).end("Der Student " + arr.getString(i) + " wurde zum 2. Mal aufgerufen!");
        return null;
      } else {
        groupSet.add(student.get());
      }
    }
    return groupSet;
  }

  private void getAll(RoutingContext context) {
    var response = context.response();

    if (groupStore.getSize() == 0) {
      response.setStatusCode(409).end("No Groups existing!");
    } else {
      response.setStatusCode(200).end("Liste der Gruppen" + groupStore.getAll().toString());
    }
  }


  //Find the best Tutor for the grouplist we got from the request
  private Tutor findBestTutor(Set<Student> groupMembers) {
    Set<Tutor> tempTutorList = new HashSet<>(tutorStore.getAll());
    Tutor bestTutor = null;
    int bestSimilarities = 0;
    int currSimilarities;

    //remove all tutor with 0 capacity from tempTutorList
    for (Tutor currTutor : tutorStore.getAll()) {
      if (currTutor.getCapacity() == 0) {
        tempTutorList.remove(currTutor);
      }
    }

    if (tempTutorList.size() == 0) {
      return null;
    } else {
      for (Tutor currTutor : tempTutorList) {
        if (tempTutorList.size() == 1) {
          return currTutor;
        } else {
          currSimilarities = 0;
          currSimilarities = similarityCounter(currTutor, groupMembers, currSimilarities);
          if (currSimilarities > bestSimilarities) {
            bestSimilarities = currSimilarities;
            bestTutor = currTutor;
          }
        }
      }
      return bestTutor;
    }
  }


  private int similarityCounter(Tutor currTutor, Set<Student> groupMembers, int currSimilarities) {
    Set<String> groupStrengths = new HashSet<>();

    //GroupStrengths
    for (Student currGroupMember : groupMembers) {
      groupStrengths.addAll(currGroupMember.getStrengths());
    }
//Counts the similarities between the competencies of a Tutor and the grouptStrengths
    for (String currCompetencies : currTutor.getCompetencies()) {
      for (String currStrength : groupStrengths) {
        if (currCompetencies.equals(currStrength)) {
          currSimilarities++;
        }
      }
    }
    return currSimilarities;
  }


}
