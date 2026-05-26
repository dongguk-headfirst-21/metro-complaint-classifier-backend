package edu.dongguk.complaint.orchestrator.service.kafka;

import edu.dongguk.complaint.orchestrator.domain.complaint.Complaint;
import edu.dongguk.complaint.orchestrator.domain.complaint.ComplaintStatus;
import edu.dongguk.complaint.orchestrator.domain.file.File;
import edu.dongguk.complaint.orchestrator.domain.file.FileStatus;
import edu.dongguk.complaint.orchestrator.event.FileDispatchCompleteEvent;
import edu.dongguk.complaint.orchestrator.repository.ComplaintRepository;
import edu.dongguk.complaint.orchestrator.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class FileKafkaConsumer {

    private final FileRepository fileRepository;
    private final ComplaintRepository complaintRepository;
    private final ApplicationEventPublisher eventPublisher;

    @KafkaListener(topics = "file-dispatch-complete", groupId = "complaint-group")
    @Transactional
    public void consume(String fileId) {
        Long id = Long.parseLong(fileId);
        List<Complaint> complaints = complaintRepository.findAllByFileId(id);

        boolean hasError = complaints.stream()
                .anyMatch(c -> c.getStatus() != ComplaintStatus.COMPLETED);

        FileStatus newStatus = hasError ? FileStatus.ERROR : FileStatus.COMPLETED;

        File file = fileRepository.findById(id).orElseThrow(NoSuchElementException::new);

        if(file.getStatus() != FileStatus.ERROR)
            file.updateStatus(newStatus);
        else
            newStatus = FileStatus.ERROR;

        eventPublisher.publishEvent(new FileDispatchCompleteEvent(id, newStatus));
    }
}