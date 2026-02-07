package br.pucrs.estudoorganizado.controller;

import br.pucrs.estudoorganizado.controller.dto.InsertStudyCycleDTO;
import br.pucrs.estudoorganizado.controller.dto.StudyCycleDTO;
import br.pucrs.estudoorganizado.controller.dto.StudyCycleWithTopicsDTO;
import br.pucrs.estudoorganizado.service.StudyCycleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Ciclo de estudos", description = "Gerenciamento de ciclos de estudo com disciplinas criadas no mapa de estudos")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/study-cycle")
public class StudyCycleController {

    private final StudyCycleService service;

    private static final Logger logger = LoggerFactory.getLogger(StudyCycleController.class);

    @Operation(summary = "Lista de ciclos de revisões e estudos ativos", description = "Tela inicial da aplicação. \n" +
            "Retorna nesta ordem: Revisões em atraso, revisões para a data atual e tópicos não concluídos do ciclo de estudo ativo")
    @GetMapping
    public ResponseEntity<StudyCycleDTO> getStudyCycle() {
        return ResponseEntity.ok(service.getActiveStudyCycles());
    }

    @Operation(summary = "Criação de novo ciclo de estudo", description = "Abre painel para permitir inclusão de novo ciclo, com suas respectivas disciplinas e tópicos linas, com seus respectivos")
    @PostMapping("/cycle")
    public ResponseEntity<StudyCycleWithTopicsDTO> postStudyCycle(@Valid @RequestBody InsertStudyCycleDTO newCycle) {
        logger.info("New cycle: {}", newCycle.toLogString());
        service.create(newCycle);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Informações de um ciclo", description = "Retorna dados do ciclo com seus respectivos tópicos")
    @GetMapping("/cycle")
    public ResponseEntity<StudyCycleWithTopicsDTO> getStudyCycleById(@RequestParam Long cycleId){
        logger.info("API getSubjectById, id: {}", cycleId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Edição de um ciclo de estudo", description = "Abre painel para permitir edição de ciclo, mesmo corpo da api de criação")
    @PutMapping("/cycle")
    public ResponseEntity<StudyCycleWithTopicsDTO> putStudyCycle(@RequestParam Long cycleId, @Valid @RequestBody InsertStudyCycleDTO cycle) {
        //todo
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Deleção de um ciclo de estudos", description = "Permite remoção do ciclo e desvinculação de seus itens")
    @DeleteMapping("/cycle")
    public ResponseEntity<Void> deleteStudyCycle(@RequestParam Long cycleId) {
        //todo
        return ResponseEntity.ok().build();
    }
}
