package edu.dongguk.complaint.orchestrator.controller;

import edu.dongguk.complaint.orchestrator.dto.response.ComplaintListResponseDto;
import edu.dongguk.complaint.orchestrator.service.query.ComplaintQueryService;
import edu.dongguk.complaint.orchestrator.service.command.DepartCheckService;
import edu.dongguk.complaint.orchestrator.dto.request.DepartListRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/departs")
@RequiredArgsConstructor
public class DepartController {
    private final ComplaintQueryService complaintQueryService;
    private final DepartCheckService departCheckService;

    @GetMapping("/{departId}")
    public ResponseEntity<ComplaintListResponseDto> getComplaints(
            @PathVariable Long departId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int  size){
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(complaintQueryService.getComplaints(departId,  pageable));
    }
}
