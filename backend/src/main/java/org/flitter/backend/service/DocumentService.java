package org.flitter.backend.service;

import org.flitter.backend.entity.Document;
import org.flitter.backend.entity.DocumentVersion;
import org.flitter.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.flitter.backend.entity.User;
import org.flitter.backend.entity.Task;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import org.flitter.backend.entity.Project;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DocumentVersionRepository documentVersionRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    // 创建新文档
    public Document createDocument(String name, Long projectId, Long taskId) throws Exception{
        Document document = new Document();
        document.setName(name);
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task == null) {
            throw new Exception("task not found");
        }
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project == null) {
            throw new Exception("project not found");
        }
        if (task.getBelongedProject().getId() != project.getId()) {
            throw new Exception("task belongs error");
        }
        document.setBelongsToProject(project);
        document.setBelongsToTask(task);
        documentRepository.save(document);
        return document;
    }

    // 获取指定项目下所有文档
    public List<Document> getDocumentsByProject(Long projectId) throws Exception{
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project == null) {
            throw new Exception("Project not found");
        }
        return documentRepository.findByBelongsToProject(project);
    }

    // 上传文档的新版本
    public DocumentVersion uploadNewVersion(Long documentId, String version, MultipartFile file) throws Exception {
        Document document = documentRepository.findById(documentId).orElse(null);
        if (document == null) {
            throw new Exception("Document not found.");
        }

        // 创建文档版本
        DocumentVersion documentVersion = new DocumentVersion();
        documentVersion.setVersion(version);
        documentVersion.setFilename(file.getOriginalFilename());
        documentVersion.setFileSize(file.getSize());
        documentVersion.setFileType(file.getContentType());
        documentVersion.setUploadTime(LocalDateTime.now());
        documentVersion.setFileData(file.getBytes());
        documentVersion.setBelongsToDocument(document);
        documentVersionRepository.save(documentVersion);

        return documentVersion;
    }

    // 获取文档的所有版本
    public List<DocumentVersion> getAllVersions(Long documentId) throws Exception{
        Document document = documentRepository.findById(documentId).orElse(null);
        if (document == null) {
            throw new Exception("Document not found.");
        }
        return documentVersionRepository.findByBelongsToDocument(document);
    }

    // 获取指定文档的指定版本
    public DocumentVersion getVersion(Long documentId, String version) throws Exception{
        Document document = documentRepository.findById(documentId).orElse(null);
        if (document == null) {
            throw new Exception("Document not found.");
        }
        DocumentVersion documentVersion = documentVersionRepository.findById(documentId).orElse(null);
        if (documentVersion == null) {
            throw new Exception("Document version not found.");
        }
        return documentVersionRepository.findByBelongsToDocumentAndVersion(document, version);
    }

    // 下载指定文档的指定版本
    public byte[] downloadDocumentVersion(Long documentId, String version) throws Exception{
        DocumentVersion documentVersion = getVersion(documentId, version);
        if (documentVersion != null) {
            return documentVersion.getFileData(); // 返回文件的二进制数据
        }
        throw new IllegalArgumentException("Version not found.");
    }


    // 获取共享文档的所有用户名称
    public List<String> getSharedUserNames(Long documentId) {
        Document document = documentRepository.findById(documentId).orElse(null);
        if (document == null) {
            throw new IllegalArgumentException("Document not found.");
        }
        return document.getSharedUserNamesList();  // 返回共享用户名称列表
    }

    // 将文档共享给某个用户
    public List<String> shareDocumentWithUser(Long documentId, Long userId) throws Exception{
        Document document = documentRepository.findById(documentId).orElse(null);
        if (document == null) {
            throw new Exception("Document not found.");
        }
        // 从数据库获取用户
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new Exception("User not found.");
        }
        // 获取当前共享用户列表
        List<String> sharedUserNames = document.getSharedUserNamesList();
        // 如果用户不在共享列表中，添加用户名称
        if (!sharedUserNames.contains(String.valueOf(userId))) {
            sharedUserNames.add(String.valueOf(userId));
        }
        // 更新文档的共享用户名称
        document.setSharedUserNamesList(sharedUserNames);
        documentRepository.save(document);

        return sharedUserNames;
    }

    // 删除文档的共享用户
    public List<String> removeSharedUser(Long documentId, Long userId) throws Exception{
        Document document = documentRepository.findById(documentId).orElse(null);
        if (document == null) {
            throw new Exception("Document not found.");
        }
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }
        // 获取当前共享用户列表
        List<String> sharedUserNames = document.getSharedUserNamesList();
        if (sharedUserNames.contains(String.valueOf(userId))) {
            sharedUserNames.remove(String.valueOf(userId));
        }
        else {
            throw new Exception("User not found.");
        }

        // 更新文档的共享用户名称
        document.setSharedUserNamesList(sharedUserNames);
        documentRepository.save(document);

        return sharedUserNames;
    }
}
