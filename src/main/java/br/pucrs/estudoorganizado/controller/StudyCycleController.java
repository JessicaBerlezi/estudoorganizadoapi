package br.pucrs.estudoorganizado.controller;

import br.pucrs.estudoorganizado.component.StudyCycleComponent;
import br.pucrs.estudoorganizado.controller.dto.StudyCycleDetailsDTO;
import br.pucrs.estudoorganizado.controller.dto.DailyTasksDTO;
import br.pucrs.estudoorganizado.controller.dto.StudyCycleWithTopicsDTO;
import br.pucrs.estudoorganizado.entity.StudyCycleEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Ciclo de estudos", description = "Gerenciamento de ciclos de estudo com disciplinas criadas no mapa de estudos")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/study-cycle")
public class StudyCycleController {

    private final StudyCycleComponent component;

    @Operation(summary = "Lista de ciclos de revisões e estudos ativos", description = "Tela inicial da aplicação. \n" +
            "Retorna nesta ordem: Revisões em atraso, revisões para a data atual e tópicos não concluídos do ciclo de estudo ativo")
    @GetMapping
    public ResponseEntity<DailyTasksDTO> getDailyTasks() {
        return ResponseEntity.ok(component.getDailyTasks());
    }

    @Operation(summary = "Criação de novo ciclo de estudo", description = "Abre painel para permitir inclusão de novo ciclo, com suas respectivas disciplinas e tópicos linas, com seus respectivos")
    @PostMapping("/cycle")
    public ResponseEntity<StudyCycleWithTopicsDTO> postStudyCycle(@Valid @RequestBody StudyCycleDetailsDTO dto) {
        StudyCycleEntity entity = component.creteStudyCycle(dto);
        return ResponseEntity.ok(component.getStudyCycleById(entity.getId()));
    }

    @Operation(summary = "Informações de um ciclo", description = "Retorna dados do ciclo com seus respectivos tópicos")
    @GetMapping("/cycle")
    public ResponseEntity<StudyCycleWithTopicsDTO> getStudyCycleById(@RequestParam Long cycleId) {
        return ResponseEntity.ok(component.getStudyCycleById(cycleId));
    }


    @Operation(summary = "Edição de um ciclo de estudo", description = "Abre painel para permitir edição de ciclo, mesmo corpo da api de criação")
    @PutMapping("/cycle")
    public ResponseEntity<StudyCycleWithTopicsDTO> putStudyCycle(@RequestParam Long cycleId, @Valid @RequestBody StudyCycleDetailsDTO dto) {
        component.updateStudyCycle(cycleId, dto);
        return ResponseEntity.ok(component.getStudyCycleById(cycleId));
    }

    @Operation(summary = "Deleção de um ciclo de estudos", description = "Permite remoção do ciclo e desvinculação de seus itens")
    @DeleteMapping("/cycle")
    public ResponseEntity<Void> deleteStudyCycle(@RequestParam Long cycleId) {
        component.deleteStudyCycle(cycleId);
        return ResponseEntity.ok().build();
    }
}
