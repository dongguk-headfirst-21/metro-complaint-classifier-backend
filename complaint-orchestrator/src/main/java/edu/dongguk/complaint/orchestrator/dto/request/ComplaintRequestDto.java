package edu.dongguk.complaint.orchestrator.dto.request;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotBlank;

public record ComplaintRequestDto(
        @NotBlank
        String title,

        @Lob
        @NotBlank
        String content
) {
}
