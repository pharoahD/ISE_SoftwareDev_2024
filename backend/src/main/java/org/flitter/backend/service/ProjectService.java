package org.flitter.backend.service;

import jakarta.transaction.Transactional;
import org.flitter.backend.config.SecurityConfig;
import org.flitter.backend.dto.ProjectListDTO;
import org.flitter.backend.entity.Project;
import org.flitter.backend.entity.User;
import org.flitter.backend.entity.enums.Role;
import org.flitter.backend.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final SecurityConfig securityConfig;

    @Autowired
    public ProjectService(ProjectRepository projectRepository,
                          SecurityConfig securityConfig) {
        this.projectRepository = projectRepository; // service可以做业务逻辑
        this.securityConfig = securityConfig;
    }

    public Project getProject(Long id) {
        if (projectRepository.findById(id).isPresent()) {
            return projectRepository.findById(id).get();
        } else {
            return null;        // may unsafe
        }
    }

    @Transactional
    public Project createProject(Project project) {
        User currentUser = securityConfig.getCurrentUser();
        project.setCreator(currentUser);

        Project exitsProject = projectRepository.findProjectByProjectName(project.getProjectName());
        if (exitsProject != null) {
            throw new IllegalArgumentException("项目名已经被使用过了");
        }

        project.setProjectName(project.getProjectName());
        project.setStartDate(project.getStartDate());
        project.setEndDate(project.getEndDate());
        project.setDescription(project.getDescription());
        project.setPriority(project.getPriority());

        project.setIsCompleted(false);
        if (project.getParticipants() == null) {
            project.setParticipants(new HashSet<>());
        }
        project.getParticipants().add(currentUser);

        System.err.println("participants size: " + project.getParticipants().size());
        projectRepository.save(project);
        return project;
    }

    @Transactional
    public List<ProjectListDTO> getAllProjects() {
        User currentUser = securityConfig.getCurrentUser();
        if (currentUser != null && currentUser.getRole() == Role.ADMIN) {
            return projectRepository.findAllProjectsDTO();
        }
        System.err.println(projectRepository.findProjectListDTOByParticipant(currentUser));
//        System.err.println(projectRepository.findProjectByCreator(currentUser));
        return projectRepository.findProjectListDTOByParticipant(currentUser);
    }
}
