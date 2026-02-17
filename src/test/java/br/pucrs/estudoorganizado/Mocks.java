package br.pucrs.estudoorganizado;

import br.pucrs.estudoorganizado.controller.dto.UpdateSubjectStructureDTO;
import br.pucrs.estudoorganizado.controller.dto.UpdateTopicStructureDTO;
import br.pucrs.estudoorganizado.entity.SubjectEntity;
import br.pucrs.estudoorganizado.entity.TopicEntity;

import java.util.List;

public class Mocks {

    public static SubjectEntity createSubjectEntityMock(){
        SubjectEntity portugues = new SubjectEntity("PortuguÊs", null);

        TopicEntity verbos = new TopicEntity();
        verbos.setDescription("Verbos");
        verbos.setSubject(portugues);

        TopicEntity pronomes = new TopicEntity();
        pronomes.setDescription("Pronomes");
        pronomes.setSubject(portugues);

        portugues.setTopics(List.of(verbos, pronomes));

        return portugues;
    }


    public static SubjectEntity createSubjectEntityMockWithId(){
        SubjectEntity portugues = new SubjectEntity("PortuguÊs", null);
        portugues.setId(101L);

        TopicEntity verbos = new TopicEntity();
        verbos.setId(101L);
        verbos.setDescription("Verbos");
        verbos.setSubject(portugues);

        TopicEntity pronomes = new TopicEntity();
        pronomes.setId(102L);
        pronomes.setDescription("Pronomes");
        pronomes.setSubject(portugues);

        portugues.setTopics(List.of(verbos, pronomes));

        return portugues;
    }

    public static UpdateSubjectStructureDTO createUpdateSubjectDTOMock(){
        UpdateTopicStructureDTO t1 = new UpdateTopicStructureDTO();
        t1.id = 101L;
        t1.description = "Verbos Editado";

        UpdateTopicStructureDTO t2 = new UpdateTopicStructureDTO();
        t2.id = 102L;
        t2.description = "Pronomes Editado";

        UpdateSubjectStructureDTO dto = new UpdateSubjectStructureDTO();
        dto.description = "descricao atualizacada";
        dto.topics =List.of(t1, t2);

        return dto;
    }
}
