package com.demo.python_demo.controller;

import com.demo.python_demo.entity.PythonProblem;
import com.demo.python_demo.service.PythonProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
}
