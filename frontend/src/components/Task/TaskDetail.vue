<template>
  <div class="task-detail">
    <!-- 任务标题和描述 -->
    <h2>{{ task.title }}</h2>
    <p>{{ task.description }}</p>

    <!-- 任务进度展示 -->
    <div class="task-progress">
      <p>任务进度: {{ task.progress }}%</p>
      <el-progress :percentage="task.progress"></el-progress>
      <el-input-number
          v-model="taskProgress"
          :min="0"
          :max="100"
          label="更新进度"
          size="small"
      ></el-input-number>
      <el-button type="primary" @click="updateProgress">更新任务进度</el-button>
    </div>

    <!-- 任务详情 -->
    <el-divider>任务详情</el-divider>
    <div>
      <p><strong>负责人:</strong> {{ task.publisher }}</p>
      <p><strong>创建时间:</strong> {{ task.startDate }}</p>
      <p><strong>更新时间:</strong> {{ task.updatedAt }}</p>
    </div>

    <!-- 时间轴记录 -->
    <el-divider>任务更新记录</el-divider>
    <el-timeline>
      <el-timeline-item
          v-for="(log, index) in task.logs"
          :key="index"
          :timestamp="log.timestamp"
          placement="top"
      >
        {{ log.content }}
      </el-timeline-item>
    </el-timeline>

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

const task = ref({});
const comment = ref('');
const taskProgress = ref(0);

// 获取任务详情
const fetchTaskDetail = async (taskId) => {
  try {
    const response = await axios.get(`http://localhost:8081/api/task/getbyid/${taskId}`);
    task.value = response.data;
    taskProgress.value = response.data.progress; // 初始化任务进度
  } catch (error) {
    ElMessage.error('获取任务详情失败');
  }
};

// 更新任务进度
const updateProgress = async () => {
  try {
    const response = await axios.post('http://localhost:8081/api/task/updateprogress', {
      id: task.value.id,
      progress: taskProgress.value,
    });
    if (response.data.success) {
      task.value.progress = taskProgress.value; // 更新本地任务进度
      ElMessage.success('任务进度更新成功');
    } else {
      ElMessage.error('任务进度更新失败');
    }
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
  const taskId = 1; // 假设从路由获取的任务ID
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
