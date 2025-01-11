  <script setup>
import { ref, onMounted, inject } from "vue";
import http from "@/http/request.js";
import { useRouter } from "vue-router";

let users = ref([]);
let roles = ref([]);
// const router = useRouter();
const showMessage = inject("showMessage");
const showError = inject("showError");

// 获取所有用户及角色数据
const fetchAllUsers = async () => {
  try {
    const response = (await http.get("/api/user/role/all")).data;
    users.value = response || [];
  } catch (error) {
    showError("获取用户列表失败: " + error.message);
  }
};

// 获取所有角色类型
const fetchAllRoles = () => {
  roles.value = ["ADMIN", "APARTMENT_LEADER", "PROJECT_LEADER", "USER"];
};

// 更新用户角色
const updateUserRole = async (user) => {
  try {
    const updateRequest = { id: user.id, role: user.role };
    await http.post("/api/user/role/update", updateRequest);
    showMessage("角色更新成功");
    await fetchAllUsers(); // 重新获取用户数据
  } catch (error) {
    showError("角色更新失败: " + error.response.data || error.message);
  }
};

onMounted(async () => {
  await fetchAllUsers();
  fetchAllRoles();
});
</script>

<template>
  <div v-if="Array.isArray(users) && users.length > 0">
    <el-table :data="users" style="width: 100%">
      <el-table-column prop="id" label="用户ID" width="150" />
      <el-table-column prop="username" label="用户名" width="200" />
      <el-table-column prop="role" label="角色" width="200">
        <template #default="scope">
          <el-select
              v-model="scope.row.role"
              placeholder="选择角色"
              size="small"
              @change="() => updateUserRole(scope.row)"
          >
            <el-option
                v-for="role in roles"
                :key="role"
                :label="role"
                :value="role"
            />
          </el-select>
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作" width="150">
        <template #default="{ row }">
          <el-button link type="primary" size="small" @click="() => updateUserRole(row)">
            更新
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
  <div v-else>
    <span>正在加载用户列表...</span>
  </div>
</template>

<style scoped>
/* 自定义样式 */
</style>
