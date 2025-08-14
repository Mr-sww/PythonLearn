package com.demo.python_demo.service.impl;

import com.demo.python_demo.entity.Role;
import com.demo.python_demo.entity.Permission;
import com.demo.python_demo.entity.User;
import com.demo.python_demo.repository.RoleRepository;
import com.demo.python_demo.repository.UserRepository;
import com.demo.python_demo.service.PermissionService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限验证服务实现类
 */
@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean hasPermission(Integer userId, String permissionCode) {
        if (userId == null || permissionCode == null) {
            return false;
        }

        List<Role> userRoles = getUserRoles(userId);
        
        for (Role role : userRoles) {
            // 管理员拥有所有权限
            if ("ADMIN".equals(role.getRoleCode())) {
                return true;
            }
            
            // 检查角色权限
            if (role.getPermissions() != null) {
                try {
                    List<String> permissions = objectMapper.readValue(role.getPermissions(), new TypeReference<List<String>>() {});
                    if (permissions.contains(permissionCode) || permissions.contains("*")) {
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
        return false;
    }

    @Override
    public boolean hasRole(Integer userId, String roleCode) {
        if (userId == null || roleCode == null) {
            return false;
        }

        List<Role> userRoles = getUserRoles(userId);
        return userRoles.stream()
                .anyMatch(role -> roleCode.equals(role.getRoleCode()));
    }

    @Override
    public List<Role> getUserRoles(Integer userId) {
        return roleRepository.findRolesByUserId(userId);
    }

    @Override
    public List<Permission> getUserPermissions(Integer userId) {
        // 这里需要实现从角色权限关联表获取权限的逻辑
        // 暂时返回空列表，后续可以扩展
        return List.of();
    }

    @Override
    public boolean assignRoleToUser(Integer userId, Integer roleId) {
        try {
            // 检查用户和角色是否存在
            User user = userRepository.findById(userId);
            Role role = roleRepository.findById(roleId);
            
            if (user == null || role == null) {
                return false;
            }
            
            int result = roleRepository.assignRoleToUser(userId, roleId);
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean removeRoleFromUser(Integer userId, Integer roleId) {
        try {
            int result = roleRepository.removeRoleFromUser(userId, roleId);
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean canAccessResource(Integer userId, String resourceType, String resourceId, String action) {
        if (userId == null || resourceType == null || action == null) {
            return false;
        }

        // 构建权限代码
        String permissionCode = resourceType + ":" + action;
        
        // 检查基本权限
        if (!hasPermission(userId, permissionCode)) {
            return false;
        }

        // 特殊资源访问控制
        if ("course".equals(resourceType)) {
            return canAccessCourse(userId, resourceId, action);
        } else if ("assignment".equals(resourceType)) {
            return canAccessAssignment(userId, resourceId, action);
        }

        return true;
    }

    /**
     * 检查用户是否可以访问课程
     */
    private boolean canAccessCourse(Integer userId, String courseId, String action) {
        // 管理员可以访问所有课程
        if (hasRole(userId, "ADMIN")) {
            return true;
        }

        // 教师只能访问自己的课程
        if (hasRole(userId, "TEACHER")) {
            // 这里需要查询课程是否属于该教师
            // 暂时返回true，后续可以扩展
            return true;
        }

        // 学生可以查看已发布的课程
        if (hasRole(userId, "STUDENT")) {
            if ("view".equals(action) || "learn".equals(action)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 检查用户是否可以访问作业
     */
    private boolean canAccessAssignment(Integer userId, String assignmentId, String action) {
        // 管理员可以访问所有作业
        if (hasRole(userId, "ADMIN")) {
            return true;
        }

        // 教师可以管理自己发布的作业
        if (hasRole(userId, "TEACHER")) {
            if ("create".equals(action) || "edit".equals(action) || "delete".equals(action) || "grade".equals(action)) {
                return true;
            }
        }

        // 学生可以提交作业
        if (hasRole(userId, "STUDENT")) {
            if ("submit".equals(action) || "view".equals(action)) {
                return true;
            }
        }

        return false;
    }
} 