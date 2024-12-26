<script setup>
import {ref, onMounted} from "vue";
import axios from "axios";

const tasks = ref([]);
const newTaskTitle = ref("");
const newTaskPublisher = ref("");

const fetchTasks = async () => {
  try {
    const res = await axios.get("http://localhost:8081/api/tasks");
    tasks.value = res.data;
  } catch (error) {
    console.error("获取任务列表出错: ", error)
  }
}

const createTask = async () => {
  try {
    await axios.post("http://localhost:8081/api/tasks", {
      title: newTaskTitle.value,
      publisher: newTaskPublisher.value,
    });
    await fetchTasks();
  } catch (error) {
    console.error("创建任务出错:", error);
  }
}

const deleteTask = async () => {
  try {
    await axios.delete("http://localhost:8081/api/tasks");
    // 删除任务后更新列表
    await fetchTasks()
  } catch (error) {
    console.error("删除任务出错:", error)
  }
}
onMounted(() => {
  fetchTasks();
})
</script>

<template>
  <div>
    <h1>任务列表</h1>
    <div>
      <label>任务标题:</label>
      <input v-model="newTaskTitle" type="text"/>
      <label>发布者:</label>
      <input v-model="newTaskPublisher" type="text"/>
      <button @click="createTask">创建任务</button>
    </div>
    <table class="task-table">
      <thead>
        <tr>
          <th>ID</th>
          <th>标题</th>
          <th>描述</th>
          <th>发布者</th>
          <th>状态</th>
          <th>完成人</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
      <tr v-for="task in tasks" :key="task.id">
        <td>{{ task.id }}</td>
        <td>{{ task.title }}</td>
        <td>{{ task.description }}</td>
        <td>{{ task.publisher }}</td>
        <td>{{ task.status }}</td>
        <td>{{ task.assignee }}</td>
        <td>
          <button @click="deleteTask(task.id)">删除</button>/
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<style scoped>
.task-table {
  border: 1px solid #000;
  border-collapse: collapse;
}

</style>