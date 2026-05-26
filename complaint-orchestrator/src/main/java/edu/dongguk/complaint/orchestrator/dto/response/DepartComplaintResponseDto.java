package edu.dongguk.complaint.orchestrator.dto.response;

import edu.dongguk.complaint.orchestrator.domain.Depart;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DepartComplaintResponseDto(
        @NotNull
        Long departId,

        @NotBlank
        String name,

        @NotNull
        Long row,

        boolean isChecked
) {
    public static DepartComplaintResponseDto from(Depart depart, Long row, boolean isChecked) {
        return new DepartComplaintResponseDto(
                depart.getId(),
                depart.getDepartName(),
                row,
                isChecked
        );
    }
}