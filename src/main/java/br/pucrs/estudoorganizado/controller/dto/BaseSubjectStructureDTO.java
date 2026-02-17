package br.pucrs.estudoorganizado.controller.dto;

import br.pucrs.estudoorganizado.controller.ValidationMessages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class BaseSubjectStructureDTO {

    @NotBlank(message = ValidationMessages.SUBJECT_NAME_REQUIRED)
    @Size(max = 150, message = ValidationMessages.SUBJECT_NAME_MAX)
    public String description;

    @Size(max = 250, message = ValidationMessages.ANNOTATION_MAX)
    public String annotation;
    }
