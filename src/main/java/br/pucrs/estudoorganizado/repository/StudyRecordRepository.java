package br.pucrs.estudoorganizado.repository;

import br.pucrs.estudoorganizado.entity.StudyRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyRecordRepository extends JpaRepository<StudyRecordEntity, Long> {
}
