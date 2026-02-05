package br.pucrs.estudoorganizado.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.pucrs.estudoorganizado.entity.StudyCycleEntity;

import java.util.List;


public interface StudyCycleRepository extends JpaRepository<StudyCycleEntity, Long> {

    @Override
    <S extends StudyCycleEntity> List<S> saveAll(Iterable<S> entities);
}
