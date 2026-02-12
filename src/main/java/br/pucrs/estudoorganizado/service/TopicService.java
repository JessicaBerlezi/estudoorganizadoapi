package br.pucrs.estudoorganizado.service;

import br.pucrs.estudoorganizado.controller.dto.SubjectTopicOptionDTO;
import br.pucrs.estudoorganizado.controller.dto.TopicSummaryDTO;
import br.pucrs.estudoorganizado.entity.TopicEntity;
import br.pucrs.estudoorganizado.entity.map.TopicMapper;
import br.pucrs.estudoorganizado.repository.SubjectRepository;
import br.pucrs.estudoorganizado.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository repository;
    private final SubjectRepository subjectRepository;

    private static final Logger logger = LoggerFactory.getLogger(TopicService.class);

    @Transactional(readOnly = true)
    public List<SubjectTopicOptionDTO> getNextTopicPerSubject() {

        List<TopicEntity> topics = repository.findFirstAvailableTopicPerSubject();

        return topics.stream()
                .map(topic -> new SubjectTopicOptionDTO(
                        topic.getSubject().getId(),
                        topic.getSubject().getDescription(),
                        TopicMapper.convertToSummaryDTO(topic)
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TopicSummaryDTO> getTopicsBySubjectId(Long subjectId) {

        if (!subjectRepository.existsById(subjectId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Subject not found: " + subjectId
            );
        }

        return repository.findTopicsToStudyBySubjectId(subjectId)
                .stream()
                .map(TopicMapper::convertToSummaryDTO)
                .toList();
    }

    public List<TopicEntity> getExistingTopicsById(List<Long> topicsId) {

        logger.debug("Buscando tópicos para o ciclo de estudo. IDs={}", topicsId);

        List<TopicEntity> topics = new ArrayList<>();
        for (Long id : topicsId) {
            try {
                repository.findById(id).ifPresentOrElse(
                        topics::add,
                        () -> logger.warn("Tópico não encontrado. ID={}, continuando com os demais.", id)
                );
            } catch (Exception e){
                logger.error("Erro inesperado ao buscar tópicos na base de dados. ID={}", topicsId, e);
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Erro inesperado ao processar a requisição"
                );
            }
        }
        return topics;
    }
}

