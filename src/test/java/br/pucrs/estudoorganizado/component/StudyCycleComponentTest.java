package br.pucrs.estudoorganizado.component;

import br.pucrs.estudoorganizado.controller.dto.StudyCycleDetailsDTO;
import br.pucrs.estudoorganizado.entity.StudyCycleEntity;
import br.pucrs.estudoorganizado.entity.StudyCycleItemEntity;
import br.pucrs.estudoorganizado.entity.SubjectEntity;
import br.pucrs.estudoorganizado.entity.TopicEntity;
import br.pucrs.estudoorganizado.entity.enumerate.StudyStatusEnum;
import br.pucrs.estudoorganizado.service.StudyCycleItemService;
import br.pucrs.estudoorganizado.service.StudyCycleService;
import br.pucrs.estudoorganizado.service.TopicService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.any;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)

public class StudyCycleComponentTest {

    @Mock
    private TopicService topicService;

    @Mock
    private StudyCycleService service;

    @Mock
    private StudyCycleItemService itemService;

    @InjectMocks
    private StudyCycleComponent component;


    @Test
    void shouldCreateStudyCycleSuccessfully() {

        // Arrange
        List<Long> topicDTOs = List.of(
             1L, 2L
        );

        StudyCycleDetailsDTO dto = new StudyCycleDetailsDTO(
                "Ciclo Teste",
                "Anotação Teste",
                topicDTOs
        );

        TopicEntity topic1 = new TopicEntity();
        topic1.setId(1L);

        TopicEntity topic2 = new TopicEntity();
        topic2.setId(2L);

        List<TopicEntity> topics = List.of(topic1, topic2);

        when(topicService.getExistingTopicsById(topicDTOs))
                .thenReturn(topics);

        StudyCycleEntity savedEntity = new StudyCycleEntity();
        savedEntity.setId(100L);

        when(service.saveStudyCycle(any(StudyCycleEntity.class)))
                .thenReturn(savedEntity);

        // Act
        StudyCycleEntity result = component.creteStudyCycle(dto);

        // Assert
        Assertions.assertNotNull(result);

        ArgumentCaptor<StudyCycleEntity> captor =
                ArgumentCaptor.forClass(StudyCycleEntity.class);

        verify(service).saveStudyCycle(captor.capture());

        StudyCycleEntity captured = captor.getValue();

        Assertions.assertEquals(2, captured.getItems().size());

        List<Long> topicIds = captured.getItems().stream()
                .map(item -> item.getTopic().getId())
                .toList();

        Assertions.assertTrue(topicIds.containsAll(List.of(1L, 2L)));

        verify(topicService).getExistingTopicsById(topicDTOs);
    }
    @Test
    void shouldThrowExceptionWhenNoTopicsFound() {

        // Arrange
        List<Long> topicDTOs = List.of(
                1L
        );

        StudyCycleDetailsDTO dto = new StudyCycleDetailsDTO(
                "Ciclo Teste",
                "Anotação Teste",
                topicDTOs
        );

        when(topicService.getExistingTopicsById(topicDTOs))
                .thenReturn(Collections.emptyList());

        // Act + Assert
        ResponseStatusException exception = Assertions.assertThrows(
                ResponseStatusException.class,
                () -> component.creteStudyCycle(dto)
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());

        verify(service, never()).saveStudyCycle(any());
    }

    private StudyCycleEntity createStudyCycleEntityMock(){
        // ---------- Subjects ----------
        SubjectEntity portugues = new SubjectEntity("PortuguÊs", null);

        SubjectEntity direitoAdmin = new SubjectEntity("Direito Administrativo", "a");

        SubjectEntity direitoConst = new SubjectEntity("Direito Constitucional", null);

        SubjectEntity segurancaInfo = new SubjectEntity("Segurança da Informação", "bbbb");

        // ---------- Topics ----------
        TopicEntity verbos = new TopicEntity();
        verbos.setId(101L);
        verbos.setDescription("Verbos");
        verbos.setSubject(portugues);

        TopicEntity licitacoes = new TopicEntity();
        licitacoes.setId(102L);
        licitacoes.setDescription("Licitações");
        licitacoes.setSubject(direitoAdmin);

        TopicEntity direitosFundamentais = new TopicEntity();
        direitosFundamentais.setId(103L);
        direitosFundamentais.setDescription("Direitos Fundamentais");
        direitosFundamentais.setSubject(direitoConst);

        TopicEntity criptografia = new TopicEntity();
        criptografia.setId(104L);
        criptografia.setDescription("Criptografia");
        criptografia.setSubject(segurancaInfo);

        StudyCycleEntity cycle = new StudyCycleEntity(
                "valid study cycle",
                StudyStatusEnum.PLANNED,
                null,
                null,
                new ArrayList<>()
        );


        // ---------- Items ----------
        StudyCycleItemEntity item1 = new StudyCycleItemEntity();
        item1.setId(1001L);
        item1.setStudyCycle(cycle);
        item1.setTopic(verbos);

        StudyCycleItemEntity item2 = new StudyCycleItemEntity();
        item2.setId(1002L);
        item2.setStudyCycle(cycle);
        item2.setTopic(licitacoes);

        StudyCycleItemEntity item3 = new StudyCycleItemEntity();
        item3.setId(1003L);
        item3.setStudyCycle(cycle);
        item3.setTopic(direitosFundamentais);

        StudyCycleItemEntity item4 = new StudyCycleItemEntity();
        item4.setId(1004L);
        item4.setStudyCycle(cycle);
        item4.setTopic(criptografia);

        cycle.getItems().addAll(
                List.of(item1, item2, item3, item4)
        );

        cycle.setId(1L);
        return cycle;
    }

