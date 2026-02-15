package br.pucrs.estudoorganizado.configuration.admincontrol;

import br.pucrs.estudoorganizado.entity.*;
import br.pucrs.estudoorganizado.entity.enumerate.ReviewStatusEnum;
import br.pucrs.estudoorganizado.entity.enumerate.StudyStatusEnum;
import br.pucrs.estudoorganizado.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PopulateHelperService {
    private final StudyCycleRepository studyCycleRepository;

    private final StudyCycleItemRepository studyCycleItemRepository;
    private final SubjectRepository subjectRepository;
    private final ReviewControlRepository reviewControlRepository;
    private final StudyRecordRepository studyRecordRepository;

    @Transactional
    public void populateData(){

        studyRecordRepository.deleteAll();
        reviewControlRepository.deleteAll();
        studyCycleRepository.deleteAll();
        studyCycleItemRepository.deleteAll();
        subjectRepository.deleteAll();


        SubjectEntity portugues = buildPortugues();
        SubjectEntity seguranca = buildSeguranca();
        SubjectEntity dconstitucional = buildDConst();
        SubjectEntity analiseDados = buildAnaliseDados();
        SubjectEntity bancoDados = buildBancoDados();
        SubjectEntity dadmin = buildDAdministrativo();

        // Garante que Topics já tenham ID no banco
        subjectRepository.flush();

        StudyCycleEntity cycle1 = new StudyCycleEntity(
                "Ciclo estudo primeira semana",
                StudyStatusEnum.DONE,
                LocalDateTime.now(),
                "anotacao",
                null
        );

        List<StudyCycleItemEntity> itens1 = new ArrayList<>();
        itens1.add(new StudyCycleItemEntity(cycle1, portugues.getTopics().getFirst()));
        itens1.add(new StudyCycleItemEntity(cycle1, seguranca.getTopics().getFirst()));
        itens1.add(new StudyCycleItemEntity(cycle1, dconstitucional.getTopics().getFirst()));
        itens1.add(new StudyCycleItemEntity(cycle1, analiseDados.getTopics().getFirst()));

        cycle1.setItems(itens1);
        studyCycleRepository.saveAndFlush(cycle1);

        ReviewControlEntity review1 = new ReviewControlEntity();
        review1.setTopic(portugues.getTopics().getFirst());
        review1.setSequenceIndex(0);
        review1.setStatus(ReviewStatusEnum.PENDING);
        review1.setScheduleDate(LocalDate.now());
        reviewControlRepository.saveAndFlush(review1);


        ReviewControlEntity review2 = new ReviewControlEntity();
        review2.setTopic(seguranca.getTopics().getFirst());
        review2.setSequenceIndex(0);
        review2.setStatus(ReviewStatusEnum.DELAYED);
        review2.setScheduleDate(LocalDate.now().minusDays(4));
        reviewControlRepository.saveAndFlush(review2);

        ReviewControlEntity review3 = new ReviewControlEntity();
        review3.setTopic(analiseDados.getTopics().getFirst());
        review3.setSequenceIndex(0);
        review3.setStatus(ReviewStatusEnum.DELAYED);
        review3.setScheduleDate(LocalDate.now().minusDays(2));
        reviewControlRepository.saveAndFlush(review3);

        StudyCycleEntity cycle2 = new StudyCycleEntity(
                "Ciclo estudo segunda semana",
                StudyStatusEnum.IN_PROGRESS,
                LocalDateTime.now(),
                "anotacao",
                null
        );

        List<StudyCycleItemEntity> itens2 = new ArrayList<>();
        itens2.add(new StudyCycleItemEntity(cycle2, bancoDados.getTopics().getFirst()));
        itens2.add(new StudyCycleItemEntity(cycle2, dadmin.getTopics().getFirst()));
        itens2.add(new StudyCycleItemEntity(cycle2, portugues.getTopics().get(2)));
        itens2.add(new StudyCycleItemEntity(cycle2, analiseDados.getTopics().get(2)));

        cycle2.setItems(itens2);
        studyCycleRepository.saveAndFlush(cycle2);
    }

    private SubjectEntity buildSeguranca() {
        SubjectEntity subject = new SubjectEntity(
                "Segurança da informação",
                "Conteúdo em pdf, priorizar criptografia",
                null
        );
        subject.setIsActive(true);

        List<TopicEntity> topics = List.of(
                new TopicEntity("Princípios", 1, 1, 1, List.of(5, 15, 30), "Ir para questões", subject),
                new TopicEntity("Criptografia", 2, 4, 1, List.of(2, 7, 15, 30), null, subject),
                new TopicEntity("Certificados", 3, 2, 0, List.of(2, 7, 10, 30), null, subject)
        );

        subject.setTopics(new ArrayList<>(topics));
        return subjectRepository.save(subject);
    }
    private SubjectEntity buildPortugues() {
        SubjectEntity subject = new SubjectEntity(
                "Português",
                "Foco em interpretação e gramática",
                null
        );
        subject.setIsActive(true);

        List<TopicEntity> topics = List.of(
                new TopicEntity("Interpretação de textos", 1, 4, 1, List.of(2, 7, 15, 30), null, subject),
                new TopicEntity("Ortografia e acentuação", 2, 2, 1, List.of(5, 15, 30), null, subject),
                new TopicEntity("Morfologia", 3, 3, 1, List.of(3, 10, 20), null, subject),
                new TopicEntity("Sintaxe", 4, 4, 1, List.of(2, 7, 15, 30), null, subject),
                new TopicEntity("Semântica", 5, 2, 1, List.of(5, 15, 30), null, subject),
                new TopicEntity("Redação oficial", 6, 3, 1, List.of(2, 7, 15, 30), null, subject),
                new TopicEntity("Figuras de linguagem", 7, 1, 0, List.of(7, 15, 30), null, subject),
                new TopicEntity("Concordância e regência", 8, 3, 1, List.of(3, 10, 20), null, subject)
        );

        subject.setTopics(new ArrayList<>(topics));
        return subjectRepository.save(subject);
    }
    private SubjectEntity buildDConst() {
        SubjectEntity subject = new SubjectEntity(
                "Direito Constitucional",
                "Foco em direitos fundamentais e organização do Estado",
                null
        );
        subject.setIsActive(true);

        List<TopicEntity> topics = List.of(
                new TopicEntity("Direitos e garantias fundamentais", 1, 4, 1, List.of(2, 7, 15, 30), null, subject),
                new TopicEntity("Organização do Estado", 2, 3, 0, List.of(3, 10, 20), null, subject),
                new TopicEntity("Poder Legislativo", 3, 2, 0, List.of(5, 15, 30), null, subject),
                new TopicEntity("Poder Executivo", 4, 2, 0, List.of(5, 15, 30), null, subject),
                new TopicEntity("Poder Judiciário", 5, 2, 0, List.of(5, 15, 30), null, subject),
                new TopicEntity("Controle de constitucionalidade", 6, 3, 0, List.of(3, 10, 20), null, subject),
                new TopicEntity("Emendas constitucionais", 7, 1, 0, List.of(7, 15, 30), null, subject)
        );

        subject.setTopics(new ArrayList<>(topics));
        return subjectRepository.save(subject);
    }
    private SubjectEntity buildAnaliseDados() {
        SubjectEntity subject = new SubjectEntity(
                "Análise de Dados",
                "Foco em estatística aplicada e ferramentas",
                null
        );
        subject.setIsActive(true);

        List<TopicEntity> topics = new ArrayList<>();

        topics.add(new TopicEntity("Exploração de dados (EDA)", 1, 4, 1, List.of(2, 7, 15, 30), null, subject));
        topics.add(new TopicEntity("Visualização de dados", 2, 3, 1, List.of(3, 10, 20), null, subject));
        topics.add(new TopicEntity("Estatística descritiva", 3, 4, 1, List.of(2, 7, 15, 30), null, subject));
        topics.add(new TopicEntity("Inferência estatística", 4, 3, 0, List.of(3, 10, 20), null, subject));
        topics.add(new TopicEntity("Machine Learning básico", 5, 2, 0, List.of(5, 15, 30), null, subject));
        topics.add(new TopicEntity("Ferramentas de análise (Excel, Python, R)", 6, 2, 1, List.of(5, 15, 30), null, subject));

        subject.setTopics(topics);
        return subjectRepository.save(subject);
    }
    private SubjectEntity buildBancoDados() {
        SubjectEntity subject = new SubjectEntity(
                "Dados",
                "Foco em modelagem e segurança",
                null
        );
        subject.setIsActive(true);

        List<TopicEntity> topics = new ArrayList<>();

        topics.add(new TopicEntity("Modelagem de dados", 1, 4, 1, List.of(2, 7, 15, 30), null, subject));
        topics.add(new TopicEntity("Banco de dados relacionais (SQL)", 2, 4, 1, List.of(2, 7, 15, 30), null, subject));
        topics.add(new TopicEntity("Normalização", 3, 3, 0, List.of(3, 10, 20), null, subject));
        topics.add(new TopicEntity("Big Data e NoSQL", 4, 2, 0, List.of(5, 15, 30), null, subject));
        topics.add(new TopicEntity("Segurança e integridade de dados", 5, 3, 0, List.of(3, 10, 20), null, subject));
        topics.add(new TopicEntity("Transações e controle de concorrência", 6, 2, 0, List.of(5, 15, 30), null, subject));
        topics.add(new TopicEntity("Armazenamento em nuvem", 7, 1, 0, List.of(7, 15, 30), null, subject));

        subject.setTopics(topics);
        return subjectRepository.save(subject);
    }
    private SubjectEntity buildDAdministrativo() {
        SubjectEntity subject = new SubjectEntity(
                "Direito Administrativo",
                "Foco em princípios, atos e poderes administrativos",
                null
        );
        subject.setIsActive(true);

        List<TopicEntity> topics = new ArrayList<>();

        topics.add(new TopicEntity("Princípios da Administração Pública", 1, 4, 1, List.of(2, 7, 15, 30), null, subject));
        topics.add(new TopicEntity("Atos Administrativos", 2, 4, 1, List.of(2, 7, 15, 30), null, subject));
        topics.add(new TopicEntity("Poderes Administrativos", 3, 3, 0, List.of(3, 10, 20), null, subject));
        topics.add(new TopicEntity("Organização da Administração Pública", 4, 2, 0, List.of(5, 15, 30), null, subject));
        topics.add(new TopicEntity("Controle da Administração Pública", 5, 3, 0, List.of(3, 10, 20), null, subject));
        topics.add(new TopicEntity("Responsabilidade Civil do Estado", 6, 3, 0, List.of(3, 10, 20), null, subject));
        topics.add(new TopicEntity("Licitações e Contratos Administrativos", 7, 4, 1, List.of(2, 7, 15, 30), null, subject));
        topics.add(new TopicEntity("Servidores Públicos", 8, 2, 0, List.of(5, 15, 30), null, subject));

        subject.setTopics(topics);
        return subjectRepository.save(subject);
    }
}
