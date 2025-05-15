package cn.edu.sdu.java.server.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;


/**
 * Student学生表实体类 保存每个学生的信息，
 * Integer personId 学生表 student 主键 person_id 与Person表主键相同
 * Person person 关联到该用户所用的Person对象，账户所对应的人员信息 person_id 关联 person 表主键 person_id
 * String major 专业
 * String className 班级
 *
 */
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(	name = "student",
        uniqueConstraints = {
        })
public class Student {
    @Id
    private Integer personId;

    @OneToOne
    @JoinColumn(name="personId")
    @JsonIgnore
    private Person person;

    @Size(max = 20)
    private String major;

    @Size(max = 50)
    private String className;

    @ManyToMany
    @JoinTable(
            name = "student_course",
            joinColumns = @JoinColumn(name = "personId"),
            inverseJoinColumns = @JoinColumn(name = "courseId")
    )
    private Set<Course> courses = new HashSet<>();
    // 此处多对多，需要添加指定关系的代码（维护方和被维护方）
    // 此外，还需要添加课程表和学生表之间的关联关系，在Sql实现
}
