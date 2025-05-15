package cn.edu.sdu.java.server.services;

import cn.edu.sdu.java.server.models.Leave;
import cn.edu.sdu.java.server.repositorys.LeaveRepository;
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
    public Leave saveLeave(Leave leave) {
        return leaveRepository.save(leave);
    }

    // 删除请假记录
    public void deleteLeave(Integer id) {
        leaveRepository.deleteById(id);
    }
}
