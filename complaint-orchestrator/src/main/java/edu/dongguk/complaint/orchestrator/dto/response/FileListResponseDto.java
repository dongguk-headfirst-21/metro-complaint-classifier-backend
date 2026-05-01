package edu.dongguk.complaint.orchestrator.dto.response;

import edu.dongguk.complaint.orchestrator.domain.file.File;

import java.util.List;

public record FileListResponseDto(
        List<FileResponseDto> files
) {
    public static FileListResponseDto from(List<File> files) {
        return new FileListResponseDto(
                files.stream()
                        .map(FileResponseDto::from)
                        .toList()
        );
    }
}
