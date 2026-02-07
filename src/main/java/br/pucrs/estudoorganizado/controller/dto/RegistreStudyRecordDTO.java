package br.pucrs.estudoorganizado.controller.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class RegistreStudyRecordDTO {

    @NotNull
    LocalDate startedAt;

    @NotNull
    Long minutes;

    int questionsSolved;
    int questionsIncorrected;

    @Size(max = 250, message = "Anotação deve ter no máximo 250 caracteres")
    String annotation;

    @NotNull
    Boolean isDone;
}
