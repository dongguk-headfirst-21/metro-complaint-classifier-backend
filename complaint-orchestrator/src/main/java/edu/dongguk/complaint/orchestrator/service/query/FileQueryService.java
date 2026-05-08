package edu.dongguk.complaint.orchestrator.service.query;

import edu.dongguk.complaint.orchestrator.domain.file.File;
import edu.dongguk.complaint.orchestrator.domain.Depart;
import edu.dongguk.complaint.orchestrator.dto.response.FileListResponseDto;
import edu.dongguk.complaint.orchestrator.dto.response.FileResponseDto;
import edu.dongguk.complaint.orchestrator.dto.response.DepartListComplaintResponseDto;
import edu.dongguk.complaint.orchestrator.dto.response.DepartComplaintResponseDto;
import edu.dongguk.complaint.orchestrator.repository.ComplaintRepository;
import edu.dongguk.complaint.orchestrator.repository.DepartRepository;
import edu.dongguk.complaint.orchestrator.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ArrayList;

@Service
@Transactional
@RequiredArgsConstructor
public class FileQueryService {
    private final FileRepository fileRepository;
    private final ComplaintRepository complaintRepository;
    private final DepartRepository departRepository;

    public FileListResponseDto getfiles() {
        List<File> files = fileRepository.findAll();
        long totalDeparts = departRepository.count();

        List<FileResponseDto> fileResponseDtoList = files.stream()
                .map(file -> {
                    long checkedDeparts = complaintRepository.countCheckedDepartsByFileId(file.getId());
                    String checkedDepartCount = checkedDeparts + "/" + totalDeparts;
                    return FileResponseDto.from(file, checkedDepartCount);
                })
                .toList();

        return new FileListResponseDto(fileResponseDtoList);
    }

    // 특정 파일에 대해 민원 분류 결과를 조회
    public DepartListComplaintResponseDto getFileResult(Long fileId) {

        // 1. 특정 부서에 분류된 민원 count
        List<Object[]> classified = complaintRepository.countComplaintsByDepart(fileId);

        // 2. isChecked==false인 민원이 있는 부서 id
        List<Depart> uncheckedDeparts = complaintRepository.findDepartsWithUnchecked(fileId);

        // 3. 분류되지 않은 민원 count
        Long unclassified = complaintRepository.countUnclassifiedComplaints(fileId);

        // 4. 분류된 민원을 DTO로 반환
        List<DepartComplaintResponseDto> departs = classified.stream()
                .map(row -> {
                    Depart depart = (Depart) row[0];
                    Long count = (Long) row[1];
                    boolean isChecked = !uncheckedDeparts.contains(depart.getId());
                    return DepartComplaintResponseDto.from(depart, count, isChecked);
                })
                .toList();

        // 5. 미분류 그룹 추가
        if (unclassified > 0) {
            departs = new ArrayList<>(departs);
            departs.add(new DepartComplaintResponseDto(null, "미분류", unclassified, false));
        }

        return DepartListComplaintResponseDto.from(departs);
    }
}
