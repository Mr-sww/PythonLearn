package com.demo.python_demo.controller;

import com.demo.python_demo.entity.UserProblemRecord;
import com.demo.python_demo.service.UserProblemRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * 用户问题记录控制器
 */
@RestController
@RequestMapping("/api/user-problem-record")
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
public class UserProblemRecordController {

    @Autowired
    private UserProblemRecordService userProblemRecordService;

    /**
     * 保存用户做题记录
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> saveRecord(@RequestBody UserProblemRecord record) {
        try {
            // 验证输入
            if (record.getUserId() == null || record.getProblemId() == null) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "用户ID和题目ID不能为空"
                ));
            }

            userProblemRecordService.saveRecord(record);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "记录保存成功",
                "data", record
            ));
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", "保存失败: " + e.getMessage()
            ));
        }
    }

    /**
     * 获取用户做题记录（分页）
     */
    @GetMapping("/records")
    public ResponseEntity<Map<String, Object>> getUserRecords(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(required = false) Integer userId,
        @RequestParam(required = false) String result
    ) {
        try {
            // 参数验证
            if (page < 1) page = 1;
            if (size < 1 || size > 100) size = 10;
            
            int offset = (page - 1) * size;
            
            List<Map<String, Object>> records;
            int total;
            
            // 根据是否有结果筛选条件来决定调用哪个方法
            if (result != null && !result.isEmpty()) {
                records = userProblemRecordService.getUserProblemRecordsByResultWithPagination(userId, result, offset, size);
                total = userProblemRecordService.getUserProblemRecordsByResultCount(userId, result);
            } else {
                records = userProblemRecordService.getUserProblemRecordsWithPagination(userId, offset, size);
                total = userProblemRecordService.getUserProblemRecordsCount(userId);
            }
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", Map.of(
                    "records", records,
                    "pagination", Map.of(
                        "page", page,
                        "size", size,
                        "total", total,
                        "totalPages", (int) Math.ceil((double) total / size)
                    )
                )
            ));
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", "获取记录失败: " + e.getMessage()
            ));
        }
    }

    /**
     * 获取用户做题记录（按条件过滤）
     */
    @GetMapping("/records/filter")
    public ResponseEntity<Map<String, Object>> getFilteredRecords(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(required = false) Integer userId,
        @RequestParam(required = false) String result,
        @RequestParam(required = false) String language,
        @RequestParam(required = false) String startDate,
        @RequestParam(required = false) String endDate
    ) {
        try {
            // 参数验证
            if (page < 1) page = 1;
            if (size < 1 || size > 100) size = 10;
            
            int offset = (page - 1) * size;
            
            // 暂时返回空数据，实际项目中需要实现这些方法
            List<Map<String, Object>> records = new ArrayList<>();
            int total = 0;
            
            // 简单的客户端过滤（实际项目中应该在数据库层面过滤）
            if (result != null && !result.trim().isEmpty()) {
                records = records.stream()
                    .filter(record -> result.equals(record.get("result")))
                    .toList();
            }
            
            if (language != null && !language.trim().isEmpty()) {
                records = records.stream()
                    .filter(record -> language.equals(record.get("language")))
                    .toList();
            }
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", Map.of(
                    "records", records,
                    "pagination", Map.of(
                        "page", page,
                        "size", size,
                        "total", total,
                        "totalPages", (int) Math.ceil((double) total / size)
                    )
                )
            ));
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", "获取记录失败: " + e.getMessage()
            ));
        }
    }

    /**
     * 获取用户统计信息
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getUserStatistics(@RequestParam(required = false) Integer userId) {
        try {
            Map<String, Object> statistics = userProblemRecordService.getUserStatistics(userId);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", statistics
            ));
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", "获取统计信息失败: " + e.getMessage()
            ));
        }
    }

    /**
     * 获取用户做题趋势
     */
    @GetMapping("/trends")
    public ResponseEntity<Map<String, Object>> getUserTrends(@RequestParam(required = false) Integer userId) {
        try {
            // 暂时返回空数据，实际项目中需要实现这些方法
            List<Map<String, Object>> dailyTrend = new ArrayList<>();
            List<Map<String, Object>> resultDistribution = new ArrayList<>();
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", Map.of(
                    "dailyTrend", dailyTrend,
                    "resultDistribution", resultDistribution
                )
            ));
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", "获取趋势数据失败: " + e.getMessage()
            ));
        }
    }

    /**
     * 获取用户做题记录详情
     */
    @GetMapping("/{recordId}")
    public ResponseEntity<Map<String, Object>> getRecordDetail(@PathVariable Integer recordId) {
        try {
            // 这里需要添加根据recordId获取记录详情的服务方法
            // 暂时返回模拟数据
            Map<String, Object> record = new HashMap<>();
            record.put("recordId", recordId);
            record.put("userId", 1);
            record.put("problemId", "P1");
            record.put("problemTitle", "A+B Problem");
            record.put("code", "# 用户代码\nprint('Hello World')");
            record.put("result", "通过");
            record.put("passRate", 100);
            record.put("usedTime", 150);
            record.put("usedMemory", 1024);
            record.put("language", "python");
            record.put("submitTime", new Date());
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", record
            ));
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", "获取记录详情失败: " + e.getMessage()
            ));
        }
    }

    /**
     * 删除用户做题记录
     */
    @DeleteMapping("/{recordId}")
    public ResponseEntity<Map<String, Object>> deleteRecord(@PathVariable Integer recordId) {
        try {
            // 这里需要添加删除记录的服务方法
            // 暂时返回成功响应
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "记录删除成功"
            ));
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", "删除记录失败: " + e.getMessage()
            ));
        }
    }

    /**
     * 批量删除用户做题记录
     */
    @DeleteMapping("/batch")
    public ResponseEntity<Map<String, Object>> batchDeleteRecords(@RequestBody List<Integer> recordIds) {
        try {
            // 这里需要添加批量删除记录的服务方法
            // 暂时返回成功响应
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "批量删除成功，共删除 " + recordIds.size() + " 条记录"
            ));
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", "批量删除失败: " + e.getMessage()
            ));
        }
    }

    /**
     * 导出用户做题记录
     */
    @GetMapping("/export")
    public ResponseEntity<Map<String, Object>> exportRecords(
        @RequestParam(required = false) Integer userId,
        @RequestParam(defaultValue = "json") String format
    ) {
        try {
            // 这里需要添加导出记录的服务方法
            // 暂时返回模拟数据
            
            // 暂时返回空数据，实际项目中需要实现这些方法
            List<Map<String, Object>> records = new ArrayList<>();
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", Map.of(
                    "format", format,
                    "records", records,
                    "total", records.size(),
                    "exportTime", new Date()
                )
            ));
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", "导出记录失败: " + e.getMessage()
            ));
        }
        }
    
        @PostMapping("/submit")
        public Map<String, Object> submit(@RequestBody UserProblemRecord record) {
            userProblemRecordService.saveRecord(record);
            Map<String, Object> map = new HashMap<>();
            map.put("message", "记录已保存");
            return map;
        }
    
        @GetMapping("/stats")
        public Map<String, Object> getStats(@RequestParam Integer userId) {
            Map<String, Object> map = new HashMap<>();
            map.put("totalSubmissions", userProblemRecordService.getTotalSubmissions(userId));
            map.put("passedProblems", userProblemRecordService.getPassedProblems(userId));
            map.put("accuracy", userProblemRecordService.getAccuracy(userId));
            return map;
        }
    }