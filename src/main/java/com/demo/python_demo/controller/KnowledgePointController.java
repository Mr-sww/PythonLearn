package com.demo.python_demo.controller;

import com.demo.python_demo.entity.KnowledgePoint;
import com.demo.python_demo.entity.PythonProblem;
import com.demo.python_demo.repository.KnowledgePointRepository;
import com.demo.python_demo.service.PythonProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/api/knowledge")
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
public class KnowledgePointController {
    @Autowired
    private KnowledgePointRepository repo;
    
    @Autowired
    private PythonProblemService pythonProblemService;

    @GetMapping("/points")
    public List<KnowledgePoint> getAllPoints() {
        try {
            return repo.findAllOrderByStage();
        } catch (Exception e) {
            // 如果数据库查询失败，返回模拟数据，基于真实的Python知识点
            List<KnowledgePoint> mockPoints = new ArrayList<>();
            
            String[] titles = {
                "Python3 教程", "Python3 环境搭建", "Python VScode 配置", "Python3 基础语法",
                "Python3 基本数据类型", "Python3 运算符", "Python3 字符串", "Python3 列表",
                "Python3 元组", "Python3 字典", "Python3 集合", "Python3 条件控制",
                "Python3 循环语句", "Python3 函数", "Python3 数据结构", "Python3 OS 文件/目录方法",
                "Python3 数据类型转换", "Python3 注释", "Python lambda (匿名函数)", "Python3 数字(Number)",
                "Python3 File 方法"
            };
            
            String[] stages = {
                "1.1", "1.2", "1.3", "1.4", "1.5", "1.6", "1.7", "1.8", "1.9",
                "2.1", "2.2", "2.3", "2.4", "2.5", "2.6", "2.7", "2.8", "2.9", "2.10", "2.11", "2.12"
            };
            
            for (int i = 0; i < titles.length; i++) {
                KnowledgePoint point = new KnowledgePoint();
                point.setId(i + 1);
                point.setTitle(titles[i]);
                point.setContent("这是关于" + titles[i] + "的详细内容。");
                point.setStage(stages[i]);
                mockPoints.add(point);
            }
            
            return mockPoints;
        }
    }

    @GetMapping("/point")
    public KnowledgePoint getPoint(@RequestParam String title) {
        return repo.findByTitle(title);
    }

    /**
     * 获取知识目录（按基础/进阶分组）
     */
    @GetMapping("/catalog")
    public List<Map<String, Object>> getCatalog() {
        try {
            List<KnowledgePoint> points = repo.findAllOrderByStage();
            List<Map<String, Object>> catalog = new ArrayList<>();
            
            // 创建基础分组
            Map<String, Object> basicGroup = Map.of(
                "id", "basic",
                "title", "Python基础",
                "type", "group",
                "status", "已完成",
                "children", points.stream()
                    .filter(p -> p.getStage() != null && p.getStage().startsWith("1."))
                    .map(p -> Map.of(
                        "id", p.getId(),
                        "title", p.getTitle(),
                        "status", "已完成"
                    ))
                    .collect(java.util.stream.Collectors.toList())
            );
            
            // 创建进阶分组
            Map<String, Object> advancedGroup = Map.of(
                "id", "advanced",
                "title", "Python进阶",
                "type", "group",
                "status", "进行中",
                "children", points.stream()
                    .filter(p -> p.getStage() != null && p.getStage().startsWith("2."))
                    .map(p -> Map.of(
                        "id", p.getId(),
                        "title", p.getTitle(),
                        "status", "已完成"
                    ))
                    .collect(java.util.stream.Collectors.toList())
            );
            
            catalog.add(basicGroup);
            catalog.add(advancedGroup);
            
            return catalog;
        } catch (Exception e) {
            // 如果数据库查询失败，返回模拟数据
            List<Map<String, Object>> catalog = new ArrayList<>();
            
            // 基础分组
            List<Map<String, Object>> basicChildren = new ArrayList<>();
            basicChildren.add(Map.of("id", 1, "title", "Python3 教程", "status", "已完成"));
            basicChildren.add(Map.of("id", 2, "title", "Python3 环境搭建", "status", "已完成"));
            basicChildren.add(Map.of("id", 3, "title", "Python VScode 配置", "status", "已完成"));
            basicChildren.add(Map.of("id", 4, "title", "Python3 基础语法", "status", "已完成"));
            basicChildren.add(Map.of("id", 5, "title", "Python3 基本数据类型", "status", "已完成"));
            basicChildren.add(Map.of("id", 6, "title", "Python3 运算符", "status", "已完成"));
            basicChildren.add(Map.of("id", 7, "title", "Python3 字符串", "status", "已完成"));
            basicChildren.add(Map.of("id", 8, "title", "Python3 列表", "status", "已完成"));
            basicChildren.add(Map.of("id", 9, "title", "Python3 元组", "status", "已完成"));
            
            // 进阶分组
            List<Map<String, Object>> advancedChildren = new ArrayList<>();
            advancedChildren.add(Map.of("id", 10, "title", "Python3 字典", "status", "已完成"));
            advancedChildren.add(Map.of("id", 11, "title", "Python3 集合", "status", "已完成"));
            advancedChildren.add(Map.of("id", 12, "title", "Python3 条件控制", "status", "已完成"));
            advancedChildren.add(Map.of("id", 13, "title", "Python3 循环语句", "status", "已完成"));
            advancedChildren.add(Map.of("id", 14, "title", "Python3 函数", "status", "已完成"));
            advancedChildren.add(Map.of("id", 15, "title", "Python3 数据结构", "status", "已完成"));
            advancedChildren.add(Map.of("id", 16, "title", "Python3 OS 文件/目录方法", "status", "已完成"));
            advancedChildren.add(Map.of("id", 17, "title", "Python3 数据类型转换", "status", "已完成"));
            advancedChildren.add(Map.of("id", 18, "title", "Python3 注释", "status", "已完成"));
            advancedChildren.add(Map.of("id", 19, "title", "Python lambda (匿名函数)", "status", "已完成"));
            advancedChildren.add(Map.of("id", 20, "title", "Python3 数字(Number)", "status", "已完成"));
            advancedChildren.add(Map.of("id", 21, "title", "Python3 File 方法", "status", "已完成"));
            
            catalog.add(Map.of(
                "id", "basic",
                "title", "Python基础",
                "type", "group",
                "status", "已完成",
                "children", basicChildren
            ));
            
            catalog.add(Map.of(
                "id", "advanced",
                "title", "Python进阶",
                "type", "group",
                "status", "进行中",
                "children", advancedChildren
            ));
            
            return catalog;
        }
    }

