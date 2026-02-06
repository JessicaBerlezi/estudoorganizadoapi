package br.pucrs.estudoorganizado.repository;

import br.pucrs.estudoorganizado.entity.SubjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository extends JpaRepository<SubjectEntity, Long> {
    List<SubjectEntity> findByIsActiveTrueOrderByIdAsc();
    Optional<SubjectEntity> findByIdAndIsActiveTrue(Long subjectId);
}
