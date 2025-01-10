package org.flitter.backend.service;

import org.flitter.backend.config.SecurityConfig;
import org.flitter.backend.entity.Document;
import org.flitter.backend.entity.Message;
import org.flitter.backend.entity.User;
import org.flitter.backend.repository.MessageRepository;
import org.flitter.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.flitter.backend.repository.PreMessageRepository;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.MessagingException;
import org.flitter.backend.entity.PreSendingMessage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import org.flitter.backend.repository.TaskRepository;
import org.flitter.backend.entity.Task;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
public class MessageService {
    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private JavaMailSender mailSender;  // 自动注入 JavaMailSender

    @Autowired
    private PreMessageRepository preMessageRepositoryRepository;

    @Autowired
    private TaskRepository taskRepository;

    // 发送站内消息
    public List<Message> sendMessage(List<Long> receiverID, List<String> messageContent) throws Exception {
        User currentUser = securityConfig.getCurrentUser();
        for (String content : messageContent) {
            if (content == null) {
                throw new Exception("没有消息");
            }
        }

        for (Long id : receiverID) {
            User receiver = userRepository.findById(id).orElse(null);
            if (receiver == null) {
                throw new Exception("找不到用户，ID: " + id);
            }
        }
        List<Message> messageList = new ArrayList<Message>();
        // 创建一个消息对象
        for (int i = 0; i < receiverID.size(); i++) {
            Long receiverid = receiverID.get(i);
            String content = messageContent.get(i);
            User receiver = userRepository.findById(receiverid).orElse(null);
            Message message = new Message();
            message.setAuthor(currentUser);
            message.setReceiver(receiver);
            message.setMessage(content);
            message.setDatetime(LocalDateTime.now());
            message.setIsRead("false"); // 默认设置为未读
            messageRepository.save(message);
            messageList.add(message);
        }
        // 站内通信，消息已保存，不需要发邮件
        return messageList;
    }

    // 获取接收方的所有未读消息
    public List<Message> getUnreadMessages() throws Exception {
        User user = securityConfig.getCurrentUser();
        Long receiverId = user.getId();
        if (receiverId == null) {
            throw new Exception("没有该用户");
        }
        return messageRepository.findByReceiverIdAndIsRead(receiverId, "false");
    }

    //标记消息为已读
    public Optional<Message> markAsRead(Long messageId) throws Exception {
        // 检查消息ID和接收者ID是否为空
        User user = securityConfig.getCurrentUser();
        Long receiverId = user.getId();
        if (messageId == null || receiverId == null) {
            throw new Exception("消息ID或接收者ID不能为空");
        }

        // 查找消息
        Optional<Message> message = messageRepository.findByIdAndReceiverId(messageId, receiverId);
        if (message.isPresent()) {
            // 如果找到消息，更新为已读
            Message msg = message.get();
            msg.setIsRead("true");
            return Optional.of(messageRepository.save(msg));
        } else {
            // 如果未找到消息，返回空
            return Optional.empty();
        }
    }

    // 获取接收方的所有消息（包括已读和未读）
    public List<Message> getMessages() throws Exception{
        User user = securityConfig.getCurrentUser();
        Long receiverId = user.getId();
        if (receiverId == null) {
            throw new Exception("没有该用户");
        }
        return messageRepository.findByReceiverId(receiverId);
    }

    //获取任务截止的用户名
    @Scheduled(cron = "0 0 0 * * ?")  // 每天午夜执行
    public void checkAndSendMessages() throws Exception{
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threeDaysLater = now.plusDays(3);

        // 查询截止时间在未来 3 天内的所有任务
        List<PreSendingMessage> tasks = preMessageRepositoryRepository.findByTimeBetween(now, threeDaysLater);

        // 遍历任务，发送通知给相应的用户
        for (PreSendingMessage task : tasks) {
            Task temp_task = taskRepository.findById(task.getTaskId()).orElse(null);
            if (temp_task == null){
                continue;
            }
            sendNotifications(task);
        }
    }

    private void sendNotifications(PreSendingMessage task) throws Exception{
        String[] userIds = task.getUserIds().split(",");
        List<Long> users = new ArrayList<>();
        for (String userId : userIds) {
            users.add(Long.parseLong(userId));
        }
        List<String> messages = new ArrayList<>();
        for (String userId : userIds) {
            String message = "Your task with ID " + task.getTaskId() + " is due in 3 days.";
            messages.add(message);
        }
        sendMessage(users, messages);
    }

//    // 发送邮件通知的逻辑
//    private void sendEmailNotification(String to, String subject, String text) {
//        try {
//            // 创建一个 MimeMessage 对象
//            MimeMessage mimeMessage = mailSender.createMimeMessage();
//
//            // 创建 MimeMessageHelper 对象
//            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
//            helper.setTo(to);  // 设置收件人
//            helper.setSubject(subject);  // 设置邮件主题
//            helper.setText(text, true);  // 设置邮件内容（true 表示支持 HTML 格式）
//
//            // 发送邮件
//            mailSender.send(mimeMessage);
//        } catch (MessagingException e) {
//            e.printStackTrace();  // 异常处理，可以根据需要做日志记录或者其他处理
//        }
//    }
}
