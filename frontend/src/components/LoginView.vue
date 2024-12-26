<script setup>
import {ref} from 'vue'
import axios from "axios"
import {useRouter} from "vue-router"
import "@/assets/login.css"

const router = useRouter()
const username = ref("")
const password = ref("")

const login = async () => {
  if (!username.value || !password.value) {
    alert("用户名和密码不能为空")
    return
  }
  try { // 调用后端登录接口
    const res = await axios.post('http://localhost:8081/api/auth/login', {
      username: username.value,
      password: password.value,
    })
    alert(res.data)
    await router.push('/project_list')   // 登录成功后转到任务列表
  } catch (error) {
    if (error.response.status === 401) {
      alert("登陆失败，请检查用户名或者密码是否正确")
    } else {
      alert("登录失败: " + error)
    }
  }
}

const register = async () => {
  if (!username.value || !password.value) {
    alert("用户名和密码不能为空")
    return
  }
  try {
    const res = await axios.post('http://localhost:8081/api/auth/register', {
      username: username.value,
      password: password.value,
    })
    if (res.data.code === 200) {
      alert("注册成功，现在请进行登录")
      window.location.reload()
    }
  } catch (error) {
    if (error.response.status === 400)
      alert(error.response.data)
    else
      alert("注册失败: " + error.message)
  }
}
</script>

<template>
  <div class="box">
    <div class="login-box">
      <div>
        <h1>注册与登录</h1>
      </div>
      <div class="input-container">
        <label for="username">用户名:</label>
        <input v-model="username" type="text" class="input-field"/>
      </div>
      <div class="input-container">
        <label for="username">用户名:</label>
        <input v-model="password" type="password" class="input-field"/>
      </div>
      <div class="button-box">
        <div class="button-left-space"></div>
        <button class="button" @click="login">登录</button>
        <div class="button-middle-space"></div>
        <button class="button" @click="register">注册</button>
        <div class="button-right-space"></div>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>