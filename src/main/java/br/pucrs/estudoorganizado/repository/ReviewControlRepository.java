package br.pucrs.estudoorganizado.repository;

import br.pucrs.estudoorganizado.entity.ReviewControlEntity;
import br.pucrs.estudoorganizado.entity.enumerate.ReviewStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewControlRepository extends JpaRepository<ReviewControlEntity, Long> {
    Optional<ReviewControlEntity> findByTopicId(Long topicId);
    List<ReviewControlEntity> findAllByStatusNot(ReviewStatusEnum status);
    List<ReviewControlEntity> findAllByStatus(ReviewStatusEnum status);
}
