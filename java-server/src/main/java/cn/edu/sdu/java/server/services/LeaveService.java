package cn.edu.sdu.java.server.services;

import cn.edu.sdu.java.server.models.po.Leave;
import cn.edu.sdu.java.server.payload.response.DataResponse;
import cn.edu.sdu.java.server.repositorys.LeaveRepository;
import cn.edu.sdu.java.server.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LeaveService {

    @Autowired
    private LeaveRepository leaveRepository;

    // 查询所有请假记录
    public List<Leave> getAllLeaves() {
        return leaveRepository.findAll();
    }

    public Optional<Leave> getLeaveById(Integer id) {
        return leaveRepository.findById(id);
    }

    // 新增或更新请假记录
    public DataResponse saveLeave(Leave leave) {
        try {
            leave.setId(null);
        leaveRepository.save(leave);
        return CommonMethod.getReturnMessageOK();}
        catch (Exception e) {
            return null;
        }
    }

    // 删除请假记录
    public void deleteLeave(Integer id) {
        leaveRepository.deleteById(id);
    }
    // 审批请假
    public DataResponse approveLeave(Integer id) {
        Optional<Leave> optionalLeave = getLeaveById(id);
        if (optionalLeave.isPresent()) {
            Leave leave = optionalLeave.get();
            leave.setStatus("approved");
            return saveLeave(leave);
        }
        throw new RuntimeException("请假记录不存在");
    }

    // 驳回请假
    public DataResponse rejectLeave(Integer id) {
        Optional<Leave> optionalLeave = getLeaveById(id);
        if (optionalLeave.isPresent()) {
            Leave leave = optionalLeave.get();
            leave.setStatus("rejected");
            return saveLeave(leave);
        }
        throw new RuntimeException("请假记录不存在");
    }
}
