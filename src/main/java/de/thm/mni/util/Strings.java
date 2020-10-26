package de.thm.mni.util;


/**
 * Utilities to handle strings.
 */
public class Strings {
  /**
   * Checks if the input is a non-null reference as well as the referenced string is not blank.
   * If so, the given string is returned, otherwise a illegal argument exception is thrown.
   * @param s The given string reference.
   * @return The given string reference.
   */
  public static String requireNotNullOrBlank(String s) {
    if (s == null || s.trim().isEmpty()) {
      throw new IllegalArgumentException("Input is null or blank.");
    }
    return s;
  }
}
