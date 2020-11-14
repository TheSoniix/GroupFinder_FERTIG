package de.thm.mni.store.impl;

import de.thm.mni.model.Student;
import de.thm.mni.model.Tutor;
import de.thm.mni.model.User;
import de.thm.mni.store.Store;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class UserStoreImpl<T extends User, S extends String> implements Store<T, S> {
  private final Set<T> runtimeStore = new HashSet<>();
  private static Store<Student, String> instanceStudent;
  private static Store<Tutor, String> instanceTutor;

  /**
   * If not already happened, this methode generate an instance of UserStoreImpl with the typ parameter Student and String.
   *
   * @return The "store" instance for the class Student.
   */
  public static Store<Student, String> getInstanceStudent() {
    if (instanceStudent == null) {
      instanceStudent = new UserStoreImpl<>();
    }
    return instanceStudent;
  }

  /**
   * If not already happened, this methode generate an instance of UserStoreImpl with the typ parameter Tutor and String.
   *
   * @return The "store" instance for the class Tutor.
   */
  public static Store<Tutor, String> getInstanceTutor() {
    if (instanceTutor == null) {
      instanceTutor = new UserStoreImpl<>();
    }
    return instanceTutor;
  }

  /**
   * Stores the user.
   *
   * @param user The User to store.
   */
  @Override
  public void store(T user) {
    this.runtimeStore.add(user);
  }

  /**
   * Searches for a group with the given username.
   *
   * @param username The username fo find the user.
   * @return A non-empty optional of an user, if the user was found.
   * Otherwise the optional is empty.
   */
  @Override
  public Optional<T> find(S username) {
    return runtimeStore.stream()
      .filter(user -> user.getUsername().equalsIgnoreCase(username))
      .findFirst();
  }

  /**
   * Removes a user from the store. If the user does not exists, the call does nothing.
   *
   * @param user The user to remove.
   */
  @Override
  public void delete(T user) {
    runtimeStore.remove(user);
  }

  /**
   * @return Returns all stored users.
   */
  @Override
  public Set<T> getAll() {
    return new HashSet<>(runtimeStore);
  }

  /**
   * @return Returns the size of the stored users.
   */
  @Override
  public int getSize() {
    return runtimeStore.size();
  }


}
