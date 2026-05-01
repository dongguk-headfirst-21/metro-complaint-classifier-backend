package edu.dongguk.complaint.orchestrator.domain.file;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Table(name = "file")
@NoArgsConstructor
@Getter
public class File {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "row_count")
    private int rowCount;

    @Column
    private int capacity;

    @Column(name = "uploaded_at", nullable = false)
    private LocalDateTime uploadedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FileStatus status;

    @Column(name = "error_message")
    private String errorMessage;

    public File(String fileName, int rowCount) {
        this.fileName = fileName;
        this.rowCount = rowCount;
        this.uploadedAt = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        this.status = FileStatus.UPLOADING;
    }
}
