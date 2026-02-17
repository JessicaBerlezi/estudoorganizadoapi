package br.pucrs.estudoorganizado.service;

import br.pucrs.estudoorganizado.entity.StudyCycleEntity;
import br.pucrs.estudoorganizado.infraestructure.exception.BusinessError;
import br.pucrs.estudoorganizado.infraestructure.exception.ApiExceptionFactory;
import br.pucrs.estudoorganizado.repository.StudyCycleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyCycleService {

    private final StudyCycleRepository repository;

    public StudyCycleEntity saveStudyCycle(StudyCycleEntity cycle) {
        return repository.save(cycle);
    }

    public StudyCycleEntity getStudyCycle(Long cycleId) {
        return repository.findById(cycleId)
                .orElseThrow(() -> ApiExceptionFactory.notFound(BusinessError.STUDY_CYCLE_LOAD));
    }


    public void deleteStudyCycle(StudyCycleEntity cycle) {
        repository.delete(cycle);
    }
}
