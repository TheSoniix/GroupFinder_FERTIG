package de.thm.mni.http;

import de.thm.mni.model.Student;
import de.thm.mni.store.GroupStore;
import de.thm.mni.store.StudentStore;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.util.*;

public class CandidatesHandler {
  private Vertx vertx;
  private final StudentStore studentStore;
  private final GroupStore groupStore;


  public CandidatesHandler(Vertx vertx) {
    this.vertx = vertx;
    this.studentStore = StudentStore.getStore();
    this.groupStore = GroupStore.getStore();
  }


  public Router getRouter() {
    var router = Router.router(vertx);
    router.get("/").handler(this::getCandidates);
    return router;
  }


  private void getCandidates(RoutingContext context) {
    var username = context.queryParam("forStudent");
    var response = context.response();
    var studentFromUrl = studentStore.find(username.get(0));

    if (studentFromUrl.isPresent()) {
      var list = createList(username);
      if (list.size() != 0) {
        response.setStatusCode(200).end("CandidatesList: " + list);
      } else response.setStatusCode(409).end("No Students are available");
    } else response.setStatusCode(404).end("This Student doesnt exist!");
  }


  public List<Student> createList(List<String> username) {
    List<Student> candidatesList = new ArrayList<>();
    Student forStudent = null;

    for (Student currStudent : studentStore.getAll()) {
      var searchStudent = groupStore.searchStudent(currStudent.getUsername());

      if (!searchStudent && !currStudent.getUsername().equals(username.get(0))) {
        candidatesList.add(currStudent);
      } else if (currStudent.getUsername().equals(username.get(0))) {
        forStudent = currStudent;
      }
    }
    Student finalForStudent = forStudent;
    candidatesList.sort(Comparator.comparingInt((Student a) -> sortCandidates(finalForStudent, a))
      .thenComparingInt((a) -> -(a.getStrengths().size())));

    return candidatesList;
  }

  public int sortCandidates(Student finalforStudent, Student objekt) {
    Set<String> tempSet = new HashSet<>(finalforStudent.getWeaknesses());
    tempSet.removeAll(objekt.getStrengths());
    return tempSet.size();
  }
}
