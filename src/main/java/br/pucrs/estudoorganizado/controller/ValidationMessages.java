package br.pucrs.estudoorganizado.controller;

public class ValidationMessages {
    public static final String SUBJECT_NAME_REQUIRED = "Nome da disciplina é obrigatório";
    public static final String SUBJECT_NAME_MAX = "Nome da disciplina deve ter no máximo 100 caracteres";


    public static final String TOPIC_REQUIRED = "Lista de tópicos da disciplina é obrigatória";
    public static final String TOPIC_ID_REQUIRED = "Id do tópico é obrigatório na atualização";
    public static final String TOPIC_NAME_REQUIRED = "Nome do tópico é obrigatório";
    public static final String TOPIC_NAME_MAX = "Nome do tópico deve ter no máximo 150 caracteres";

    public static final String INCIDENCE_RANGE = "Incidência recebe valores no intervalo de 0 a 4";
    public static final String KNOWLEDGE_RANGE = "Domínio recebe valores no intervalo de 0 a 2";

    public static final String REVIEW_INTERVALS_EMPTY = "Lista de intervalos não pode ser vazia";
    public static final String REVIEW_INTERVALS_INVALID = "Intervalo para datas de revisão aceita valores até 90 dias";

    public static final String ANNOTATION_MAX = "Anotação deve ter no máximo 250 caracteres";

}
