package de.thm.mni.http;

import de.thm.mni.model.Student;
import de.thm.mni.model.Tutor;
import de.thm.mni.store.GroupStore;
import de.thm.mni.store.UserStore;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.util.HashSet;
import java.util.Set;

public class UserHandler {
  private final Vertx vertx;
  private final UserStore<Student> studentStore;
  private final UserStore<Tutor> tutorStore;
  private final GroupStore groupStore;

  public UserHandler(Vertx vertx) {
    this.vertx = vertx;
    this.studentStore = UserStore.getStoreStudent();
    this.tutorStore = UserStore.getStoreTutor();
    this.groupStore = GroupStore.getStore();
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
      response.setStatusCode(404).end("Both Lists are empty!");
    } else {
      response.setStatusCode(200).end("Liste der Studenten: " + studentStore.getAll().toString() +
        " Liste der Tutoren: " + tutorStore.getAll().toString());
    }
  }
  // TODO: - VERBESSERN DIESER DELETE FUNKTION
  private void deleteUser(RoutingContext context) {
    String username = context.pathParam("username");
    var response = context.response();
    var userStudent = studentStore.find(username);
    var userTutor = tutorStore.find(username);

    try {
      if (userStudent.isPresent()) {
        String text = "";
        var groupOfStudent = groupStore.searchStudent(userStudent.get());
        if (groupOfStudent != null) {
          if (groupOfStudent.getMembers().size() == 2) {
            groupStore.delete(groupOfStudent);
          } else groupStore.deleteStudentFromGroup(userStudent.get());
          text = " And removed from the group.id = \"" + groupOfStudent.getId() + "\" If the number of the groupmembers drop lower then 2, the goup will be dissolved!";
        }
        studentStore.delete(userStudent.get());
        response.setStatusCode(200).end("User: \"" + username + "\" succesfully deleted!" + text);



      } else if (userTutor.isPresent()) {
        if (!groupStore.searchTutor(userTutor.get())) {
          tutorStore.delete(userTutor.get());
          response.setStatusCode(200).end("User: \"" + username + "\" succesfully deleted!");
        } else throw new IllegalArgumentException("The tutor : \"" + username + "\" is already in a group. Resolve the group before delete the tutor!");
      } else throw new IllegalArgumentException("The user: \"" + username + "\" does not exists!");


    } catch (IllegalArgumentException | NullPointerException | ClassCastException ex) {
      response.setStatusCode(404).end(ex.getMessage());
    }
  }

  private void createUser(RoutingContext context) {
    var response = context.response();
    JsonObject data = context.getBodyAsJson();
    var competencies = data.getJsonArray("competencies");
    var username = data.getString("username");

    try {
      if (studentStore.find(username).isPresent() || tutorStore.find(username).isPresent()) {
        response.setStatusCode(409).end("Another user with the same username already exists!");
      } else if (competencies == null) {
        var student = createStudent(data);
        studentStore.store(student);
      } else {
        var tutor = createTutor(data);
        tutorStore.store(tutor);
      }
      response.setStatusCode(201).end("User succesfully created!");

    } catch (IllegalArgumentException | NullPointerException | ClassCastException ex) {
      response.setStatusCode(406).end("Invalid Content. " + ex.getMessage());
    }
  }

  private Student createStudent(JsonObject data) {
    var strengths = data.getJsonArray("strengths");
    var weaknesses = data.getJsonArray("weaknesses");

    return new Student(
      data.getString("fname"),
      data.getString("sname"),
      data.getString("username"),
      data.getString("email"),
      data.getString("password"),
      arrayToStringSet(strengths),
      arrayToStringSet(weaknesses)
    );
  }

  private Tutor createTutor(JsonObject data) {
    var competencies = data.getJsonArray("competencies");

    return new Tutor(
      data.getString("fname"),
      data.getString("sname"),
      data.getString("username"),
      data.getString("email"),
      data.getString("password"),
      arrayToStringSet(competencies),
      data.getInteger("capacity")
    );
  }

  private Set<String> arrayToStringSet(JsonArray arr) {
    Set<String> set = new HashSet<>();
    arr.getList().forEach(o -> set.add((String) o));
    return set;
  }
}
