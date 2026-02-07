package br.pucrs.estudoorganizado.controller;

import br.pucrs.estudoorganizado.controller.dto.RegistreStudyRecordDTO;
import br.pucrs.estudoorganizado.service.ReviewControlService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Execução de revisão", description = "Registra a execução de revisão planejada constante no ciclo de estudos")
@RestController
@RequestMapping("/v1/review-record")
public class ReviewRecordController {

    private final ReviewControlService service;

    public ReviewRecordController(ReviewControlService service) {
        this.service = service;
    }

    public ResponseEntity<Void> postReviewRecord(
            @RequestParam Long topicId,
            @Valid @RequestBody RegistreStudyRecordDTO request) {
        service.recordReview(topicId, request);
        return ResponseEntity.ok().build();
    }
}
