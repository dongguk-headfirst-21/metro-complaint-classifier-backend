package edu.dongguk.complaint.orchestrator.service.query;

import edu.dongguk.complaint.orchestrator.domain.file.File;
import edu.dongguk.complaint.orchestrator.dto.response.FileListResponseDto;
import edu.dongguk.complaint.orchestrator.dto.response.FileResponseDto;
import edu.dongguk.complaint.orchestrator.repository.ComplaintRepository;
import edu.dongguk.complaint.orchestrator.repository.DepartRepository;
import edu.dongguk.complaint.orchestrator.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
}
