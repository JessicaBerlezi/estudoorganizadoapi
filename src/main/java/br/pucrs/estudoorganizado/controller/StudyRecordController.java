package br.pucrs.estudoorganizado.controller;

import br.pucrs.estudoorganizado.controller.dto.RegistreStudyRecordDTO;
import br.pucrs.estudoorganizado.service.StudyRecordService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Registro de tempo de estudo", description = "Registra a execução de estudo de um tópico no ciclo de estudo.")
@RestController
@RequestMapping("/v1/study-record")
public class StudyRecordController {

    private final StudyRecordService service;

    public StudyRecordController(StudyRecordService service) {
        this.service = service;
    }

    @PostMapping()
    public ResponseEntity<Void> postStudyRecord(
            @RequestParam Long cycleId,
            @RequestParam Long topicId,
            @Valid @RequestBody RegistreStudyRecordDTO request) {
        service.recordStudy(cycleId, topicId, request);
        return ResponseEntity.ok().build();
    }
}
