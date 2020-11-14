package de.thm.mni.http;

import de.thm.mni.model.Student;
import de.thm.mni.store.Store;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.util.*;


public class CandidatesHandler {
  private Vertx vertx;
  private final Store<Student, String> studentStore;

  /**
   * A constructor of the class CandidatesHandler that initialize vertx and the instances of the Stores.
   *
   * @param vertx An instance to be able to communicate with Vertx.
   */
  public CandidatesHandler(Vertx vertx) {
    this.vertx = vertx;
    this.studentStore = Store.getStoreStudent();
  }

  /**
   * Method which assigns routes to the appropriate method.
   *
   * @return the route that was called.
   */
  public Router getRouter() {
    var router = Router.router(vertx);
    router.get("/").handler(this::getCandidates);
    return router;
  }


  private void getCandidates(RoutingContext context) {
    var username = context.queryParam("forStudent");
    var response = context.response();
    var studentFromUrl = studentStore.find(username.get(0));

    if (studentFromUrl.isEmpty()) {
      response.setStatusCode(404).end("This Student: " + username.get(0) + " doesnt exist!");
    } else {
      var list = createList(username.get(0));
      if (list.size() == 0) {
        response.setStatusCode(409).end("No Students are available");
      } else response.setStatusCode(200).end("CandidatesList: " + list);
    }

  }


  private List<Student> createList(String username) {
    List<Student> candidatesList = new ArrayList<>();
    Student forStudent = null;

    for (Student currStudent : studentStore.getAll()) {
      if (!currStudent.isGroupMember() && !currStudent.getUsername().equals(username)) {
        candidatesList.add(currStudent);
      } else if (currStudent.getUsername().equals(username)) {
        forStudent = currStudent;
      }
    }
    return sortList(forStudent, candidatesList);
  }

  private List<Student> sortList(Student forStudent, List<Student> candidatesList) {
    candidatesList.sort(Comparator.comparingInt((Student a) -> contrastCounter(forStudent, a))
      .thenComparingInt((a) -> -(a.getStrengths().size())));
    return candidatesList;
  }

  private int contrastCounter(Student finalforStudent, Student objekt) {
    Set<String> tempSet = new HashSet<>(finalforStudent.getWeaknesses());
    tempSet.removeAll(objekt.getStrengths());
    return tempSet.size();
  }
}
