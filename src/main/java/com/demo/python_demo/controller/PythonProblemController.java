package com.demo.python_demo.controller;

import com.demo.python_demo.entity.PythonProblem;
import com.demo.python_demo.service.PythonProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/python-problems")
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
public class PythonProblemController {
    @Autowired
    private PythonProblemService pythonProblemService;

    @GetMapping
    public List<PythonProblem> getAllProblems() {
        return pythonProblemService.getAllProblems();
    }

    @GetMapping("/{id}")
    public PythonProblem getProblemById(@PathVariable String id) {
        return pythonProblemService.getProblemById(id);
    }

    @GetMapping("/search")
    public List<PythonProblem> searchProblems(@RequestParam String keyword) {
        return pythonProblemService.searchByKeyword(keyword);
    }

    @GetMapping("/recent")
    public List<PythonProblem> getRecentProblems(@RequestParam(defaultValue = "10") int limit) {
        return pythonProblemService.getRecentProblems(limit);
    }

    @PostMapping("/batch")
    public List<PythonProblem> getProblemsByIds(@RequestBody List<String> ids) {
        return pythonProblemService.findByIds(ids);
    }

    // 更新题目：支持基础或全量字段
    @PutMapping("/{id}")
    public Map<String, Object> updateProblem(@PathVariable String id, @RequestBody Map<String, Object> body) {
        String title = String.valueOf(body.getOrDefault("title", ""));
        Integer dif = null;
        try { dif = Integer.valueOf(String.valueOf(body.get("dif"))); } catch (Exception ignore) {}

        boolean hasExtended = body.containsKey("description") || body.containsKey("inputFormat") || body.containsKey("outputFormat") || body.containsKey("samples") || body.containsKey("note") || body.containsKey("background");
        boolean ok;
        if (hasExtended) {
            ok = pythonProblemService.updateProblemFull(
                    id,
                    title,
                    (String) body.getOrDefault("description", null),
                    (String) body.getOrDefault("inputFormat", null),
                    (String) body.getOrDefault("outputFormat", null),
                    (String) body.getOrDefault("samples", null),
                    (String) body.getOrDefault("note", null),
                    (String) body.getOrDefault("background", null),
                    dif
            );
        } else {
            ok = pythonProblemService.updateProblemBasic(id, title, dif);
        }
        return java.util.Collections.singletonMap("success", ok);
    }

    // 删除题目
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteProblem(@PathVariable String id) {
        boolean ok = pythonProblemService.deleteProblemById(id);
        return java.util.Collections.singletonMap("success", ok);
    }
}
