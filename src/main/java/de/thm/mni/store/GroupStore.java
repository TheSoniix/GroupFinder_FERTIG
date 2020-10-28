package de.thm.mni.store;

import de.thm.mni.model.Group;
import de.thm.mni.model.Tutor;
import de.thm.mni.store.impl.GroupStoreImpl;

import java.util.Optional;
import java.util.Set;

public interface GroupStore {

  static GroupStore getStore() {
    return GroupStoreImpl.getInstance();
  }

  void store(Group group);

  Optional<Group> find(Integer id);


  void delete(Group group);

  Set<Group> getAll();

  int getSize();

}
