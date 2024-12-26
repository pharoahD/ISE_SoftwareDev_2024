package org.flitter.backend.repository;

import org.flitter.backend.entity.Project;

import org.springframework.data.repository.CrudRepository;

public interface ProjectRepository  extends CrudRepository<Project, Long> {
    Project findProjectByProjectName(String projectName);
}
