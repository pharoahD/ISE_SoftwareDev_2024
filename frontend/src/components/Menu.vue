<template>
  <el-row class="tac">
    <el-col>
      <el-menu
          class="el-menu-vertical-demo"
          router
          @open="handleOpen"
          @close="handleClose"
      >
        <el-sub-menu index="1">
          <template #title>
<!--            <el-icon>-->
<!--              <el-icon type="question-circle-outline" />-->
<!--            </el-icon>-->
            <span>项目管理</span>
          </template>
          <el-menu-item index="/project/list">
            项目列表
          </el-menu-item>
          <el-menu-item index="/project/create">
            创建项目
          </el-menu-item>
        </el-sub-menu>

        <!-- 任务管理 -->
        <el-sub-menu index="2">
          <template #title>
            <span>任务管理</span>
          </template>
          <el-menu-item index="/task/create">
            创建任务
          </el-menu-item>
          <el-menu-item index="/task/list">
            被指派任务列表
          </el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="3">
          <template #title>
            <span>文档管理</span>
          </template>
          <el-menu-item index="/document/list">
            文档查看
          </el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="4">
          <template #title>
            <span>消息</span>
          </template>
          <el-menu-item index="/message">
            消息管理
          </el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-col>
  </el-row>
</template>

<script setup>
import {computed} from "vue";
import {useRouter} from "vue-router";

</script>

<style scoped>

</style>