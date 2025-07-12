package com.demo.python_demo.controller;

import com.demo.python_demo.repository.PythonProblemRepository;
import com.demo.python_demo.entity.PythonProblem;
import com.demo.python_demo.repository.ProblemCommentRepository;
import com.demo.python_demo.entity.ProblemComment;
import com.demo.python_demo.repository.ProblemFavoriteRepository;
import com.demo.python_demo.entity.ProblemFavorite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/practice")
public class PracticeController {
    @Autowired
    private PythonProblemRepository problemRepo;

    @Autowired
    private ProblemCommentRepository problemCommentRepo;

    @Autowired
    private ProblemFavoriteRepository favoriteRepo;

    @GetMapping("/problems")
    public List<PythonProblem> getProblems() {
        return problemRepo.findAll();
    }

    @GetMapping("/problem/{id}/comments")
    public Map<String, Object> getComments(
        @PathVariable String id,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int pageSize
    ) {
        int offset = (page - 1) * pageSize;
        List<Map<String, Object>> comments = problemCommentRepo.findPagedByProblemId(id, offset, pageSize);
        int total = problemCommentRepo.countByProblemId(id);
        Map<String, Object> result = new HashMap<>();
        result.put("comments", comments);
        result.put("total", total);
        return result;
    }

    @PostMapping("/problem/{id}/comments")
    public void addComment(@PathVariable String id, @RequestBody ProblemComment comment) {
        comment.setProblemId(id);
        comment.setCreatedAt(new java.util.Date());
        comment.setUpdatedAt(new java.util.Date());
        problemCommentRepo.insert(comment);
    }

    @PostMapping("/comment/{commentId}/like")
    public void likeComment(@PathVariable Integer commentId) {
        problemCommentRepo.likeComment(commentId);
    }

    @GetMapping("/problem/{id}/favorite")
    public boolean isFavorite(@RequestParam(required = false) Integer userId, @PathVariable String id) {
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

    @PostMapping("/problem/{id}/favorite")
    public void addFavorite(@RequestParam(required = false) Integer userId, @PathVariable String id) {
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

    @DeleteMapping("/problem/{id}/favorite")
    public void removeFavorite(@RequestParam(required = false) Integer userId, @PathVariable String id) {
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

    @GetMapping("/favorites")
    public List<Map<String, Object>> getFavorites(@RequestParam int userId) {
        return favoriteRepo.findAllByUserId(userId);
    }

    @GetMapping("/problem/{id}/favorite/count")
    public int getFavoriteCount(@PathVariable String id) {
        return favoriteRepo.countByProblemId(id);
    }

    @GetMapping("/progress")
    public Map<String, Object> practiceProgress() {
        Map<String, Object> progress = new HashMap<>();
        progress.put("completed", 23);
        progress.put("accuracy", 92);
        progress.put("practiceTime", 8);
        progress.put("continuousDays", 4);
        return progress;
    }

    @GetMapping("/recommend")
    public List<Map<String, Object>> recommendProblems() {
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