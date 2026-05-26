package edu.dongguk.complaint.orchestrator.service.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ComplaintKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendDispatchRequest(Long complaintId) {
        kafkaTemplate.send("complaint-dispatch", complaintId.toString());
    }
}
