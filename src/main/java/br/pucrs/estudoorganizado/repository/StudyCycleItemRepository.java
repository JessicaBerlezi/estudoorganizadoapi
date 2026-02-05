package br.pucrs.estudoorganizado.repository;

import br.pucrs.estudoorganizado.entity.StudyCycleItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyCycleItemRepository extends JpaRepository<StudyCycleItemEntity, Long> {
}
