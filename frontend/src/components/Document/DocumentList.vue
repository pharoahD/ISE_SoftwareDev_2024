<template>
  <!-- 选择项目下拉菜单 -->
<div v-if="projects.length">
    <h2>选择项目</h2>
    <el-select v-model="selectedProjectId" placeholder="请选择项目" @change="fetchDocuments">
      <el-option
          v-for="project in projects"
          :key="project.id"
          :label="project.projectName"
          :value="project.id">
      </el-option>
    </el-select>
  </div>

  <!-- 显示文档列表 -->
  <h2>文档列表</h2>
  <!-- 下拉菜单展示文档 -->
  <select v-model="selectedDocumentId" @change="viewDocument(selectedDocumentId)">
    <option value="" disabled>请选择文档</option>
    <option v-for="document in documents" :key="document.id" :value="document.id">
      {{ document.name }}
    </option>
  </select>

  <!-- 显示文档版本 -->
  <div v-if="documentVersions.length">
    <h3>文档版本</h3>
    <ul>
      <li v-for="version in documentVersions" :key="version.id">
        <span>{{ version.version }} - {{ version.filename }}</span>
        <button @click="downloadDocument(version)">下载</button>
      </li>
    </ul>
  </div>

  <!-- 加载中提示 -->
  <div v-if="loading">加载中...</div>
</template>

<script>
import axios from 'axios';
import { ElSelect, ElOption } from 'element-plus'; // 引入Element Plus组件

export default {
  data() {
    return {
      projects: [],            // 存储项目列表
      selectedProjectId: null, // 选中的项目ID
      documents: [],           // 存储文档列表
      selectedDocumentId: null, // 选中的文档ID
      documentVersions: [],    // 存储文档版本列表
      documentContent: null,   // 存储单个文档的详细内容
      loading: false,          // 加载状态
    };
  },
  methods: {
    // 获取当前用户参与的项目
    fetchProjects() {
      this.loading = true;
      axios
          .get('http://localhost:8081/api/project/list/participated')
          .then(response => {
            this.projects = response.data.map(project => ({
              id: project.id,
              projectName: project.projectName, // 提取项目名称
            }));
          })
          .catch(error => {
            console.error('获取项目列表失败:', error);
          })
          .finally(() => {
            this.loading = false;
          });
    },

    // 获取指定项目的文档列表
    fetchDocuments() {
      if (!this.selectedProjectId) return; // 如果没有选择项目，直接返回

      this.loading = true;
      axios
          .post('http://localhost:8081/api/documents/projects', {
            projectId: this.selectedProjectId // 传递 projectId 作为请求体参数
          })
          .then(response => {
            // 成功获取文档列表后
            this.documents = response.data.map(doc => ({
              id: doc.id,
              name: doc.name,

            }));
          })
          .catch(error => {
            console.error('获取文档失败:', error);
          })
          .finally(() => {
            this.loading = false;
          });
    },

    // 获取选中文档的所有版本
    viewDocument(documentId) {
      if (!documentId) return;
      this.loading = true;
      axios
          .post('http://localhost:8081/api/documents/projects/versions', {
            documentId: documentId,
          })
          .then(response => {
            this.documentVersions = response.data; // 获取文档版本信息
          })
          .catch(error => {
            console.error('获取文档版本失败:', error);
          })
          .finally(() => {
            this.loading = false;
          });
    },

    // 下载文档
    downloadDocument(version) {
      this.loading = true;
      axios
          .post('http://localhost:8081/api/documents/projects/versions/download', {
            documentId: version.id, // 传递 documentId 和 version 信息
            version: version.version,
          }, {
            responseType: 'blob', // 设置响应类型为 blob，用于文件下载
          })
          .then(response => {
            // 创建一个临时的下载链接
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', version.filename); // 设置下载文件名为 filename
            document.body.appendChild(link);
            link.click();
          })
          .catch(error => {
            console.error('下载文档失败:', error);
          })
          .finally(() => {
            this.loading = false;
          });
    },
  },
  mounted() {
    // 页面加载时获取用户参与的项目列表
    this.fetchProjects();
  },
};
</script>

<style scoped>
/* 页面样式 */
h1 {
  text-align: center;
}

ul {
  list-style-type: none;
}

li {
  margin: 10px 0;
}

button {
  margin-left: 10px;
}
</style>
