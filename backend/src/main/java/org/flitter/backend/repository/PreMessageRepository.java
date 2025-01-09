package org.flitter.backend.repository;

import org.flitter.backend.entity.PreSendingMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.time.LocalDateTime;

public interface PreMessageRepository extends JpaRepository<PreSendingMessage, Long> {
    List<PreSendingMessage> findByTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
}
