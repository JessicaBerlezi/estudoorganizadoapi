package br.pucrs.estudoorganizado.repository;

import br.pucrs.estudoorganizado.entity.view.CycleStudyView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CycleStudyViewRepository extends JpaRepository<CycleStudyView, Long> {

    List<CycleStudyView>
    findAllByOrderByCycleStartedAtAscCycleIdAscTopicOrderAscRecordStartedAtDesc();
}
