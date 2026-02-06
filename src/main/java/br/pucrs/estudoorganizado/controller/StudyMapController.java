package br.pucrs.estudoorganizado.controller;

import br.pucrs.estudoorganizado.controller.dto.*;
import br.pucrs.estudoorganizado.service.StudyMapService;
import br.pucrs.estudoorganizado.service.SubjectService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Mapa de estudos", description = "Controle de disciplinas")
@RestController
@RequestMapping("/v1/study-map")
public class StudyMapController {

    private final StudyMapService service;
    private final SubjectService subjectService;

    public StudyMapController(StudyMapService service, SubjectService subjectService) {
        this.service = service;
        this.subjectService = subjectService;
    }

    private static final Logger logger = LoggerFactory.getLogger(StudyMapController.class);

    @Operation(summary = "Lista de disciplinas em aberto", description = "Retorna a lista de disciplinas e tópicos cadastrados, que ainda não foram concluídos")
    @GetMapping
    public ResponseEntity<StudyMapsDTO> getStudyMaps() {
        return ResponseEntity.ok(service.getStudyMaps());
    }

    @Operation(summary = "Criação de disciplina no Mapa de estudos", description = "Abre painel para permitir inclusão de disciplinas, com seus respectivos tópicos")
    @PostMapping("/subjects")
    public ResponseEntity<SubjectDTO> postSubject(@Valid @RequestBody InsertSubjectDTO request){
        logger.info("API postSubject, request: {}", request.toLogString());
        return ResponseEntity.ok(subjectService.create(request));
    }

    @Operation(summary = "Edição de disciplina no Mapa de estudos", description = "Abre painel para permitir ajuste de disciplinas e sua lista de tópicos")
    @PutMapping("/subjects")
    public ResponseEntity<SubjectDTO> putSubject(@RequestParam Long id, @Valid @RequestBody UpdateSubjectDTO request) {
        logger.info("Subject update: {}", request.toLogString());
        return ResponseEntity.ok(subjectService.update(id, request));
    }

    @Operation(summary = "Informações de disciplina", description = "Retorna dados da disciplina com seus respectivos tópicos")
    @GetMapping("/subjects")
    public ResponseEntity<SubjectDTO> getSubjectById(@RequestParam Long id){
        logger.info("API getSubjectById, id: {}", id);
        return ResponseEntity.ok(subjectService.getActiveSubject(id));
    }
}


