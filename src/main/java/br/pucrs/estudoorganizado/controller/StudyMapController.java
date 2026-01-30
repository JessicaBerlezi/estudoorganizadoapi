package br.pucrs.estudoorganizado.controller;

import br.pucrs.estudoorganizado.controller.dto.InsertSubjectDTO;
import br.pucrs.estudoorganizado.controller.dto.StudyMapsDTO;
import br.pucrs.estudoorganizado.controller.dto.UpdateSubjectOrderDTO;
import br.pucrs.estudoorganizado.controller.dto.UpdateTopicOrderDTO;
import br.pucrs.estudoorganizado.service.StudyMapService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Mapa de estudoss", description = "Controle de disciplinas e tópicos")
@RestController
@RequestMapping("/mock-api/study-map")
public class StudyMapController {

    private final StudyMapService service;

    public StudyMapController(StudyMapService service) {
        this.service = service;
    }

    private static final Logger logger = LoggerFactory.getLogger(StudyMapController.class);

    @Operation(summary = "Lista de disciplinas em aberto", description = "Retorna a lista de disciplinas e tópicos cadastrados, que ainda não foram concluídos")
    @GetMapping
    public StudyMapsDTO getStudyMaps() {
        return MocksFactory.createStudyMapsMock();
    }

    @Operation(summary = "Ordenação de disciplinas", description = "Permite alterar a ordem das disciplinas dentro do Mapa de estudos")
    @PutMapping("/subjects/order")
    public ResponseEntity<Void> updateSubjectsOrder(@Valid @RequestBody UpdateSubjectOrderDTO request) {
        logger.info("Updating subjects order: {}", request.toLogString());
        service.updateSubjectsOrder(request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Ordenação de tópicos da disciplina", description = "Permite alterar a ordem das disciplinas dentro da disciplina do Mapa de estudos")
    @PutMapping("/subject/topics/order")
    public ResponseEntity<Void> updateTopicsOrder(@Valid @RequestBody UpdateTopicOrderDTO request) {
        logger.info("Updating topics order in subject: {}", request.toLogString());
        service.updateTopicsOrder(request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Criação de item no Mapa de estudos", description = "Abre painel para permitir inclusão de disciplinas, com seus respectivos tópicos")
    @PostMapping("/subject")
    public ResponseEntity<Void> postSubject(@Valid @RequestBody InsertSubjectDTO newSubject){
        logger.info("New subject: {}", newSubject.toLogString());
        return ResponseEntity.ok().build();
    }
}


