<script setup>

</script>

<template>
  <header></header>
  <div class="banner">
    <h1>企业项目任务管理系统</h1>
  </div>

  <div class="content">
    <router-view>

    </router-view>
  </div>
</template>

<style scoped>
header {
  line-height: 1.5;
}

@media (min-width: 300px) {
  header {
    display: flex;
    place-items: center;
    padding-right: calc(var(--section-gap) / 2);
  }
  .banner {
    position: fixed;
    left: 0;
    top: 0;
    width: 100%;
    background: #a1faba;
    text-align: left;
  }

  .content {
    margin-top: 100px;
    padding: 20px;
  }
}
</style>
