package edu.dongguk.complaint.orchestrator.domain;

import edu.dongguk.complaint.orchestrator.domain.complaint.Complaint;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "depart")
@NoArgsConstructor
@Getter
public class Depart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "depart_name", nullable = false, length = 50)
    private String departName;

    @OneToMany(mappedBy = "depart")
    private List<Feature> features = new ArrayList<>();

    @OneToMany(mappedBy = "depart")
    private List<Complaint> complaints = new ArrayList<>();
}