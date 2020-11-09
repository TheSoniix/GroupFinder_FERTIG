package de.thm.mni.util;


import java.util.Set;

/**
 * Utilities to handle strings.
 */
public class CheckConstructorInputs {
  /**
   * Checks if the input is a non-null reference as well as the referenced string is not blank.
   * If so, the given string is returned, otherwise a illegal argument exception is thrown.
   * @param s The given string reference.
   * @return The given string reference.
   */
  public static String requireNotNullOrBlankString(String s) {
    if (s == null || s.trim().isEmpty()) {
      throw new IllegalArgumentException("Input is null or blank.");
    }
    return s;
  }

  public static Set<String> requireNotNullOrBlankSet(Set<String> s)  {
    if (s == null || s.isEmpty() ) {
      throw new NullPointerException("Input is null or blank.");
    } else {
      for (String currS : s) {
        if (currS.trim().isEmpty()) {
          throw  new NullPointerException("Input is null or blank");
        }
      }
    }
    return s;
  }

  public static int requireNotZeroOrNegativeInt(int i) {
    if (i <= 0 ) {
      throw new NullPointerException("Number is zero or negative");
    }
    return i;
  }

}
