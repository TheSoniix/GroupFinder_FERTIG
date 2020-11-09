package de.thm.mni.model;


import java.util.Set;

public class Group {
  private static Integer counter = 1;
  private final Integer id;
  private final Tutor tutor;
  private final Set<Student> members;

  public Group(Tutor tutor, Set<Student> members) {
    this.id = Group.counter++;
    this.tutor = tutor;
    this.members = members;
  }

  public Integer getId() {
    return id;
  }

  public static Integer getCounter() {
    return counter;
  }

  public Tutor getTutor() {
    return tutor;
  }

  public Set<Student> getMembers() {
    return members;
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
