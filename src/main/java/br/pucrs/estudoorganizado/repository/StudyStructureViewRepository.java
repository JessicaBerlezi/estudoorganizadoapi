package br.pucrs.estudoorganizado.repository;

import br.pucrs.estudoorganizado.entity.view.StudyStructureView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyStructureViewRepository extends JpaRepository<StudyStructureView, Long> {
    List<StudyStructureView> findBySubjectId(Long subjectId);

    List<StudyStructureView> findByStudyCycleId(Long studyCycleId);
}