    @Test
    void shouldUpdateStudyCycleByKeepingItemsWithoutChanging(){
        StudyCycleEntity cycle1Mock = createStudyCycleEntityMock();
        when(service.getStudyCycle(1L))
                .thenReturn(cycle1Mock);

        when(itemService.findAllByStudyCycleId(1L))
                .thenReturn(cycle1Mock.getItems());

        StudyCycleDetailsDTO dto = new StudyCycleDetailsDTO(
                "valid study cycle",
                null,
                List.of(101L, 102L, 103L, 104L)
        );

        ArgumentCaptor<StudyCycleEntity> captor =
                ArgumentCaptor.forClass(StudyCycleEntity.class);
        when(service.saveStudyCycle(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));
        component.updateStudyCycle(1L, dto);
        verify(service).saveStudyCycle(captor.capture());
        StudyCycleEntity savedEntity = captor.getValue();

        //Não irá chamar, pq não tem nada novo a ser salvo, topiIdsToAdd voltará vazio
        verify(topicService, never())
                .getExistingTopicsById(any());

        List<Long> resultingIds = savedEntity.getItems()
                .stream()
                .map(i -> i.getTopic().getId())
                .toList();

        Assertions.assertEquals(
                dto.getTopics(),
                resultingIds
        );
    }

    private StudyCycleItemEntity createNewStudyCycleItemMock(){
        StudyCycleEntity cycle1Mock = createStudyCycleEntityMock();

        TopicEntity newTopic = new TopicEntity();
        newTopic.setId(201L);
        newTopic.setDescription("Interpretacao");
        newTopic.setSubject(cycle1Mock.getItems().getFirst().getTopic().getSubject());


        StudyCycleItemEntity item5 = new StudyCycleItemEntity();
        item5.setId(2001L);
        item5.setStudyCycle(cycle1Mock);
        item5.setTopic(newTopic);

        return item5;
    }


