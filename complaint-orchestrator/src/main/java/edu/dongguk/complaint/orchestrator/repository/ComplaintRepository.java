package edu.dongguk.complaint.orchestrator.repository;

import edu.dongguk.complaint.orchestrator.domain.complaint.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    List<Complaint> findAllByFileId(Long fileId);

    @Query("SELECT COUNT(DISTINCT c.depart.id) FROM Complaint c WHERE c.file.id = :fileId AND c.isChecked = true")
    long countCheckedDepartsByFileId(@Param("fileId") Long fileId);

    // 사용자가 선택한 부서에 배부된 민원들의 isChecked를 true로 update하는 query
    @Modifying
    @Query("UPDATE Complaint c SET c.isChecked = true WHERE c.file.id = :fileId AND c.depart.id IN :departIds")
    void checkComplaintsByFileIdAndDepartIds(@Param("fileId") Long fileId, @Param("departIds") List<Long> departIds);
}
