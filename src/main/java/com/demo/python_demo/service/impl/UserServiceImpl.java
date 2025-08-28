package com.demo.python_demo.service.impl;

import com.demo.python_demo.entity.User;
import com.demo.python_demo.repository.UserRepository;
import com.demo.python_demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public User login(String account, String password) {
        User user = userRepository.findByAccount(account);
        System.out.println("登录账号: " + account);
        System.out.println("数据库查到用户: " + user);
        if (user != null) {
            System.out.println("数据库密码: " + user.getPassword());
            System.out.println("前端输入密码: " + password);
            System.out.println("密码校验结果: " + passwordEncoder.matches(password, user.getPassword()));
        }
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    @Override
    public boolean register(User user) {
        if (userRepository.findByAccount(user.getAccount()) != null ||
            userRepository.findByPhone(user.getPhone()) != null) {
            return false;
        }
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        // groupType/intestType 可为 null 或前端传递
        System.out.println("注册用户：" + user);
        int result = userRepository.insert(user);
        System.out.println("插入结果：" + result);
        return result > 0;
    }

    @Override
    public Optional<User> getUserById(Integer userId) {
        return Optional.ofNullable(userRepository.findById(userId));
    }

    @Override
    public User updateUser(User user) {
        int rows = userRepository.update(user);
        return rows > 0 ? user : null;
    }

    @Override
    public Optional<User> updateUser(Integer id, User user) {
        // TODO: 实现用户更新逻辑
        return Optional.empty();
    }

    @Override
    public Optional<User> getUserByAccount(String account) {
        return Optional.ofNullable(userRepository.findByAccount(account));
    }

    @Override
    public User createUser(User user) {
        try {
            // 检查账号是否已存在
            User existingUser = userRepository.findByAccount(user.getAccount());
            if (existingUser != null) {
                return null; // 账号已存在
            }
            
            // 检查手机号是否已存在（如果提供了手机号）
            if (user.getPhone() != null && !user.getPhone().trim().isEmpty()) {
                User existingPhoneUser = userRepository.findByPhone(user.getPhone());
                if (existingPhoneUser != null) {
                    return null; // 手机号已存在
                }
            }
            
            // 设置默认值
            if (user.getStatus() == null) {
                user.setStatus("active");
            }
            
            if (user.getGroupType() == null) {
                user.setGroupType(1); // 默认为学生
            }
            
            // 加密密码
            if (user.getPassword() != null && !user.getPassword().trim().isEmpty()) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            
            // 插入用户
            int result = userRepository.insert(user);
            if (result > 0) {
                // 返回创建成功的用户（包含生成的ID）
                return user;
            }
            
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public Object getLearningStatistics(Integer userId) {
        // TODO: 实现学习统计逻辑
        return null;
    }

    @Override
    public int updateGroupType(Integer userId, Integer groupType) {
        return userRepository.updateGroupType(userId, groupType);
    }

    @Override
    public int updateIntestTypes(Integer userId, String intestTypes) {
        return userRepository.updateIntestTypes(userId, intestTypes);
    }

    // 管理员功能实现
    @Override
    public List<User> searchUsers(String keyword, Integer groupType, String status) {
        return userRepository.searchUsers(keyword, groupType, status);
    }

    @Override
    public boolean updateUserStatus(Integer userId, String status) {
        return userRepository.updateUserStatus(userId, status) > 0;
    }

    @Override
    public boolean updateUserGroupType(Integer userId, Integer groupType) {
        return userRepository.updateGroupType(userId, groupType) > 0;
    }

    @Override
    public boolean deleteUser(Integer userId) {
        return userRepository.deleteById(userId) > 0;
    }

    @Override
    public int countAllUsers() {
        return userRepository.countAllUsers();
    }

    @Override
    public int countUsersByStatus(String status) {
        return userRepository.countUsersByStatus(status);
    }

    @Override
    public List<User> getAllStudents() {
        return userRepository.findByGroupType(1); // 假设1是学生
    }

    @Override
    public List<User> getStudentsByMajor(Integer majorType) {
        return userRepository.findStudentsByMajor(majorType);
    }

    @Override
    public List<User> getAllTeachers() {
        return userRepository.findByGroupType(2); // 假设2是教师
    }

    @Override
    public List<User> getAllAdmins() {
        return userRepository.findByGroupType(3); // 假设3是管理员
    }

    @Override
    public List<Object> getMajorStatistics() {
        return userRepository.getMajorStatistics();
    }

    @Override
    public List<Object> getRoleStatistics() {
        return userRepository.getRoleStatistics();
    }
}
