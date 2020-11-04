package de.thm.mni.store;

import de.thm.mni.model.Group;
import de.thm.mni.model.Tutor;
import de.thm.mni.store.impl.GroupStoreImpl;

import java.util.Optional;
import java.util.Set;

public interface GroupStore {

  /**
   * Store to save users.
   */

  /**
   * @return The instance of the user store.
   */

  static GroupStore getStore() {
    return GroupStoreImpl.getInstance();
  }


  /**
   * Store a user into the store. If the user already exists, it will be overwritten.
   *
   * @param group The group to store.
   */
  void store(Group group);

  /**
   * Searches and returns a user by username.
   *
   * @param id The username of the user to find.
   * @return A non-empty optional of a user u with u.getUsername() = username exists.
   * Otherwise the optional is empty.
   */
  Optional<Group> find(Integer id);

  /**
   * Remove a user from the store. If the user does not exists, the call does nothing.
   *
   * @param group The user to remove.
   */
  void delete(Group group);

  /**
   * @return Return all stored students.
   */
  Set<Group> getAll();

  int getSize();

  boolean searchStudent(String username);

}
