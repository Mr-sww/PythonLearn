package com.demo.python_demo.controller;

import com.demo.python_demo.entity.UserFavorite;
import com.demo.python_demo.service.UserFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户收藏课程控制器
 */
@RestController
@RequestMapping("/api/favorites")
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
public class UserFavoriteController {

    @Autowired
    private UserFavoriteService userFavoriteService;

    /**
     * 获取用户收藏的课程列表
     * @param userId 用户ID
     * @return 收藏课程列表
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserFavorite>> getUserFavorites(@PathVariable Integer userId) {
        List<UserFavorite> favorites = userFavoriteService.getUserFavorites(userId);
        return ResponseEntity.ok(favorites);
    }

    /**
     * 添加收藏
     * @param params 请求参数
     * @return 操作结果
     */
    @PostMapping("/add")
    public ResponseEntity<?> addFavorite(@RequestBody Map<String, Integer> params) {
        Integer userId = params.get("userId");
        Integer courseId = params.get("courseId");
        
        if (userId == null || courseId == null) {
            return ResponseEntity.badRequest().body("用户ID和课程ID不能为空");
        }
        
        boolean success = userFavoriteService.addFavorite(userId, courseId);
        if (success) {
            return ResponseEntity.ok().body("收藏成功");
        } else {
            return ResponseEntity.badRequest().body("收藏失败，可能已收藏或课程不存在");
        }
    }

    /**
     * 取消收藏
     * @param params 请求参数
     * @return 操作结果
     */
    @PostMapping("/remove")
    public ResponseEntity<?> removeFavorite(@RequestBody Map<String, Integer> params) {
        Integer userId = params.get("userId");
        Integer courseId = params.get("courseId");
        
        if (userId == null || courseId == null) {
            return ResponseEntity.badRequest().body("用户ID和课程ID不能为空");
        }
        
        boolean success = userFavoriteService.removeFavorite(userId, courseId);
        if (success) {
            return ResponseEntity.ok().body("取消收藏成功");
        } else {
            return ResponseEntity.badRequest().body("取消收藏失败");
        }
    }

    /**
     * 检查是否已收藏
     * @param userId 用户ID
     * @param courseId 课程ID
     * @return 是否已收藏
     */
    @GetMapping("/check/{userId}/{courseId}")
    public ResponseEntity<Boolean> isFavorited(@PathVariable Integer userId, @PathVariable Integer courseId) {
        boolean isFavorited = userFavoriteService.isFavorited(userId, courseId);
        return ResponseEntity.ok(isFavorited);
    }

    /**
     * 获取用户收藏数量
     * @param userId 用户ID
     * @return 收藏数量
     */
    @GetMapping("/count/{userId}")
    public ResponseEntity<Integer> getFavoriteCount(@PathVariable Integer userId) {
        int count = userFavoriteService.getFavoriteCount(userId);
        return ResponseEntity.ok(count);
    }
} 