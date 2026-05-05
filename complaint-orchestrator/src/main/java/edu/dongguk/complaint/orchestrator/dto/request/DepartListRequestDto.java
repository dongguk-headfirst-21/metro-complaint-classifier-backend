package edu.dongguk.complaint.orchestrator.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record DepartListRequestDto(
        @NotNull
        List<Long> departIds
) {
}
