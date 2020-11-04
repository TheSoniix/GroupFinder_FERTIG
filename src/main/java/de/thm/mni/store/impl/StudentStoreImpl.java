package de.thm.mni.store.impl;

import de.thm.mni.model.Student;
import de.thm.mni.store.StudentStore;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class StudentStoreImpl implements StudentStore {
  private final Set<Student> runtimeStore = new HashSet<>();


  private static StudentStore instanceStudent;


  public static StudentStore getInstanceStudent() {
    if (instanceStudent == null) {
      instanceStudent = new StudentStoreImpl();
    }
    return instanceStudent;
  }

  @Override
  public void store(Student student) {
    this.runtimeStore.add(student);
  }

  @Override
  public Optional<Student> find(String username) {
    return runtimeStore.stream()
      .filter(user -> user.getUsername().equalsIgnoreCase(username))
      .findFirst();
  }

  @Override
  public void delete(Student student) {
    runtimeStore.remove(student);
  }

  @Override
  public Set<Student> getAll() {
    return new HashSet<>(runtimeStore);
  }

  public int getSize() {
    return runtimeStore.size();
  }


}
