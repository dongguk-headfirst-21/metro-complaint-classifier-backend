package edu.dongguk.complaint.orchestrator.service.command;

import edu.dongguk.complaint.orchestrator.dto.request.DepartListRequestDto;
import edu.dongguk.complaint.orchestrator.repository.ComplaintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DepartCheckService {

    private final ComplaintRepository complaintRepository;

    public void checkDeparts(Long fileId, DepartListRequestDto requestDto){
        complaintRepository.checkComplaintsByFileIdAndDepartIds(fileId, requestDto.departIds());
    }
}
