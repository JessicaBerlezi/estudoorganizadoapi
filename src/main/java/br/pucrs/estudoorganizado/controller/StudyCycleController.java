package br.pucrs.estudoorganizado.controller;

import br.pucrs.estudoorganizado.controller.dto.InsertStudyCycleDTO;
import br.pucrs.estudoorganizado.controller.dto.StudyCycleDTO;
import br.pucrs.estudoorganizado.controller.dto.SubjectTopicOptionDTO;
import br.pucrs.estudoorganizado.controller.dto.TopicSummaryDTO;
import br.pucrs.estudoorganizado.service.StudyCycleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@Tag(name = "Ciclo de estudos", description = "Gerenciamento de ciclos de estudo")
@RestController
@RequiredArgsConstructor
@RequestMapping("/mock-api/study-cycle")
public class StudyCycleController {

    private final StudyCycleService service;

    private static final Logger logger = LoggerFactory.getLogger(StudyCycleController.class);

    @Operation(summary = "Ciclos de revisões e estudos ativos", description = "Retorna nesta ordem: Revisões em atraso, revisões para a data atual e tópicos não concluídos do ciclo de estudo ativo")
    @GetMapping
    public StudyCycleDTO getStudyCycleMock() {
        return MocksFactory.createStudyCycleMock();
    }

    @Operation(summary = "Lista de disciplina e tópico para criação de novo ciclo", description = "Na criação de novo ciclo, retornará identificação básica da disciplina e informações do tópico pré-selecionado")
    @GetMapping("/subject-topic-option")
    public List<SubjectTopicOptionDTO> getSubjectTopicOptionMock() {
        return MocksFactory.getSubjectTopicOptionMock();
    }

    @Operation(summary = "Criação de novo ciclo de estudo", description = "Abre painel para permitir inclusão de novo ciclo, com suas respectivas disciplinas e tópicos linas, com seus respectivos")
    @PostMapping()
    public ResponseEntity<Void> postStudyCycle(@Valid @RequestBody InsertStudyCycleDTO newCycle) {
        logger.info("New cycle: {}", newCycle.toLogString());
        service.create(newCycle);
        return ResponseEntity.ok().build();
    }

}
