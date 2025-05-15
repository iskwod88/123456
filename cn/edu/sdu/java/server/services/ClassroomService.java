package cn.edu.sdu.java.server.services;

import cn.edu.sdu.java.server.models.*;
import cn.edu.sdu.java.server.payload.request.DataRequest;
import cn.edu.sdu.java.server.payload.response.DataResponse;
import cn.edu.sdu.java.server.repositorys.*;
import cn.edu.sdu.java.server.util.ComDataUtil;
import cn.edu.sdu.java.server.util.CommonMethod;
import cn.edu.sdu.java.server.util.DateTimeTool;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.List;

@Service
public class ClassroomService {
    private static final Logger log = LoggerFactory.getLogger(ClassroomService.class);
    private final ClassroomRepository classroomRepository;
    public ClassroomService(ClassroomRepository classroomRepository) {
        this.classroomRepository = classroomRepository;
    }

    public Map<String,Object> getMapFromClassroom(Classroom classroom) {
        Map<String,Object> m = new HashMap<>();
        if(classroom == null)
            return m;
        m.put("classroomId",classroom.getClassroomId());
        m.put("classroomName",classroom.getClassroomName());
        if(classroom.getIsActive()) {
            m.put("isActive",1);
        }else {
            m.put("isActive",0);
        }
        m.put("studentName",classroom.getStudentName());
        m.put("studentNum",classroom.getStudentNum());
        return m;
    }

    public List<Map<String,Object>> getClassroomMapList(String classroomName) {
        List<Map<String,Object>> list = new ArrayList<>();
        List<Classroom> classroomList = classroomRepository.findClassroomListByName(classroomName);
        if(classroomList == null || classroomList.isEmpty())
            return list;
        for(Classroom c : classroomList) {
            list.add(getMapFromClassroom(c));
        }
        return list;
    }

    public DataResponse getClassroomList(DataRequest dataRequest) {
        String classroomName = dataRequest.getString("classroomName");
        List<Map<String,Object>> list = getClassroomMapList(classroomName);
        return CommonMethod.getReturnData(list);
    }

    public DataResponse getClassroomInfo(DataRequest dataRequest) {
        Integer classroomId = dataRequest.getInteger("classroomId");
        Classroom c = null;
        Optional<Classroom> op;
        if (classroomId != null) {
            op = classroomRepository.findClassroomById(classroomId);
            if (op.isPresent()) {
                c = op.get();
            }
        }
        return CommonMethod.getReturnData(getMapFromClassroom(c)); //这里回传包含学生信息的Map对象
    }

    public DataResponse classroomOccupation(DataRequest dataRequest) {
        Integer classroomId = dataRequest.getInteger("classroomId");
        Map<String,Object> form = dataRequest.getMap("form");
        String classroomName = CommonMethod.getString(form, "classroomName");
        Classroom c;
        Optional<Classroom> op;
        if(classroomId != null) {
            op = classroomRepository.findClassroomById(classroomId);
        }

        return CommonMethod.getReturnData(0);









    }
//    public List<Map<String,Object>> getClassroomMapList() {
//        List<Map<String,Object>> list = new ArrayList<>();
//        List<Classroom> classroomList = classroomRepository.findClassroomListByName();
//
//
//        return list;
//    }

}
