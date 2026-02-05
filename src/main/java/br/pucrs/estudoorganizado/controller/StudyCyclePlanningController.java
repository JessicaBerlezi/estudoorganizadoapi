package br.pucrs.estudoorganizado.controller;

import br.pucrs.estudoorganizado.controller.dto.SubjectTopicOptionDTO;
import br.pucrs.estudoorganizado.controller.dto.TopicSummaryDTO;
import br.pucrs.estudoorganizado.service.TopicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Planejamento de Ciclo de Estudos",
        description = "Fornece dados e sugestões para auxiliar na montagem de um novo ciclo de estudos")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/study-cycle/planning/")
public class StudyCyclePlanningController {

    private final TopicService service;

    @Operation(summary = "Sugestão automática de tópico por disciplina",
            description = "Retorna, para cada disciplina cadastrada, um tópico sugerido para iniciar o próximo ciclo de estudos. \n" +
                    "A sugestão considera apenas tópicos que ainda não foram concluídos ou removidos em ciclos anteriores.")
    @GetMapping("subjects/topics-suggestion")
    public ResponseEntity<List<SubjectTopicOptionDTO>> getTopicToStudyPerSubject() {
        return ResponseEntity.ok(service.getTopicToStudyPerSubject());
    }

    @Operation(
            summary = "Listar tópicos disponíveis para estudo por disciplina",
            description = "Retorna os tópicos de uma disciplina específica que estão disponíveis para inclusão em um novo ciclo de estudos. \n" +
                    "São considerados disponíveis os tópicos que ainda não foram concluídos ou removidos em ciclos anteriores.")
    @GetMapping("subjects/{subjectId}/topics-option")
    public ResponseEntity<List<TopicSummaryDTO>> getTopicsToStudyBySubjectId(@PathVariable Long subjectId) {
        return ResponseEntity.ok(service.getTopicsToStudyBySubjectId(subjectId));
    }

}
