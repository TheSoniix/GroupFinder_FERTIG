package de.thm.mni.model;

import de.thm.mni.util.CheckConstructorInputs;

public abstract class User {
  private final String fname;
  private final String sname;
  private final String username;
  private final String email;
  private final String password;

  public User(String fname, String sname, String username, String email, String password) {
    this.fname = CheckConstructorInputs.requireNotNullOrBlankString(fname);
    this.sname = CheckConstructorInputs.requireNotNullOrBlankString(sname);
    this.username = CheckConstructorInputs.requireNotNullOrBlankString(username);
    this.email = CheckConstructorInputs.requireNotNullOrBlankString(email);
    this.password = CheckConstructorInputs.requireNotNullOrBlankString(password);
  }

  public String getFname() {
    return fname;
  }

  public String getSname() {
    return sname;
  }

  public String getUsername() {
    return username;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }
}