    /**
     * 获取知识点详情和关联题目
     */
    @GetMapping("/{id}/detail")
    public Map<String, Object> getKnowledgeDetailWithProblems(@PathVariable Integer id) {
        try {
            // 获取知识点详情
            KnowledgePoint knowledge = repo.findById(id);
            
            // 只有当question字段有值时才获取题目
            List<Map<String, Object>> problems = new ArrayList<>();
            if (knowledge != null && knowledge.getQuestion() != null && !knowledge.getQuestion().trim().isEmpty()) {
                // 兼容中文逗号、顿号、分号以及空白
                String normalized = knowledge.getQuestion()
                        .replace('，', ',')
                        .replace('、', ',')
                        .replace('；', ',')
                        .replace(';', ',')
                        .replace('\n', ',')
                        .replace('\r', ',')
                        .replaceAll("\\s+", "");

                String[] parts = normalized.split(",");
                if (parts.length > 0) {
                    List<String> idsInOrder = new ArrayList<>();
                    for (String raw : parts) {
                        if (raw == null) continue;
                        String idStr = raw.trim();
                        if (!idStr.isEmpty()) idsInOrder.add(idStr);
                    }

                    if (!idsInOrder.isEmpty()) {
                        try {
                            List<PythonProblem> actualProblems = pythonProblemService.findByIds(idsInOrder);
                            Map<String, PythonProblem> idToProblem = new HashMap<>();
                            for (PythonProblem p : actualProblems) {
                                idToProblem.put(p.getId(), p);
                            }
                            // 按原顺序逐个输出，没查到的给占位
                            for (String qid : idsInOrder) {
                                PythonProblem p = idToProblem.get(qid);
                                if (p != null) {
                                    String difficulty = "简单";
                                    if (p.getDif() != null) {
                                        if (p.getDif() == 1) difficulty = "简单";
                                        else if (p.getDif() == 2) difficulty = "中等";
                                        else if (p.getDif() == 3) difficulty = "困难";
                                    }
                                    problems.add(Map.of(
                                        "id", p.getId(),
                                        "title", p.getTitle(),
                                        "difficulty", difficulty
                                    ));
                                } else {
                                    problems.add(Map.of(
                                        "id", qid,
                                        "title", "题目 " + qid,
                                        "difficulty", "简单"
                                    ));
                                }
                            }
                        } catch (Exception ignore) {
                            // 查询失败时，全部用占位标题
                            for (String qid : idsInOrder) {
                                problems.add(Map.of(
                                    "id", qid,
                                    "title", "题目 " + qid,
                                    "difficulty", "简单"
                                ));
                            }
                        }
                    }
                }
            }
            
            return Map.of(
                "knowledge", knowledge != null ? knowledge : Map.of("id", id, "title", "知识点", "content", "暂无内容"),
                "problems", problems
            );
        } catch (Exception e) {
            // 如果数据库查询失败，返回模拟数据
            KnowledgePoint mockKnowledge = new KnowledgePoint();
            mockKnowledge.setId(id);
            mockKnowledge.setTitle("Python知识点 " + id);
            mockKnowledge.setContent("这是关于Python知识点的详细内容。请稍后再试或联系管理员。");
            // 模拟数据不包含question字段，所以不显示题目列表
            
            return Map.of(
                "knowledge", mockKnowledge,
                "problems", new ArrayList<>()
            );
        }
    }

