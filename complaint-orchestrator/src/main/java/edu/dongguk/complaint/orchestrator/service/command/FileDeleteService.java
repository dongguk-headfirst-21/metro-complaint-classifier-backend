package edu.dongguk.complaint.orchestrator.service.command;

import edu.dongguk.complaint.orchestrator.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FileDeleteService {
    private final FileRepository  fileRepository;

    public void deleteFile(Long id){
        fileRepository.deleteById(id);
    }
}
