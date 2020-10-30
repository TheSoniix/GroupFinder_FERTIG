package de.thm.mni.http;

import de.thm.mni.model.Student;
import de.thm.mni.store.StudentStore;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.util.*;

public class CandidatesHandler {
  private Vertx vertx;
  private final StudentStore studentStore;
  // TODO: do something reasonable

  public CandidatesHandler(Vertx vertx) {
    this.vertx = vertx;
    this.studentStore = StudentStore.getStore();
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
    List<Student> candidatesList = new ArrayList<>();
    Student forStudent = null;

    try {

      //CandidatesList
      if (studentFromUrl.isPresent()) {
        for (Student currStudent : studentStore.getAll()) {
          if (!currStudent.getAlreadyMember() && !currStudent.getUsername().equals(username.get(0)) ) {
            candidatesList.add(currStudent);
          }else if (currStudent.getUsername().equals(username.get(0))) {
            forStudent = currStudent;
          }
        }

        //CandidatesList sort
        Student finalForStudent = forStudent;
        candidatesList.sort(Comparator.comparingInt((Student a) -> sortCandidates(finalForStudent, a)));


        //responses
        response.setStatusCode(200).end("CandidatesList: " + candidatesList.toString());
      } response.setStatusCode(409).end("This Student doesnt exist!");
    } catch (IllegalArgumentException | NullPointerException | ClassCastException ex) {
      response.setStatusCode(406).end("Invalid Content");
    }
  }

  public int sortCandidates (Student finalforStudent, Student objekt) {
    Set<String> tempSet = new HashSet<>(finalforStudent.getWeaknesses());
    tempSet.removeAll(objekt.getStrengths());
    return tempSet.size();
  }




}
