package br.pucrs.estudoorganizado.controller;

import br.pucrs.estudoorganizado.controller.dto.RegisterStudyRecordDTO;
import br.pucrs.estudoorganizado.service.ReviewControlService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Registro de tempo de revisão", description = "Registra a execução de revisão planejada, \n" +
        "após conclusão do tópico no ciclo de estudos.\n" +
        "Obedecendo o espaço de datas salvas como informação do tópico.\n")
@RestController
@RequestMapping("/v1/review-record")
public class ReviewRecordController {

    private final ReviewControlService service;

    public ReviewRecordController(ReviewControlService service) {
        this.service = service;
    }

    public ResponseEntity<Void> postReviewRecord(
            @RequestParam Long topicId,
            @Valid @RequestBody RegisterStudyRecordDTO request) {
        service.recordReview(topicId, request);
        return ResponseEntity.ok().build();
    }
}
