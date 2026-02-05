package br.pucrs.estudoorganizado.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.pucrs.estudoorganizado.entity.StudyCycleEntity;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudyCycleRepository extends JpaRepository<StudyCycleEntity, Long> {


    @Query("""
            SELECT sc
            FROM StudyCycleItemEntity i
            JOIN i.studyCycle sc
            JOIN i.topic t
            WHERE sc.isActive = TRUE
              AND t.isActive = TRUE
              AND i.doneAt IS NULL
            ORDER BY sc.startedAt ASC,
                     sc.id ASC,
                     t.order ASC
    """)
    List<StudyCycleEntity> findActivesCycles();
}
