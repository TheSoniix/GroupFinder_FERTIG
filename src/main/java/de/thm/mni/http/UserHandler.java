package de.thm.mni.http;

import de.thm.mni.model.Student;
import de.thm.mni.model.Tutor;
import de.thm.mni.store.StudentStore;
import de.thm.mni.store.TutorStore;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.util.HashSet;
import java.util.Set;

public class UserHandler {
  private final Vertx vertx;
  private final StudentStore studentStore;
  private final TutorStore tutorStore;

  public UserHandler(Vertx vertx) {
    this.vertx = vertx;
    this.studentStore = StudentStore.getStore();
    this.tutorStore = TutorStore.getStore();
  }

  public Router getRouter() {
    var router = Router.router(vertx);
    router.post("/").handler(this::createUser);
    router.get("/").handler(this::getAllUser);
    router.delete("/:username").handler(this::deleteUser);
    return router;
  }

  private void getAllUser(RoutingContext context) {
    var response = context.response();

    if (studentStore.getSize() == 0 && tutorStore.getSize() == 0) {
      response.setStatusCode(409).end("Both Lists are empty!");
    } else {
      response.setStatusCode(200).end("Liste der Studenten: " + studentStore.getAll().toString() +
        " Liste der Tutoren: " + tutorStore.getAll().toString());

    }
  }

  private void deleteUser(RoutingContext context) {
    String username = context.pathParam("username");
    var response = context.response();
    var userStudent = studentStore.find(username);
    var userTutor = tutorStore.find(username);

    if (userStudent.isPresent()) {
      studentStore.delete(userStudent.get());
      response.setStatusCode(200).end("User: \"" + username + "\" succesfully deleted!");
    } else if (userTutor.isPresent()) {
      tutorStore.delete(userTutor.get());
      response.setStatusCode(200).end("User: \"" + username + "\" succesfully deleted!");
    } else {
      response.setStatusCode(409).end("The user: \"" + username + "\" does not exists!");
    }
    context.response().end();
  }

  private void createUser(RoutingContext context) {
    var response = context.response();
    try {
      JsonObject data = context.getBodyAsJson();
      var competencies = data.getJsonArray("competencies");

      if (competencies == null) {
        var strengthsArray = data.getJsonArray("strengths");
        var weaknessesArray = data.getJsonArray("weaknesses");
        var user = new Student(
          data.getString("fname"),
          data.getString("sname"),
          data.getString("username"),
          data.getString("email"),
          data.getString("password"),
          arrayToStringSet(strengthsArray),
          arrayToStringSet(weaknessesArray)
        );

        if (studentStore.find(user.getUsername()).isPresent() || tutorStore.find(user.getUsername()).isPresent()) {
          response.setStatusCode(409).end("Another user with the same username already exists!");
        } else {
          studentStore.store(user);
          response.setStatusCode(201).end("Succesfully Created: " + "\"" + user + "\"");
        }
      } else {
        var user = new Tutor(
          data.getString("fname"),
          data.getString("sname"),
          data.getString("username"),
          data.getString("email"),
          data.getString("password"),
          arrayToStringSet(competencies),
          data.getInteger("capacity")
        );

        if (tutorStore.find(user.getUsername()).isPresent() || studentStore.find(user.getUsername()).isPresent()) {
          response.setStatusCode(409).end("Another user with the same username already exists!");
        } else {
          tutorStore.store(user);
          response.setStatusCode(201).end("Succesfully Created: " + "\"" + user + "\"");
        }
      }

    } catch (IllegalArgumentException | NullPointerException | ClassCastException ex) {
      response.setStatusCode(406).end("Invalid Content");
    }
  }

  private Set<String> arrayToStringSet(JsonArray arr) {
    Set<String> set = new HashSet<>();
    arr.getList().forEach(o -> set.add((String) o));
    return set;
  }
}
