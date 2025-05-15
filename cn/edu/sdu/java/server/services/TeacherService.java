package cn.edu.sdu.java.server.services;
import cn.edu.sdu.java.server.models.*;
import cn.edu.sdu.java.server.repositorys.AssistantRepository;
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
public class TeacherService {
    private static final Logger log = LoggerFactory.getLogger(TeacherService.class);
    private final PersonRepository personRepository;  //人员数据操作自动注入
    private final TeacherRepository teacherRepository;  //学生数据操作自动注入
    private final AssistantRepository assistantRepository;
    private final UserRepository userRepository;  //学生数据操作自动注入
    private final UserTypeRepository userTypeRepository; //用户类型数据操作自动注入
    private final PasswordEncoder encoder;  //密码服务自动注入
    private final SystemService systemService;
    public TeacherService(PersonRepository personRepository, TeacherRepository teacherRepository, AssistantRepository assistantRepository, UserRepository userRepository, UserTypeRepository userTypeRepository, PasswordEncoder encoder,  FeeRepository feeRepository, FamilyMemberRepository familyMemberRepository, SystemService systemService) {
        this.personRepository = personRepository;
        this.teacherRepository = teacherRepository;
        this.assistantRepository = assistantRepository;
        this.userRepository = userRepository;
        this.userTypeRepository = userTypeRepository;
        this.encoder = encoder;
        //fee
        //familyMember
        this.systemService = systemService;
    }

    public Map<String,Object> getMapFromTeacher(Teacher t) {
        Map<String,Object> m = new HashMap<>();
        Person p;
        if(t == null)
            return m;
        m.put("title",t.getTitle());
        m.put("degree",t.getDegree());
        m.put("response",t.getResponse());
        p = t.getPerson();
        if(p == null)
            return m;
        m.put("personId", t.getPersonId());
        m.put("num",p.getNum());
        m.put("name",p.getName());
        m.put("dept",p.getDept());
        m.put("card",p.getCard());
        String gender = p.getGender();
        m.put("gender",gender);
        m.put("genderName", ComDataUtil.getInstance().getDictionaryLabelByValue("XBM", gender)); //性别类型的值转换成数据类型名
        m.put("birthday", p.getBirthday());  //时间格式转换字符串
        m.put("email",p.getEmail());
        m.put("phone",p.getPhone());
        m.put("address",p.getAddress());
        m.put("introduce",p.getIntroduce());
        return m;
    }

    public List<Map<String,Object>> getTeacherMapList(String numName) {
        List<Map<String,Object>> dataList = new ArrayList<>();
        List<Teacher> sList = teacherRepository.findTeacherListByNumName(numName);  //数据库查询操作
        if (sList == null || sList.isEmpty())
            return dataList;
        for (Teacher teacher : sList) {
            dataList.add(getMapFromTeacher(teacher));
        }
        return dataList;
    }

    public DataResponse getTeacherList(DataRequest dataRequest) {
        String numName = dataRequest.getString("numName");
        List<Map<String,Object>> dataList = getTeacherMapList(numName);
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }

    //删除有待补充

    public DataResponse getTeacherInfo(DataRequest dataRequest) {
        Integer personId = dataRequest.getInteger("personId");
        Teacher t = null;
        Optional<Teacher> op;
        if (personId != null) {
            op = teacherRepository.findById(personId); //根据学生主键从数据库查询学生的信息
            if (op.isPresent()) {
                t = op.get();
            }
        }
        return CommonMethod.getReturnData(getMapFromTeacher(t)); //这里回传包含学生信息的Map对象
    }

