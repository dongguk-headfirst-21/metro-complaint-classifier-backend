package edu.dongguk.complaint.orchestrator.domain.complaint;

import edu.dongguk.complaint.orchestrator.domain.Depart;
import edu.dongguk.complaint.orchestrator.domain.file.File;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "complaint")
@NoArgsConstructor
@Getter
public class Complaint {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String title;

    @Column(nullable = false, length = 2000)
    private String content;

    @Column
    private Short code;

    @Column(name = "is_checked", nullable = false)
    private boolean isChecked;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ComplaintStatus status;

    @Column(name = "failure_reason", length = 100)
    private String failureReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", nullable = false)
    private File file;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "depart_id")
    private Depart depart;

    @Builder
    public Complaint(String title, String content, File file) {
        this.title = title;
        this.content = content;
        this.file = file;
        this.status = ComplaintStatus.PENDING;
        this.isChecked = false;
    }
}
