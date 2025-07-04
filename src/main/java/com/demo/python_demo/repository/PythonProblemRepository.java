package com.demo.python_demo.repository;

import com.demo.python_demo.entity.PythonProblem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Result;
import java.util.List;

@Mapper
public interface PythonProblemRepository {
    @Select("SELECT * FROM PythonProblems")
    List<PythonProblem> findAll();

    @Select({
        "<script>",
        "SELECT * FROM PythonProblems WHERE Id IN",
        "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
        "#{id}",
        "</foreach>",
        "</script>"
    })
    @Results({
        @Result(property = "id", column = "Id"),
        @Result(property = "title", column = "Title"),
        @Result(property = "description", column = "Description"),
        @Result(property = "inputFormat", column = "InputFormat"),
        @Result(property = "outputFormat", column = "OutputFormat"),
        @Result(property = "note", column = "Note"),
        @Result(property = "samples", column = "Samples"),
        @Result(property = "background", column = "Background"),
        @Result(property = "createTime", column = "CreateTime")
    })
    List<PythonProblem> findByIds(@org.apache.ibatis.annotations.Param("ids") List<String> ids);
} 