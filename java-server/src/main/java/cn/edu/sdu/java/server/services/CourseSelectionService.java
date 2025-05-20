package cn.edu.sdu.java.server.services;

import cn.edu.sdu.java.server.mapper.CourseMapper;
import cn.edu.sdu.java.server.mapper.CourseSelectionMapper;
import cn.edu.sdu.java.server.mapper.UserMapper;
import cn.edu.sdu.java.server.models.po.Course;
import cn.edu.sdu.java.server.payload.response.DataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseSelectionService {

    @Autowired
    private CourseSelectionMapper courseSelectionMapper;
    @Autowired
    private CourseMapper courseMapper;

    /**
     * 搜索课程
     */
    public DataResponse searchCourse(String keyword, String term, String type) {
        try {
            List<Course> courses = courseMapper.searchCourses(keyword, term, type);
            return DataResponse.ok(courses, "搜索成功");
        } catch (Exception e) {
            return DataResponse.getReturnMessageError("搜索课程失败：" + e.getMessage());
        }
    }


    /**
     * 选课
     */
    public DataResponse selectCourse(String userId, Integer courseId) {
        try {
            Course course = courseMapper.getCourseById(courseId);
            if (course == null) {
                return DataResponse.error(1, "课程不存在");
            }

            courseSelectionMapper.insertSelection(userId, courseId);

            return DataResponse.ok(null, "选课成功");
        } catch (Exception e) {
            return DataResponse.getReturnMessageError("选课失败：" + e.getMessage());
        }
    }


    /**
     * 退选课程
     */
    public DataResponse dropCourse(String userId, Integer courseId) {
        try {
            if (!courseSelectionMapper.hasSelected(userId, courseId)) {
                return DataResponse.error(1, "未选择该课程");
            }

            courseSelectionMapper.deleteSelection(userId, courseId);

            return DataResponse.ok(null, "退选成功");
        } catch (Exception e) {
            return DataResponse.getReturnMessageError("退选失败：" + e.getMessage());
        }
    }


    /**
     * 查询选课结果
     */
    public DataResponse getCourseSelectionResults(String userId) {
        try {
            List<Course> selectedCourses = courseSelectionMapper.getSelectedCourses(userId);
            return DataResponse.ok(selectedCourses, "查询成功");
        } catch (Exception e) {
            return DataResponse.getReturnMessageError("查询选课结果失败：" + e.getMessage());
        }
    }

    /**
     * 获取未选课程
     */
    public DataResponse getUnselectedCourses(String userId) {
        try {
            List<Course> allCourses = courseMapper.getAllCourses();
            List<Course> selectedCourses = courseSelectionMapper.getSelectedCourses(userId);

            // 筛选出未选课程
            List<Course> unselectedCourses = allCourses.stream()
                    .filter(course -> !selectedCourses.contains(course))
                    .toList();

            return DataResponse.ok(unselectedCourses, "获取未选课程成功");
        } catch (Exception e) {
            return DataResponse.getReturnMessageError("获取未选课程失败：" + e.getMessage());
        }
    }
}
