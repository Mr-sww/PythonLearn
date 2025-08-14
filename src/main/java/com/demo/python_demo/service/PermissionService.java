package com.demo.python_demo.service;

import com.demo.python_demo.entity.Role;
import com.demo.python_demo.entity.Permission;

import java.util.List;

/**
 * 权限验证服务接口
 */
public interface PermissionService {

    /**
     * 检查用户是否有指定权限
     * @param userId 用户ID
     * @param permissionCode 权限代码
     * @return 是否有权限
     */
    boolean hasPermission(Integer userId, String permissionCode);

    /**
     * 检查用户是否有指定角色
     * @param userId 用户ID
     * @param roleCode 角色代码
     * @return 是否有角色
     */
    boolean hasRole(Integer userId, String roleCode);

    /**
     * 获取用户的所有角色
     * @param userId 用户ID
     * @return 角色列表
     */
    List<Role> getUserRoles(Integer userId);

    /**
     * 获取用户的所有权限
     * @param userId 用户ID
     * @return 权限列表
     */
    List<Permission> getUserPermissions(Integer userId);

    /**
     * 为用户分配角色
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return 是否成功
     */
    boolean assignRoleToUser(Integer userId, Integer roleId);

    /**
     * 移除用户角色
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return 是否成功
     */
    boolean removeRoleFromUser(Integer userId, Integer roleId);

    /**
     * 检查用户是否可以访问指定资源
     * @param userId 用户ID
     * @param resourceType 资源类型
     * @param resourceId 资源ID
     * @param action 操作类型
     * @return 是否可以访问
     */
    boolean canAccessResource(Integer userId, String resourceType, String resourceId, String action);
} 