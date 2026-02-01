package br.pucrs.estudoorganizado.controller;

import br.pucrs.estudoorganizado.controller.dto.InsertSubjectDTO;
import br.pucrs.estudoorganizado.controller.dto.InsertSubjectTopicDTO;
import br.pucrs.estudoorganizado.controller.dto.SubjectDTO;
import br.pucrs.estudoorganizado.controller.dto.UpdateTopicOrderDTO;
import br.pucrs.estudoorganizado.service.StudyMapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Disciplina do Mapa de estudos", description = "Controle de disciplina e seus tópicos")
@RestController
@RequestMapping("/mock-api/study-map/subject")
public class StudyMapSubjectController {

    private final StudyMapService service;

    public StudyMapSubjectController(StudyMapService service) {
        this.service = service;
    }

    private static final Logger logger = LoggerFactory.getLogger(StudyMapSubjectController.class);

    @Operation(summary = "Criação de disciplina no Mapa de estudos", description = "Abre painel para permitir inclusão de disciplinas, com seus respectivos tópicos")
    @PostMapping()
    public ResponseEntity<Void> postSubject(@Valid @RequestBody InsertSubjectDTO request){
        logger.info("New subject: {}", request.toLogString());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Informações de disciplina", description = "Retorna dados da disciplina com seus respectivos tópicos")
    @GetMapping()
    public SubjectDTO getSubjectById(@RequestParam Long id){
        logger.info("Getting subject by id: {}", id);
        return service.getSubjectById(id);
    }

    @Operation(summary = "Edição de disciplina no Mapa de estudos", description = "Abre painel para permitir ajuste de disciplinas e sua lista de tópicos")
    @PutMapping()
    public ResponseEntity<Void> putSubject(@Valid @RequestBody InsertSubjectDTO request) {
        logger.info("Subject update: {}", request.toLogString());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Criação de novo tópico na disciplinas", description = "Permite inclusão individual de tópico em disciplina já criada")
    @PostMapping("/topic")
    public ResponseEntity<Void> postTopic(@Valid @RequestBody InsertSubjectTopicDTO request){
        logger.info("New topic in subject: {}", request.toLogString());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Ordenação de tópicos da disciplina", description = "Permite alterar a ordem das disciplinas dentro da disciplina do Mapa de estudos")
    @PutMapping("/topics/order")
    public ResponseEntity<Void> updateTopicsOrder(@Valid @RequestBody UpdateTopicOrderDTO request) {
        logger.info("Updating topics order in subject: {}", request.toLogString());
        service.updateTopicsOrder(request);
        return ResponseEntity.noContent().build();
    }
}


