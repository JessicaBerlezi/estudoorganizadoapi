package br.pucrs.estudoorganizado.repository;

import br.pucrs.estudoorganizado.entity.SubjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository extends JpaRepository<SubjectEntity, Long> {
    List<SubjectEntity> findByIsActiveTrueOrderByIdAsc();

    @Query("""
    select s from SubjectEntity s
    left join fetch s.topics
    where s.id = :id
    """)
    Optional<SubjectEntity> findByIdWithTopics(Long id);}
