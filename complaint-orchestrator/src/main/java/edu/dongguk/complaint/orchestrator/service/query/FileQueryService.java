package edu.dongguk.complaint.orchestrator.service.query;

import edu.dongguk.complaint.orchestrator.dto.response.FileListResponseDto;
import edu.dongguk.complaint.orchestrator.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FileQueryService {
    private final FileRepository fileRepository;

    public FileListResponseDto getfiles() {
        return FileListResponseDto.from(fileRepository.findAll());
    }
}
