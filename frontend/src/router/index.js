import {createRouter, createWebHistory} from "vue-router";
import LoginView from "@/components/LoginView.vue";
import TaskListView from "@/components/TaskListView.vue";
import ProjectView from "@/components/ProjectView.vue";

const routes = [
    {path : "/", redirect: "/login"},
    {path: "/login", name: "Login", component: LoginView},
    {path: "/tasks", name: "Tasks", component: TaskListView},
    {path: "/project_list", name: "Projects", component: ProjectView},
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;