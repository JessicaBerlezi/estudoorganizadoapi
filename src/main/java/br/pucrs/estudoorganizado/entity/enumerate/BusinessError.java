package br.pucrs.estudoorganizado.entity.enumerate;

public enum BusinessError {
    UPDATE_ERROR("Erro ao atualizar registro"),
    DISABLE_SUBJECT("Erro ao desabilitar dados de disciplina."),
    SUBJECT_NOT_FOUND("Disciplina não encontrada ou inativa");
    public final String message;

    BusinessError(String message) {
        this.message = message;
    }

    public String message() {
        return  message;
    }
}
