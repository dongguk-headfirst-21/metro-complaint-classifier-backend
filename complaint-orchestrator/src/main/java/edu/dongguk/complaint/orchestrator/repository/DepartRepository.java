package edu.dongguk.complaint.orchestrator.repository;

import edu.dongguk.complaint.orchestrator.domain.Depart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartRepository extends JpaRepository<Depart, Long> {
}
