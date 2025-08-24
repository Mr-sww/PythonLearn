package com.demo.python_demo.controller;

import com.demo.python_demo.entity.KnowledgeStudyRecord;
import com.demo.python_demo.entity.VideoWatchRecord;
import com.demo.python_demo.entity.PythonVideo;
import com.demo.python_demo.service.LearningRecordService;
import com.demo.python_demo.repository.PythonVideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学习记录控制器
 */
@RestController
@RequestMapping("/api/learning")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8081", "http://127.0.0.1:3000", "http://127.0.0.1:8081"}, allowCredentials = "true")
public class LearningRecordController {

    @Autowired
    private LearningRecordService learningRecordService;
    
    @Autowired
    private PythonVideoRepository pythonVideoRepository;

    // 知识点学习记录相关接口

    /**
     * 开始知识点学习
     */
    @PostMapping("/knowledge/start")
    public ResponseEntity<?> startKnowledgeStudy(
            @RequestParam Integer knowledgeId,
            @RequestParam String knowledgeTitle,
            HttpSession session) {
        
        // 添加调试信息
        System.out.println("=== 开始知识点学习调试信息 ===");
        System.out.println("Session ID: " + session.getId());
        System.out.println("知识点ID: " + knowledgeId);
        System.out.println("知识点标题: " + knowledgeTitle);
        
        // 检查session中的所有属性
        java.util.Enumeration<String> attributeNames = session.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String name = attributeNames.nextElement();
            Object value = session.getAttribute(name);
            System.out.println("Session属性 - " + name + ": " + value);
        }
        
        Integer userId = (Integer) session.getAttribute("userId");
        System.out.println("从session获取的userId: " + userId);
        
        if (userId == null) {
            System.out.println("用户未登录，返回错误");
            return ResponseEntity.badRequest().body("用户未登录");
        }

        try {
            KnowledgeStudyRecord record = learningRecordService.startKnowledgeStudy(userId, knowledgeId, knowledgeTitle, "text");
            System.out.println("学习记录创建成功: " + record);
            return ResponseEntity.ok(record);
        } catch (Exception e) {
            System.out.println("创建学习记录失败: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("开始学习失败: " + e.getMessage());
        }
    }

    /**
     * 更新知识点学习进度
     */
    @PutMapping("/knowledge/progress")
    public ResponseEntity<?> updateKnowledgeProgress(
            @RequestParam Integer knowledgeId,
            @RequestParam Integer studyTime,
            @RequestParam Double progress,
            HttpSession session) {
        
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.badRequest().body("用户未登录");
        }

        try {
            boolean success = learningRecordService.updateKnowledgeProgress(userId, knowledgeId, studyTime, progress);
            if (success) {
                return ResponseEntity.ok("进度更新成功");
            } else {
                return ResponseEntity.badRequest().body("进度更新失败");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("进度更新失败: " + e.getMessage());
        }
    }

    /**
     * 记录视频点击事件
     */
    @PostMapping("/video-click")
    public ResponseEntity<?> recordVideoClick(
            @RequestParam Integer videoId,
            HttpSession session) {
        
        System.out.println("=== 视频点击记录调试信息 ===");
        System.out.println("Session ID: " + session.getId());
        System.out.println("视频ID: " + videoId);
        
        Integer userId = (Integer) session.getAttribute("userId");
        System.out.println("从session获取的userId: " + userId);
        
        if (userId == null) {
            System.out.println("用户未登录，返回错误");
            return ResponseEntity.badRequest().body("用户未登录");
        }

        try {
            // 根据视频ID查询视频课程信息
            PythonVideo video = pythonVideoRepository.findById(videoId);
            if (video == null) {
                System.out.println("未找到视频课程，ID: " + videoId);
                return ResponseEntity.badRequest().body("未找到视频课程");
            }
            
            System.out.println("找到视频课程: " + video.getTitle());
            
            // 创建学习记录，标记为视频类型
            KnowledgeStudyRecord record = learningRecordService.startKnowledgeStudy(
                userId, 
                videoId, 
                video.getTitle(),
                "video"
            );
            
            System.out.println("视频点击记录创建成功: " + record);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "视频点击记录成功");
            response.put("record", record);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.out.println("创建视频点击记录失败: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("视频点击记录失败: " + e.getMessage());
        }
    }

    /**
     * 完成知识点学习
     */
    @PostMapping("/knowledge/complete")
    public ResponseEntity<?> completeKnowledgeStudy(
            @RequestParam Integer knowledgeId,
            HttpSession session) {
        
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.badRequest().body("用户未登录");
        }

