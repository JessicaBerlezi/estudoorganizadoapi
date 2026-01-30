package br.pucrs.estudoorganizado.controller;

import br.pucrs.estudoorganizado.controller.dto.StudyCycleDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mock-api/study-cycle")
public class StudyCycleController {

    @GetMapping
    public StudyCycleDTO getStudyCycleMock() {
        return MocksFactory.createStudyCycleMock();
    }

    @GetMapping("/no-review")
    public StudyCycleDTO getStudyCycleNoReviewMock() {
        return MocksFactory.createStudyCycleNoReviewMock();
    }

}
