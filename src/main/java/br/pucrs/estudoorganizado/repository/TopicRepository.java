package br.pucrs.estudoorganizado.repository;

import br.pucrs.estudoorganizado.entity.TopicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TopicRepository extends JpaRepository<TopicEntity, Long> {

    @Query("""
    SELECT t
    FROM TopicEntity t
    WHERE t.order = (
        SELECT MIN(t2.order)
        FROM TopicEntity t2
        WHERE t2.subject = t.subject
        AND NOT EXISTS (
            SELECT 1
            FROM StudyCycleItemEntity sci
            WHERE sci.topic = t2
            AND sci.status IN ('DONE', 'REMOVED')
        )
    )
    ORDER BY t.subject.id
    """)
    List<TopicEntity> findFirstAvailableTopicPerSubject();

    @Query("""
        SELECT t
        FROM TopicEntity t
        WHERE t.subject.id = :subjectId
        AND NOT EXISTS (
            SELECT 1
            FROM StudyCycleItemEntity sci
            WHERE sci.topic = t
            AND sci.status IN ('DONE', 'REMOVED')
        )
        ORDER BY t.order ASC
    """)
    List<TopicEntity> findTopicsToStudyBySubjectId(@Param("subjectId") Long subjectId);


}
