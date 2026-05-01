package edu.dongguk.complaint.orchestrator.dto.response;

import edu.dongguk.complaint.orchestrator.domain.file.File;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record FileResponseDto(
        @NotNull
        Long id,

        @NotBlank
        String name,

        @NotNull
        int capacity,

        int complaintCount,

        @NotNull
        LocalDateTime uploadedAt
) {
    public static FileResponseDto from(File file) {
        return new FileResponseDto(
                file.getId(),
                file.getFileName(),
                file.getCapacity(),
                file.getRowCount(),
                file.getUploadedAt()
        );
    }
}
