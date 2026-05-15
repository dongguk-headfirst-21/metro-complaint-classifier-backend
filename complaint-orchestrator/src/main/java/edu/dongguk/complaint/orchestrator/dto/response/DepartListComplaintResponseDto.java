package edu.dongguk.complaint.orchestrator.dto.response;

import java.util.List;

public record DepartListComplaintResponseDto(
        List<DepartComplaintResponseDto> departs
) {
    public static DepartListComplaintResponseDto from(List<DepartComplaintResponseDto> departs) {
        return new DepartListComplaintResponseDtodeparts();
    }
}