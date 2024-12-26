package org.flitter.backend.repository;

import org.flitter.backend.entity.Project;

import org.flitter.backend.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProjectRepository  extends CrudRepository<Project, Long> {
    Project findProjectByProjectName(String projectName);
    List<Project> findProjectByParticipantsContains(User user);
}
