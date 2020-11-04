package de.thm.mni.store.impl;

import de.thm.mni.model.Tutor;
import de.thm.mni.store.TutorStore;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class TutorStoreImpl implements TutorStore {
  private final Set<Tutor> runtimeStore = new HashSet<>();


  private static TutorStore instanceTutor;


  public static TutorStore getInstanceTutor() {
    if (instanceTutor == null) {
      instanceTutor = new TutorStoreImpl();
    }
    return instanceTutor;
  }

  @Override
  public void store(Tutor tutor) {
    this.runtimeStore.add(tutor);
  }

  @Override
  public Optional<Tutor> find(String username) {
    return runtimeStore.stream()
      .filter(user -> user.getUsername().equalsIgnoreCase(username))
      .findFirst();
  }

  @Override
  public void delete(Tutor tutor) {
    runtimeStore.remove(tutor);
  }

  @Override
  public Set<Tutor> getAll() {
    return new HashSet<>(runtimeStore);
  }

  @Override
  public int getSize() {
     return runtimeStore.size();
  }

}
