package de.thm.mni.http;

import de.thm.mni.model.Student;
import de.thm.mni.model.Tutor;
import de.thm.mni.store.StudentStore;
import de.thm.mni.store.TutorStore;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
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
  }

  private void createUser(RoutingContext context) {
    var response = context.response();
    try {
      JsonObject data = context.getBodyAsJson();
      var competencies = data.getJsonArray("competencies");
      var username = data.getJsonArray("username");

      if (studentStore.find(username.encode()).isPresent() || tutorStore.find(username.encode()).isPresent()) {
        response.setStatusCode(409).end("Another user with the same username already exists!");
      } else {
        if (competencies == null) {
          var student = createStudent(data);
          response.setStatusCode(201).end("Succesfully Created: " + "\"" + student + "\"");
        } else {
          var tutor = createTutor(data);
          response.setStatusCode(201).end("Succesfully Created: " + "\"" + tutor + "\"");
        }
      }
    } catch (IllegalArgumentException | NullPointerException | ClassCastException ex) {
      response.setStatusCode(406).end("Invalid Content");
    }
  }

  private Student createStudent(JsonObject data) {
    var strengths = data.getJsonArray("strengths");
    var weaknesses = data.getJsonArray("weaknesses");

    var student = new Student(
      data.getString("fname"),
      data.getString("sname"),
      data.getString("username"),
      data.getString("email"),
      data.getString("password"),
      arrayToStringSet(strengths),
      arrayToStringSet(weaknesses)
    );
    return student;
  }

  private Tutor createTutor(JsonObject data) {
    var competencies = data.getJsonArray("competencies");

    var tutor = new Tutor(
      data.getString("fname"),
      data.getString("sname"),
      data.getString("username"),
      data.getString("email"),
      data.getString("password"),
      arrayToStringSet(competencies),
      data.getInteger("capacity")
    );
    return tutor;
  }

  private Set<String> arrayToStringSet(JsonArray arr) {
    Set<String> set = new HashSet<>();
    arr.getList().forEach(o -> set.add((String) o));
    return set;
  }
}
