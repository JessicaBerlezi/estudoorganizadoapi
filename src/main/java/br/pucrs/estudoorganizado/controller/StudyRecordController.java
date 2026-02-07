package br.pucrs.estudoorganizado.controller;

import br.pucrs.estudoorganizado.controller.dto.InsertStudyRecordDTO;
import br.pucrs.estudoorganizado.service.StudyRecordService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
            @Valid @RequestBody InsertStudyRecordDTO request){
        service.create(cycleId, topicId, request);
        return ResponseEntity.ok().build();
    }
}
