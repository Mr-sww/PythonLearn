package com.demo.python_demo.service;

import com.demo.python_demo.entity.PythonVideo;
import java.util.List;

public interface PythonVideoService {
    List<PythonVideo> getAllVideos();
    List<PythonVideo> findByCategories(List<String> categories, List<String> excluded);
} 