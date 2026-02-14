package br.pucrs.estudoorganizado.controller;

import br.pucrs.estudoorganizado.component.StudyMapComponent;
import br.pucrs.estudoorganizado.controller.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Mapa de estudos", description = "Controle de disciplinas e tópicos/assunto de forma verticalizada. " +
        "Aqui a lista de itens a serem estudados é organizada e elencada. " +
        "No mapa de estudo esta lista é percorrida.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/study-map")
public class StudyMapController {
    private final StudyMapComponent component;

    @Operation(summary = "Lista de disciplinas em aberto", description = "Retorna a lista de disciplinas e tópicos cadastrados, que ainda não foram concluídos")
    @GetMapping
    public ResponseEntity<StudyMapStructureDTO> getStudyMaps() {
        return ResponseEntity.ok(component.buildStudyMapInfo());
    }

    @Operation(summary = "Criação de disciplina no Mapa de estudos", description = "A partir da tela inicial, abre um painel que permite incluir disciplinas e seus respectivos tópicos.")
    @PostMapping("/subject")
    public ResponseEntity<SubjectDTO> postSubject(@Valid @RequestBody InsertSubjectDTO dto){
        return ResponseEntity.ok(component.createSubjectWithTopics(dto));
    }

    @Operation(summary = "Informações de uma disciplina", description = "Retorna dados da disciplina com seus respectivos tópicos não concluídos")
    @GetMapping("/subject")
    public ResponseEntity<StudyStructureDTO> getSubjectById(@RequestParam Long subjectId){
        return ResponseEntity.ok(component.getSubjectById(subjectId));
    }

    @Operation(summary = "Edição de uma disciplina no Mapa de estudos", description = "Abre painel para permitir ajuste de disciplinas e sua lista de tópicos")
    @PutMapping("/subject")
    public ResponseEntity<SubjectDTO> putSubject(@RequestParam Long subjectId, @Valid @RequestBody UpdateSubjectDTO request) {
        return ResponseEntity.ok(component.updateSubjectWithTopics(subjectId, request));
    }

    @Operation(summary = "Desativa disciplina e seus tópicos (soft delete)")
    @DeleteMapping("/subject")
    public ResponseEntity<Void> disableSubject(@RequestParam Long subjectId) {
        component.disableSubject(subjectId);
        return ResponseEntity.ok().build();
    }
}


