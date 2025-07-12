package com.demo.python_demo.controller;

import com.demo.python_demo.entity.UserProblemRecord;
import com.demo.python_demo.service.UserProblemRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user/problem")
public class UserProblemRecordController {
    @Autowired
    private UserProblemRecordService service;

    @PostMapping("/submit")
    public Map<String, Object> submit(@RequestBody UserProblemRecord record) {
        service.saveRecord(record);
        Map<String, Object> map = new HashMap<>();
        map.put("message", "记录已保存");
        return map;
    }

    @GetMapping("/stats")
    public Map<String, Object> getStats(@RequestParam Integer userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("totalSubmissions", service.getTotalSubmissions(userId));
        map.put("passedProblems", service.getPassedProblems(userId));
        map.put("accuracy", service.getAccuracy(userId));
        return map;
    }
} 