        try {
            boolean success = learningRecordService.completeKnowledgeStudy(userId, knowledgeId);
            if (success) {
                return ResponseEntity.ok("学习完成");
            } else {
                return ResponseEntity.badRequest().body("学习完成失败");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("学习完成失败: " + e.getMessage());
        }
    }

    /**
     * 获取知识点学习记录
     */
    @GetMapping("/knowledge/records")
    public ResponseEntity<?> getKnowledgeRecords(
            @RequestParam(defaultValue = "10") Integer limit,
            HttpSession session,
            HttpServletRequest request) {
        
        // 添加详细的调试信息
        System.out.println("=== 获取学习记录调试信息 ===");
        System.out.println("Session ID: " + session.getId());
        System.out.println("Session创建时间: " + new java.util.Date(session.getCreationTime()));
        System.out.println("Session最后访问时间: " + new java.util.Date(session.getLastAccessedTime()));
        
        // 检查session中的所有属性
        java.util.Enumeration<String> attributeNames = session.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String name = attributeNames.nextElement();
            Object value = session.getAttribute(name);
            System.out.println("Session属性 - " + name + ": " + value);
        }
        
        Integer userId = (Integer) session.getAttribute("userId");
        System.out.println("从session获取的userId: " + userId);
        
        if (userId == null) {
            System.out.println("Session中没有userId，尝试从请求头获取");
            // 尝试从请求头获取用户ID（备用方案）
            String userIdHeader = request.getHeader("X-User-ID");
            System.out.println("请求头X-User-ID: " + userIdHeader);
            
            // 打印所有请求头
            System.out.println("=== 所有请求头 ===");
            java.util.Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                String headerValue = request.getHeader(headerName);
                System.out.println(headerName + ": " + headerValue);
            }
            
