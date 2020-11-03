package de.thm.mni.model;


import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Student extends User {

  private final Set<String> strengths;
  private final Set<String> weaknesses;
  private boolean alreadyMember;


  public Student(String fname, String sname, String username, String email, String password,
                 Set<String> strengths, Set<String> weaknesses) {
    super(fname, sname, username, email, password);
    this.strengths = Objects.requireNonNull(strengths);
    this.weaknesses = Objects.requireNonNull(weaknesses);
    this.alreadyMember = false;
  }


  public Set<String> getStrengths() {
    return new HashSet<>(strengths);
  }

  public Set<String> getWeaknesses() {
    return new HashSet<>(weaknesses);
  }

  public boolean getAlreadyMember() {
    return alreadyMember;
  }

  public void setAlreadyMember(boolean alreadyMember) {
    this.alreadyMember = alreadyMember;
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
