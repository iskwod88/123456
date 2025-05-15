package cn.edu.sdu.java.server.repositorys;

import cn.edu.sdu.java.server.models.Classroom;
import cn.edu.sdu.java.server.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ClassroomRepository extends JpaRepository<Classroom, Integer>{
    @Query(value = "from Classroom where ?1='' or classroomId like %?1%")
    Optional<Classroom> findClassroomById(Integer classroomId);

    @Query(value = "from Classroom where ?1='' or classroomName like %?1%")
    List<Classroom> findClassroomListByName(String classroomName);

    Optional<Classroom> findByClassroomName(String classroomName);

}

