package br.pucrs.estudoorganizado.repository;

import br.pucrs.estudoorganizado.entity.view.TopicWithHistoryView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicWithHistoryViewRepository extends JpaRepository<TopicWithHistoryView, Long> {
    List<TopicWithHistoryView> findByTopicIdIn(List<Long> topicIds);
//findByTopicIdInOrderByRecordStartedAtDescStudyRecordIdDesc
}
