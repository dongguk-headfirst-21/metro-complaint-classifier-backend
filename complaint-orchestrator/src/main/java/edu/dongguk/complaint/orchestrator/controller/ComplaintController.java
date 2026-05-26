package edu.dongguk.complaint.orchestrator.controller;

import edu.dongguk.complaint.orchestrator.dto.request.ComplaintRequestDto;
import edu.dongguk.complaint.orchestrator.service.command.ComplaintCreateService;
import edu.dongguk.complaint.orchestrator.service.sse.SseEmitterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("complaints")
@RequiredArgsConstructor
public class ComplaintController {

    private final ComplaintCreateService complaintCreateService;
    private final SseEmitterService sseEmitterService;

    @PostMapping
    public ResponseEntity<Long> createComplaint(@RequestBody @Valid ComplaintRequestDto complaintRequestDto) {
        return ResponseEntity.ok(complaintCreateService.createComplaint(complaintRequestDto));
    }

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe() {
        return sseEmitterService.subscribe();
    }
}
