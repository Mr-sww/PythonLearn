package com.demo.python_demo.repository;

import com.demo.python_demo.entity.Role;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RoleRepository {
    
    @Select("SELECT role_id AS roleId, role_name AS roleName, role_code AS roleCode, description, permissions, create_time AS createTime, update_time AS updateTime FROM `role`")
    List<Role> findAll();
    
    @Select("SELECT role_id AS roleId, role_name AS roleName, role_code AS roleCode, description, permissions, create_time AS createTime, update_time AS updateTime FROM `role` WHERE role_id = #{roleId}")
    Role findById(Integer roleId);
    
    @Select("SELECT role_id AS roleId, role_name AS roleName, role_code AS roleCode, description, permissions, create_time AS createTime, update_time AS updateTime FROM `role` WHERE role_code = #{roleCode}")
    Role findByRoleCode(String roleCode);
    
    @Insert("INSERT INTO `role` (role_name, role_code, description, permissions) VALUES (#{roleName}, #{roleCode}, #{description}, #{permissions})")
    @Options(useGeneratedKeys = true, keyProperty = "roleId")
    int insert(Role role);
    
    @Update("UPDATE `role` SET role_name=#{roleName}, role_code=#{roleCode}, description=#{description}, permissions=#{permissions} WHERE role_id=#{roleId}")
    int update(Role role);
    
    @Delete("DELETE FROM `role` WHERE role_id = #{roleId}")
    int deleteById(Integer roleId);
    
    // 根据用户ID获取用户角色
    @Select("SELECT r.role_id AS roleId, r.role_name AS roleName, r.role_code AS roleCode, r.description, r.permissions, r.create_time AS createTime, r.update_time AS updateTime " +
            "FROM `role` r " +
            "JOIN `user_role` ur ON r.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId}")
    List<Role> findRolesByUserId(Integer userId);
    
    // 为用户分配角色
    @Insert("INSERT INTO `user_role` (user_id, role_id) VALUES (#{userId}, #{roleId})")
    int assignRoleToUser(@Param("userId") Integer userId, @Param("roleId") Integer roleId);
    
    // 移除用户角色
    @Delete("DELETE FROM `user_role` WHERE user_id = #{userId} AND role_id = #{roleId}")
    int removeRoleFromUser(@Param("userId") Integer userId, @Param("roleId") Integer roleId);
} 