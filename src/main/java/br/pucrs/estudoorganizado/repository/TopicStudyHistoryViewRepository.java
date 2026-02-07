package br.pucrs.estudoorganizado.repository;

import br.pucrs.estudoorganizado.entity.TopicStudyHistoryView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicStudyHistoryViewRepository extends JpaRepository<TopicStudyHistoryView, Long> {

    List<TopicStudyHistoryView>
    findAllByOrderByCycleStartedAtAscCycleIdAscTopicOrderAscRecordStartedAtDesc();
}
