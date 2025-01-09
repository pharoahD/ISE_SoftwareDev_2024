package org.flitter.backend.controller;

import lombok.Data;
import org.flitter.backend.config.SecurityConfig;
import org.flitter.backend.entity.Message;
import org.flitter.backend.entity.User;
import org.flitter.backend.repository.UserRepository;
import org.flitter.backend.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;  //我爱你
    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private UserRepository userRepository;
    private SendMessage message;

    @Data
    static private class SendMessage{
        private List<Long> receiverId;
        private List<String> message;
    }

    @Data
    static private class ModifyMessage{
        private Long messageId;
    }
    // 发送消息通知（任务经理可以分配任务后，调用即自动发送）
    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody SendMessage message) {
        try {
            List<Long> receiverId = message.getReceiverId();
            List<String> messageContent = message.getMessage();
            List<Message> remessage = messageService.sendMessage(receiverId, messageContent);
            return ResponseEntity.ok(remessage);  // 包装网络请求
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.toString());
        }
    }

    // 获取所有未读消息(点击显示未读消息后立即显示未读消息 / 把未读消息放在已读消息前面)
    @PostMapping("/unread")
    public ResponseEntity<?> getUnreadMessages() {
        try {
            List<Message> unreadMessage = messageService.getUnreadMessages();
            return ResponseEntity.ok(unreadMessage);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.toString());
        }
    }

    // 标记消息为已读 (点击消息后设置消息为已读)
    @PostMapping("/markAsRead")
    public ResponseEntity<?> markAsRead(@RequestBody ModifyMessage message) {
        try {
            Long messageId = message.getMessageId();
            Optional<Message> remessage = messageService.markAsRead(messageId);
            if (remessage.isPresent()) {
                return ResponseEntity.ok(remessage.get());
            } else {
                return ResponseEntity.badRequest().body("未找到指定的消息或消息已被删除");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("错误: " + e.getMessage());
        }
    }

    // 获取所有消息（包括已读和未读）(点击后立即展现当前登录用户的)
    @PostMapping("/all")
    public ResponseEntity<?> getAllMessages() {
        try {
            List<Message> unreadMessage = messageService.getMessages();
            return ResponseEntity.ok(unreadMessage);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.toString());
        }
    }

    @PostMapping("/task_end_sending")
    public ResponseEntity<?> taskEndSending() {
        try {
            //messageService.checkAndSendMessages();
            return ResponseEntity.ok("发送成功");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.toString());
        }
    }
}
