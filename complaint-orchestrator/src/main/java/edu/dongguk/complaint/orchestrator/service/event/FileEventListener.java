package edu.dongguk.complaint.orchestrator.service.event;

import edu.dongguk.complaint.orchestrator.domain.file.FileStatus;
import edu.dongguk.complaint.orchestrator.event.FileDispatchCompleteEvent;
import edu.dongguk.complaint.orchestrator.event.FileUploadedEvent;
import edu.dongguk.complaint.orchestrator.service.kafka.FileKafkaProducer;
import edu.dongguk.complaint.orchestrator.service.sse.SseEmitterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class FileEventListener {

    private final FileKafkaProducer fileKafkaProducer;
    private final SseEmitterService sseEmitterService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleFileUploaded(FileUploadedEvent event) {
        sseEmitterService.notify(event.fileId(), FileStatus.PENDING);
        fileKafkaProducer.sendAnalysisRequest(event.fileId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleFileDispatchComplete(FileDispatchCompleteEvent event) {
        sseEmitterService.notify(event.fileId(), event.status());
    }
}
