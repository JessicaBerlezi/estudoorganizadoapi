package br.pucrs.estudoorganizado.repository;

import br.pucrs.estudoorganizado.entity.StudyCycleItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudyCycleItemRepository extends JpaRepository<StudyCycleItemEntity, Long> {

    Optional<StudyCycleItemEntity>
    findByStudyCycleIdAndTopicId(Long studyCycleId, Long topicId);

    List<StudyCycleItemEntity>
    findAllByStudyCycleId(Long studyCycleId);
}
