package br.pucrs.estudoorganizado.controller;

import br.pucrs.estudoorganizado.controller.dto.StudyMapsDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mock-api/study-map")
public class StudyMapController {

    @GetMapping
    public StudyMapsDTO getStudyMaps() {
        return MocksFactory.createStudyMapsMock();

    }

}


