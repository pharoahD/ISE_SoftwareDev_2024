<template>
    <!-- 显示任务列表 -->
    <el-table :data="tasks" style="width: 100%">
      <el-table-column prop="title" label="任务名称" width="180"></el-table-column>
      <el-table-column prop="description" label="任务描述" width="300"></el-table-column>
      <el-table-column prop="priority" label="优先级" width="100"></el-table-column>
      <el-table-column prop="startDate" label="开始时间" width="150"></el-table-column>
      <el-table-column prop="endDate" label="结束时间" width="150"></el-table-column>

      <!-- 使用 v-slot 来替代 slot-scope -->
      <el-table-column label="操作">
        <template v-slot="scope">
          <el-button @click="goToTaskDetail(scope.row.id)" size="small">查看</el-button>
        </template>
      </el-table-column>
    </el-table>

</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';

const router = useRouter();
const tasks = ref([]);  // 存储任务列表

// 获取任务列表
const fetchTasks = async () => {
  try {
    const response = await axios.get('http://localhost:8081/api/task/getbyworkerid');
    tasks.value = response.data; // 将获取的数据赋值给 tasks
  } catch (error) {
    const showError= inject('获取任务列表失败');
  }
};

// 跳转到任务详情页
const goToTaskDetail = (taskId) => {
  router.push({ path: `/task/detail/${taskId}` }); // 手动构建路径
};


// 页面加载时获取任务列表
onMounted(() => {
  fetchTasks();
});
</script>

<style scoped>
.task-list {
  padding: 20px;
}


</style>
