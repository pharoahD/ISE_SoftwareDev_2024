<script setup>
import {ref, onMounted, inject} from "vue";
import "@/assets/project.css"
import http from "@/http/request.js";
import {useRouter} from "vue-router";


let projects = ref([]);
const router = useRouter()
const showMessage = inject("showMessage");
const showError = inject("showError");

const priorityMapping = {
  LOW: "低",
  MEDIUM: "中",
  HIGH: "高",
};

const mapPriority = (priority) => {
  return priorityMapping[priority] || "未知";
};

const fetchAllProjects = async () => {
  try {
    const response = (await http.get("http://localhost:8081/api/project/list/all")).data;
    projects.value = response || [];
  } catch (error) {
    showError("获取项目失败" + error.message);
  }
};


// 选择任务，跳转到任务详情页面
const selectRow = async (row) => {
  // 跳转到任务详情页，传递项目的 ID
  showMessage("正在进入项目: " + row.projectName);
  await router.push({
    name: "ProjectDetail",
    params: {id: row.id}
  })
};

onMounted(async () => {
  await fetchAllProjects();
})
</script>

<template>
  <el-table :data="projects" style="width: 100%">
    <el-table-column fixed prop="projectName" label="项目名称" width="150"/>
    <el-table-column prop="startDate" label="开始时间" width="150"/>
    <el-table-column prop="endDate" label="截止时间" width="150"/>
    <el-table-column prop="description" label="项目描述" width="300">
      <template #default="scope">
        <el-tooltip class="item" effect="dark" :content="scope.row.description" placement="top">
          <span class="ellipsis-text">{{ scope.row.description }}</span>
        </el-tooltip>
      </template>
    </el-table-column>
    <el-table-column prop="priority" label="优先级" width="120">
      <template #default="scope">
        <span>{{ mapPriority(scope.row.priority) }}</span>
      </template>
    </el-table-column>
    <el-table-column prop="isCompleted" label="完成情况" width="120">
      <template #default="{ row }">
        <span>{{ row.isCompleted ? '是' : '否' }}</span>
      </template>
    </el-table-column>
    <el-table-column prop="progress" label="进度" width="120"/>
    <el-table-column fixed="right" label="进入项目" min-width="120">
      <template #default="{ row }">
        <el-button link type="primary" size="small" @click="selectRow(row)">
          查看
        </el-button>
      </template>
    </el-table-column>
  </el-table>
</template>

<style scoped>
.ellipsis-text {
  display: inline-block;
  max-width: 100%;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  vertical-align: middle;
}
</style>