package org.flitter.backend.controller;

import org.flitter.backend.config.SecurityConfig;
import org.flitter.backend.entity.User;
import org.flitter.backend.entity.Project;
import org.flitter.backend.entity.enums.Priority;
import org.flitter.backend.service.ProjectService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/project")
public class ProjectController {
    private final ProjectService projectService;
    private final SecurityConfig securityConfig;

    ProjectController(@Autowired ProjectService projectService,
                      @Autowired SecurityConfig securityConfig) {
        this.projectService = projectService;
        this.securityConfig = securityConfig;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Project project) {
        if (project.getProjectName() == null || project.getProjectName().isEmpty() ||
                project.getDescription() == null || project.getDescription().isEmpty()) {
            return ResponseEntity.badRequest().body("项目名称和描述不能为空");
        }
        if (project.getPriority() == null) {
            return ResponseEntity.badRequest().body("项目优先级不能为空");
        }
        if (project.getStartDate() == null || project.getEndDate() == null) {
            return ResponseEntity.badRequest().body("项目开始截止日期不能为空");
        }
        try {
            Priority.valueOf(project.getPriority().toString());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("优先级字段应为HIGH,LOW或者MEDIUM");
        }

        try {
            var project1 = projectService.createProject(project);
            return ResponseEntity.ok(project1);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

//    @PostMapping("/list")// 看到所有的项目列表
//    public ResponseEntity<?> getProjectList() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String username = auth.getName();
//        User user = userService.findByUsername(username);
//
//        List<Project> list = projectService.getProjectByUser(user);
//        return ResponseEntity.ok(list);
//    }

//    @PostMapping("/search")//通过项目名称来查看具体项目信息
//    public ResponseEntity<?> searchProjectByName(@RequestBody String name) {
//        if (name == null || name.isEmpty()) {
//            return ResponseEntity.ok(projectService.getAll());  // 为空则返回全部结果
//        } else {
//            List<Project> list = projectService.searchProject(name);
//            return ResponseEntity.ok(list);
//        }
//    }

    @GetMapping("/get")
    public ResponseEntity<?> getProjectById(@RequestBody Long id) {
        User cuser = securityConfig.getCurrentUser();

        Project project = projectService.getProject(id);
        if (project == null) {
            return ResponseEntity.notFound().build();
        }
        // 进行鉴权？
        if (!project.getParticipants().contains(cuser)) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(project);
    }
}
