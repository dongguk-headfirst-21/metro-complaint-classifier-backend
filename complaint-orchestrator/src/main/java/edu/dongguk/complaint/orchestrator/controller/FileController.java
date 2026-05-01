package edu.dongguk.complaint.orchestrator.controller;

import edu.dongguk.complaint.orchestrator.dto.response.FileListResponseDto;
import edu.dongguk.complaint.orchestrator.service.query.FileQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {
    private final FileQueryService fileQueryService;

    @GetMapping("")
    public ResponseEntity<FileListResponseDto> getFiles() {
        return ResponseEntity.ok(fileQueryService.getfiles());
    }

}
