package de.thm.mni.store;

import de.thm.mni.model.Student;
import java.util.Optional;
import java.util.Set;
import de.thm.mni.store.impl.StudentStoreImpl;

/**
 * Store to save users.
 */
public interface StudentStore {
  /**
   * @return The instance of the user store.
   */

  static StudentStore getStore(){    return StudentStoreImpl.getInstanceStudent();};

  /**
   * Store a user into the store. If the user already exists, it will be overwritten.
   * @param student The user to store.
   */
  void store(Student student);
  /**
   * Searches and returns a user by username.
   * @param username The username of the user to find.
   * @return A non-empty optional of a user u with u.getUsername() = username exists.
   * Otherwise the optional is empty.
   */
  Optional<Student> find(String username);
  /**
   * Remove a user from the store. If the user does not exists, the call does nothing.
   * @param student The user to remove.
   */
  void delete(Student student);
  /**
   * @return Return all stored students.
   */
  Set<Student> getAll();

  int getSize();
}
