package br.pucrs.estudoorganizado.controller;

import br.pucrs.estudoorganizado.controller.dto.InsertSubjectDTO;
import br.pucrs.estudoorganizado.controller.dto.StudyMapsDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/mock-api/study-map")
public class StudyMapController {

    private static final Logger logger = LoggerFactory.getLogger(StudyMapController.class);

    @GetMapping
    public StudyMapsDTO getStudyMaps() {
        return MocksFactory.createStudyMapsMock();
    }

    @PostMapping("/subject")
    public ResponseEntity<Integer> postSubject(@Valid @RequestBody InsertSubjectDTO newSubject){
        logger.info("New subject: {}", newSubject.toLogString());
        return ResponseEntity.ok(200);
    }
}


