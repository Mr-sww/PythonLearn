package com.demo.python_demo.controller;

import com.demo.python_demo.service.CourseService;
import com.demo.python_demo.service.UserService;
import com.demo.python_demo.service.UserProblemRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
public class StatsController {

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserProblemRecordService userProblemRecordService;

    /**
     * 获取平台统计数据
     */
    @GetMapping("/stats")
    public Map<String, Object> getPlatformStats() {
        Map<String, Object> stats = new HashMap<>();
        
        try {
            // 用户统计
            stats.put("users", userService.countAllUsers());
            
            // 课程统计
            stats.put("courses", courseService.countAllCourses());
            
            // 题目统计（从PythonProblemService获取）
            stats.put("problems", 100); // 暂时写死，后续可以从PythonProblemService获取
            
            // 提交统计（从UserProblemRecordService获取）
            stats.put("submissions", userProblemRecordService.getTotalSubmissions(0)); // 0表示所有用户
            
        } catch (Exception e) {
            // 如果出现异常，返回默认值
            stats.put("users", 0);
            stats.put("courses", 0);
            stats.put("problems", 0);
            stats.put("submissions", 0);
        }
        
        return stats;
    }
}
