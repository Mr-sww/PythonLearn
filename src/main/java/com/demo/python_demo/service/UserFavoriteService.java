package com.demo.python_demo.service;

import com.demo.python_demo.entity.UserFavorite;
import java.util.List;

/**
 * 用户收藏课程服务接口
 */
public interface UserFavoriteService {
    
    /**
     * 获取用户收藏的课程列表
     * @param userId 用户ID
     * @return 收藏课程列表
     */
    List<UserFavorite> getUserFavorites(Integer userId);
    
    /**
     * 添加收藏
     * @param userId 用户ID
     * @param courseId 课程ID
     * @return 是否成功
     */
    boolean addFavorite(Integer userId, Integer courseId);
    
    /**
     * 取消收藏
     * @param userId 用户ID
     * @param courseId 课程ID
     * @return 是否成功
     */
    boolean removeFavorite(Integer userId, Integer courseId);
    
    /**
     * 检查是否已收藏
     * @param userId 用户ID
     * @param courseId 课程ID
     * @return 是否已收藏
     */
    boolean isFavorited(Integer userId, Integer courseId);
    
    /**
     * 获取用户收藏数量
     * @param userId 用户ID
     * @return 收藏数量
     */
    int getFavoriteCount(Integer userId);
} 