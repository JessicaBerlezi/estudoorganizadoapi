package br.pucrs.estudoorganizado.repository;

import br.pucrs.estudoorganizado.entity.SubjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<SubjectEntity, Long> {
}
