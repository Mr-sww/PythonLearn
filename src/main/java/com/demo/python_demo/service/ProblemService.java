package com.demo.python_demo.service;

import com.demo.python_demo.entity.PythonProblem;
import java.util.List;

public interface ProblemService {
    List<PythonProblem> findByIds(List<String> ids);
} 