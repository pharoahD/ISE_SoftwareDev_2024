import {createRouter, createWebHistory} from "vue-router";
import LoginView from "@/components/LoginView.vue";
import ProjectList from "@/components/Projects/ProjectListView.vue";
import ProjectCreate from "@/components/Projects/ProjectCreate.vue";

import TaskList from "@/components/Task/TaskListView.vue";
import TaskCreate from "@/components/Task/TaskCreate.vue";
import ProjectDetail from "@/components/Projects/ProjectDetail.vue";
import TaskDetail from "@/components/Task/TaskDetail.vue";
import TaskListView from "@/components/Task/TaskListView.vue";
// import FileUpload from "@/components/FileUploadTest/FileUpload.vue";

import UserRoleList from "@/components/HumanResource/UserRoleList.vue"
import MessageView from "@/components/message/message.vue"
import DocumentList from "@/components/Document/DocumentList.vue";
import ProjectGantt from "@/components/Projects/ProjectGantt.vue";

const routes = [
    {path : "/", redirect: "/login"},
    {path: "/login", name: "Login", component: LoginView, meta: {nonLayout: true}},
    {path: "/project", redirect: "/project/list"},
    {path: "/project/list", name: "ProjectsList", component: ProjectList, meta: {header: "项目列表选择"}},
    {path: "/project/create", name: "ProjectCreate", component: ProjectCreate, meta: {header: "创建项目"}},
    {path: "/project/detail/:id", name: "ProjectDetail", component: ProjectDetail, meta: {header: "项目详情"}},
    {path: "/project/gantt/:id", name: "ProjectGantt", component: ProjectGantt,  meta: {header: "项目甘特图"}},

    {path: "/task/list", name: "TasksList", component: TaskListView, meta: {header: "任务列表"}},
    {path: "/task/create", name: "TasksCreate", component: TaskCreate, meta: {header: "创建任务"}},
    {path: "/user/role/list", name: "UserRoleList", component: UserRoleList, meta: {header: "角色管理"}},
    {path: "/message", name: "message", component: MessageView, meta: {header: "消息系统"}},
    {path: "/document/list", name: "DocumentList", component: DocumentList, meta: {header: "文件查看"}},
    // {path: "/task/detail/:id", name: "TasksDetail", component: TaskDetail, meta: {header: "任务详情"}},
    //{path: "/task/detail", name: "TaskDetail", component: TaskDetail},
    // {path: "/file", name: "File", component: FileUpload},
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;