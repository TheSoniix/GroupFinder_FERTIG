package de.thm.mni.model;


import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Student extends User {

  private final Set<String> strengths;
  private final Set<String> weaknesses;

  public Student(String fname, String sname, String username, String email, String password,
                 Set<String> strengths, Set<String> weaknesses) {
    super(fname, sname, username, email, password);
    this.strengths = Objects.requireNonNull(strengths);
    this.weaknesses = Objects.requireNonNull(weaknesses);
  }


  public Set<String> getStrengthsStudent() {
    return new HashSet<>(strengths);
  }

  public Set<String> getWeaknessesStudent() {
    return new HashSet<>(weaknesses);
  }



  public String toString() {
    return "Student{" +
      "fname='" + getFname() + '\'' +
      ", sname='" + getSname() + '\'' +
      ", username='" + getUsername() + '\'' +
      ", email='" + getEmail() + '\'' +
      "strengths=" + strengths +
      ", weaknesses=" + weaknesses +
      '}';
  }


}
