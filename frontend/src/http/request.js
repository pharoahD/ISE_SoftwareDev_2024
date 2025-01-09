import axios from "axios"
import {throwError} from "element-plus/es/utils/index";


const http = axios.create({
    baseURL: "http://localhost:8081/",
    withCredentials: true,
    timeout: 5000,
    // headers: {
    //     'Content-Type': 'application/json',
    //     Authorization: `Bearer ${localStorage.getItem('token')}`
    // }
})

http.interceptors.request.use(
    // (config) => {
    //     const token = localStorage.getItem('token');
    //     if (token) {
    //         config.headers.Authorization = `Bearer ${token}`;
    //     }
    //     console.log("请求配置", config);
    //     return config;
    // },
    // (error) => {
    //     return Promise.reject(error);
    // }
)

http.interceptors.response.use(
    (response) => {
        console.log("相应数据", response);
        return response
    },
    (error) => {
        if (error.response.status === 403) {
            alert("登录已过期，请重新登录");
            window.location.href = "/login";
        } else {
            if (error.response) {
                console.error("错误响应:", error.response.data);
            } else if (error.request) {
                console.error("未受到响应", error.request);
            } else {
                console.error("请求配置错误", error.message);
            }
            return Promise.reject(error);
        }
    }
)


export default http;