package org.flitter.backend.service;

import org.flitter.backend.config.SecurityConfig;
import org.flitter.backend.dto.TaskForGanttDTO;
import org.flitter.backend.entity.Project;
import org.flitter.backend.entity.Task;
import org.flitter.backend.entity.User;
import org.flitter.backend.repository.ProjectRepository;
import org.flitter.backend.repository.TaskRepository;
import org.flitter.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.flitter.backend.entity.PreSendingMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import org.flitter.backend.dto.TaskAssigneeDTO;

@Service
public class TaskAllocationService{
    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    public Task allocateTask(Task task, TaskAssigneeDTO taskAss) {
        //判空都在controller中实现了
        User user = securityConfig.getCurrentUser();
        Task existingTask = taskRepository.findByTitle(task.getTitle());
        if(existingTask != null) {
            throw new IllegalArgumentException("任务名已经被使用过");
        }

        //判断权限

        //校验任务的分配人信息是否有效
        for (User assignee : taskAss.getAssignees()) {
            if (assignee == null) {
                throw new IllegalArgumentException("任务的分配人信息不能为空");
            }

            // 校验assignee是否有有效的id
            if (assignee.getId() == null) {
                throw new IllegalArgumentException("任务的分配人信息不完整：缺少用户ID");
            }

            // 校验assignee是否已存在于数据库
//            User existingUser = userRepository.findById(assignee.getId()).orElse(null);
//            if (existingUser == null) {
//                throw new IllegalArgumentException("任务的分配人 " + assignee.getId() + " 不存在");
//            }
        }

        //如果前端没有输入发布者信息，那就把当前操作人当成发布人
        if(task.getPublisher()==null) {
            task.setPublisher(user);
        }
        task.setAssignees(taskAss.getAssignees());
        // 创建任务
        taskRepository.save(task);
        //InitPreMessage(task);
        return task;
    }

    public Task getTaskByID(Long id) { //通过任务id来具体获取一个具体任务
        User user = securityConfig.getCurrentUser();

        Task existingTask= taskRepository.findById(id).orElse(null);
        if(existingTask == null) {
            throw new IllegalArgumentException("没有找到对应的任务");
        }
        return existingTask;
    }

    //通过项目编号来获取属于该项目所有任务,所以传入的id为项目编号
    public List<Task> getAllTasks(Long id) {
        User user = securityConfig.getCurrentUser();
        //权限判断

        //查看输入的项目id是否有效
        Project project = projectRepository.findById(id).orElse(null);
        if(project == null) {
            throw new IllegalArgumentException("没有找到对应的项目,您输入的项目id不正确");
        }

        return taskRepository.findByBelongedProjectId(id);        // 这里有问题
    }

    //通过员工编号，来查看他所参与的任务
    public List<Task> getAllTasksByWorkerid(Long Workerid) {
        User user = securityConfig.getCurrentUser();

        User existingWorker= userRepository.findById(Workerid).orElse(null);
        if(existingWorker==null){
            throw new IllegalArgumentException("没有该id对应的员工");
        }
        User tuser = new User();
        tuser.setId(existingWorker.getId());
        return taskRepository.findByAssignees(tuser);
    }

    public boolean ModifyTask(Task task, TaskAssigneeDTO taskAss) {
        User currentUser= securityConfig.getCurrentUser();
        //获取任务
        Task existingTask = (Task) taskRepository.findById(task.getId())
                .orElseThrow(()->new NoSuchElementException("Task not found"));

        //权限验证

        //修改任务内容
        existingTask.setTitle(task.getTitle()!=null?task.getTitle():existingTask.getTitle());
        existingTask.setAssignees(taskAss.getAssignees()!=null?taskAss.getAssignees():existingTask.getAssignees());
        existingTask.setEndDate(task.getEndDate()!=null?task.getEndDate():existingTask.getEndDate());
        existingTask.setDescription(task.getDescription()!=null?task.getDescription():existingTask.getDescription());

        //保存修改
        taskRepository.save(existingTask);
        return true;    // TODO : ?

    }

    public void InitPreMessage(Task task){
        PreSendingMessage preSendingMessage = new PreSendingMessage();
        preSendingMessage.setTaskId(task.getId());
        Set<User> assignees = task.getAssignees();
        String taskUser = assignees.stream()
                .map(user -> String.valueOf(user.getId())) // 提取id并转换为字符串
                .collect(Collectors.joining(","));
        preSendingMessage.setUserIds(taskUser);
        preSendingMessage.setTime(task.getStartDate());
        preSendingMessage.setCreatedAt(task.getEndDate());
    }

    public List<TaskForGanttDTO> getGanttElement(Long id) {
        User user = securityConfig.getCurrentUser();
        //权限判断

        //查看输入的项目id是否有效
        Project project = projectRepository.findById(id).orElse(null);
        if(project == null) {
            throw new IllegalArgumentException("没有找到对应的项目,您输入的项目id不正确");
        }

        List<Task> tasks = taskRepository.findByBelongedProjectId(id);


        List<TaskForGanttDTO> taskGantt = new ArrayList<>();


        return taskGantt;

    }
}
