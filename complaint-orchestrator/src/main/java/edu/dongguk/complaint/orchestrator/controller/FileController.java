package edu.dongguk.complaint.orchestrator.controller;

import edu.dongguk.complaint.orchestrator.dto.request.DepartListRequestDto;
import edu.dongguk.complaint.orchestrator.dto.response.FileListResponseDto;
import edu.dongguk.complaint.orchestrator.service.command.DepartCheckService;
import edu.dongguk.complaint.orchestrator.service.command.FileUploadService;
import edu.dongguk.complaint.orchestrator.service.query.FileQueryService;
import edu.dongguk.complaint.orchestrator.service.sse.SseEmitterService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {
    private final FileQueryService fileQueryService;
    private final FileUploadService fileUploadService;
    private final SseEmitterService sseEmitterService;
    private final DepartCheckService departCheckService;


    @GetMapping
    public ResponseEntity<FileListResponseDto> getFiles() {
        return ResponseEntity.ok(fileQueryService.getfiles());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(fileUploadService.uploadFile(file));
    }

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe() {
        return sseEmitterService.subscribe();
    }

    @PatchMapping(value = "/{fileId}/departs/check")
    public ResponseEntity<Void> checkDeparts(
            @PathVariable Long fileId,
            @RequestBody DepartListRequestDto requestDto
    ) {
        departCheckService.checkDeparts(fileId, requestDto);
        return ResponseEntity.ok().build();
    }
}
