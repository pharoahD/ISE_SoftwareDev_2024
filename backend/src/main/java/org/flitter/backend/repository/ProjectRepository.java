package org.flitter.backend.repository;

import org.flitter.backend.dto.ProjectListDTO;
import org.flitter.backend.entity.Project;

import org.flitter.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Project findProjectByProjectName(String projectName);

//    List<Project> findProjectByParticipantsContains(User user);
//
//    Project findProjectByCreator(User user);

    @Query("SELECT new org.flitter.backend.dto.ProjectListDTO(p.id, p.projectName," +
            "p.description, p.startDate, p.endDate, p.priority, p.progress," +
            "p.isCompleted, p.creator) FROM Project p JOIN p.participants u " +
            "WHERE u = :user")
    List<ProjectListDTO> findProjectListDTOByParticipant(@Param("user") User user);

    @Query("SELECT new org.flitter.backend.dto.ProjectListDTO(p.id, p.projectName, " +
            "p.description, p.startDate, p.endDate, p.priority, p.progress, " +
            "p.isCompleted, p.creator) FROM Project p")
    List<ProjectListDTO> findAllProjectsDTO();
}

