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

@RestController
@RequestMapping("/mock-api/study-map")
public class StudyMapController {

    private final StudyMapService service;

    public StudyMapController(StudyMapService service) {
        this.service = service;
    }

    private static final Logger logger = LoggerFactory.getLogger(StudyMapController.class);

    @GetMapping
    public StudyMapsDTO getStudyMaps() {
        return MocksFactory.createStudyMapsMock();
    }

    @PutMapping("/subjects/order")
    public ResponseEntity<Void> updateSubjectsOrder(@Valid @RequestBody UpdateSubjectOrderDTO request) {
        logger.info("Updating subjects order: {}", request.toLogString());
        service.updateSubjectsOrder(request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/subject/topics/order")
    public ResponseEntity<Void> updateTopicsOrder(@Valid @RequestBody UpdateTopicOrderDTO request) {
        logger.info("Updating topics order in subject: {}", request.toLogString());
        service.updateTopicsOrder(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/subject")
    public ResponseEntity<Void> postSubject(@Valid @RequestBody InsertSubjectDTO newSubject){
        logger.info("New subject: {}", newSubject.toLogString());
        return ResponseEntity.ok().build();
    }
}


