package cn.edu.sdu.java.server.repositorys;
import cn.edu.sdu.java.server.models.Student;
import cn.edu.sdu.java.server.models.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Integer>{
    Optional<Teacher> findByPersonId(Integer personId);
    Optional<Teacher> findByPersonNum(String num);
    List<Teacher> findByPersonName(String name);
    //1、通过命名规范定义查询方法

    @Query(value = "from Teacher where ?1='' or person.num like %?1% or person.name like %?1% ")
    List<Teacher> findTeacherListByNumName(String numName);
    //2、通过注解方式实现基于实体对象和属性的查询

    //第三种见手册

    @Query(value = "from Teacher where ?1='' or person.num like %?1% or person.name like %?1% ",
            countQuery = "SELECT count(personId) from Teacher where ?1='' or person.num like %?1% or person.name like %?1% ")
    Page<Student> findTeacherPageByNumName(String numName, Pageable pageable);
}
