package cn.edu.sdu.java.server.mapper;

import cn.edu.sdu.java.server.models.po.Course;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface CourseSelectionMapper extends BaseMapper<Course> {

    boolean hasSelected(String userId, Integer courseId);

    void insertSelection(String userId, Integer courseId);

    void deleteSelection(String userId, Integer courseId);

    List<Course> getSelectedCourses(String userId);
}
