package br.pucrs.estudoorganizado.repository;

import br.pucrs.estudoorganizado.entity.StudyRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudyRecordRepository extends JpaRepository<StudyRecordEntity, Long> {
    @Query("""
    SELECT sr
    FROM StudyRecordEntity sr
    WHERE sr.studyCycleItem.topic.id = :topicId
    ORDER BY sr.startedAt DESC, sr.id DESC""")
    List<StudyRecordEntity> findHistoryByTopicId(@Param("topicId") Long topicId);

    //TODO USAR PARA GET DE UM TOPICO
}
