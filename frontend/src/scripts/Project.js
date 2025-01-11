import http from "@/http/request.js"
import {inject} from "vue";

const showError = inject("showError");

/**
 * 获取项目详情及参与者
 * @param {string} projectId - 项目ID
 * @returns {Promise<{project: Object, participants: Array}>} 返回项目详情和参与者
 */
export const fetchProjectDetails = async (projectId) => {
    let project = {};

    try {
        const {data} = await http.post("/api/project/get", {
            id: projectId,
        });
        project = data;
    } catch (error) {
        console.error("获取项目详情失败:", error);
        throw new Error("Failed to fetch project details");
    }

    return project;
};
