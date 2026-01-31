package br.pucrs.estudoorganizado.service;

import br.pucrs.estudoorganizado.controller.dto.InsertStudyCycleDTO;
import br.pucrs.estudoorganizado.entity.StudyCycleEntity;
import br.pucrs.estudoorganizado.repository.StudyCycleRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StudyCycleService {

    private final StudyCycleRepository repository;

    public void create(InsertStudyCycleDTO requestDTO) {
        StudyCycleEntity entity = StudyCycleEntity.fromDTO(requestDTO, LocalDateTime.now());
        repository.save(entity);
    }
}
