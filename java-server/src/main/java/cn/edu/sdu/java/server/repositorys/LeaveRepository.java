package cn.edu.sdu.java.server.repositorys;

import cn.edu.sdu.java.server.models.FamilyMember;
import cn.edu.sdu.java.server.models.Leave;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveRepository extends JpaRepository<Leave,Integer> {
}
