package br.pucrs.estudoorganizado.controller;

import br.pucrs.estudoorganizado.controller.dto.UpdateSubjectOrderDTO;
import br.pucrs.estudoorganizado.controller.dto.UpdateTopicOrderDTO;
import br.pucrs.estudoorganizado.service.SubjectService;
import br.pucrs.estudoorganizado.service.TopicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Ajuste na ordem dos itens em Mapa de estudo", description = "Ajuste na ordem de disciplinas e tópicos da disciplinade disciplinas")
@RestController
@RequestMapping("/v1/study-map/change-order/")
public class StudyMapChangeOrderController {

    private final SubjectService subjectService;
    private final TopicService topicService;

    public StudyMapChangeOrderController(SubjectService subjectService, TopicService topicService) {
        this.subjectService = subjectService;
        this.topicService = topicService;
    }

    private static final Logger logger = LoggerFactory.getLogger(StudyMapChangeOrderController.class);

    @Operation(summary = "Ordenação de disciplinas", description = "Permite alterar a ordem das disciplinas dentro do Mapa de estudos")
    @PutMapping("subjects")
    public ResponseEntity<Void> updateSubjectsOrder(@Valid @RequestBody UpdateSubjectOrderDTO request) {
        logger.info("Updating subjects order: {}", request.toLogString());
       // subjectService.updateSubjectsOrder(request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Ordenação de tópicos da disciplina", description = "Permite alterar a ordem das disciplinas dentro da disciplina do Mapa de estudos")
    @PutMapping("subjects/{subjectId}/topics")
    public ResponseEntity<Void> updateTopicsOrder(@Valid @RequestBody UpdateTopicOrderDTO request) {
        logger.info("Updating topics order in subject: {}", request.toLogString());
        //topicService.updateTopicsOrder(request);
        return ResponseEntity.noContent().build();
    }
}


