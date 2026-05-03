package edu.dongguk.complaint.orchestrator.dto.response;

import edu.dongguk.complaint.orchestrator.domain.file.File;
import edu.dongguk.complaint.orchestrator.domain.file.FileStatus;
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
        long capacity,

        int complaintCount,

        @NotNull
        LocalDateTime uploadedAt,

        @NotNull
        FileStatus status,

        @NotNull
        String checkedDepartCount
) {
    public static FileResponseDto from(File file, String checkedDepartCount) {
        return new FileResponseDto(
                file.getId(),
                file.getFileName(),
                file.getCapacity(),
                file.getRowCount(),
                file.getUploadedAt(),
                file.getStatus(),
                checkedDepartCount
        );
    }
}
