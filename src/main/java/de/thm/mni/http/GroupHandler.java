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
      response.setStatusCode(200).end("Group: \"" + group + "\" succesfully deleted!");
    } else {
      response.setStatusCode(409).end("Group: \"" + group + "\" does not exists!");
    }
    context.response().end();
  }

  private void registerGroup(RoutingContext context) {
    var response = context.response();
    JsonArray usernameArray = context.getBodyAsJsonArray();
    Set<Student> groupList = groupList(usernameArray);
    var bestTutor = findBestTutor(groupList);

    System.out.println("Liste: " + groupList);
    System.out.println("Turor: " + bestTutor);

    if (groupList.size() == 0) {
      //Was ist, wenn groupList nicht null ist, da ein Gruppenmitglied erstellt wurde
      response.setStatusCode(400).end("Richtigen Fehler zu werfen! Bsp. keine Studenten verfügbar, falsche usernames");
    } else if (bestTutor == null) {
      response.setStatusCode(400).end("Richtigen Fehler zu werfen! Bsp. keine Kapazität, kein Tutor erstellt");
    } else {
      //Gruppe erstellen && Beindungen erfüllen
      var group = new Group(bestTutor, groupList);
      groupStore.store(group);
      response.setStatusCode(200).end("Succesfully Created Group:" + group);
    }
  }

  private void getAll(RoutingContext context) {
    // TODO: do something reasonable
    var response = context.response();

    if (groupStore.getSize() == 0) {
      response.setStatusCode(409).end("No Groups existing!");
    } else {
      response.setStatusCode(200).end("Liste der Gruppen" + groupStore.getAll().toString());
    }
  }


  //Create a grouplist for the request
  private Set<Student> groupList(JsonArray usernameArray) {
    Set<Student> groupMembers = new HashSet<>();

    for (int i = 0; i < usernameArray.size(); i++) {
      var studentFromArray = studentStore.find(usernameArray.getString(i));
      if (studentFromArray.isPresent() && studentFromArray.get().getAlreadyMember()) {
        groupMembers.add(studentFromArray.get());
        studentFromArray.get().setAlreadyMember(true);
      }
    }
    System.out.println("liste after groupList funktion: " + groupMembers);
    return groupMembers;
  }

  //Find the best Tutor for the grouplist we got from the request
  private Tutor findBestTutor(Set<Student> groupMembers) {
    Set<Tutor> tempTutorList = new HashSet<>(tutorStore.getAll());
    Tutor bestTutor = null;
    int bestSimilarities = 0;
    int currSimilarities;

    //tempTutorList removed all tutor with 0 capacity
    for (Tutor currTutor : tutorStore.getAll()) {
      if (currTutor.getCapacity() == 0) {
        tempTutorList.remove(currTutor);
      }
    }

    //find the best tutor
    for (Tutor currTutor : tempTutorList) {
      if (tempTutorList.size() == 1) {
        currTutor.setCapacity(currTutor.getCapacity() - 1);
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
    assert bestTutor != null;
    bestTutor.setCapacity(bestTutor.getCapacity() - 1);

    return bestTutor;
  }

  //Counts the similarities between the competencies of a Tutor and the grouptStrengths
  private int similarityCounter(Tutor currTutor, Set<Student> groupMembers, int currSimilarities) {
    Set<String> groupStrengths = new HashSet<>();

    //GroupStrengths
    for (Student currGroupMember : groupMembers) {
      groupStrengths.addAll(currGroupMember.getStrengths());
    }

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
