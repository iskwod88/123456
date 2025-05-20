package cn.edu.sdu.java.server.models.dto;

import lombok.Data;

@Data
public class CourseSelectionResultDTO {
    private Integer id;
    private String num;
    private String name;
    private Integer credit;
    private Integer pre_course_id;
    private String course_path;
}
