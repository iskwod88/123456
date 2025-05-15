package cn.edu.sdu.java.server.repositorys;

import cn.edu.sdu.java.server.models.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VolunteerRepository extends JpaRepository<Volunteer, Integer> {
    @Query(value="from Volunteer where (?1=0 or student.personId=?1)")
    List<Volunteer> findByStudent(Integer studentId, Integer teacherId);

    @Query(value="from Volunteer where (?1=0 or student.personId=?1) and (?2=0 or teacher.personId=?2)" )
    List<Volunteer> findByStudentOrTeacher(Integer studentId, Integer teacherId);
}
