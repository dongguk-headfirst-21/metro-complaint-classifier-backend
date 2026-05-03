package edu.dongguk.complaint.orchestrator.repository;

import edu.dongguk.complaint.orchestrator.domain.complaint.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    List<Complaint> findAllByFileId(Long fileId);

    @Query("SELECT COUNT(DISTINCT c.depart.id) FROM Complaint c WHERE c.file.id = :fileId AND c.isChecked = true")
    long countCheckedDepartsByFileId(@Param("fileId") Long fileId);

    // 부서별로 분류된 민원 개수 반환
    @Query("SELECT c.depart, COUNT(c) FROM Complaint c WHERE c.file.id = :fileId AND c.depart IS NOT NULL GROUP BY c.depart")
    List<Object[]> countComplaintsByDepart(@Param("fileId") Long fileId);

    // 특정 부서로 분류되지 않은 민원 개수 반환
    @Query("SELECT COUNT(c) FROM Complaint c WHERE c.file.id = :fileId AND c.depart IS NULL")
    Long countUnclassifiedComplaints(@Param("fileId") Long fileId);
}
