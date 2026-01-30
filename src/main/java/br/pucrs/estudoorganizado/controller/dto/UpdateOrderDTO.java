package br.pucrs.estudoorganizado.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class UpdateOrderDTO {

    @NotNull(message = "Id de identificação do item é obrigatório")
    public Long id;

    @NotNull(message = "Ordem atualizada do item é obrigatória")
    @Min(value = 1, message = "A ordem deve começar em 1")
    public int order;
}
