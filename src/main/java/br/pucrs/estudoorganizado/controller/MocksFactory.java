package br.pucrs.estudoorganizado.controller;

import br.pucrs.estudoorganizado.service.utils.Utils;
import br.pucrs.estudoorganizado.controller.dto.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class MocksFactory {
    private static String getRandomRGB(){
        return Utils.getRandomRGB();
    }

    private static LinkedList<SubjectDTO> createSubjectDTOMock(){
        LinkedList<SubjectDTO> subject = new LinkedList<>();

        subject.add(createSubjectMock(
                1,
                1,
                "Análise de dados",
                "Em andamento (1/3)",
                "Em SQL ver as formas normais",
                List.of("DW", "ETL", "SQL")));

        subject.add(createSubjectMock(
                2,
                2,
                "Direito Administrativo",
                "Em andamento (1/7)",
                "Resumo das principais leis",
                List.of("Organização do Estado", "Licitações", "Atos Administrativos", "Servidores Públicos", "Controle da Administração", "Responsabilidade Civil do Estado")));


        subject.add(createSubjectMock(
                3,
                3,
                "Estatística",
                "Não iniciado (0/3)",
                "Anotações sobre probabilidade",
                List.of("Probabilidade", "Regressão Linear", "Correlação")));

        subject.add(createSubjectMock(
                5,
                4,
                "Segurança da Informação",
                "Em andamento (0/1)",
                null,
                List.of("Criptografia")));

        subject.add(createSubjectMock(
                4,
                5,
                "Português",
                "Em andamento (0/2)",
                "Contnuir da vídeo aula 4",
                List.of("Verbos", "Interpretação")
        ));
        return subject;
    }

    public static StudyMapsDTO createStudyMapsMock() {
        StudyMapsDTO studyMaps = new StudyMapsDTO();
        studyMaps.subjects = new LinkedList<>(createSubjectDTOMock());
        return studyMaps;
    }

    private static SubjectDTO createSubjectMock(long id, int order, String description, String statusInfo, String annotation, List<String> topics) {
        SubjectDTO subject = new SubjectDTO();
        subject.id = id;
        subject.order = order;
        subject.description = description;
        subject.statusInfo = statusInfo;
        subject.annotation = annotation;
        subject.topics = new LinkedList<>();

        int i = 1;
        Random random = new Random();
        for (String t : topics) {
            TopicSummaryDTO topic = new TopicSummaryDTO();
            topic.id = i;
            topic.order = i++;
            topic.rgb = getRandomRGB();
            topic.description = t;
            topic.elapsedTime = random.nextInt(13) + "h " + random.nextInt(60) + "min";
            topic.score = random.nextInt(100) + 1 + "%";
            subject.topics.add(topic);
        }
        return subject;
    }


    public static StudyCycleDTO createStudyCycleMock() {
        StudyCycleDTO studyCycleDTO = new StudyCycleDTO();

        studyCycleDTO.reviews = createReviewDTOMock();

        studyCycleDTO.cycles = new LinkedList<>();
        studyCycleDTO.cycles.add(createCycleDTOMock());

        return  studyCycleDTO;
    }

    public static StudyCycleDTO createStudyCycleNoReviewMock() {
        StudyCycleDTO studyCycleDTO = new StudyCycleDTO();

        studyCycleDTO.reviews = new LinkedList<>();
        studyCycleDTO.reviews.add(null);

        studyCycleDTO.cycles = new LinkedList<>();
        studyCycleDTO.cycles.add(createCycleDTOMock());

        return  studyCycleDTO;
    }


    private static CycleDTO createCycleDTOMock() {
        LinkedList<TopicDetailDTO> cycleTopics = new LinkedList<>();
        cycleTopics.add(
                createTopicDetail(
                        1,
                        "Criptografia",
                        "Segurança da Informação",
                        "55min",
                        "Ver livro x, capitulo 2"
                ));
        cycleTopics.add(
                createTopicDetail(
                        2,
                        "Verbos",
                        "Português",
                        "0min",
                        null

                )
        );


        return new CycleDTO(
                "Ciclo 2 Prova X",
                "Data de início: 14/02/2026",
                "Ciclo de Segurança , repetir o ciclo por 4 dias. " +
                        "Fazer simulado 4 após fim das matérias",
                cycleTopics
        );
    }


    private static LinkedList<ReviewDTO> createReviewDTOMock(){
        LinkedList<ReviewDTO> reviews = new LinkedList<>();

        reviews.add(createReview1Mock());
        reviews.add(createReview2Mock());
        return reviews;
    }

    private static TopicReviewDetailDTO createTopicReviewDetailDTO(
            int order,
            String description,
            String subject,
            String elapsedTime,
            String score,
            String reviewInfo,
            String annotation,
            LinkedList<TopicHistoryDTO> history) {

        TopicReviewDetailDTO topic = new TopicReviewDetailDTO();
        topic.order = order;
        topic.rgb = getRandomRGB();
        topic.description = description;
        topic.elapsedTime = elapsedTime;
        topic.score = score;
        topic.subject = subject;
        topic.annotation = annotation;
        topic.reviewInfo = reviewInfo;
        topic.history = history;

        return topic;
    }

    private static TopicHistoryDTO createTopicHistory(String description, String information, String annotation) {
        TopicHistoryDTO history = new TopicHistoryDTO();
        history.description = description;
        history.information = information;
        history.annotation = annotation;
        return history;
    }


    private static TopicDetailDTO createTopicDetail(
            int order,
            String description,
            String subject,
            String elapsedTime,
            String annotation) {

        TopicDetailDTO topic = new TopicDetailDTO();

        topic.order = order;
        topic.rgb = getRandomRGB();
        topic.description = description;
        topic.elapsedTime = elapsedTime;
        topic.score = "-%";
        topic.subject = subject;
        topic.annotation = annotation;

        return topic;
    }
    private static ReviewDTO createReview1Mock(){
        LinkedList<TopicHistoryDTO> history = new LinkedList<>();
        history.add(createTopicHistory("Questões 20/01/2026", "60Q 40A 66%",null));
        history.add(createTopicHistory("Revisão 01/02/2026", "20Q 17A 85%", "Refazer questões 35 a 70 do pdf, para rever os erros\n" +
                " na revisão 3"));
        LinkedList<TopicReviewDetailDTO> reviewTopics = new LinkedList<>();

        reviewTopics.add(
                createTopicReviewDetailDTO(
                        1,
                        "Licitações",
                        "Direito Administrativo",
                        "3h45min",
                        "85%",
                        "Agenda de próximas revisões: 3d, 5d, 15d, 30d",
                        "Focar nos tópicos 4, 7, 8",
                        history
                )
        );
        ReviewDTO review = new ReviewDTO();
        review.description = "Agenda de revisão";
        review.statusInfo = "Em atraso";
        review.topics = reviewTopics;
        return review;
    }


    private static ReviewDTO createReview2Mock(){
        LinkedList<TopicHistoryDTO> history = new LinkedList<>();
        history.add(createTopicHistory("Questões 20/01/2026", "20Q 18A 90%",null));

        LinkedList<TopicReviewDetailDTO> reviewTopics = new LinkedList<>();

        reviewTopics.add(
                createTopicReviewDetailDTO(
                        1,
                        "DW",
                        "Análise de dados",
                        "1h20min",
                        "90%",
                        "Agenda de próximas revisões: 15d, 30d",
                        null,
                        history
                )
        );
        ReviewDTO review = new ReviewDTO();
        review.description = "Agenda de revisão";
        review.statusInfo = "Hoje";
        review.topics = reviewTopics;
        return review;
    }


   public static List<SubjectTopicOptionDTO> getSubjectTopicOptionMock(){
        LinkedList<SubjectDTO> subjects = new LinkedList<>(createSubjectDTOMock());

        return subjects.stream()
                .map(s -> new SubjectTopicOptionDTO(
                        s.getId(),
                        s.getDescription(),
                        (s.getTopics() != null && !s.getTopics().isEmpty()) ? s.getTopics().getFirst() : null
                ))
                .collect(Collectors.toList());
    }


}
