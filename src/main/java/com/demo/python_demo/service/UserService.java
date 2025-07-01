package com.demo.python_demo.service;

import com.demo.python_demo.entity.User;

import java.util.List;
import java.util.Optional;

/**
 * 用户服务接口
 */
public interface UserService {

    /**
     * 获取所有用户
     * @return 用户列表
     */
    List<User> getAllUsers();

    /**
     * 根据ID获取用户
     * @param userId 用户ID
     * @return 用户信息
     */
    Optional<User> getUserById(Integer userId);

    /**
     * 根据账号获取用户
     * @param account 账号
     * @return 用户信息
     */
    Optional<User> getUserByAccount(String account);

    /**
     * 创建用户
     * @param user 用户信息
     * @return 创建的用户
     */
    User createUser(User user);

    /**
     * 更新用户信息
     * @param userId 用户ID
     * @param user 用户信息
     * @return 更新后的用户
     */
    Optional<User> updateUser(Integer userId, User user);

    /**
     * 删除用户
     * @param userId 用户ID
     */
    void deleteUser(Integer userId);

    /**
     * 用户登录
     * @param account 账号
     * @param password 密码
     * @return 用户信息
     */
    User login(String account, String password);

    /**
     * 获取用户学习统计
     * @param userId 用户ID
     * @return 学习统计信息
     */
    Object getLearningStatistics(Integer userId);

    boolean register(User user);


    User updateUser(User user);

    int updateGroupType(Integer userId, Integer groupType);

    int updateIntestTypes(Integer userId, String intestTypes);
}
