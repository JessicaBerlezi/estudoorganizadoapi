package br.pucrs.estudoorganizado.repository;

import br.pucrs.estudoorganizado.entity.TopicEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicRepository extends JpaRepository<TopicEntity, Long> {

    List<TopicEntity> findBySubjectIdOrderByOrderAsc(Long subjectId);

}
