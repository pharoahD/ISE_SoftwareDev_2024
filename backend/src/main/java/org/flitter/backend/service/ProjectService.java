package org.flitter.backend.service;

import jakarta.transaction.Transactional;
import org.flitter.backend.config.SecurityConfig;
import org.flitter.backend.dto.ProjectCreateDTO;
import org.flitter.backend.dto.ProjectListDTO;
import org.flitter.backend.entity.Project;
import org.flitter.backend.entity.User;
import org.flitter.backend.repository.ProjectRepository;
import org.flitter.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final SecurityConfig securityConfig;
    private final UserRepository userRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository,
                          SecurityConfig securityConfig,
                          UserRepository userRepository) {
        this.projectRepository = projectRepository; // service可以做业务逻辑
        this.securityConfig = securityConfig;
        this.userRepository = userRepository;
    }

    public Project getProject(Long id) {
        if (projectRepository.findById(id).isPresent()) {
            return projectRepository.findById(id).get();
        } else {
            return null;        // may unsafe
        }
    }

    @Transactional
    public Project createProject(ProjectCreateDTO projectCreate) {
        Project project = new Project();

        Project exitsProject = projectRepository.findProjectByProjectName(projectCreate.getName());
        if (exitsProject != null) {
            throw new IllegalArgumentException("项目名已经被使用过了");
        }
        User manager = userRepository.findById(projectCreate.getManagerId()).orElse(null);
        if (manager == null) {
            throw new IllegalArgumentException("负责人的ID无效");
        }
        project.setManager(manager);
        project.setProjectName(projectCreate.getName());
        project.setStartDate(projectCreate.getStartDate());
        project.setEndDate(projectCreate.getEndDate());
        project.setDescription(projectCreate.getDescription());
        project.setPriority(projectCreate.getPriority());

        project.setIsCompleted(false);
        if (project.getParticipants() == null) {
            project.setParticipants(new HashSet<>());
        }

        projectRepository.save(project);
        return project;
    }

    @Transactional
    public List<ProjectListDTO> getAllProjects() {
        return projectRepository.findAllProjectsDTO();
    }

    @Transactional
    public List<ProjectListDTO> getAllParticipatedProjects() {
        User currentUser = securityConfig.getCurrentUser();
        return projectRepository.findProjectListDTOByParticipant(currentUser);
    }
}
