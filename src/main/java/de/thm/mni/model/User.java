package de.thm.mni.model;

import de.thm.mni.util.CheckConstructorInputs;

public abstract class User {
  private final String fname;
  private final String sname;
  private final String username;
  private final String email;
  private final String password;

  /**
   * A Constructor for the abstract class "User".
   * Includes methods to check the values and throw error if blank or null.
   *
   * @param fname    The first name of the user.
   * @param sname    The second name of the user.
   * @param username The username of the user.
   * @param email    The email of the user.
   * @param password The password of the user.
   */
  public User(String fname, String sname, String username, String email, String password) {
    this.fname = CheckConstructorInputs.requireNotNullOrBlankString(fname);
    this.sname = CheckConstructorInputs.requireNotNullOrBlankString(sname);
    this.username = CheckConstructorInputs.requireNotNullOrBlankString(username);
    this.email = CheckConstructorInputs.requireNotNullOrBlankString(email);
    this.password = CheckConstructorInputs.requireNotNullOrBlankString(password);
  }

  /**
   * Get-Methode for the first name.
   *
   * @return first name of the user.
   */
  public String getFname() {
    return fname;
  }

  /**
   * Get-Methode for the second name.
   *
   * @return second name of the user.
   */
  public String getSname() {
    return sname;
  }

  /**
   * Get-Methode for the username.
   *
   * @return username of the user.
   */
  public String getUsername() {
    return username;
  }

  /**
   * Get-Methode for the email.
   *
   * @return email of the user.
   */
  public String getEmail() {
    return email;
  }
}
