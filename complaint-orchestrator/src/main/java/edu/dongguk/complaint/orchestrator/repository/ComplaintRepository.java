package edu.dongguk.complaint.orchestrator.repository;

import edu.dongguk.complaint.orchestrator.domain.Depart;
import edu.dongguk.complaint.orchestrator.domain.complaint.Complaint;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    List<Complaint> findAllByFileId(Long fileId);
    Slice<Complaint> findByDepartId(Long departId, Pageable pageable);

    @Query("SELECT COUNT(DISTINCT c.depart.id) FROM Complaint c WHERE c.file.id = :fileId AND c.isChecked = true")
    long countCheckedDepartsByFileId(@Param("fileId") Long fileId);

    // 사용자가 선택한 부서에 배부된 민원들의 isChecked를 true로 update하는 query
    @Modifying
    @Query("UPDATE Complaint c SET c.isChecked = true WHERE c.file.id = :fileId AND c.depart.id IN :departIds")
    void checkComplaintsByFileIdAndDepartIds(@Param("fileId") Long fileId, @Param("departIds") List<Long> departIds);
    // 부서별로 분류된 전체 민원 개수 반환
    @Query("SELECT c.depart, COUNT(c) FROM Complaint c WHERE c.file.id = :fileId AND c.depart IS NOT NULL GROUP BY c.depart")
    List<Object[]> countComplaintsByDepart(@Param("fileId") Long fileId);

    // 부서별로 분류된 민원 중 isChecked==false인 민원 반환
    @Query("SELECT c.depart FROM Complaint c WHERE c.file.id = :fileId AND c.depart IS NOT NULL AND c.isChecked = false GROUP BY c.depart")
    List<Depart> findDepartsWithUnchecked(@Param("fileId") Long fileId);

    // 특정 부서로 분류되지 않은 전체 민원 개수 반환
    @Query("SELECT COUNT(c) FROM Complaint c WHERE c.file.id = :fileId AND c.depart IS NULL")
    Long countUnclassifiedComplaints(@Param("fileId") Long fileId);

    // 여러 부서의 민원 확인을 취소하는 query
    @Modifying
    @Query("UPDATE Complaint c SET c.isChecked = false WHERE c.file.id = :fileId AND c.depart.id IN :departIds")
    void uncheckComplaintsByFileIdAndDepartIds(@Param("fileId") Long fileId, @Param("departIds") List<Long> departIds);
}
