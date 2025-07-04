package com.demo.python_demo.service.impl;

import com.demo.python_demo.entity.PythonVideo;
import com.demo.python_demo.repository.PythonVideoRepository;
import com.demo.python_demo.service.PythonVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;

@Service
public class PythonVideoServiceImpl implements PythonVideoService {
    @Autowired
    private PythonVideoRepository pythonVideoRepository;

    @Override
    public List<PythonVideo> getAllVideos() {
        return pythonVideoRepository.findAll();
    }

    @Override
    public List<PythonVideo> findByCategories(List<String> categories, List<String> excluded) {
        if ((categories == null || categories.isEmpty()) && (excluded == null || excluded.isEmpty())) {
            return pythonVideoRepository.findAll();
        }
        return pythonVideoRepository.findByCategories(categories, excluded);
    }
} 