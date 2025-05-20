package cn.edu.sdu.java.server.mapper;

import cn.edu.sdu.java.server.models.po.Course;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CourseMapper extends BaseMapper<Course> {

    List<Course> searchCourses(@Param("keyword") String keyword,
                               @Param("term") String term,
                               @Param("type") String type);

    Course getCourseById(Integer courseId);

    List<Course> getAllCourses();
}
