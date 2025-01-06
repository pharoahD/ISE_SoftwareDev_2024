import {createRouter, createWebHistory} from "vue-router";
import LoginView from "@/components/LoginView.vue";
// import TaskListView from "@/components/TaskListView.vue";
import ProjectList from "@/components/Projects/ProjectListView.vue";
// import TaskCreate from "@/components/TaskCreate.vue";
// import TaskDetail from "@/components/TaskDetail.vue";
// import FileUpload from "@/components/FileUploadTest/FileUpload.vue";

const routes = [
    {path : "/", redirect: "/login"},
    {path: "/login", name: "Login", component: LoginView, meta: {nonLayout: true}},
    {path: "project", redirect: "/project/list"},
    {path: "/project/list", name: "ProjectsList", component: ProjectList, meta: {header: "项目列表"}},

    // {path: "/tasks", name: "Tasks", component: TaskListView},
    // {path: "/tasks/:id", name: "Detail", component: TaskDetail},
    // {path: "/file", name: "File", component: FileUpload},
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;