package org.flitter.backend.service;

import org.flitter.backend.entity.Task;

import java.util.List;

public interface TaskAlloc {
    //分配任务
    Task allocateTask(Task task);

    //通过id查询任务
    Task getTaskByID(Long id);

    //查看所有任务列表
    List<Task> getAllTasks(Long id);

    //修改任务
    boolean ModifyTask(Task task);
}
