package org.flitter.backend.repository;

import org.flitter.backend.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByReceiverIdAndIsRead(Long receiverId, String isRead);
    Optional<Message> findByIdAndReceiverId(Long messageId, Long receiverId);
    List<Message> findByReceiverId(Long receiverId);
}
