package cn.edu.sdu.java.server.controllers;

import cn.edu.sdu.java.server.models.Leave;
import cn.edu.sdu.java.server.services.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaves")
public class LeaveController {

    @Autowired
    private LeaveService leaveService;

    // 获取所有请假记录
    @GetMapping
    public List<Leave> getAllLeaves() {
        return leaveService.getAllLeaves();
    }

    // 获取单个请假记录
    @GetMapping("/{id}")
    public Leave getLeave(@PathVariable Integer id) {
        return leaveService.getLeaveById(id).orElse(null);
    }

    // 新增请假记录
    @PostMapping
    public Leave createLeave(@RequestBody Leave leave) {
        return leaveService.saveLeave(leave);
    }

    // 更新请假记录
    @PutMapping("/{id}")
    public Leave updateLeave(@PathVariable Integer id, @RequestBody Leave leaveDetails) {
        Leave leave = leaveService.getLeaveById(id).orElse(null);
        if (leave != null) {
            leave.setTime(leaveDetails.getTime());
            leave.setDay(leaveDetails.getDay());
            leave.setReason(leaveDetails.getReason());
            leave.setStatus(leaveDetails.getStatus());
            return leaveService.saveLeave(leave);
        }
        return null;
    }

    // 删除请假记录
    @DeleteMapping("/{id}")
    public void deleteLeave(@PathVariable Integer id) {
        leaveService.deleteLeave(id);
    }
}
