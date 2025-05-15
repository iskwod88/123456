package cn.edu.sdu.java.server.controllers;
import cn.edu.sdu.java.server.payload.request.DataRequest;
import cn.edu.sdu.java.server.payload.response.DataResponse;
import cn.edu.sdu.java.server.services.TeacherService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teacher")
public class TeacherController {
    private final TeacherService teacherService;
    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @PostMapping("/getTeacherList")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getTeacherList(@Valid @RequestBody DataRequest dataRequest) {
        return teacherService.getTeacherList(dataRequest);
    }

    //删除有待补充

    @PostMapping("/getTeacherInfo")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getTeacherInfo(@Valid @RequestBody DataRequest dataRequest) {
        return teacherService.getTeacherInfo(dataRequest);
    }

    @PostMapping("/teacherEditSave")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse teacherEditSave(@Valid @RequestBody DataRequest dataRequest) {
        return teacherService.teacherEditSave(dataRequest);
    }

    @PostMapping("/getTeacherAssistantById")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse getTeacherAssistantById(@Valid @RequestBody DataRequest dataRequest) {
        return teacherService.getTeacherAssistantById(dataRequest);
    }

    @PostMapping("/getTeacherAssistantByName")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse getTeacherAssistantByName(@Valid @RequestBody DataRequest dataRequest) {
        return teacherService.getTeacherAssistantByName(dataRequest);
    }

    @PostMapping("/getTeacherAssistantByNum")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse getTeacherAssistantByNum(@Valid @RequestBody DataRequest dataRequest) {
        return teacherService.getTeacherAssistantByNum(dataRequest);
    }

    //教师成绩有待补充

    //导出excel有待补充

    //家庭成员有待补充(等效于教师助理)

    //教室工资有待补充

}
