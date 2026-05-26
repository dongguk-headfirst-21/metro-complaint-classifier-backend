package edu.dongguk.complaint.orchestrator.service.command;

import edu.dongguk.complaint.orchestrator.domain.complaint.Complaint;
import edu.dongguk.complaint.orchestrator.dto.request.ComplaintRequestDto;
import edu.dongguk.complaint.orchestrator.event.ComplaintCreatedEvent;
import edu.dongguk.complaint.orchestrator.repository.ComplaintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ComplaintCreateService {
    private final ComplaintRepository complaintRepository;
    private final ApplicationEventPublisher eventPublisher;

    public Long createComplaint(ComplaintRequestDto complaintRequestDto) {
        Complaint complaint = new Complaint(complaintRequestDto.title(), complaintRequestDto.content(), null);
        complaintRepository.save(complaint);
        eventPublisher.publishEvent(new ComplaintCreatedEvent(complaint.getId()));
        return complaint.getId();
    }
}
