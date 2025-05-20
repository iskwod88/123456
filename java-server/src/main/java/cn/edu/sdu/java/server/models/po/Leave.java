package cn.edu.sdu.java.server.models.po;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
//自动生成getter和setter方法
@Getter
@Setter
@Entity
@Table(name = "`leave`",
        uniqueConstraints = {
        }
)
public class Leave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Size(max=10)
    private String name;
    @Size(max=225)
    private String time;
    @Size(max=225)
    private String day;

    private Integer userId;
    @Size(max=10)
    private String status;
    @Size(max=225)
    private String reason;

}