            if (userIdHeader != null && !userIdHeader.trim().isEmpty()) {
                try {
                    userId = Integer.parseInt(userIdHeader.trim());
                    System.out.println("从请求头获取到userId: " + userId);
                } catch (NumberFormatException e) {
                    System.out.println("请求头中的userId格式错误: " + userIdHeader);
                }
            } else {
                System.out.println("请求头中没有X-User-ID或为空");
            }
        }
        
        if (userId == null) {
            System.out.println("用户未登录，返回错误");
            return ResponseEntity.badRequest().body("用户未登录");
        }

        try {
            List<KnowledgeStudyRecord> records = learningRecordService.getKnowledgeRecords(userId, limit);
            System.out.println("成功获取学习记录，数量: " + (records != null ? records.size() : 0));
            return ResponseEntity.ok(records);
        } catch (Exception e) {
            System.out.println("获取学习记录失败: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("获取记录失败: " + e.getMessage());
        }
    }

    /**
     * 获取知识点学习统计
     */
    @GetMapping("/knowledge/stats")
    public ResponseEntity<?> getKnowledgeStats(HttpSession session, HttpServletRequest request) {
        // 添加详细的调试信息
        System.out.println("=== 获取学习统计调试信息 ===");
        System.out.println("Session ID: " + session.getId());
        
        // 检查session中的所有属性
        java.util.Enumeration<String> attributeNames = session.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String name = attributeNames.nextElement();
            Object value = session.getAttribute(name);
            System.out.println("Session属性 - " + name + ": " + value);
        }
        
        Integer userId = (Integer) session.getAttribute("userId");
        System.out.println("从session获取的userId: " + userId);
        
        if (userId == null) {
            System.out.println("Session中没有userId，尝试从请求头获取");
            // 尝试从请求头获取用户ID（备用方案）
            String userIdHeader = request.getHeader("X-User-ID");
            System.out.println("请求头X-User-ID: " + userIdHeader);
            
            // 打印所有请求头
            System.out.println("=== 所有请求头 ===");
            java.util.Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                String headerValue = request.getHeader(headerName);
                System.out.println(headerName + ": " + headerValue);
            }
            
            if (userIdHeader != null && !userIdHeader.trim().isEmpty()) {
                try {
                    userId = Integer.parseInt(userIdHeader.trim());
                    System.out.println("从请求头获取到userId: " + userId);
                } catch (NumberFormatException e) {
                    System.out.println("请求头中的userId格式错误: " + userIdHeader);
                }
            } else {
                System.out.println("请求头中没有X-User-ID或为空");
            }
        }
        
        if (userId == null) {
            System.out.println("用户未登录，返回错误");
            return ResponseEntity.badRequest().body("用户未登录");
        }

        try {
            Object stats = learningRecordService.getKnowledgeStudyStats(userId);
            System.out.println("成功获取学习统计");
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            System.out.println("获取学习统计失败: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("获取统计失败: " + e.getMessage());
        }
    }

    // 视频观看记录相关接口

    /**
     * 开始视频观看
     */
    @PostMapping("/video/start")
    public ResponseEntity<?> startVideoWatch(
            @RequestParam Integer videoId,
            @RequestParam String videoTitle,
            @RequestParam String videoUrl,
            @RequestParam(defaultValue = "0") Integer totalDuration,
            HttpSession session,
            HttpServletRequest request) {
        
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            // 尝试从请求头获取用户ID
            String userIdHeader = request.getHeader("X-User-ID");
            if (userIdHeader != null && !userIdHeader.trim().isEmpty()) {
                try {
                    userId = Integer.parseInt(userIdHeader.trim());
                } catch (NumberFormatException e) {
                    return ResponseEntity.badRequest().body("用户未登录");
                }
            } else {
                return ResponseEntity.badRequest().body("用户未登录");
            }
        }

        try {
            VideoWatchRecord record = learningRecordService.startVideoWatch(userId, videoId, videoTitle, videoUrl, totalDuration);
            return ResponseEntity.ok(record);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("开始观看失败: " + e.getMessage());
        }
    }

    /**
     * 更新视频观看进度
     */
    @PutMapping("/video/progress")
    public ResponseEntity<?> updateVideoProgress(
            @RequestParam Integer videoId,
            @RequestParam Integer watchTime,
            @RequestParam Double progress,
            HttpSession session,
            HttpServletRequest request) {
        
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            // 尝试从请求头获取用户ID
            String userIdHeader = request.getHeader("X-User-ID");
            if (userIdHeader != null && !userIdHeader.trim().isEmpty()) {
                try {
                    userId = Integer.parseInt(userIdHeader.trim());
                } catch (NumberFormatException e) {
                    return ResponseEntity.badRequest().body("用户未登录");
                }
            } else {
                return ResponseEntity.badRequest().body("用户未登录");
            }
        }

        try {
            boolean success = learningRecordService.updateVideoProgress(userId, videoId, watchTime, progress);
            if (success) {
                return ResponseEntity.ok("进度更新成功");
            } else {
                return ResponseEntity.badRequest().body("进度更新失败");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("进度更新失败: " + e.getMessage());
        }
    }

    /**
     * 完成视频观看
     */
    @PostMapping("/video/complete")
    public ResponseEntity<?> completeVideoWatch(
            @RequestParam Integer videoId,
            HttpSession session,
            HttpServletRequest request) {
        
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            // 尝试从请求头获取用户ID
            String userIdHeader = request.getHeader("X-User-ID");
            if (userIdHeader != null && !userIdHeader.trim().isEmpty()) {
                try {
                    userId = Integer.parseInt(userIdHeader.trim());
                } catch (NumberFormatException e) {
                    return ResponseEntity.badRequest().body("用户未登录");
                }
            } else {
                return ResponseEntity.badRequest().body("用户未登录");
            }
        }

        try {
            boolean success = learningRecordService.completeVideoWatch(userId, videoId);
            if (success) {
                return ResponseEntity.ok("观看完成");
            } else {
                return ResponseEntity.badRequest().body("观看完成失败");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("观看完成失败: " + e.getMessage());
        }
    }

    /**
     * 获取视频观看记录
     */
    @GetMapping("/video/records")
    public ResponseEntity<?> getVideoRecords(
            @RequestParam(defaultValue = "10") Integer limit,
            HttpSession session,
            HttpServletRequest request) {
        
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            // 尝试从请求头获取用户ID
            String userIdHeader = request.getHeader("X-User-ID");
            if (userIdHeader != null && !userIdHeader.trim().isEmpty()) {
                try {
                    userId = Integer.parseInt(userIdHeader.trim());
                } catch (NumberFormatException e) {
                    return ResponseEntity.badRequest().body("用户未登录");
                }
            } else {
                return ResponseEntity.badRequest().body("用户未登录");
            }
        }

        try {
            List<VideoWatchRecord> records = learningRecordService.getVideoRecords(userId, limit);
            return ResponseEntity.ok(records);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("获取记录失败: " + e.getMessage());
        }
    }

    /**
     * 获取视频观看统计
     */
    @GetMapping("/video/stats")
    public ResponseEntity<?> getVideoStats(HttpSession session, HttpServletRequest request) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            // 尝试从请求头获取用户ID
            String userIdHeader = request.getHeader("X-User-ID");
            if (userIdHeader != null && !userIdHeader.trim().isEmpty()) {
                try {
                    userId = Integer.parseInt(userIdHeader.trim());
                } catch (NumberFormatException e) {
                    return ResponseEntity.badRequest().body("用户未登录");
                }
            } else {
                return ResponseEntity.badRequest().body("用户未登录");
            }
        }

        try {
            Object stats = learningRecordService.getVideoWatchStats(userId);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("获取统计失败: " + e.getMessage());
        }
    }
}
