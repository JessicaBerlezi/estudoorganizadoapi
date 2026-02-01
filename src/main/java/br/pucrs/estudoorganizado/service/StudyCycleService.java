package br.pucrs.estudoorganizado.service;

import br.pucrs.estudoorganizado.controller.MocksFactory;
import br.pucrs.estudoorganizado.controller.dto.SubjectDTO;
import br.pucrs.estudoorganizado.controller.dto.TopicSummaryDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Service
public class StudyCycleService {
    private static final Logger logger = LoggerFactory.getLogger(StudyCycleService.class);

    public List<TopicSummaryDTO> getTopicsBySubjectId(Long id) {
        LinkedList<SubjectDTO> subjects = MocksFactory.createSubjectDTOMock();

        List<TopicSummaryDTO> topics = getTopicsBySubjectId(subjects, id);
        if (!topics.isEmpty()) {
            logger.info("Topics by subject find: {}", topics);
        } else {
            logger.info("Subject not find for id: {}", id);
        }
        return topics;
    }


    private static List<TopicSummaryDTO> getTopicsBySubjectId(LinkedList<SubjectDTO> subjects, Long id) {
        for (SubjectDTO subject : subjects) {
            if (Objects.equals(subject.getId(), id)) {
                return subject.getTopics();
            }
        }
        return List.of();
    }

}
