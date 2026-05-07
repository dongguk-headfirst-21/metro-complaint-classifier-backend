package edu.dongguk.complaint.orchestrator.controller;

import edu.dongguk.complaint.orchestrator.dto.response.ComplaintListResponseDto;
import edu.dongguk.complaint.orchestrator.service.query.ComplaintQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/departs")
@RequiredArgsConstructor
public class DepartController {
    private final ComplaintQueryService complaintQueryService;


    @GetMapping("/{departId}")
    public ResponseEntity<ComplaintListResponseDto> getComplaints(@PathVariable Long departId){
        return ResponseEntity.ok(complaintQueryService.getComplaints(departId));
    }
}
