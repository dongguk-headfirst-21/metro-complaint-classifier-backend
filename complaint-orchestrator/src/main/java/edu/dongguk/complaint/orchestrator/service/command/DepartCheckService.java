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

        // 모든 부서 체크 완료 시 file status를 COMPLETE로 변경
        long totalDeparts = complaintRepository.countDistinctDepartsByFileId(fileId);
        long checkedDeparts = complaintRepository.countCheckedDepartsByFileId(fileId);

        if (totalDeparts > 0 && totalDeparts == checkedDeparts) {
            File file = fileRepository.findById(fileId)
                    .orElseThrow(NoSuchElementException::new);
            file.updateStatus(FileStatus.COMPLETED);
        }
    }

    public void uncheckDeparts(Long fileId, DepartListRequestDto requestDto) {
        complaintRepository.uncheckComplaintsByFileIdAndDepartIds(fileId, requestDto.departIds());

        // 부서 체크 해제 시 file status를 PENDING로 변경
        File file = fileRepository.findById(fileId)
                .orElseThrow(NoSuchElementException::new);
        if (file.getStatus() == FileStatus.COMPLETED) {
            file.updateStatus(FileStatus.PENDING);
        }
    }
}
