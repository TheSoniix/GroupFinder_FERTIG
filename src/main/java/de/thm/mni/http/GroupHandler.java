package de.thm.mni.http;

import de.thm.mni.model.Group;
import de.thm.mni.model.Student;
import de.thm.mni.model.Tutor;
import de.thm.mni.store.Store;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.util.HashSet;
import java.util.Set;

import static java.lang.Integer.parseInt;

public class GroupHandler {
  private Vertx vertx;
  private final Store<Student, String> studentStore;
  private final Store<Group, Integer> groupStore;
  private final Store<Tutor, String> tutorStore;

  /**
   * A constructor of the class GroupHandler that initialize vertx and the instances of the Stores.
   *
   * @param vertx An instance to be able to communicate with Vertx.
   */
  public GroupHandler(Vertx vertx) {
    this.vertx = vertx;
    this.studentStore = Store.getStoreStudent();
    this.tutorStore = Store.getStoreTutor();
    this.groupStore = Store.getStore();
  }

  /**
   * Method which assigns routes to the appropriate method.
   *
   * @return the route that was called.
   */
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
      group.get().getMembers().forEach((Student student) -> student.setGroupMember(false));
      groupStore.delete(group.get());
      response.setStatusCode(200).end("Group: \"" + group.get().getId() + "\" succesfully deleted!");
    } else response.setStatusCode(404).end("Group: \"" + groupId + "\" does not exists!");
  }


  private void registerGroup(RoutingContext context) {
    JsonArray usernameArray = context.getBodyAsJsonArray();
    var response = context.response();

    try {
      var groupSet = createGroupSet(usernameArray);
      var bestTutor = findBestTutor(groupSet);
      bestTutor.setCapacity(bestTutor.getCapacity() - 1);
      groupSet.forEach((Student student) -> student.setGroupMember(true));

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

    for (int i = 0; i < arr.size(); i++) {
      var student = studentStore.find(arr.getString(i));

      if (arr.size() <= 1) {
        throw new IllegalArgumentException("The group needs minimum 2 members!");
      } else if (student.isEmpty()) {
        throw new NullPointerException("The student " + arr.getString(i) + " doesnt exist!");
      } else if (student.get().isGroupMember()) {
        throw new NullPointerException("The student " + arr.getString(i) + " is already in a group!");
      } else if (groupSet.contains(student.get())) {
        throw new NullPointerException("The student " + arr.getString(i) + " was called for the second time!");
      } else {
        groupSet.add(student.get());
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
        } else if (bestTutor != null && currSimilarities == bestSimilarities) {
          if (currTutor.getCompetencies().size() > bestTutor.getCompetencies().size()) {
            bestTutor = currTutor;
          }
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
