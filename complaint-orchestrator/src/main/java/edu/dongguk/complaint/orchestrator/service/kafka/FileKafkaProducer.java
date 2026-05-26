package edu.dongguk.complaint.orchestrator.service.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FileKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendAnalysisRequest(Long fileId) {
        kafkaTemplate.send("file-dispatch", fileId.toString());
    }
}