    //此为添加新老师
    public DataResponse teacherEditSave(DataRequest dataRequest) {
        Integer personId = dataRequest.getInteger("personId");
        Map<String,Object> form = dataRequest.getMap("form"); //参数获取Map对象
        String num = CommonMethod.getString(form, "num");  //Map 获取属性的值
        Teacher t = null;
        Person p;
        User u;
        Optional<Teacher> op;
        boolean isNew = false;
        if (personId != null) {
            op = teacherRepository.findById(personId);  //查询对应数据库中主键为id的值的实体对象
            if (op.isPresent()) {
                t = op.get();
            }
        }
        Optional<Person> nOp = personRepository.findByNum(num); //查询是否存在num的人员
        if (nOp.isPresent()) {
            if (t == null || !t.getPerson().getNum().equals(num)) {
                return CommonMethod.getReturnMessageError("新工号已经存在，不能添加或修改！");
            }
        }
        if (t == null) {
            p = new Person();
            p.setNum(num);
            p.setType("1");
            personRepository.saveAndFlush(p);  //插入新的Person记录
            personId = p.getPersonId();
            String password = encoder.encode("123456");
            u = new User();
            u.setPersonId(personId);
            u.setUserName(num);
            u.setPassword(password);
            u.setUserType(userTypeRepository.findByName(EUserType.ROLE_STUDENT));
            u.setCreateTime(DateTimeTool.parseDateTime(new Date()));
            u.setCreatorId(CommonMethod.getPersonId());
            userRepository.saveAndFlush(u); //插入新的User记录
            t = new Teacher();   // 创建实体对象
            t.setPersonId(personId);
            teacherRepository.saveAndFlush(t);  //插入新的Student记录
            isNew = true;
        } else {
            p = t.getPerson();
        }
        personId = p.getPersonId();
        if (!num.equals(p.getNum())) {   //如果人员编号变化，修改人员编号和登录账号
            Optional<User> uOp = userRepository.findByPersonPersonId(personId);
            if (uOp.isPresent()) {
                u = uOp.get();
                u.setUserName(num);
                userRepository.saveAndFlush(u);
            }
            p.setNum(num);  //设置属性
        }
        p.setName(CommonMethod.getString(form, "name"));
        p.setDept(CommonMethod.getString(form, "dept"));
        p.setCard(CommonMethod.getString(form, "card"));
        p.setGender(CommonMethod.getString(form, "gender"));
        p.setBirthday(CommonMethod.getString(form, "birthday"));
        p.setEmail(CommonMethod.getString(form, "email"));
        p.setPhone(CommonMethod.getString(form, "phone"));
        p.setAddress(CommonMethod.getString(form, "address"));
        personRepository.save(p);  // 修改保存人员信息
        t.setTitle(CommonMethod.getString(form, "title"));
        t.setDegree(CommonMethod.getString(form, "degree"));
        t.setResponse(CommonMethod.getString(form, "response"));
        teacherRepository.save(t);  //修改保存学生信息
        systemService.modifyLog(t,isNew);
        return CommonMethod.getReturnData(t.getPersonId());  // 将personId返回前端
    }

//下列五个方法为对教师助理的简化代码

    public DataResponse getTeacherAssistantById(DataRequest dataRequest) {
        Integer personId = dataRequest.getInteger("personId");
        List<Assistant> idList = assistantRepository.findByTeacherPersonId(personId);
        return getAssistantList(idList);
    }

    public DataResponse getTeacherAssistantByName(DataRequest dataRequest) {
        String assistantName = dataRequest.getString("assistantName");
        List<Assistant> nameList = assistantRepository.findByAssistantName(assistantName);
        return getAssistantList(nameList);
    }

    public DataResponse getTeacherAssistantByNum(DataRequest dataRequest) {
        Integer assistantNum = dataRequest.getInteger("assistantNum");
        List<Assistant> numList = assistantRepository.findByAssistantNum(assistantNum);
        return getAssistantList(numList);
    }

    public Map<String, Object> getAssistantToMap(Assistant a) {
        Map<String, Object> m = new HashMap<>();
        if(a.getTeacher() == null && a == null) {
            return null;
        }
        m.put("assistantId", a.getAssistantId());
        m.put("personId", a.getTeacher().getPersonId());
        m.put("assistantName", a.getAssistantName());
        m.put("assistantNum", a.getAssistantNum());
        m.put("wage", a.getWage());
        m.put("time", a.getTime());
        return m;
    }

    public DataResponse getAssistantList(List<Assistant> aList) {
        if(aList == null) {
            return null;
        }
        List<Map<String, Object>> dataList= new ArrayList<>();
        for(Assistant a : aList) {
            Map<String, Object> m = getAssistantToMap(a);
            if(m != null){
                dataList.add(m);
            }
        }
        return CommonMethod.getReturnData(dataList);
    }
    //教师成绩有待补充

    //导出excel有待补充

    //家庭成员有待补充

    //教室工资有待补充
}
