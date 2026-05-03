package edu.dongguk.complaint.orchestrator.service.command;

import edu.dongguk.complaint.orchestrator.domain.complaint.Complaint;
import edu.dongguk.complaint.orchestrator.domain.file.File;
import edu.dongguk.complaint.orchestrator.domain.file.FileStatus;
import edu.dongguk.complaint.orchestrator.global.util.ComplaintData;
import edu.dongguk.complaint.orchestrator.global.util.ExcelParser;
import edu.dongguk.complaint.orchestrator.repository.ComplaintRepository;
import edu.dongguk.complaint.orchestrator.repository.FileRepository;
import edu.dongguk.complaint.orchestrator.service.kafka.FileKafkaProducer;
import edu.dongguk.complaint.orchestrator.service.sse.SseEmitterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FileUploadService {

    private final FileRepository fileRepository;
    private final ComplaintRepository complaintRepository;
    private final FileKafkaProducer kafkaProducer;
    private final SseEmitterService sseEmitterService;
    private final ExcelParser excelParser;

    public Long uploadFile(MultipartFile multipartFile) throws IOException {
        File file = new File(multipartFile.getOriginalFilename(), 0);
        file.updateCapacity(multipartFile.getSize());
        fileRepository.save(file);

        List<ComplaintData> dataList = excelParser.parse(multipartFile);
        List<Complaint> complaints = dataList.stream()
                .map(data -> new Complaint(data.getTitle(), data.getContent(), file))
                .toList();
        complaintRepository.saveAll(complaints);

        file.updateStatus(FileStatus.PENDING);
        file.updateRowCount(complaints.size());
        fileRepository.save(file);

        sseEmitterService.notify(file.getId(), FileStatus.PENDING);

        kafkaProducer.sendAnalysisRequest(file.getId());

        return file.getId();
    }
}