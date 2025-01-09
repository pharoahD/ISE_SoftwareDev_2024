package org.flitter.backend.service;

import org.flitter.backend.config.SecurityConfig;
import org.flitter.backend.entity.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.flitter.backend.dto.CommentCreateDTO;
import org.flitter.backend.repository.DocumentRepository;
import org.flitter.backend.repository.TaskRepository;
import org.flitter.backend.repository.UserRepository;
import org.flitter.backend.repository.CommentRepository;
import org.flitter.backend.entity.CommentAdapter;
import org.flitter.backend.entity.enums.CommentType;
import org.springframework.web.multipart.MultipartFile;
import org.flitter.backend.repository.DocumentVersionRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    private final SecurityConfig securityConfig;
    private final CommentRepository commentRepository;
    private final DocumentRepository documentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final DocumentVersionRepository documentVersionRepository;

    @Autowired
    public CommentService(SecurityConfig securityConfig,
                            CommentRepository commentRepository,
                          DocumentRepository documentRepository,
                          TaskRepository taskRepository,
                          UserRepository userRepository,
                          DocumentVersionRepository documentVersionRepository) {
        this.securityConfig = securityConfig;
        this.commentRepository = commentRepository;
        this.documentRepository = documentRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.documentVersionRepository = documentVersionRepository;
    }

    // 添加字符串评论
    public CommentAdapter addStringComment(CommentCreateDTO commentCreateDTO) throws Exception {
        // 验证任务是否存在
        Task task = taskRepository.findById(commentCreateDTO.getBelongedTask().getId())
                .orElseThrow(() -> new Exception("任务不存在"));
        User publisher = securityConfig.getCurrentUser();
        // 创建字符串评论
        if (commentCreateDTO.getStringComment() == null || commentCreateDTO.getStringComment().isEmpty()) {
            throw new Exception("the comment is empty");
        }
        CommentAdapter comment = new CommentAdapter();
        comment.setType(CommentType.COMMENT);
        comment.setStringComment(commentCreateDTO.getStringComment());
        comment.setPublisher(publisher);
        comment.setDatetime(LocalDateTime.now());
        comment.setBelongedTask(task);
        commentRepository.save(comment);
        // 保存评论
        return comment;
    }

    //添加文件
    public CommentAdapter uploadNewVersion(Long documentId, Long taskId, String version, MultipartFile file) throws Exception {
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

        // 创建comment
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new Exception("任务不存在"));
        User publisher = securityConfig.getCurrentUser();
        CommentAdapter comment = new CommentAdapter();
        comment.setType(CommentType.DOCUMENT);
        comment.setPublisher(publisher);
        comment.setDatetime(LocalDateTime.now());
        comment.setBelongedTask(task);
        comment.setFileId(documentVersionRepository.findByFilename(documentVersion.getFilename()).getId());
        comment.setFilename(documentVersion.getFilename());
        commentRepository.save(comment);


        return comment;
    }

    public List<CommentAdapter> getAllComments(Long id) {
        Task task=taskRepository.findById(id).orElse(null);
        if(task==null) throw new IllegalArgumentException("没有找到对应的任务");
        return commentRepository.findByBelongedTask(task);
    }
}
