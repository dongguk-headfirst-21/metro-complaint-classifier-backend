package edu.dongguk.complaint.orchestrator.service.kafka;

import edu.dongguk.complaint.orchestrator.domain.complaint.Complaint;
import edu.dongguk.complaint.orchestrator.repository.ComplaintRepository;
import edu.dongguk.complaint.orchestrator.service.sse.SseEmitterService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class ComplaintKafkaConsumer {

    private final ComplaintRepository complaintRepository;
    private final SseEmitterService sseEmitterService;

    @KafkaListener(topics = "complaint-dispatch-complete", groupId = "complaint-group")
    @Transactional
    public void consume(String complaintId) {
        Complaint complaint = complaintRepository.findById(Long.parseLong(complaintId))
                .orElseThrow(NoSuchElementException::new);

        sseEmitterService.notifyComplaintResult(
                complaint.getId(),
                complaint.getDepart() != null ? complaint.getDepart().getId() : null,
                complaint.getProcessCodeType() != null ? complaint.getProcessCodeType().getCode() : null,
                complaint.getFailureReason()
        );
    }
}
