package br.pucrs.estudoorganizado.repository;

import br.pucrs.estudoorganizado.entity.enumerate.ReviewStatusEnum;
import br.pucrs.estudoorganizado.entity.view.StudyStructureView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudyStructureViewRepository extends JpaRepository<StudyStructureView, Long> {
    List<StudyStructureView> findBySubjectId(Long subjectId);

    List<StudyStructureView> findByStudyCycleId(Long studyCycleId);

    @Query("""
       SELECT v
       FROM StudyStructureView v
       WHERE v.reviewStatus IN :statuses
       ORDER BY v.reviewScheduleDate ASC
       """)
    List<StudyStructureView> findReviewAgendaOrdered(
            @Param("statuses") List<ReviewStatusEnum> statuses
    );

    @Query("""
       SELECT v
       FROM StudyStructureView v
       WHERE v.studyCycleId IS NOT NULL
   
       """)
    List<StudyStructureView> findActivesStudyCycle();

}
