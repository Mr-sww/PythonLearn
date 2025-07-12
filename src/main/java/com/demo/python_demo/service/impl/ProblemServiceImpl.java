package com.demo.python_demo.service.impl;

import com.demo.python_demo.entity.PythonProblem;
import com.demo.python_demo.repository.PythonProblemRepository;
import com.demo.python_demo.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProblemServiceImpl implements ProblemService {
    @Autowired
    private PythonProblemRepository pythonProblemRepository;

    @Override
    public List<PythonProblem> findByIds(List<String> ids) {
        return pythonProblemRepository.findByIds(ids);
    }
} 