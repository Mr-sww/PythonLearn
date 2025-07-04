package com.demo.python_demo.controller;

import com.demo.python_demo.entity.PythonVideo;
import com.demo.python_demo.service.PythonVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/python-videos")
public class PythonVideoController {
    @Autowired
    private PythonVideoService pythonVideoService;

    @GetMapping
    public List<PythonVideo> getAllVideos() {
        return pythonVideoService.getAllVideos();
    }

    @PostMapping("/filter")
    public List<PythonVideo> filterCourses(@RequestBody Map<String, List<String>> body) {
        List<String> categories = body.get("categories");
        List<String> excluded = body.get("excluded");
        return pythonVideoService.findByCategories(categories, excluded);
    }
} 