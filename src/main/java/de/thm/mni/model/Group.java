package de.thm.mni.model;

import java.util.Set;

public class Group {
  private static Integer counter = 1;
  private final Integer id;
  private final Tutor tutor;
  private final Set<Student> members;

  /**
   * A Constructor for the class "Group".
   *
   * @param tutor   The tutor for the Group.
   * @param members The members of the group.
   */
  public Group(Tutor tutor, Set<Student> members) {
    this.id = Group.counter++;
    this.tutor = tutor;
    this.members = members;
  }

  /**
   * Get-Methode for the Id.
   *
   * @return id of the group.
   */
  public Integer getId() {
    return id;
  }

  /**
   * Get-Methode for the tutor.
   *
   * @return the tutor instance of the group.
   */
  public Tutor getTutor() {
    return tutor;
  }

  /**
   * Get-Methode for the members.
   *
   * @return the members of the group.
   */
  public Set<Student> getMembers() {
    return members;
  }


  /**
   * The method specifies the representation of an instance of Group on the output.
   *
   * @return the representation of a instance of Group.
   */
  @Override
  public String toString() {
    return "Group{" +
      "id=" + id +
      ", tutor=" + tutor +
      ", members=" + members +
      '}';
  }
}
