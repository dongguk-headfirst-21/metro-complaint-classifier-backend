package edu.dongguk.complaint.orchestrator.dto.response;

import java.util.List;

public record ComplaintListResponseDto(List<ComplaintResponseDto> complaints, Boolean hasNext) {
}
