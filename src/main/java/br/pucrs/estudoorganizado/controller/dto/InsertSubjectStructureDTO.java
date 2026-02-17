package br.pucrs.estudoorganizado.controller.dto;
import br.pucrs.estudoorganizado.controller.ValidationMessages;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class InsertSubjectStructureDTO extends BaseSubjectStructureDTO{

    @Valid
    @NotEmpty(message = ValidationMessages.TOPIC_REQUIRED)
    public List<InsertTopicStructureDTO> topics;

    public String toLogString() {
        String topicsLog = (topics == null || topics.isEmpty())
                ? "[]"
                : topics.stream()
                .map(InsertTopicStructureDTO::toLogString)
                .collect(Collectors.joining(", ", "[", "]"));

        return String.format(
                "description='%s', annotation=%s, topics=%s",
                description,
                annotation != null && !annotation.isBlank(),
                topicsLog
        );
    }
}