    /**
     * 简单评论模型
     */
    public static class KnowledgeCommentDto {
        public Integer id;
        public Integer knowledgeId;
        public Integer userId;
        public String nickname;
        public String content;
        public Long likes;
        public String avatar;
        public Integer parentId;
        public Integer rootId;
        public Integer replyToUserId;
        public Long replyCount;
        public Long createdAt;
    }

    @Autowired
    private com.demo.python_demo.service.KnowledgeCommentService knowledgeCommentService;

    @GetMapping("/{id}/comments")
    public List<KnowledgeCommentDto> getComments(@PathVariable Integer id,
                                                @RequestParam(defaultValue = "1") int page,
                                                @RequestParam(defaultValue = "20") int pageSize) {
        List<com.demo.python_demo.entity.KnowledgeComment> list = knowledgeCommentService.listByKnowledgeId(id, page, pageSize);
        List<KnowledgeCommentDto> out = new ArrayList<>(list.size());
        for (com.demo.python_demo.entity.KnowledgeComment c : list) {
            KnowledgeCommentDto dto = new KnowledgeCommentDto();
            dto.id = c.getId();
            dto.knowledgeId = c.getKnowledgeId();
            dto.userId = c.getUserId();
            dto.nickname = c.getNickname();
            dto.avatar = c.getAvatar();
            dto.parentId = c.getParentId();
            dto.rootId = c.getRootId();
            dto.replyToUserId = c.getReplyToUserId();
            dto.replyCount = c.getReplyCount();
            dto.content = c.getContent();
            dto.likes = c.getLikes() == null ? 0L : c.getLikes();
            dto.createdAt = c.getCreatedAt() != null ? c.getCreatedAt().getTime() : null;
            out.add(dto);
        }
        return out;
    }

    @PostMapping("/{id}/comments")
    public Map<String, Object> addComment(@PathVariable Integer id, @RequestBody Map<String, Object> body) {
        String content = String.valueOf(body.getOrDefault("content", ""));
        if (content == null || content.trim().isEmpty()) {
            return Map.of("success", false, "message", "内容不能为空");
        }
        com.demo.python_demo.entity.KnowledgeComment comment = new com.demo.python_demo.entity.KnowledgeComment();
        comment.setKnowledgeId(id);
        Object uid = body.get("userId");
        try {
            comment.setUserId(uid == null ? 0 : Integer.valueOf(String.valueOf(uid)));
        } catch (NumberFormatException ignore) {
            comment.setUserId(0);
        }
        comment.setNickname(String.valueOf(body.getOrDefault("nickname", "")));
        comment.setAvatar(String.valueOf(body.getOrDefault("avatar", "")));
        try { comment.setParentId(Integer.valueOf(String.valueOf(body.getOrDefault("parentId", "0")))); } catch (Exception e) { comment.setParentId(0); }
        try { comment.setRootId(Integer.valueOf(String.valueOf(body.getOrDefault("rootId", "0")))); } catch (Exception e) { comment.setRootId(0); }
        try { comment.setReplyToUserId(Integer.valueOf(String.valueOf(body.getOrDefault("replyToUserId", "0")))); } catch (Exception e) { comment.setReplyToUserId(0); }
        comment.setContent(content.trim());
        comment.setLikes(0L);
        comment.setReplyCount(0L);
        knowledgeCommentService.addComment(comment);
        return Map.of("success", true);
    }

    @RequestMapping(value = "/comments/{commentId}/likes", method = {RequestMethod.POST, RequestMethod.GET})
    public Map<String, Object> changeLikes(@PathVariable Integer commentId, @RequestParam(defaultValue = "1") long delta) {
        knowledgeCommentService.changeLikes(commentId, delta);
        return Map.of("success", true);
    }
}
