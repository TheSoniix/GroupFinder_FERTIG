package de.thm.mni.store;

import de.thm.mni.model.Tutor;
import de.thm.mni.store.impl.StudentStoreImpl;
import de.thm.mni.store.impl.TutorStoreImpl;
import java.util.Optional;
import java.util.Set;

public interface TutorStore {


  /**
   * Store to save users.
   */

  /**
   * @return The instance of the user store.
   */

  static TutorStore getStore() {
    return TutorStoreImpl.getInstanceTutor();
  }

  /**
   * Store a user into the store. If the user already exists, it will be overwritten.
   *
   * @param tutor The user to store.
   */
  void store(Tutor tutor);

  /**
   * Searches and returns a user by username.
   *
   * @param username The username of the user to find.
   * @return A non-empty optional of a user u with u.getUsername() = username exists.
   * Otherwise the optional is empty.
   */
  Optional<Tutor> find(String username);

  /**
   * Remove a user from the store. If the user does not exists, the call does nothing.
   *
   * @param tutor The user to remove.
   */
  void delete(Tutor tutor);

  /**
   * @return Return all stored students.
   */
  Set<Tutor> getAll();

  int getSize();
}


