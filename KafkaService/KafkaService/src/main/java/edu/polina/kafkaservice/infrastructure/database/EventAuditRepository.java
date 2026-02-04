package edu.polina.kafkaservice.infrastructure.database;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventAuditRepository extends JpaRepository<EventAudit, Long> {

    Optional<EventAudit> findByEventId(String eventId);

}
