package de.thm.mni.store.impl;

import de.thm.mni.model.Group;
import de.thm.mni.model.Tutor;
import de.thm.mni.store.GroupStore;
import de.thm.mni.store.TutorStore;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class GroupStoreImpl implements GroupStore {

  private final Set<Group> runtimeStore = new HashSet<>();


  private static GroupStore instance;
  public static GroupStore getInstance() {
    if (instance == null) {
      instance = new GroupStoreImpl();
    }
    return instance;
  }

  @Override
  public void store(Group group) {
    this.runtimeStore.add(group);
  }

  @Override
  public Optional<Group> find(String username) {
   return Optional.empty();
  }

  @Override
  public void delete(Group group) {

  }

  @Override
  public Set<Group> getAll() {
    return null;
  }

  @Override
  public int getSize() {
    return 0;
  }
}
