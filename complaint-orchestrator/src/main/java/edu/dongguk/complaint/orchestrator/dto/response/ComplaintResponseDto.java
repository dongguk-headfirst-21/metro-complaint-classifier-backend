package edu.dongguk.complaint.orchestrator.dto.response;

import edu.dongguk.complaint.orchestrator.domain.ProcessCodeType;
import edu.dongguk.complaint.orchestrator.domain.complaint.Complaint;
import jakarta.validation.constraints.NotNull;

public record ComplaintResponseDto(
        @NotNull
        String title,

        @NotNull
        String content,

        @NotNull
        ProcessCodeType code
) {
    public static ComplaintResponseDto from(Complaint complaint) {
        return new ComplaintResponseDto(
                complaint.getTitle(),
                complaint.getContent(),
                complaint.getProcessCodeType()
        );
    }
}
