package cn.edu.sdu.java.server.services;


import cn.edu.sdu.java.server.repositorys.StudentRepository;
import cn.edu.sdu.java.server.repositorys.TeacherRepository;
import cn.edu.sdu.java.server.repositorys.VolunteerRepository;

public class VolunteerService {
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final VolunteerRepository volunteerRepository;

    public VolunteerService(TeacherRepository teacherRepository, StudentRepository studentRepository, VolunteerRepository volunteerRepository) {
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.volunteerRepository = volunteerRepository;
    }
}
