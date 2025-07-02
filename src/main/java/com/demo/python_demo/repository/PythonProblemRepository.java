package com.demo.python_demo.repository;

import com.demo.python_demo.entity.PythonProblem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface PythonProblemRepository {
    @Select("SELECT * FROM PythonProblems")
    List<PythonProblem> findAll();
} 