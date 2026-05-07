package edu.dongguk.complaint.orchestrator.repository;

import edu.dongguk.complaint.orchestrator.domain.complaint.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    List<Complaint> findAllByFileId(Long fileId);
    List<Complaint> findByDepartId(Long departId);

    @Query("SELECT COUNT(DISTINCT c.depart.id) FROM Complaint c WHERE c.file.id = :fileId AND c.isChecked = true")
    long countCheckedDepartsByFileId(@Param("fileId") Long fileId);
}
