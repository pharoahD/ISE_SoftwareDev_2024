<template>
<!--  <el-row class="tac">-->
<!--    <el-col>-->
<!--      <el-menu-->
<!--          class="el-menu-vertical-demo"-->
<!--          router-->
<!--          @open="handleOpen"-->
<!--          @close="handleClose"-->
<!--      >-->
<!--        <el-sub-menu index="1">-->
<!--          <template #title>-->
<!--            <el-icon>-->
<!--              <location/>-->
<!--            </el-icon>-->
<!--            <span>项目管理</span>-->
<!--          </template>-->
<!--          <el-menu-item index="/project/list">-->
<!--            项目列表-->
<!--          </el-menu-item>-->
<!--          <el-menu-item index="/project/create">-->
<!--            创建项目-->
<!--          </el-menu-item>-->
<!--        </el-sub-menu>-->

<!--        &lt;!&ndash; 任务管理 &ndash;&gt;-->
<!--        <el-sub-menu index="2">-->
<!--          <template #title>-->
<!--            <el-icon>-->
<!--              <icon-menu/>-->
<!--            </el-icon>-->
<!--            <span>任务管理</span>-->
<!--          </template>-->
<!--          <el-menu-item index="/task/create">-->
<!--            创建任务-->
<!--          </el-menu-item>-->
<!--          <el-menu-item index="/task/list">-->
<!--            任务列表-->
<!--          </el-menu-item>-->
<!--        </el-sub-menu>-->

<!--      </el-menu>-->
<!--    </el-col>-->
<!--  </el-row>-->

  <!-- 消息图标 -->
  <div>
    <div class="message-icon" @click="showDialog = true">
      <el-icon><Bell /></el-icon>
      <!-- 未读消息标识 -->
      <div v-if="unreadCount > 0" class="unread-badge">{{ unreadCount }}</div>
    </div>
  </div>


  <!-- 消息弹窗 -->
  <el-dialog
      title="消息中心"
      v-model="showDialog"
      width="50%"
      :close-on-click-modal="false"
  >
    <el-table :data="sortedMessages" style="width: 100%">
      <!-- 时间列 -->
      <el-table-column
          prop="datetime"
          label="时间"
          width="160"
          :formatter="formatTime"
      ></el-table-column>

      <!-- 发送人列 -->
      <el-table-column
          prop="author.username"
          label="发送人"
          width="120"
      ></el-table-column>

      <!-- 消息内容列 -->
      <el-table-column label="消息内容">
        <template #default="{ row }">
          {{ row.message.length > 10 ? row.message.slice(0, 10) + '...' : row.message }}
        </template>
      </el-table-column>

      <!-- 已读/未读列 -->
      <el-table-column
          prop="isRead"
          label="状态"
          width="80"
      >
        <template #default="{ row }">
          <span v-if="row.isRead === 'false'" style="color: red;">未读</span>
          <span v-else style="color: green;">已读</span>
        </template>
      </el-table-column>

      <!-- 查看按钮列 -->
      <el-table-column label="操作" width="100">
        <template #default="{ row }">
          <el-button size="small" @click="handleView(row)">查看</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 弹窗的内容显示 -->
  <el-dialog
      v-if="selectedMessage"
      :title="'查看消息: '"
      v-model="selectedMessageDialog"
      width="40%"
  >
    <!-- 显示信息 -->
    <div class="message-details">
      <div><strong>发送人：</strong> {{ selectedMessage.author.username }}</div>
      <div><strong>发送时间：</strong> {{ formatTime(null, null, selectedMessage.datetime) }}</div>
      <div><strong>状态：</strong>
        <span v-if="selectedMessage.isRead === 'false'" style="color: red;">未读</span>
        <span v-else style="color: green;">已读</span>
      </div>

      <!-- 内容部分 -->
      <div>
        <strong>消息内容：</strong>
        <div class="message-content">{{ selectedMessage.message }}</div>
      </div>
    </div>

    <!-- 操作按钮 -->
    <div class="dialog-footer">
      <el-button v-if="selectedMessage.isRead === 'false'" @click="markAsRead(selectedMessage.id)" type="primary">标记为已读</el-button>
<!--      <el-button @click="selectedMessageDialog = false">关闭</el-button>-->
    </div>
  </el-dialog>

<!--  <template #footer>-->
<!--    <el-button @click="showDialog = false">关闭</el-button>-->
<!--  </template>-->
  </el-dialog>`

</template>

<script lang="ts" setup>
import {
  Document,
  Menu as IconMenu,
  Location,
  Setting,
  Bell,
} from '@element-plus/icons-vue'
import { reactive, ref ,onMounted, computed} from 'vue'
import axios from 'axios';

const handleOpen = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
}
const handleClose = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
}

// 控制弹窗显示
const showDialog = ref(false)

// 控制选择的消息弹窗
const selectedMessageDialog = ref(false)

// 消息数据
const messages = reactive([
  {
    "id": 5,
    "message": "子",
    "author": {
      "id": 5,
      "username": "王柯",
      "email": "wakee@qq.com",
      "role": "USER"
    },
    "datetime": "2025-01-07T14:50:12.11332",
    "receiver": {
      "id": 5,
      "username": "王柯",
      "email": "wakee@qq.com",
      "role": "USER"
    },
    "isRead": "false"
  }
])

// 存储选中的消息
const selectedMessage = ref(null)

// 获取消息列表
const fetchMessages = async () => {
  try {
    const response = await axios.post('http://localhost:8081/api/messages/all')
    messages.splice(0, messages.length, ...response.data) // 更新消息数据
  } catch (error) {
    console.error('获取消息失败:', error)
  }
}

// 获取未读消息数量
const unreadCount = computed(() => {
  return messages.filter((msg) => msg.isRead === 'false').length
})

// 格式化时间
const formatTime = (row: any, column: any, value: string) => {
  const date = new Date(value)
  return date.toLocaleString()
}

// 排序后的消息列表
const sortedMessages = computed(() => {
  return [...messages]
      .sort((a, b) => {
        // 按未读优先和时间倒序排序
        if (a.isRead === 'false' && b.isRead !== 'false') return -1
        if (a.isRead !== 'false' && b.isRead === 'false') return 1
        return new Date(b.datetime).getTime() - new Date(a.datetime).getTime()
      })
})

// 查看按钮逻辑
const handleView = (row: any) => {
  selectedMessage.value = row
  selectedMessageDialog.value = true
}

// 标记消息为已读
const markAsRead = async (messageId: number) => {
  try {
    const response = await axios.post('http://localhost:8081/api/messages/markAsRead', { messageId })
    // 更新消息的状态
    const message = messages.find(msg => msg.id === messageId)
    if (message) {
      message.isRead = 'true'
    }
  } catch (error) {
    console.error('标记消息为已读失败:', error)
  }
}

onMounted(() => {
  fetchMessages()
})

</script>

<style scoped>
.message-icon {
  position: fixed;
  bottom: 20px; /* 距离底部20px */
  left: 20px; /* 距离左侧20px */
  width: 50px;
  height: 50px;
  background-color: #409eff;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  color: white;
  cursor: pointer;
  font-size: 24px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}
.message-icon:hover {
  background-color: #66b1ff;
}
.message-content {
  margin-top: 10px;
  padding: 10px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  background-color: #f9fafb;
}
.message-details {
  padding: 20px;
}
.unread-badge {
  position: absolute;
  top: -3px;
  right: -3px;
  background-color: red;
  color: white;
  font-size: 12px;
  width: 20px;
  height: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 50%;
  font-weight: bold;
}
</style>