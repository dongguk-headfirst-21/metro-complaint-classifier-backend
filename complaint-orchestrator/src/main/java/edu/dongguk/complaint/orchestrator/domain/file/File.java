package edu.dongguk.complaint.orchestrator.domain.file;

import edu.dongguk.complaint.orchestrator.domain.complaint.Complaint;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

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
    private long capacity;

    @Column(name = "uploaded_at", nullable = false)
    private LocalDateTime uploadedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FileStatus status;

    @Column(name = "error_message")
    private String errorMessage;

    @OneToMany(mappedBy = "file", cascade = CascadeType.ALL)
    private List<Complaint> complaints = new ArrayList<>();

    @Builder
    public File(String fileName, int rowCount) {
        this.fileName = fileName;
        this.rowCount = rowCount;
        this.uploadedAt = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        this.status = FileStatus.UPLOADING;
    }

    public void updateStatus(FileStatus status) {
        this.status = status;
    }

    public void updateRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public void updateCapacity(long capacity) {
        this.capacity = capacity;
    }
}
