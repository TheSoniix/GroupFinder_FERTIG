package de.thm.mni.util;

import java.util.Set;

/**
 * Utilities to handle values in the constructor.
 */
public class CheckConstructorInputs {

  /**
   * Checks if the input is a non-null reference as well as the referenced string is not blank.
   * If so, the given string is returned, otherwise a illegal argument exception is thrown.
   *
   * @param s The given string reference.
   * @return The given string reference.
   */
  public static String requireNotNullOrBlankString(String s) {
    if (s == null || s.trim().isEmpty()) {
      throw new IllegalArgumentException("Input is null or blank.");
    }
    return s;
  }

  /**
   * Checks if the input is non-null reference as well as the referenced Set<String> is not blank and
   * the contents of the Set are not null or blank either.
   * If so, the given Set is returned, otherwise a illegal argument exception is thrown.
   *
   * @param s The given Set<String> reference.
   * @return The given Set<String> reference.
   */
  public static Set<String> requireNotNullOrBlankSet(Set<String> s) {
    if (s == null || s.isEmpty()) {
      throw new IllegalArgumentException("Input is null or blank.");
    } else {
      for (String currS : s) {
        requireNotNullOrBlankString(currS);
      }
    }
    return s;
  }

  /**
   * Checks if the input is not negative or equal 0.
   *
   * @param i The given int reference.
   * @return The given int reference.
   */
  public static int requireNotZeroOrNegativeInt(int i) {
    if (i <= 0) {
      throw new IllegalArgumentException("Number is zero or negative");
    }
    return i;
  }

}
