package cn.edu.sdu.java.server.models.po;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@TableName("course_reg")
public class CourseSelection {
    @TableId(type= IdType.AUTO)
    private  Integer id;
    private  Integer studentId;
    private  Integer courseId;
    private  String  classNum;
}
/*
需要引入mybatis-plus的依赖
 */
