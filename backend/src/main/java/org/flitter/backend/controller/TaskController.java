package org.flitter.backend.controller;

import lombok.Data;
import org.flitter.backend.config.SecurityConfig;
import org.flitter.backend.dto.TaskAssigneeDTO;
import org.flitter.backend.service.TaskAllocationService;
import org.flitter.backend.entity.Task;
import org.flitter.backend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.flitter.backend.dto.TaskAssigneeDTO;
import java.util.List;

@RestController
@RequestMapping("/api/task")
public class TaskController {
    @Autowired
    private TaskAllocationService TaskAllocation;
    @Autowired
    private SecurityConfig SecurityConfig;
    @Data
    public static class TaskModify{
        private Task task;
        private TaskAssigneeDTO taskAssigneeDTO;
    }

    //分配任务，也即创建任务

    @PostMapping("/allocation")
    public ResponseEntity<?> allocation(@RequestBody TaskModify taskModify) {
        Task task = taskModify.getTask();
        TaskAssigneeDTO taskAssigneeDTO = taskModify.getTaskAssigneeDTO();
        if (task.getTitle() == null || task.getTitle().isEmpty() ||
                task.getDescription() == null || task.getDescription().isEmpty()) {
            return ResponseEntity.badRequest().body("任务的名称和描述不能为空");
        }
        /*if (task.getPublisher() == null) {
            return ResponseEntity.badRequest().body("任务的发布人不能为空");
        }*/
        if (task.getStartDate() == null || task.getEndDate() == null) {
            return ResponseEntity.badRequest().body("任务的开始截止时间不能为空");
        }
        if (task.getAssignees() == null) {
            return ResponseEntity.badRequest().body("任务的分配人信息不能为空");
        }
        System.out.println(task.getBelongedProject());
        if (task.getBelongedProject() == null) {
            return ResponseEntity.badRequest().body("任务所属的项目不能为空");
        }
        try {
            var task1 = TaskAllocation.allocateTask(task, taskAssigneeDTO);
            return ResponseEntity.ok(task1);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //修改任务信息
    @PostMapping("/modify")
    public ResponseEntity<?> modify(@RequestBody TaskModify taskModify) {
        Task task = taskModify.getTask();
        TaskAssigneeDTO taskAssigneeDTO = taskModify.getTaskAssigneeDTO();
        if (task.getId() == null) {
            return ResponseEntity.badRequest().body("所修改的任务id不能为空");
        }
        Task task1;
        try{
            task1 = TaskAllocation.getTaskByID(task.getId());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        if (task1 == null) {
            return ResponseEntity.badRequest().body("所修改的任务不存在");
        }
        //调用service层修改任务
        boolean modified = TaskAllocation.ModifyTask(task, taskAssigneeDTO);
        if (modified) {
            return ResponseEntity.ok("任务修改成功");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("任务修改失败");
        }
        /*
        Task task1=TaskAllocation.getTaskByID(task.getId());
        if(task1==null) {
            return ResponseEntity.badRequest().body("所修改的任务不存在");
        }
        TaskAllocation.ModifyTask(task);
        return ResponseEntity.ok("task modified successfully");*/
    }

    //获取所有任务的信息（应该是一个项目里面的全部任务）
    @GetMapping("/get")
    public ResponseEntity<?> get(@RequestParam Long id) {
        User user = SecurityConfig.getCurrentUser();
        List<Task> tasks;
        try {
            tasks=TaskAllocation.getAllTasks(id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        if(tasks==null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(tasks);
        //return ResponseEntity.ok(TaskAllocation.getAllTasks(id));
    }


    //通过任务id来查询任务
    @GetMapping("/getbyid")
    public ResponseEntity<?> getById(@RequestParam Long id) {
        User user = SecurityConfig.getCurrentUser();
        Task task;
        try{
            task = TaskAllocation.getTaskByID(id);
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        if (task == null) {
            return ResponseEntity.notFound().build();
        }
            /*Project project = task.getBelongedProject();//该任务所属的项目
            if (!project.getParticipants().contains(user)) {//若此时查询人员不是项目组内部人员，则无法现实任务内容
                return ResponseEntity.status(403).build();
            }*/
        return ResponseEntity.ok(task);//若没问题，咋返回该任务。
    }

    @GetMapping("/getbyworkerid")//通过员工id查找其所参与的项目
    public ResponseEntity<?> getByWorkerid() {
        User user = SecurityConfig.getCurrentUser();
        List<Task> tasks;

        try{
            tasks = TaskAllocation.getAllTasksByWorkerid(user.getId());
        }catch(IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        if(tasks==null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/gantti")
    public ResponseEntity<?> getGantt
}