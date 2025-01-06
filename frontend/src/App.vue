<template>
  <div v-if="!$route.meta.nonLayout" class="common-layout">
    <el-header class="main-header">企业管理系统</el-header>
    <el-container class="container">
      <el-aside class="aside">
        <Menu/>
      </el-aside>
      <el-container class="container">
        <el-header class="header">{{ currentHeader }}</el-header>
        <el-main class="main">
          <router-view/>
        </el-main>
      </el-container>
    </el-container>
  </div>
  <div v-else class="center-pos">
    <router-view/>
  </div>
</template>

<script>
export default {
  name: "DynamicLayout",
  computed: {
    // 通过路由 meta 动态获取 Header 信息
    currentHeader() {
      return this.$route.meta.header || "默认标题";
    },
  },
};

</script>

<style scoped>
header {
  line-height: 1.5;
}

@media (min-width: 300px) {
  .aside {
    background: #ffffff;
    width: 230px;
  }

  .common-layout {
    height: 100vh;
    width: 100%;
  }

  .container {
    display: flex;
    height: 100%;
    width: 100%;
  }

  .header {
    background: #a4fdfd;
    height: 70px;
    line-height: 70px;
    text-align: center;
  }

  .main-header {
    background: #aafbeb;
    height: 70px;
    line-height: 70px;
  }

  .center-pos {
    display: grid;
    align-items: center;
    justify-content: center;
    height: 100%;
    width: 100%;
  }
}

</style>
