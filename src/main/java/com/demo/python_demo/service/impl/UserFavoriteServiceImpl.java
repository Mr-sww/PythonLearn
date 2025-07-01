package com.demo.python_demo.service.impl;

import com.demo.python_demo.entity.UserFavorite;
import com.demo.python_demo.repository.UserFavoriteRepository;
import com.demo.python_demo.service.UserFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户收藏课程服务实现类
 */
@Service
public class UserFavoriteServiceImpl implements UserFavoriteService {
    
    @Autowired
    private UserFavoriteRepository userFavoriteRepository;
    
    @Override
    public List<UserFavorite> getUserFavorites(Integer userId) {
        return userFavoriteRepository.findByUserId(userId);
    }
    
    @Override
    public boolean addFavorite(Integer userId, Integer courseId) {
        try {
            // 检查是否已收藏
            if (userFavoriteRepository.countByUserIdAndCourseId(userId, courseId) > 0) {
                return false; // 已收藏
            }
            
            UserFavorite userFavorite = new UserFavorite();
            userFavorite.setUserId(userId);
            userFavorite.setCourseId(courseId);
            
            return userFavoriteRepository.insert(userFavorite) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean removeFavorite(Integer userId, Integer courseId) {
        try {
            return userFavoriteRepository.deleteByUserIdAndCourseId(userId, courseId) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean isFavorited(Integer userId, Integer courseId) {
        return userFavoriteRepository.countByUserIdAndCourseId(userId, courseId) > 0;
    }
    
    @Override
    public int getFavoriteCount(Integer userId) {
        return userFavoriteRepository.countByUserId(userId);
    }
} 