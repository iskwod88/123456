package cn.edu.sdu.java.server.models.po;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "course")
@TableName("course") // MyBatis-Plus 表名映射
@Data
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(value = "course_id", type = IdType.AUTO) // MyBatis-Plus 主键注解
    private Integer courseId;

    @NotBlank
    @Size(max = 20)
    private String num;

    @Size(max = 50)
    private String name;

    private Integer credit;

    @ManyToOne
    @JoinColumn(name="pre_course_id")
    private Course preCourse;

    @Size(max = 12)
    private String coursePath;
}
