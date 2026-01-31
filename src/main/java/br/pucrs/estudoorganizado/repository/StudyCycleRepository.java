package br.pucrs.estudoorganizado.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.pucrs.estudoorganizado.entity.StudyCycleEntity;

public interface StudyCycleRepository extends JpaRepository<StudyCycleEntity, Long> {
}
