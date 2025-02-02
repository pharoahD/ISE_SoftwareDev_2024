package org.flitter.backend.controller;

import jakarta.annotation.sql.DataSourceDefinition;
import lombok.Data;
import org.flitter.backend.entity.Document;
import org.flitter.backend.entity.DocumentVersion;
import org.flitter.backend.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @Data
    static class DocumentAccess {
        Long projectId;
    }

    @Data
    static class DocumentVersionAccess {
        Long documentId;
        Long userId;
    }

    @Data
    static class DocumentInit{
        String name;
        Long projectId;
        Long taskId;
    }

    @Data
    static class SpecificDocument{
        Long documentId;
        String version;
    }

    @Data
    static class DocumentOutput{
        Long documentId;
        String name;
    }

    @Data
    static class DocumentVersionOutput{
        Long documentId;
        String version;
        String name;
    }
    // 创建新文档
    @PostMapping("/create")
    public ResponseEntity<?> createDocument(@RequestBody DocumentInit doc_temp) {
        try {
            String name = doc_temp.getName();
            Long projectId = doc_temp.getProjectId();
            Long taskId = doc_temp.getTaskId();
            Document document = documentService.createDocument(name, projectId, taskId);
            return ResponseEntity.ok("成功创建文档");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.toString());
        }
    }

    // 获取指定项目下所有类型文档
    @PostMapping("/projects")
    public ResponseEntity<?> getDocumetsByProject(@RequestBody DocumentAccess doc_access) {
        try {
            Long projectId = doc_access.getProjectId();
            List<DocumentOutput> documentOutputs = new ArrayList<>();
            List<Document> documents = documentService.getDocumentsByProject(projectId);
            for (Document document : documents) {
                DocumentOutput documentOutput = new DocumentOutput();
                documentOutput.setDocumentId(document.getId()); // 假设 Document 有 getId() 方法
                documentOutput.setName(document.getName());
                documentOutputs.add(documentOutput);
            }
            return ResponseEntity.ok(documentOutputs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.toString());
        }
    }

    // 上传新版本的文档
    @PostMapping("/upload")
    public ResponseEntity<?> uploadNewVersion(@RequestParam Long documentId,
                                              @RequestParam String version,
                                              @RequestParam("file") MultipartFile file) {
        try {
            DocumentVersion documentVersion = documentService.uploadNewVersion(documentId, version, file);
            return ResponseEntity.ok(documentVersion);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.toString());
        }
    }

    // 获取文档的所有版本
    @PostMapping("/projects/versions")
    public ResponseEntity<?> getAllVersions(@RequestBody DocumentVersionAccess docversion_access) {
        try {
            Long documentId = docversion_access.getDocumentId();
            List<DocumentVersion> versions = documentService.getAllVersions(documentId);
            List<DocumentVersionOutput> documentVersionOutputs = new ArrayList<>();
            for (DocumentVersion version : versions) {
                DocumentVersionOutput documentVersionOutput = new DocumentVersionOutput();
                documentVersionOutput.setDocumentId(version.getBelongsToDocument().getId());
                documentVersionOutput.setVersion(version.getVersion());
                documentVersionOutput.setName(version.getFilename());
                documentVersionOutputs.add(documentVersionOutput);
            }
            return ResponseEntity.ok(documentVersionOutputs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.toString());
        }
    }

    // 获取指定版本的文档
    @PostMapping("/projects/versions/specific")
    public ResponseEntity<?> getVersion(@RequestBody SpecificDocument specific_document) {
        try {
            Long documentId = specific_document.getDocumentId();
            String version = specific_document.getVersion();
            DocumentVersion documentVersion = documentService.getVersion(documentId, version);
            return ResponseEntity.ok(documentVersion);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.toString());
        }
    }

    // 下载文档的指定版本
    @PostMapping("/projects/versions/download")
    public ResponseEntity<?> downloadDocumentVersion(@RequestBody SpecificDocument specific_document) {
        try {
            Long documentId = specific_document.getDocumentId();
            String version = specific_document.getVersion();
            byte[] fileData = documentService.downloadDocumentVersion(documentId, version);
            return ResponseEntity.ok()
                    .header("Content-Type", "application/octet-stream")
                    .header("Content-Disposition", "attachment; filename=document-" + version + ".pdf")
                    .body(fileData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.toString());
        }
    }


    // 获取文档的所有共享用户
    @PostMapping("/shared_users")    // TODO: 删除大写
    public ResponseEntity<?> getSharedUsers(@RequestBody DocumentVersionAccess docversion_access) throws Exception {
        try{
            Long documentId = docversion_access.getDocumentId();
            List<String> sharedUserNames = documentService.getSharedUserNames(documentId);
            return ResponseEntity.ok(sharedUserNames);  // 返回共享用户的名称列表
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.toString());
        }
    }

    // 将文档共享给某个用户
    @PostMapping("/share")
    public ResponseEntity<?> shareDocumentWithUser(@RequestBody DocumentVersionAccess docversion_access) throws Exception {
        try {
            Long documentId = docversion_access.getDocumentId();
            Long userId = docversion_access.getUserId();
            List<String> userList = documentService.shareDocumentWithUser(documentId, userId);
            return ResponseEntity.ok(userList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.toString());
        }
    }

    // 删除文档的共享用户
    @PostMapping("/remove_shared_users")
    public ResponseEntity<?> removeSharedUser(@RequestBody DocumentVersionAccess docversion_access) throws Exception {
        try {
            Long documentId = docversion_access.getDocumentId();
            Long userId = docversion_access.getUserId();
            List<String> userList = documentService.removeSharedUser(documentId, userId);
            return ResponseEntity.ok(userList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.toString());
        }
    }
}
