package de.thm.mni.store.impl;

import de.thm.mni.model.Group;
import de.thm.mni.store.Store;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class GroupStoreImpl<T extends Group, S extends Integer> implements Store<T, S> {
  private final Set<T> runtimeStore = new HashSet<>();
  private static Store<Group, Integer> instanceGroup;

  /**
   * If not already happened, this methode generate an instance of GroupStoreImpl with the typ parameter Group and Integer.
   *
   * @return the instance of StoreImpl.
   */
  public static Store<Group, Integer> getInstanceGroup() {
    if (instanceGroup == null) {
      instanceGroup = new GroupStoreImpl<>();
    }
    return instanceGroup;
  }

  /**
   * Stores the group.
   *
   * @param group The group to store.
   */
  @Override
  public void store(T group) {
    this.runtimeStore.add(group);
  }

  /**
   * Searches for a group with the given id.
   *
   * @param id The id to find the group.
   * @return A non-empty optional of an group, if the group was found.
   * Otherwise the optional is empty.
   */
  @Override
  public Optional<T> find(S id) {
    return runtimeStore.stream()
      .filter(group -> group.getId().equals(id))
      .findFirst();
  }

  /**
   * Removes a group from the store. If the group does not exists, the call does nothing.
   *
   * @param group The group to remove
   */
  @Override
  public void delete(T group) {
    runtimeStore.remove(group);
    var tutor = group.getTutor();
    tutor.setCapacity(tutor.getCapacity() + 1);
  }

  /**
   * @return Returns all stored groups.
   */
  @Override
  public Set<T> getAll() {
    return new HashSet<>(runtimeStore);
  }

  /**
   * @return Returns the size of the stored groups.
   */
  @Override
  public int getSize() {
    return runtimeStore.size();
  }

}
