package br.pucrs.estudoorganizado.controller;

import br.pucrs.estudoorganizado.controller.dto.StudyCycleDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Ciclo de estudos", description = "Gerenciamento de ciclos de estudo")
@RestController
@RequestMapping("/mock-api/study-cycle")
public class StudyCycleController {

    @Operation(summary = "Ciclos de revisões e estudos ativos", description = "Retorna nesta ordem: Revisões em atraso, revisões para a data atual e tópicos não concluídos do ciclo de estudo ativo")
    @GetMapping
    public StudyCycleDTO getStudyCycleMock() {
        return MocksFactory.createStudyCycleMock();
    }

    @Operation(summary = "Ciclo de estudos", description = "Adaptação do endpoint para mock sem retorno de revisões ")
    @GetMapping("/no-review")
    public StudyCycleDTO getStudyCycleNoReviewMock() {
        return MocksFactory.createStudyCycleNoReviewMock();
    }

}
