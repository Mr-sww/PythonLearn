package com.demo.python_demo.repository;

import com.demo.python_demo.entity.PythonProblem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Result;
import java.util.List;

@Mapper
public interface PythonProblemRepository {
    @Select("SELECT * FROM pythonproblems")
    @Results({
        @Result(property = "id", column = "Id"),
        @Result(property = "title", column = "Title"),
        @Result(property = "description", column = "Description"),
        @Result(property = "inputFormat", column = "InputFormat"),
        @Result(property = "outputFormat", column = "OutputFormat"),
        @Result(property = "note", column = "Note"),
        @Result(property = "samples", column = "Samples"),
        @Result(property = "background", column = "Background"),
        @Result(property = "createTime", column = "CreateTime"),
        @Result(property = "dif", column = "dif")
    })
    List<PythonProblem> findAll();

    @Select("SELECT * FROM pythonproblems WHERE Id = #{id}")
    @Results({
        @Result(property = "id", column = "Id"),
        @Result(property = "title", column = "Title"),
        @Result(property = "description", column = "Description"),
        @Result(property = "inputFormat", column = "InputFormat"),
        @Result(property = "outputFormat", column = "OutputFormat"),
        @Result(property = "note", column = "Note"),
        @Result(property = "samples", column = "Samples"),
        @Result(property = "background", column = "Background"),
        @Result(property = "createTime", column = "CreateTime"),
        @Result(property = "dif", column = "dif")
    })
    PythonProblem findById(String id);

    @Select({
        "<script>",
        "SELECT * FROM pythonproblems WHERE Id IN",
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
        @Result(property = "createTime", column = "CreateTime"),
        @Result(property = "dif", column = "dif")
    })
    List<PythonProblem> findByIds(@org.apache.ibatis.annotations.Param("ids") List<String> ids);

    @Select("SELECT * FROM pythonproblems WHERE Title LIKE CONCAT('%', #{keyword}, '%') OR Description LIKE CONCAT('%', #{keyword}, '%')")
    @Results({
        @Result(property = "id", column = "Id"),
        @Result(property = "title", column = "Title"),
        @Result(property = "description", column = "Description"),
        @Result(property = "inputFormat", column = "InputFormat"),
        @Result(property = "outputFormat", column = "OutputFormat"),
        @Result(property = "note", column = "Note"),
        @Result(property = "samples", column = "Samples"),
        @Result(property = "background", column = "Background"),
        @Result(property = "createTime", column = "CreateTime"),
        @Result(property = "dif", column = "dif")
    })
    List<PythonProblem> searchByKeyword(String keyword);

    @Select("SELECT * FROM pythonproblems ORDER BY CreateTime DESC LIMIT #{limit}")
    @Results({
        @Result(property = "id", column = "Id"),
        @Result(property = "title", column = "Title"),
        @Result(property = "description", column = "Description"),
        @Result(property = "inputFormat", column = "InputFormat"),
        @Result(property = "outputFormat", column = "OutputFormat"),
        @Result(property = "note", column = "Note"),
        @Result(property = "samples", column = "Samples"),
        @Result(property = "background", column = "Background"),
        @Result(property = "createTime", column = "CreateTime"),
        @Result(property = "dif", column = "dif")
    })
    List<PythonProblem> findRecent(int limit);
} 