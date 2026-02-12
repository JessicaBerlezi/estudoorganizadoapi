package br.pucrs.estudoorganizado.service;

import br.pucrs.estudoorganizado.entity.StudyCycleItemEntity;
import br.pucrs.estudoorganizado.repository.StudyCycleItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyCycleItemService {

    private final StudyCycleItemRepository repository;

    public List<StudyCycleItemEntity> findAllByStudyCycleId(Long studyCycleId) {
        return repository.findAllByStudyCycleId(studyCycleId);
    }
}
