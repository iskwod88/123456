package cn.edu.sdu.java.server.repositorys;

import cn.edu.sdu.java.server.models.Assistant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssistantRepository extends JpaRepository<Assistant, Integer> {
    List<Assistant> findByTeacherPersonId(Integer personId);
    List<Assistant> findByAssistantName(String assistantName);
    List<Assistant> findByAssistantNum(Integer assistantNum);
}

