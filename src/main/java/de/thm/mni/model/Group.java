package de.thm.mni.model;


import java.util.Objects;
import java.util.Set;

public class Group {
  private static Integer counter = 1;
  private final Integer id;
  private final Tutor tutor;
  private final Set<Student> members;

  public Group(Tutor tutor, Set<Student> members) {
    this.id = Group.counter++;
    this.tutor = Objects.requireNonNull(tutor);
    this.members = Objects.requireNonNull(members);
  }

  public Tutor getTutor() {
    return tutor;
  }

  public Set<Student> getMembers() {
    return members;
  }

  public Integer getId() {
    return id;
  }

  @Override
  public String toString() {
    return "Group{" +
      "id=" + id +
      ", tutor=" + tutor +
      ", members=" + members +
      '}';
  }
}
