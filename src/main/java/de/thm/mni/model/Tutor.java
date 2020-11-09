package de.thm.mni.model;


import de.thm.mni.util.CheckConstructorInputs;

import java.util.HashSet;
import java.util.Set;

public class Tutor extends User {

  private final Set<String> competencies;
  private int capacity;

  public Tutor(String fname, String sname, String username, String email, String password,
               Set<String> competencies, int capacity) {
    super(fname, sname, username, email, password);
    this.competencies = CheckConstructorInputs.requireNotNullOrBlankSet(competencies);
    this.capacity = CheckConstructorInputs.requireNotZeroOrNegativeInt(capacity);
  }


  public Set<String> getCompetencies() {
    return new HashSet<>(competencies);
  }

  public int getCapacity() {
    return capacity;
  }

  public void setCapacity(int capacity) {
    this.capacity = capacity;
  }



  @Override
  public String toString() {
    return "Tutor{" +
      "fname='" + getFname() + '\'' +
      ", sname='" + getSname() + '\'' +
      ", username='" + getUsername() + '\'' +
      ", email='" + getEmail() + '\'' +
      ", competencies=" + competencies +
      ", capacity=" + capacity +
      '}';
  }
}
