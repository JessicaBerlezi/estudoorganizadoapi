package br.pucrs.estudoorganizado.entity.enumerate;

public enum BusinessError {
    STUDY_CYCLES_LOAD("Erro ao carregar ciclos de estudo e revisões."),
    STUDY_CYCLE_LOAD("Erro ao buscar dados do ciclo de estudo."),
    STUDY_CYCLE_CREATE("Erro ao criar novo ciclo de estudo."),
    STUDY_CYCLE_UPDATE("Erro ao atualizar dados do ciclo de estudo."),
    STUDY_CYCLE_DELETE("Erro ao remover o ciclo de estudo."),

    STUDY_MAPS_LOAD("Erro ao carregar disciplinas."),
    STUDY_MAP_LOAD("Erro ao buscar dados de disciplina."),
    STUDY_MAP_CREATE("Erro ao criar nova disciplina."),
    STUDY_MAP_UPDATE("Erro ao atualizar dados da disciplina."),
    STUDY_MAP_DELETE("Erro ao remover a disciplina."),

    SUBJECT_LOAD("Disciplina não encontrada ou inativa"),
    SUBJECT_UPDATE("Erro ao atualizar dados da disciplina."),
    SUBJECT_DISABLE("Erro ao desabilitar dados de disciplina."),

    TOPIC_NOT_FOUND("Registro de tópico não encontrado."),
    REVIEW_NOT_FOUND("Não há revisão pendente para este tópico"),

    SUBJECT_DESCRIPTION("Descrição da disciplina é obrigatória"),
    TOPIC_DESCRIPTION("Descrição do tópico é obrigatória"),
    TOPIC_MANDATORY("Necessário envio de pelo menos um tópico"),
    CYCLE_DESCRIPTION("Descrição do ciclo é obrigatória"),

    UPDATE_ERROR("Erro ao atualizar registro");
    public final String message;

    BusinessError(String message) {
        this.message = message;
    }

    public String message() {
        return  message;
    }
}
