package de.thm.mni.http;

import de.thm.mni.model.Group;
import de.thm.mni.model.Student;
import de.thm.mni.model.Tutor;
import de.thm.mni.store.GroupStore;
import de.thm.mni.store.UserStore;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.util.HashSet;
import java.util.Set;

import static java.lang.Integer.parseInt;

public class GroupHandler {
  private Vertx vertx;
  private final UserStore<Student> studentStore;
  private final GroupStore groupStore;
  private final UserStore<Tutor> tutorStore;


  public GroupHandler(Vertx vertx) {
    this.vertx = vertx;
    this.studentStore = UserStore.getStoreStudent();
    this.tutorStore = UserStore.getStoreTutor();
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
    } else response.setStatusCode(404).end("Group: \"" + group + "\" does not exists!");
  }


  private void registerGroup(RoutingContext context) {
    JsonArray usernameArray = context.getBodyAsJsonArray();
    var response = context.response();

    try {
      var groupSet = createGroupSet(usernameArray);
      var bestTutor = findBestTutor(groupSet);
      bestTutor.setCapacity(bestTutor.getCapacity() - 1);

      var group = new Group(
        bestTutor,
        groupSet
      );
      groupStore.store(group);
      response.setStatusCode(200).end("Succesfully Created Group:" + group);

    } catch (IllegalArgumentException | NullPointerException | ClassCastException ex) {
      response.setStatusCode(406).end("Invalid Content. " + ex.getMessage());
    }

  }


  private void getAll(RoutingContext context) {
    var response = context.response();

    if (groupStore.getSize() == 0) {
      response.setStatusCode(409).end("No Groups existing!");
    } else {
      response.setStatusCode(200).end("Liste der Gruppen" + groupStore.getAll().toString());
    }
  }


  private Set<Student> createGroupSet(JsonArray arr) {
    Set<Student> groupSet = new HashSet<>();

    if (arr.size() <= 1) {
      throw new IllegalArgumentException("The group needs minimum 2 members!");
    } else {
      for (int i = 0; i < arr.size(); i++) {
        var student = studentStore.find(arr.getString(i));

        if (student.isEmpty()) {
          throw new NullPointerException("Der  Student " + arr.getString(i) + " existiert nicht!");
        } else if (groupStore.searchStudent(student.get()) != null) {
          throw new NullPointerException("Der Student " + arr.getString(i) + " ist bereits in einer Gruppe!");
        } else if (groupSet.contains(student.get())) {
          throw new NullPointerException("Der Student " + arr.getString(i) + " wurde zum 2. Mal aufgerufen!");
        } else {
          groupSet.add(student.get());
        }
      }
    }
    return groupSet;
  }


  private Tutor findBestTutor(Set<Student> groupMembers) {
    Tutor bestTutor = null;
    int bestSimilarities = 0;
    int currSimilarities;
    var tempTutorSet = createTutorSet();
    var groupStrength = createGroupStrengthSet(groupMembers);

    if (tempTutorSet.size() == 0) {
      throw new NullPointerException("Es steht kein Tutor zur Verfügung!");
    } else if (tempTutorSet.size() == 1) {
      return tempTutorSet.stream().findFirst().get();
    } else {
      for (Tutor currTutor : tempTutorSet) {
          currSimilarities = 0;
          currSimilarities = similarityCounter(currTutor, groupStrength, currSimilarities);
          if (currSimilarities > bestSimilarities) {
            bestSimilarities = currSimilarities;
            bestTutor = currTutor;
          }
      }
      return bestTutor;
    }
  }

  private Set<Tutor> createTutorSet() {
    Set<Tutor> tempTutorList = new HashSet<>(tutorStore.getAll());
    for (Tutor currTutor : tutorStore.getAll()) {
      if (currTutor.getCapacity() == 0) {
        tempTutorList.remove(currTutor);
      }
    }
    return tempTutorList;
  }

  private Set<String> createGroupStrengthSet(Set<Student> groupMembers) {
    Set<String> groupStrength = new HashSet<>();
    for (Student currGroupMember : groupMembers) {
      groupStrength.addAll(currGroupMember.getStrengths());
    }
    return groupStrength;
  }

  private int similarityCounter(Tutor currTutor, Set<String> groupStrength, int currSimilarities) {
    for (String currCompetencies : currTutor.getCompetencies()) {
      for (String currStrength : groupStrength) {
        if (currCompetencies.equals(currStrength)) {
          currSimilarities++;
        }
      }
    }
    return currSimilarities;
  }


}
