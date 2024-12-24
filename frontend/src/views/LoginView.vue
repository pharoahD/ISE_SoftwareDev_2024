<script setup>
import {ref} from 'vue'
import axios from "axios"
import {useRouter} from "vue-router"

const router = useRouter()
const username = ref("")
const password = ref("")

const login = async () => {
  try { // 调用后端登录接口
    const res = await axios.post('http://localhost:8080/auth/login', {
      username: username.value,
      password: password.value,
    })
    alert(res.data)
    await router.push('/tasks')   // 登录成功后转到任务列表
  } catch (error) {
    if (error.response.status === 401) {
      alert("登陆失败，请检查用户名或者密码是否正确")
    } else {
      alert("登录失败: " + error)
    }
  }
}

const register = async () => {
  try {
    const res = await axios.post('http://localhost:8080/auth/register', {
      username: username.value,
      password: password.value,
    })
    alert(res.data)
  } catch (error) {
    alert("注册失败: " + error)
  }
}
</script>

<template>
  <div>
    <h1>登录</h1>
    <div>
      <label>用户名:</label>
      <input v-model="username" type="text"/>
    </div>
    <div>
      <label>密码:</label>
      <input v-model="password" type="password"/>
    </div>
    <button @click="login">登录</button>
    <button @click="register">注册</button>
  </div>
</template>

<style scoped>

</style>