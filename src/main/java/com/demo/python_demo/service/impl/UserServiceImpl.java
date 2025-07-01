package com.demo.python_demo.service.impl;

import com.demo.python_demo.entity.User;
import com.demo.python_demo.repository.UserRepository;
import com.demo.python_demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
    public void deleteUser(Integer userId) {
        // TODO: 实现用户删除逻辑
    }

    @Override
    public User createUser(User user) {
        // TODO: 实现用户创建逻辑
        return null;
    }

    @Override
    public java.util.List<User> getAllUsers() {
        // TODO: 实现获取所有用户逻辑
        return java.util.Collections.emptyList();
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
}
