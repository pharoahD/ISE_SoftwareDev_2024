<template>
  <div>
    <h2>甘特图</h2>
    <div ref="ganttContainer" class="gantt-container"></div>
  </div>
</template>

<script setup>
import {ref, onMounted, onBeforeUnmount, inject} from "vue";
import "dhtmlx-gantt/codebase/dhtmlxgantt.css"; // 导入 dhtmlx-gantt 样式
import gantt from "dhtmlx-gantt"; // 导入 dhtmlx-gantt
import http from "@/http/request.js"; // 自定义的 HTTP 模块
import {useRoute} from "vue-router";
import {fetchProjectDetails} from "@/scripts/Project.js";

const showMessage = inject("showMessage");
const showError = inject("showError");

const ganttContainer = ref(null); // 甘特图容器
const route = useRoute();
const projectId = route.params.id; // 从路由中获取项目 ID
let project = ref({})

// 获取任务数据的函数
const fetchTasks = async () => {
  try {
    const response = await http.get(`/api/task/gantt?id=${projectId}`);
    const taskData = response.data;

    // 转换为 dhtmlx-gantt 所需的格式
    return {
      data: taskData.map(task => ({
        id: task.id,
        text: task.title,
        start_date: task.startDate,
        end_date: task.endDate,
        progress: task.isCompleted ? 1 : 0,
        open: true
      })),
      links: []
    };
  } catch (error) {
    console.error("获取任务数据失败：", error);
    return {data: [], links: []};
  }
};

const updateTaskDates = async (id, start, end) => {
  try {
    await http.post("/api/task/modify", {
      id: id,
      start_date: start,
      end_date: end
    });
    showMessage(`任务 ${id} 日期更新成功: ${start} - ${end}`);
  } catch (error) {
    showError(`任务 ${id} 日期更新失败:`, error);
  }
};

// 初始化甘特图的函数
const initGantt = async () => {
  const tasks = await fetchTasks();
  project = fetchProjectDetails(projectId);
  gantt.config.date_format = "%Y-%m-%d"; // 后端日期格式
  gantt.config.drag_progress = false;
  gantt.config.autofit = true;

  // 初始化甘特图
  gantt.init(ganttContainer.value);
  gantt.start_date = project.start_date;
  gantt.end_date = project.end_date;

  // 加载任务数据
  gantt.parse(tasks);

  gantt.attachEvent("onAfterTaskUpdate", function (id, task) {
    console.log("任务被更新:", id);
    console.log("新日期:", task.start_date, task.end_date);

    // 向后端发送更新请求
    updateTaskDates(task.id, task.start_date, task.end_date);
  });
};

// 组件挂载时初始化甘特图
onMounted(() => {
  initGantt();
});

// 组件卸载前清理甘特图实例
onBeforeUnmount(() => {
  gantt.clearAll(); // 清空所有数据
});
</script>

<style scoped>
.gantt-container {
  width: 100%;
  height: 500px;
  overflow-x: auto;
}
</style>
