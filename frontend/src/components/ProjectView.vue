<script setup>
import {ref, onMounted} from "vue";
import axios from "axios";

let projects = ref([]);
const fetchAllProjects = async () => {
  try {
    const response = await axios.post("http://localhost:8081/api/project/list");
    projects = [...response.data];
    console.log(projects);
  } catch (error) {
    alert("获取项目失败" + error.message);
  }
}

const selectRow = async (row) => {
  alert("you selected: " + row.id);
}

onMounted(async () => {
  await fetchAllProjects();
})
</script>

<template>
  <div v-if="Array.isArray(projects) && projects.length > 0">
    <el-table :data="projects" style="width: 100%">
      <el-table-column fixed prop="projectName" label="项目名称" width="150"/>
      <el-table-column prop="startDate" label="开始时间" width="150"/>
      <el-table-column prop="endDate" label="截止时间" width="150"/>
      <el-table-column prop="description" label="项目描述" width="300"/>
      <el-table-column prop="priority" label="优先级" width="120"/>
      <el-table-column prop="isCompleted" label="完成情况" width="120">
        <template #default="{ row }">
          <span>{{ row.isCompleted ? '是' : '否' }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="progress" label="进度" width="120"/>
      <el-table-column fixed="right" label="操作" min-width="120">
        <template #default="{ row }">
          <el-button link type="primary" size="small" @click="selectRow(row)">
            选择
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
  <div v-else>
    <span>正在加载项目列表</span>
  </div>

</template>

<style scoped>

</style>