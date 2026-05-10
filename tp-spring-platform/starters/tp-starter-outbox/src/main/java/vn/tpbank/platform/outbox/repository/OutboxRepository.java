package vn.tpbank.platform.outbox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.tpbank.platform.outbox.entity.OutboxEvent;

import java.util.List;

public interface OutboxRepository extends JpaRepository<OutboxEvent, Long> {
    List<OutboxEvent> findByProcessedFalseOrderByCreatedAtAsc();
}
