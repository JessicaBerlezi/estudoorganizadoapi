package br.pucrs.estudoorganizado.controller;

import br.pucrs.estudoorganizado.controller.dto.InsertStudyRecordDTO;
import br.pucrs.estudoorganizado.service.ReviewControlService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/review-record")
public class ReviewRecordController {

    private final ReviewControlService service;

    public ReviewRecordController(ReviewControlService service) {
        this.service = service;
    }

    @PostMapping()
    public ResponseEntity<Void> postReviewRecord(
            @RequestParam Long topicId,
            @Valid @RequestBody InsertStudyRecordDTO request){
        service.recordReview(topicId, request);
        return ResponseEntity.ok().build();
    }
}
