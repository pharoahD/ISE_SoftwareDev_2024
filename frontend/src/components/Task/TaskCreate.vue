<script setup>
import {ref, onMounted} from 'vue';
import axios from 'axios';
import {useRouter} from 'vue-router';
import {ElMessage} from 'element-plus';

const router = useRouter();

const taskTitle = ref('');
const taskDescription = ref('');
const taskPriority = ref('');
const taskStartDate = ref('');
const taskEndDate = ref('');
const assignedMembers = ref([]); // 存储选中的任务分配成员
const selectedProjectId = ref(''); // 存储选中的项目ID
const users = ref([]); // 存储项目成员
// 存储项目列表
const projects = ref([]);

// 获取项目列表
const fetchProjects = async () => {
  try {
    const response = await axios.get('http://localhost:8081/api/project/list/all'); // 获取项目列表
    projects.value = response.data.map(project => ({
      id: project.id,
      projectName: project.projectName // 映射项目名称
    }));
  } catch (error) {
    ElMessage.error('获取项目列表失败');
  }
};


// 获取用户列表
const fetchUsers = async () => {
  try {
    const response = await axios.get('http://localhost:8081/api/user/all');
    // 映射数据，确保有成员的 id 和 username 属性
    users.value = response.data.map(user => ({
      id: user.id,
      username: user.username  // 映射为 name
    }));
  } catch (error) {
    alert('获取成员列表失败');
  }
};

onMounted(() => {
  fetchUsers();  // 页面加载时获取成员列表
});

// 创建任务
const createTask = async () => {
  if (!taskTitle.value || !taskDescription.value || !taskPriority.value || !taskStartDate.value || !taskEndDate.value || !selectedProjectId.value) {
    ElMessage.error('请填写完整任务信息');
    return;
  }

  // 构建请求数据
  const requestData = {
    title: taskTitle.value,
    description: taskDescription.value,
    startDate: taskStartDate.value,
    endDate: taskEndDate.value,
    belongedProject: {
      id: selectedProjectId.value,  // 获取选中的项目ID
    },
    assignees: assignedMembers.value.map(member => ({
      id: member.id,  // 确保 assignedMembers 是成员对象数组，每个成员对象包含 id
    })),
  };

  try {
    await axios.post('http://localhost:8081/api/task/allocation', requestData);
    alert('任务创建成功');
    await router.push(''); // 跳转到任务列表页
  } catch (error) {
    alert('创建任务失败');
  }
};

onMounted(() => {
  fetchUsers();
  fetchProjects();  // 加载项目列表
});
</script>

<template>
  <el-form
    label-width="80px"
  >
    <!-- 任务名称 -->
    <el-form-item label="任务名称">
      <el-input v-model="taskTitle" placeholder="请输入任务名称"></el-input>
    </el-form-item>

    <!-- 任务描述 -->
    <el-form-item label="任务描述">
      <el-input v-model="taskDescription" placeholder="请输入任务描述" type="textarea"></el-input>
    </el-form-item>

    <!-- 优先级 -->
    <el-form-item label="优先级">
      <el-select v-model="taskPriority" placeholder="选择优先级">
        <el-option label="高" value="高"></el-option>
        <el-option label="中" value="中"></el-option>
        <el-option label="低" value="低"></el-option>
      </el-select>
    </el-form-item>

    <!-- 开始时间 -->
    <el-form-item label="开始时间">
      <el-date-picker v-model="taskStartDate" type="date" placeholder="选择开始时间"></el-date-picker>
    </el-form-item>

    <!-- 结束时间 -->
    <el-form-item label="结束时间">
      <el-date-picker v-model="taskEndDate" type="date" placeholder="选择结束时间"></el-date-picker>
    </el-form-item>

    <!-- 选择项目 -->
    <el-form-item label="选择项目">
      <el-select v-model="selectedProjectId" placeholder="选择所属项目">
        <el-option
            v-for="project in projects"
            :key="project.id"
            :label="project.projectName"
            :value="project.id"
        />
      </el-select>
    </el-form-item>

    <!-- 选择任务分配成员 -->
    <el-form-item label="选择成员">
      <el-select v-model="assignedMembers" multiple placeholder="选择任务分配成员">
        <el-option
            v-for="user in users"
            :key="user.id"
            :label="user.username"
            :value="user.id"
        />
      </el-select>
    </el-form-item>

    <!-- 创建任务按钮 -->
    <el-button type="primary" @click="createTask">创建任务</el-button>
  </el-form>
</template>

<style scoped>

.el-form-item button {
  margin-top: 20px; /* 增加按钮上方的间距 */
}
</style>
