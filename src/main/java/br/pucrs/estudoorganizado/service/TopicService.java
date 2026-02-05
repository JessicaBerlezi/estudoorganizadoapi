package br.pucrs.estudoorganizado.service;

import br.pucrs.estudoorganizado.controller.dto.SubjectTopicOptionDTO;
import br.pucrs.estudoorganizado.controller.dto.TopicSummaryDTO;
import br.pucrs.estudoorganizado.entity.TopicEntity;
import br.pucrs.estudoorganizado.entity.map.TopicMapper;
import br.pucrs.estudoorganizado.repository.SubjectRepository;
import br.pucrs.estudoorganizado.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;
    private final SubjectRepository subjectRepository;



    @Transactional(readOnly = true)
    public List<SubjectTopicOptionDTO> getTopicToStudyPerSubject() {

        List<TopicEntity> topics = topicRepository.findFirstAvailableTopicPerSubject();

        return topics.stream()
                .map(topic -> new SubjectTopicOptionDTO(
                        topic.getSubject().getId(),
                        topic.getSubject().getDescription(),
                        TopicMapper.convertToSummaryDTO(topic)
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TopicSummaryDTO> getTopicsToStudyBySubjectId(Long subjectId) {

        if (!subjectRepository.existsById(subjectId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Subject not found: " + subjectId
            );
        }

        return topicRepository.findTopicsToStudyBySubjectId(subjectId)
                .stream()
                .map(TopicMapper::convertToSummaryDTO)
                .toList();
    }

}

