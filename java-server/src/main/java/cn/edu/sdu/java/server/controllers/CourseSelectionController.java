package cn.edu.sdu.java.server.controllers;

import cn.edu.sdu.java.server.annotation.Auth;
import cn.edu.sdu.java.server.payload.response.DataResponse;
import cn.edu.sdu.java.server.services.CourseSelectionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course-selection")
public class CourseSelectionController {

    @Autowired
    private CourseSelectionService courseSelectionService;
    @Autowired
    private HttpServletRequest request;

    /*
     * 搜索课程
     */
    @Auth
    @GetMapping("/search")
    public ResponseEntity<DataResponse> searchCourse(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "term", required = false) String term,
            @RequestParam(value = "type", required = false) String type) {
        return ResponseEntity.ok(courseSelectionService.searchCourse(keyword, term, type));
    }

    /*
     * 选课
     */
    @Auth
    @PostMapping("/select/{courseId}")
    public ResponseEntity<DataResponse> selectCourse(@PathVariable("courseId") Integer courseId) {
        String userId = (String) request.getAttribute("userId");
        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.ok(DataResponse.getReturnMessageError("用户未登录"));
        }

        DataResponse result = courseSelectionService.selectCourse(userId, courseId);

        // 如果 data 是 null，则替换成空对象
        if (result.getData() == null) {
            result.setData(new Object());
        }

        return ResponseEntity.ok(result);
    }



    /*
     * 退选
     */
    @Auth
    @PostMapping("/drop/{courseId}")
    public ResponseEntity<DataResponse> dropCourse(@PathVariable("courseId") Integer courseId) {
        String userId = (String) request.getAttribute("userId");
        return ResponseEntity.ok(courseSelectionService.dropCourse(userId, courseId));
    }

    /*
     * 查询选课结果
     */
    @Auth
    @GetMapping("/results")
    public ResponseEntity<DataResponse> getCourseSelectionResults() {
        String userId = (String) request.getAttribute("userId");
        return ResponseEntity.ok(courseSelectionService.getCourseSelectionResults(userId));
    }

    /*
     * 获得未选课程
     */
    @Auth
    @GetMapping("/unselected")
    public ResponseEntity<DataResponse> getUnselectedCourses() {
        String userId = (String) request.getAttribute("userId");
        return ResponseEntity.ok(courseSelectionService.getUnselectedCourses(userId));
    }
}
