import {createRouter, createWebHistory} from "vue-router";
import LoginView from "@/views/LoginView.vue";
import TaskListView from "@/views/TaskListView.vue";

const routes = [
    {path : "/", redirect: "/login"},
    {path: "/login", name: "Login", component: LoginView},
    {path: "/tasks", name: "Tasks", component: TaskListView}
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;