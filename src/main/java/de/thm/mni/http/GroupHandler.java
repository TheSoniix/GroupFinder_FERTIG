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
    var response = context.response();
    JsonArray usernameArray = context.getBodyAsJsonArray();
    var status = arrayControll(usernameArray);

    if (status == 404) {
      response.setStatusCode(status).end("Ein Student existiert nicht!");
    } else if (status == 409) {
      response.setStatusCode(status).end("Ein Student ist bereits in einer Gruppe!");
    } else {
      var groupSet = createGroupList(usernameArray);
      var bestTutor = findBestTutor(groupSet);
      var group = new Group(bestTutor, groupSet);
      groupStore.store(group);
      response.setStatusCode(200).end("Succesfully Created Group:" + group);
    }
  }

  private Set<Student> createGroupList(JsonArray array) {
    Set<Student> groupSet = new HashSet<>();
    for (int i = 0; i < array.size(); i++) {
      var studentFromArray = studentStore.find(array.getString(i));
      groupSet.add(studentFromArray.get());
      studentFromArray.get().setAlreadyMember(true);
    }
    return groupSet;
  }

  private int arrayControll (JsonArray arr) {
    for (int i = 0; i < arr.size(); i++) {
      var student = studentStore.find(arr.getString(i));
      if (student.isEmpty()) {
        return 404;
      } else if (student.get().getAlreadyMember()) {
        return 409;
      }
    }
    return 200;
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


    //find the best tutor from tempTutorList
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
