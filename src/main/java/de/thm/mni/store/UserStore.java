package de.thm.mni.store;

import de.thm.mni.model.Student;
import de.thm.mni.model.Tutor;
import de.thm.mni.store.impl.StudentStoreImpl;
import de.thm.mni.store.impl.TutorStoreImpl;

import java.util.Optional;
import java.util.Set;

public interface UserStore <T>{
  /**
   * @return The instance of the user store.
   */

  static UserStore<Student> getStoreStudent(){    return StudentStoreImpl.getInstanceStudent();}
  static UserStore<Tutor> getStoreTutor(){    return TutorStoreImpl.getInstanceTutor();}



  /**
   * Store a user into the store. If the user already exists, it will be overwritten.
   * @param user The user to store.
   */
  void store(T user);
  /**
   * Searches and returns a user by username.
   * @param username The username of the user to find.
   * @return A non-empty optional of a user u with u.getUsername() = username exists.
   * Otherwise the optional is empty.
   */
  Optional<T> find(String username);
  /**
   * Remove a user from the store. If the user does not exists, the call does nothing.
   * @param user The user to remove.
   */
  void delete(T user);
  /**
   * @return Return all stored students.
   */
  Set<T> getAll();

  int getSize();


}
