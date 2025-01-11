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

    <!-- 评论显示区域 -->
    <el-divider>评论</el-divider>
    <div v-if="task.logs && task.logs.length > 0">
      <div v-for="(log, index) in task.logs" :key="index" class="comment-item">
        <p><strong>{{ log.publisher }}:</strong> <span>{{ log.timestamp }}</span></p>
        <p>{{ log.content }}</p>
        <hr />
      </div>
    </div>

    <!-- 评论输入区 -->
    <el-input
        type="textarea"
        v-model="comment"
        placeholder="写评论，提及人使用 @"
        rows="3"
    ></el-input>

    <!-- 文件上传区 -->
    <el-upload
        ref="upload"
        class="upload-demo"
        action="http://localhost:8081/api/comment/add/file"
        :on-success="handleFileSuccess"
        :on-error="handleFileError"
        :show-file-list="false"
        :auto-upload="false"
        :data="uploadData"
    >
      <el-button size="small" type="primary">上传文件</el-button>
    </el-upload>

    <el-button type="primary" @click="submitComment">提交评论</el-button>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import http from '@/http/request.js';
import { ElMessage } from 'element-plus';
import { useRoute } from "vue-router";

const task = ref({});
const comment = ref('');
const taskProgress = ref(0);
const route = useRoute();
const taskId = route.params.taskId;
const file = ref(null);
const uploadData = ref({});

// 获取任务详情
const fetchTaskDetail = async (taskId) => {
  try {
    const response = await http.get(`http://localhost:8081/api/task/getbyid?id=${taskId}`);
    task.value = {
      id: response.data.id,
      title: response.data.title,
      description: response.data.description,
      publisher: response.data.publisher.username,
      startDate: response.data.startDate,
      endDate: response.data.endDate,
      isCompleted: response.data.isCompleted,
      percentCompleted: response.data.percentCompleted,
      logs: []  // 确保初始化日志数组
    };
    taskProgress.value = task.value.percentCompleted;
  } catch (error) {
    ElMessage.error('获取任务详情失败');
  }
};

// 进度更新
const updateProgress = async () => {
  try {
    task.value.percentCompleted = taskProgress.value;
    task.value.isCompleted = taskProgress.value === 100;

    const requestData = {
      id: task.value.id,
      title: task.value.title,
      description: task.value.description,
      startDate: task.value.startDate,
      endDate: task.value.endDate,
      isCompleted: task.value.isCompleted,
      percentCompleted: task.value.percentCompleted,
      publisher: { id: task.value.publisher.id },
    };

    const response = await http.post('/api/task/modify', requestData);
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
    const response = await http.post('http://localhost:8081/api/comment/add/string', {
      stringComment: comment.value,
      belongedTask: { id: task.value.id },
    });

    if (response.data && response.data.id) {
      task.value.logs.push({
        timestamp: new Date().toISOString(),
        content: comment.value,
        publisher: response.data.publisher.username,
      });
      comment.value = ''; // 清空评论框
      ElMessage.success('评论提交成功');
    } else {
      ElMessage.error('评论提交失败');
    }
  } catch (error) {
    console.error('提交评论时发生错误:', error);
    ElMessage.error('评论提交失败');
  }
};

// 处理文件上传成功
const handleFileSuccess = (response, file, fileList) => {
  uploadData.value = {
    documentId: response.data.documentId,  // 假设返回文件ID
    taskId: task.value.id,
    file,
  };
  ElMessage.success('文件上传成功');
};

// 处理文件上传失败
const handleFileError = (err, file, fileList) => {
  ElMessage.error('文件上传失败');
};

// 页面加载时初始化任务信息和评论
onMounted(() => {
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

.comment-item {
  margin-bottom: 20px;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
}

.comment-item p {
  margin: 5px 0;
}
</style>
