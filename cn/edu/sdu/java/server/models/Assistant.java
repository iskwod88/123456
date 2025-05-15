package cn.edu.sdu.java.server.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
@Table( name = "Assistant",
        uniqueConstraints = {
        })
public class Assistant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer assistantId;

    @ManyToOne
    @JoinColumn(name = "personId")
    private Teacher teacher;
    //多对一：多个助理ID对一个老师

//    private Student student;
    @Size(max=20)
    private String assistantName;
    private Integer assistantNum;
    private Double wage;
    @Size(max=50)
    private String time;
}