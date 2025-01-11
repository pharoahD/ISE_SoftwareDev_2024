<template>
  <div class="task-detail">
    <!-- 任务标题和描述 -->
    <h2>任务名称：{{ task.title }}</h2>


    <!-- 任务进度展示 -->
    <div class="task-progress">
      <p>任务进度: {{ taskProgress }}%</p>
      <el-progress :percentage="taskProgress"></el-progress>
      <el-input-number
          v-model="taskProgress"
          :min="0"
          :max="100"
          label="更新进度"
          size="small"
      ></el-input-number>
      <el-button :loading="loading" type="primary" @click="updateProgress">更新任务进度</el-button>
    </div>

    <!-- 任务详情 -->
    <el-divider>任务详情</el-divider>
    <div>
      <p><strong>负责人:</strong> {{ task.publisher }}</p>
      <p><strong>创建时间:</strong> {{ task.startDate }}</p>
      <p><strong>结束时间:</strong> {{ task.endDate }}</p>
      <p>任务描述：{{ task.description }}</p>
    </div>



    <!-- 评论功能 -->
    <el-divider>评论</el-divider>
    <el-input
        type="textarea"
        v-model="comment"
        placeholder="写评论，提及人使用 @"
        rows="3"
    ></el-input>
    <el-button type="primary" @click="submitComment">提交评论</el-button>
  </div>
</template>


<script setup>
import {ref, onMounted} from 'vue';
import axios from 'axios';
import {ElMessage} from 'element-plus';
import {useRoute} from "vue-router";

const task = ref({});
const comment = ref('');
const taskProgress = ref(0);
const route = useRoute();
const taskId = route.params.taskId;

const fetchTaskDetail = async (taskId) => {
  try {
    const response = await axios.get(`http://localhost:8081/api/task/getbyid?id=${taskId}`);
    // 假设返回的数据结构是:
    // {
    //   id: 1,
    //   title: "Test Task70",
    //   description: "This is a description of the test task.",
    //   publisher: { id: 5, username: "王柯", ... },
    //   startDate: "2025-03-02",
    //   endDate: "2025-04-07",
    //   percentCompleted: 0.0,
    // }

    // 更新任务信息
    task.value = {
      id: response.data.id,
      title: response.data.title,
      description: response.data.description,
      publisher: response.data.publisher.username, // 负责人直接取用户名
      startDate: response.data.startDate,
      endDate: response.data.endDate,
      isCompleted: response.data.isCompleted,
      percentCompleted:response.data.percentCompleted,
    };

    // 初始化任务进度
    taskProgress.value = task.value.percentCompleted;
  } catch (error) {
    ElMessage.error('获取任务详情失败');
  }
};

// 进度更新
const updateProgress = async () => {
  try {
    // 更新进度相关字段
    task.value.percentCompleted = taskProgress.value; // 更新进度
    task.value.isCompleted = taskProgress.value === 1; // 如果进度是 100%，标记为完成

    // 构造发送数据
    const requestData = {
      id: task.value.id,
      title: task.value.title,
      description: task.value.description,
      startDate: task.value.startDate,
      endDate: task.value.endDate,
      isCompleted: task.value.isCompleted, // 如果进度是100%，标记为完成
      percentCompleted: task.value.percentCompleted, // 任务进度
      belongedProject: null, // 假设归属项目ID为1，需根据实际情况设置
      publisher: { id: task.value.publisher.id },
      assignees: null, // 假设有固定分配人，需根据实际情况设置
    };

    const response = await axios.post('http://localhost:8081/api/task/modify', requestData);

    ElMessage.success('任务进度更新成功');
  } catch (error) {
    ElMessage.error('任务进度更新失败');
  }
};




// 提交评论
const submitComment = async () => {
  if (!comment.value.trim()) {
    ElMessage.warning('评论内容不能为空');
    return;
  }
  try {
    const response = await axios.post('http://localhost:8081/api/task/addcomment', {
      taskId: task.value.id,
      content: comment.value,
    });
    if (response.data.success) {
      task.value.logs.push({
        timestamp: new Date().toISOString(),
        content: comment.value,
      });
      comment.value = '';
      ElMessage.success('评论提交成功');
    } else {
      ElMessage.error('评论提交失败');
    }
  } catch (error) {
    ElMessage.error('评论提交失败');
  }
};

// 页面加载时初始化
onMounted(() => {
  // alert(taskId);
  fetchTaskDetail(taskId);
});
</script>

<style scoped>
.task-detail {
  padding: 20px;
}

.task-progress {
  margin-bottom: 20px;
}
</style>
