package cn.edu.sdu.java.server.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
@Table( name = "Classroom",
        uniqueConstraints = {
        })
public class Classroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer classroomId;

    @Size(max = 20)
    private String classroomName;

    @Size(max = 2)
    private Boolean isActive;

    @Size(max = 20)
    private String studentName;

    @Size(max = 20)
    private Integer studentNum;

}
