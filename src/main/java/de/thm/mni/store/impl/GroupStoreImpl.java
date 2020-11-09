package de.thm.mni.store.impl;

import de.thm.mni.model.Group;
import de.thm.mni.model.Student;
import de.thm.mni.model.Tutor;
import de.thm.mni.store.GroupStore;
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
  public Optional<Group> find(Integer id) {
   return runtimeStore.stream()
     .filter(group -> group.getId().equals(id))
     .findFirst();
  }

  @Override
  public void delete(Group group) {
    runtimeStore.remove(group);
    var tutor = group.getTutor();
    tutor.setCapacity(tutor.getCapacity() + 1);
  }

  @Override
  public Set<Group> getAll() {
    return new HashSet<>(runtimeStore);
  }

  @Override
  public int getSize() {
    return runtimeStore.size();
  }

  @Override
  public Group searchStudent(Student student) {
    for (Group currGroup : runtimeStore) {
      for (Student currStudent : currGroup.getMembers()) {
        if (currStudent == student) {
          return currGroup;
        }
      }
    }
    return null;
  }

  public boolean searchTutor(Tutor tutor) {
    for (Group currGroup : runtimeStore) {
      if (currGroup.getTutor() == tutor) {
        return true;
      }
    }
    return false;
  }

  public void deleteStudentFromGroup(Student student) {
    for (Group currGroup : runtimeStore) {
      currGroup.getMembers().remove(student);
    }
  }

}
