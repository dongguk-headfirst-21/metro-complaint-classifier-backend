package edu.dongguk.complaint.orchestrator.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "process_code_type")
@Getter
@NoArgsConstructor
public class ProcessCodeType {
    @Id
    private Integer code;

    @Column(nullable = false, length = 100)
    private String text;
}
