package de.thm.mni.model;

import de.thm.mni.util.Strings;

public abstract class User {
  private final String fname;
  private final String sname;
  private final String username;
  private final String email;
  private final String password;

  public User(String fname, String sname, String username, String email, String password) {
    this.fname = Strings.requireNotNullOrBlank(fname);
    this.sname = Strings.requireNotNullOrBlank(sname);
    this.username = Strings.requireNotNullOrBlank(username);
    this.email = Strings.requireNotNullOrBlank(email);
    this.password = Strings.requireNotNullOrBlank(password);
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
