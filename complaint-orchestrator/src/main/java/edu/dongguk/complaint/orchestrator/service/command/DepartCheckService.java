package edu.dongguk.complaint.orchestrator.service.command;

import edu.dongguk.complaint.orchestrator.domain.file.File;
import edu.dongguk.complaint.orchestrator.domain.file.FileStatus;
import edu.dongguk.complaint.orchestrator.dto.request.DepartListRequestDto;
import edu.dongguk.complaint.orchestrator.repository.ComplaintRepository;
import edu.dongguk.complaint.orchestrator.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class DepartCheckService {

    private final ComplaintRepository complaintRepository;
    private final FileRepository fileRepository;

    public void checkDeparts(Long fileId, DepartListRequestDto requestDto){
        complaintRepository.checkComplaintsByFileIdAndDepartIds(fileId, requestDto.departIds());
    }

    public void uncheckDeparts(Long fileId, DepartListRequestDto requestDto) {
        complaintRepository.uncheckComplaintsByFileIdAndDepartIds(fileId, requestDto.departIds());
    }
}
