package cn.edu.sdu.java.server.repositorys;

import cn.edu.sdu.java.server.models.po.MenuInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
/*
 * MenuInfo 数据操作接口，主要实现MenuInfo数据的查询操作
 * Integer getMaxId()  menu 表中的最大的menu_id;    JPQL 注解
 * Optional<Person> findByNum(String num);  根据num查询获得Option<Person>对象,  命名规范
 * List<MenuInfo> findByUserTypeId(Integer userTypeId); 根据userTypeId查询获得pid为空的 菜单List<MenuInfo>集合 查询相应角色的所有跟菜单 JPQL 注解
 * List<MenuInfo> findByUserTypeIdAndPid(Integer userTypeId, Integer pid);根据userTypeId和pid查询获得pid的所有子菜单List<MenuInfo>集合 命名规范
 */
public interface MenuInfoRepository extends JpaRepository<MenuInfo,Integer> {
    @Query(value=" from MenuInfo where (pid is null or pid=0) and (?1='' or userTypeIds like %?1%)")
    List<MenuInfo> findByUserTypeIds(String userTypeIds);
    @Query(value=" from MenuInfo where pid =?2 and (?1='' or userTypeIds like %?1%)")
    List<MenuInfo> findByUserTypeIds(String userTypeIds, Integer pid);

    int countMenuInfoByPid(Integer count);
}
