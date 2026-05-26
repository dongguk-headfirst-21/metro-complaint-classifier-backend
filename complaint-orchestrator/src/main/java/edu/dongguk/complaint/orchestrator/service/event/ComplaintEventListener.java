package edu.dongguk.complaint.orchestrator.service.event;

import edu.dongguk.complaint.orchestrator.event.ComplaintCreatedEvent;
import edu.dongguk.complaint.orchestrator.service.kafka.ComplaintKafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ComplaintEventListener {

    private final ComplaintKafkaProducer complaintKafkaProducer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleComplaintCreated(ComplaintCreatedEvent event) {
        complaintKafkaProducer.sendDispatchRequest(event.complaintId());
    }
}