    @Test
    void shouldUpdateStudyCycleByAddItems(){

        StudyCycleItemEntity newItem = createNewStudyCycleItemMock();
        TopicEntity newTopic = newItem.getTopic() ;
        StudyCycleDetailsDTO dto = new StudyCycleDetailsDTO(
                "valid study cycle",
                null,
                List.of(101L, 102L, 103L, 104L, newItem.getTopic().getId())
        );

        StudyCycleEntity cycle1Mock = createStudyCycleEntityMock();
        when(service.getStudyCycle(1L))
                .thenReturn(cycle1Mock);

        when(itemService.findAllByStudyCycleId(1L))
                .thenReturn(cycle1Mock.getItems());

        List<Long> topicIdsToAdd = List.of(newTopic.getId());
        List<TopicEntity> topicsToAdd =  List.of(newTopic);
        when(topicService.getExistingTopicsById(topicIdsToAdd))
                .thenReturn(topicsToAdd);

        ArgumentCaptor<StudyCycleEntity> captor =
                ArgumentCaptor.forClass(StudyCycleEntity.class);
        when(service.saveStudyCycle(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        component.updateStudyCycle(1L, dto);

        verify(service).saveStudyCycle(captor.capture());
        StudyCycleEntity savedEntity = captor.getValue();

        Assertions.assertEquals(savedEntity.getItems().getLast().getTopic().getId(),
                dto.getTopics().getLast());
        Assertions.assertTrue(
                savedEntity.getItems()
                        .stream()
                        .anyMatch(i -> i.getTopic().getId().equals(dto.getTopics().getLast()))
        );

        List<Long> resultingIds = savedEntity.getItems()
                .stream()
                .map(i -> i.getTopic().getId())
                .toList();

        Assertions.assertEquals(dto.getTopics().size(), resultingIds.size());
        Assertions.assertTrue(resultingIds.contains(201L));
        Assertions.assertEquals(dto.getTopics(), resultingIds);
    }

    @Test
    void shouldUpdateStudyCycleByRemovingItem() {
        StudyCycleDetailsDTO dto = new StudyCycleDetailsDTO(
                "valid study cycle",
                null,
                List.of(101L, 102L, 103L) // remove 104
        );

        StudyCycleEntity cycle1Mock = createStudyCycleEntityMock();
        when(service.getStudyCycle(1L))
                .thenReturn(cycle1Mock);

        when(itemService.findAllByStudyCycleId(1L))
                .thenReturn(cycle1Mock.getItems());

        ArgumentCaptor<StudyCycleEntity> captor =
                ArgumentCaptor.forClass(StudyCycleEntity.class);
        when(service.saveStudyCycle(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));
        component.updateStudyCycle(1L, dto);
        verify(service).saveStudyCycle(captor.capture());
        StudyCycleEntity savedEntity = captor.getValue();

        // Não deve buscar tópicos novos
        verify(topicService, never())
                .getExistingTopicsById(any());

        List<Long> resultingIds = savedEntity.getItems()
                .stream()
                .map(i -> i.getTopic().getId())
                .toList();

        Assertions.assertEquals(dto.getTopics().size(), resultingIds.size());
        Assertions.assertFalse(resultingIds.contains(104L));
        Assertions.assertEquals(dto.getTopics(), resultingIds);
    }

    @Test
    void shouldUpdateOnlyDescriptionAndKeepItems(){
        StudyCycleDetailsDTO dto = new StudyCycleDetailsDTO(
                "nova descricao",
                null,
                List.of(101L, 102L, 103L, 104L)
        );

        StudyCycleEntity cycle1Mock = createStudyCycleEntityMock();

        when(service.getStudyCycle(1L))
                .thenReturn(cycle1Mock);

        when(itemService.findAllByStudyCycleId(1L))
                .thenReturn(cycle1Mock.getItems());


        ArgumentCaptor<StudyCycleEntity> captor =
                ArgumentCaptor.forClass(StudyCycleEntity.class);
        when(service.saveStudyCycle(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));
        component.updateStudyCycle(1L, dto);
        verify(service).saveStudyCycle(captor.capture());
        StudyCycleEntity savedEntity = captor.getValue();

        //Não irá chamar, pq não tem nada novo a ser salvo, topiIdsToAdd voltará vazio
        verify(topicService, never())
                .getExistingTopicsById(any());

        Assertions.assertEquals(savedEntity.getDescription(), dto.getDescription());
        Assertions.assertNull(savedEntity.getAnnotation());
    }


    @Test
    void shouldUpdateStudyCycleByAlterAllPossibleInfo() {
        StudyCycleDetailsDTO dto = new StudyCycleDetailsDTO(
                "novo ciclo atualizado",
                "comentário importante",
                List.of(101L, 105L)
        );

        StudyCycleEntity cycleMock = createStudyCycleEntityMock();
        when(service.getStudyCycle(1L))
                .thenReturn(cycleMock);

        when(itemService.findAllByStudyCycleId(1L))
                .thenReturn(cycleMock.getItems());

        TopicEntity newTopic = new TopicEntity();
        newTopic.setId(105L);
        newTopic.setDescription("Novo Tópico");

        //buscou apenas o novo item 105
        when(topicService.getExistingTopicsById(List.of(105L)))
                .thenReturn(List.of(newTopic));

        ArgumentCaptor<StudyCycleEntity> captor =
                ArgumentCaptor.forClass(StudyCycleEntity.class);
        when(service.saveStudyCycle(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));
        component.updateStudyCycle(1L, dto);
        verify(service).saveStudyCycle(captor.capture());
        StudyCycleEntity savedEntity = captor.getValue();

        //  Valida alteração de atributos
        Assertions.assertEquals("novo ciclo atualizado", savedEntity.getDescription());
        Assertions.assertEquals("comentário importante", savedEntity.getAnnotation());

        //validação de itens
        List<Long> resultingIds = savedEntity.getItems()
                .stream()
                .map(i -> i.getTopic().getId())
                .toList();

        Assertions.assertEquals(2, resultingIds.size());
        Assertions.assertTrue(resultingIds.containsAll(dto.getTopics()));

        Assertions.assertTrue(resultingIds.contains(101L)); //permanece
        Assertions.assertTrue(resultingIds.contains(105L)); //adicionado

        Assertions.assertFalse(resultingIds.contains(102L)); //removidos
        Assertions.assertFalse(resultingIds.contains(103L));
        Assertions.assertFalse(resultingIds.contains(104L));
    }

    @Test
    void shouldDisableSubject() {
        StudyCycleEntity cycle = createStudyCycleEntityMock();

        when(service.getStudyCycle(1L)).thenReturn(cycle);

        component.disableSubject(1L);

        verify(service).deleteStudyCycle(cycle);
    }
}
