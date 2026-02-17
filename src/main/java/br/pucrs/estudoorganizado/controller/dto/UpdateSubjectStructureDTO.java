package br.pucrs.estudoorganizado.controller.dto;

import br.pucrs.estudoorganizado.controller.ValidationMessages;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
public class UpdateSubjectStructureDTO extends BaseSubjectStructureDTO {

    @Valid
    @NotEmpty(message = ValidationMessages.TOPIC_REQUIRED)
    public List<UpdateTopicStructureDTO> topics;

    public String toLogString() {
        String topicsLog = (topics == null || topics.isEmpty())
                ? "[]"
                : topics.stream()
                .map(UpdateTopicStructureDTO::toLogString)
                .collect(Collectors.joining(", ", "[", "]"));

        return String.format(
                "description='%s', annotation=%s, topics=%s",
                description,
                annotation != null && !annotation.isBlank(),
                topicsLog
        );
    }
}
