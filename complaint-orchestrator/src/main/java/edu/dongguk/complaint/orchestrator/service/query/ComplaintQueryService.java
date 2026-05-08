package edu.dongguk.complaint.orchestrator.service.query;


import edu.dongguk.complaint.orchestrator.domain.complaint.Complaint;
import edu.dongguk.complaint.orchestrator.dto.response.ComplaintListResponseDto;
import edu.dongguk.complaint.orchestrator.dto.response.ComplaintResponseDto;
import edu.dongguk.complaint.orchestrator.repository.ComplaintRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ComplaintQueryService {
    private final ComplaintRepository complaintRepository;

    public ComplaintListResponseDto getComplaints(Long departId, Pageable pageable) {

        Slice<Complaint> slice = complaintRepository.findByDepartId(departId, pageable);

        List<ComplaintResponseDto> complaints = slice
                .stream()
                .map(complaint -> ComplaintResponseDto.from(complaint))
                .toList();
        return new ComplaintListResponseDto(complaints, slice.hasNext());
    }
}
