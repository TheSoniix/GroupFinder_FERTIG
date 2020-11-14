package de.thm.mni.model;


import de.thm.mni.util.CheckConstructorInputs;

import java.util.HashSet;
import java.util.Set;

public class Student extends User {
  private final Set<String> strengths;
  private final Set<String> weaknesses;
  private boolean groupMember;


  /**
   * A Constructor for the class "Student".
   * Includes methods to check the values and throw error if blank or null.
   * When creating a Student groupMember is automatically set on false.
   *
   * @param fname      The first name of the student.
   * @param sname      The second name of the student.
   * @param username   The username of the student.
   * @param email      The email of the student.
   * @param password   The password of the student.
   * @param strengths  The strengths of the student.
   * @param weaknesses The weaknesses of the student.
   */
  public Student(String fname, String sname, String username, String email, String password,
                 Set<String> strengths, Set<String> weaknesses) {
    super(fname, sname, username, email, password);
    this.strengths = CheckConstructorInputs.requireNotNullOrBlankSet(strengths);
    this.weaknesses = CheckConstructorInputs.requireNotNullOrBlankSet(weaknesses);
    this.groupMember = false;
  }

  /**
   * Get-Methode for the strengths.
   *
   * @return strengths of the student.
   */
  public Set<String> getStrengths() {
    return new HashSet<>(strengths);
  }

  /**
   * Get-Methode of the weaknesses.
   *
   * @return weaknesses of the student.
   */
  public Set<String> getWeaknesses() {
    return new HashSet<>(weaknesses);
  }

  /**
   * Get-Methode of groupMember.
   *
   * @return groupMember of the student.
   */
  public boolean isGroupMember() {
    return groupMember;
  }

  /**
   * Set-Method for the groupMember.
   *
   * @param groupMember The groupMember of the Student.
   */
  public void setGroupMember(boolean groupMember) {
    this.groupMember = groupMember;
  }


  /**
   * The method specifies the representation of an instance of Student on the output.
   *
   * @return the representation of a instance of Student.
   */
  public String toString() {
    return "Student{" +
      "fname='" + getFname() + '\'' +
      ", sname='" + getSname() + '\'' +
      ", username='" + getUsername() + '\'' +
      ", email='" + getEmail() + '\'' +
      ", strengths=" + strengths +
      ", weaknesses=" + weaknesses +
      '}';
  }
}
