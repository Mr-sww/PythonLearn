package com.demo.python_demo.controller;

import com.demo.python_demo.entity.PythonProblem;
import com.demo.python_demo.entity.ProblemComment;
import com.demo.python_demo.entity.ProblemFavorite;
import com.demo.python_demo.repository.PythonProblemRepository;
import com.demo.python_demo.repository.ProblemCommentRepository;
import com.demo.python_demo.repository.ProblemFavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.*;

/**
 * 练习控制器
 */
@RestController
@RequestMapping("/api/practice")
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
public class PracticeController {

    @Autowired
    private PythonProblemRepository pythonProblemRepository;

    @Autowired
    private ProblemCommentRepository problemCommentRepository;

    @Autowired
    private ProblemFavoriteRepository favoriteRepo;

    /**
     * 获取练习进度
     */
    @GetMapping("/progress")
    public ResponseEntity<Map<String, Object>> getPracticeProgress() {
        Map<String, Object> progress = new HashMap<>();
        progress.put("completed", 23);
        progress.put("accuracy", 92);
        progress.put("practiceTime", 8);
        progress.put("continuousDays", 4);
        return ResponseEntity.ok(progress);
    }

    /**
     * 获取推荐练习题
     */
    @GetMapping("/recommend")
    public ResponseEntity<List<Map<String, Object>>> getRecommendProblems() {
        List<Map<String, Object>> problems = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Map<String, Object> p = new HashMap<>();
            p.put("id", "task" + i);
            p.put("title", "推荐练习题" + i);
            p.put("difficulty", "中等");
            problems.add(p);
        }
        return ResponseEntity.ok(problems);
    }

    /**
     * 获取所有练习题
     */
    @GetMapping("/problems")
    public ResponseEntity<List<PythonProblem>> getProblems() {
        List<PythonProblem> problems = pythonProblemRepository.findAll();
        return ResponseEntity.ok(problems);
    }

    /**
     * 获取用户收藏的练习题
     */
    @GetMapping("/favorites")
    public ResponseEntity<List<Map<String, Object>>> getFavorites(@RequestParam Integer userId) {
        List<Map<String, Object>> favorites = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Map<String, Object> p = new HashMap<>();
            p.put("id", "P" + (i * 3));
            p.put("title", "收藏练习题 " + (i * 3));
            p.put("difficulty", i % 3 == 0 ? "困难" : i % 2 == 0 ? "中等" : "简单");
            p.put("category", "基础语法");
            p.put("status", "未开始");
            favorites.add(p);
        }
        return ResponseEntity.ok(favorites);
    }

    /**
     * 获取题目评论
     */
    @GetMapping("/problem/{problemId}/comments")
    public ResponseEntity<Map<String, Object>> getComments(
        @PathVariable String problemId,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int pageSize
    ) {
        int offset = (page - 1) * pageSize;
        List<Map<String, Object>> comments = problemCommentRepository.findPagedByProblemId(problemId, offset, pageSize);
        int total = problemCommentRepository.countByProblemId(problemId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("comments", comments);
        result.put("total", total);
        return ResponseEntity.ok(result);
    }

    /**
     * 添加题目评论
     */
    @PostMapping("/problem/{problemId}/comments")
    public ResponseEntity<?> addComment(
        @PathVariable String problemId,
        @RequestBody Map<String, Object> request
    ) {
        ProblemComment comment = new ProblemComment();
        comment.setProblemId(problemId);
        comment.setUserId((Integer) request.get("userId"));
        comment.setContent((String) request.get("content"));
        comment.setParentId((Integer) request.get("parentId"));
        
        problemCommentRepository.insert(comment);
        return ResponseEntity.ok().build();
    }

    /**
     * 点赞评论
     */
    @PostMapping("/comment/{commentId}/like")
    public ResponseEntity<?> likeComment(@PathVariable Integer commentId) {
        problemCommentRepository.likeComment(commentId);
        return ResponseEntity.ok().build();
    }

    /**
     * 检查用户是否收藏了题目
     */
    @GetMapping("/problem/{problemId}/favorite")
    public ResponseEntity<Boolean> checkFavorite(
        @PathVariable String problemId,
        @RequestParam Integer userId
    ) {
        // 模拟检查收藏状态
        boolean isFavorite = Math.random() > 0.5; // 随机返回true/false
        return ResponseEntity.ok(isFavorite);
    }

    /**
     * 获取题目收藏数量
     */
    @GetMapping("/problem/{problemId}/favorite/count")
    public ResponseEntity<Integer> getFavoriteCount(@PathVariable String problemId) {
        // 模拟收藏数量
        int count = (int) (Math.random() * 100) + 10; // 10-109之间的随机数
        return ResponseEntity.ok(count);
    }

    /**
     * 添加收藏
     */
    @PostMapping("/problem/{problemId}/favorite")
    public ResponseEntity<?> addFavorite(
        @PathVariable String problemId,
        @RequestParam Integer userId
    ) {
        // 模拟添加收藏
        return ResponseEntity.ok().build();
    }

    /**
     * 取消收藏
     */
    @DeleteMapping("/problem/{problemId}/favorite")
    public ResponseEntity<?> removeFavorite(
        @PathVariable String problemId,
        @RequestParam Integer userId
    ) {
        // 模拟取消收藏
        return ResponseEntity.ok().build();
    }

    @GetMapping("/problem/{id}/favorite/legacy")
    public boolean isFavoriteLegacy(@RequestParam(required = false) Integer userId, @PathVariable String id) {
        if (id != null) {
            System.out.println("[isFavorite] 原始problemId: '" + id + "' 字节=" + Arrays.toString(id.getBytes()));
            id = id.trim();
            System.out.println("[isFavorite] 去空格后problemId: '" + id + "' 字节=" + Arrays.toString(id.getBytes()));
        }
        System.out.println("[isFavorite] userId=" + userId + ", problemId=" + id);
        if (userId == null || id == null || id.isEmpty()) {
            System.err.println("[isFavorite] 参数缺失");
            return false;
        }
        try {
            ProblemFavorite pf = favoriteRepo.findByUserAndProblem(userId, id);
            System.out.println("[isFavorite] 查到的对象: " + pf);
            return pf != null;
        } catch (Exception e) {
            System.err.println("[isFavorite] 查询异常: " + e.getMessage());
            return false;
        }
    }

    @PostMapping("/problem/{id}/favorite/legacy")
    public void addFavoriteLegacy(@RequestParam(required = false) Integer userId, @PathVariable String id) {
        if (id != null) {
            System.out.println("[addFavorite] 原始problemId: '" + id + "' 字节=" + Arrays.toString(id.getBytes()));
            id = id.trim();
            System.out.println("[addFavorite] 去空格后problemId: '" + id + "' 字节=" + Arrays.toString(id.getBytes()));
        }
        System.out.println("[addFavorite] userId=" + userId + ", problemId=" + id);
        if (userId == null || id == null || id.isEmpty()) {
            System.err.println("[addFavorite] 参数缺失");
            return;
        }
        try {
            if (favoriteRepo.findByUserAndProblem(userId, id) != null) {
                return;
            }
            ProblemFavorite pf = new ProblemFavorite();
            pf.setUserId(userId);
            pf.setProblemId(id);
            favoriteRepo.insert(pf);
        } catch (Exception e) {
            System.err.println("[addFavorite] 插入异常: " + e.getMessage());
        }
    }

    @DeleteMapping("/problem/{id}/favorite/legacy")
    public void removeFavoriteLegacy(@RequestParam(required = false) Integer userId, @PathVariable String id) {
        if (id != null) {
            System.out.println("[removeFavorite] 原始problemId: '" + id + "' 字节=" + Arrays.toString(id.getBytes()));
            id = id.trim();
            System.out.println("[removeFavorite] 去空格后problemId: '" + id + "' 字节=" + Arrays.toString(id.getBytes()));
        }
        System.out.println("[removeFavorite] userId=" + userId + ", problemId=" + id);
        if (userId == null || id == null || id.isEmpty()) {
            System.err.println("[removeFavorite] 参数缺失");
            return;
        }
        try {
            favoriteRepo.delete(userId, id);
        } catch (Exception e) {
            System.err.println("[removeFavorite] 删除异常: " + e.getMessage());
        }
    }

    @GetMapping("/favorites/db")
    public List<Map<String, Object>> getFavoritesLegacy(@RequestParam int userId) {
        return favoriteRepo.findAllByUserId(userId);
    }

    @GetMapping("/problem/{id}/favorite/count-legacy")
    public int getFavoriteCountLegacy(@PathVariable String id) {
        return favoriteRepo.countByProblemId(id);
    }

    @GetMapping("/progress/legacy")
    public Map<String, Object> practiceProgressLegacy() {
        Map<String, Object> progress = new HashMap<>();
        progress.put("completed", 23);
        progress.put("accuracy", 92);
        progress.put("practiceTime", 8);
        progress.put("continuousDays", 4);
        return progress;
    }

    @GetMapping("/recommend/legacy")
    public List<Map<String, Object>> recommendProblemsLegacy() {
        List<Map<String, Object>> problems = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Map<String, Object> p = new HashMap<>();
            p.put("id", "task" + i);
            p.put("title", "推荐练习题" + i);
            p.put("difficulty", "中等");
            problems.add(p);
        }
        return problems;
    }
} 