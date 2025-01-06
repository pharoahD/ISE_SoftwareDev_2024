import './assets/main.css'

import {createApp} from 'vue'
import App from './App.vue'
import router from './router';
import axios from "axios";

const app = createApp(App);
app.use(router);
app.mount('#app');

axios.defaults.baseURL = 'http://127.0.0.1:8081/';
axios.defaults.withCredentials = true;
axios.interceptors.response.use(
    response => response,
    error => {
        if (error.response && error.response.status === 403){
            alert("您的登录似乎过期了，请重新登录")
            window.location.href = "http://localhost/login";
        } else
            return Promise.reject(error);
    }
)

export default axios;