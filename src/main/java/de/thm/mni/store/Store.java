package de.thm.mni.store;

import de.thm.mni.model.Group;
import de.thm.mni.model.Student;
import de.thm.mni.model.Tutor;
import de.thm.mni.store.impl.GroupStoreImpl;
import de.thm.mni.store.impl.UserStoreImpl;

import java.util.Optional;
import java.util.Set;

/**
 * An interface to Store Objects of the classes in the model directory (Group, Student, Tutor)
 *
 * @param <T> typ parameter of the Object
 * @param <S> Type parameter that specifies the type of the parameter in the find methode
 *            Should be String for Tutor and Student, and Integer for Group.
 */
public interface Store<T, S> {

  /**
   * Use this static methode to get the instance of the UserStoreImpl for Students.
   *
   * @return The instance of the StoreImpl with typ parameter Student and String.
   */
  static Store<Student, String> getStoreStudent() {
    return UserStoreImpl.getInstanceStudent();
  }

  /**
   * Use this static methode to get the store of the Tutor.
   *
   * @return The instance of the StoreImpl with typ parameter Tutor and String.
   */
  static Store<Tutor, String> getStoreTutor() {
    return UserStoreImpl.getInstanceTutor();
  }

  /**
   * User this static methode to get the store of the Group.
   *
   * @return The instance of the groupStoreImpl with the parameter Group and Integer.
   */
  static Store<Group, Integer> getStore() {
    return GroupStoreImpl.getInstanceGroup();
  }


  /**
   * Stores an object into the store. If the object already exists, it will be overwritten.
   *
   * @param object The object to store.
   */
  void store(T object);

  /**
   * Searches for the content of an attribute of the object and returns an object.
   *
   * @param content The content to find.
   * @return A non-empty optional of an object, if content was found.
   * Otherwise the optional is empty.
   */
  Optional<T> find(S content);

  /**
   * Removes a object from the store. If the object does not exists, the call does nothing.
   *
   * @param object The object to remove.
   */
  void delete(T object);

  /**
   * @return Returns all stored objects.
   */
  Set<T> getAll();

  /**
   * @return Returns the size of the instance of Store.
   */
  int getSize();


}
