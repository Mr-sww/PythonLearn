package com.demo.python_demo.service.impl;

import com.demo.python_demo.entity.PythonProblem;
import com.demo.python_demo.repository.PythonProblemRepository;
import com.demo.python_demo.service.PythonProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;

@Service
public class PythonProblemServiceImpl implements PythonProblemService {
    @Autowired
    private PythonProblemRepository pythonProblemRepository;

    @Override
    public List<PythonProblem> getAllProblems() {
        return pythonProblemRepository.findAll();
    }

    @Override
    public PythonProblem getProblemById(String id) {
        return pythonProblemRepository.findById(id);
    }

    @Override
    public List<PythonProblem> findByIds(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }
        return pythonProblemRepository.findByIds(ids);
    }

    @Override
    public List<PythonProblem> searchByKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return pythonProblemRepository.searchByKeyword(keyword.trim());
    }

    @Override
    public List<PythonProblem> getRecentProblems(int limit) {
        if (limit <= 0) {
            limit = 10; // 默认返回10条
        }
        return pythonProblemRepository.findRecent(limit);
    }

    @Override
    public boolean updateProblemBasic(String id, String title, Integer dif) {
        if (id == null) return false;
        try {
            return pythonProblemRepository.updateBasic(id, title, dif) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateProblemFull(String id, String title, String description, String inputFormat, String outputFormat, String samples, String note, String background, Integer dif) {
        if (id == null) return false;
        try {
            return pythonProblemRepository.updateFull(id, title, description, inputFormat, outputFormat, samples, note, background, dif) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean deleteProblemById(String id) {
        if (id == null) return false;
        try {
            return pythonProblemRepository.deleteById(id) > 0;
        } catch (Exception e) {
            return false;
        }
    }
}
