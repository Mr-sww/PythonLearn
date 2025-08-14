package com.demo.python_demo.service;

import com.demo.python_demo.entity.PythonProblem;
import java.util.List;

public interface PythonProblemService {
    List<PythonProblem> getAllProblems();
    PythonProblem getProblemById(String id);
    List<PythonProblem> findByIds(List<String> ids);
    List<PythonProblem> searchByKeyword(String keyword);
    List<PythonProblem> getRecentProblems(int limit);
}
