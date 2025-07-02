package com.demo.python_demo.repository;

import com.demo.python_demo.entity.ProblemFavorite;
import org.apache.ibatis.annotations.*;
import java.util.List;
import java.util.Map;

@Mapper
public interface ProblemFavoriteRepository {
    @Select("SELECT * FROM problem_favorite WHERE user_id = #{userId} AND problem_id = #{problemId}")
    @Results({
        @Result(property = "favoriteId", column = "favorite_id"),
        @Result(property = "userId", column = "user_id"),
        @Result(property = "problemId", column = "problem_id"),
        @Result(property = "createdAt", column = "created_at")
    })
    ProblemFavorite findByUserAndProblem(@Param("userId") int userId, @Param("problemId") String problemId);

    @Insert("INSERT INTO problem_favorite (user_id, problem_id, created_at) VALUES (#{userId}, #{problemId}, GETDATE())")
    void insert(ProblemFavorite favorite);

    @Delete("DELETE FROM problem_favorite WHERE user_id = #{userId} AND problem_id = #{problemId}")
    void delete(@Param("userId") int userId, @Param("problemId") String problemId);

    @Select("SELECT pf.*, p.Title as title, p.Description as description FROM problem_favorite pf LEFT JOIN PythonProblems p ON pf.problem_id = p.Id WHERE pf.user_id = #{userId}")
    List<Map<String, Object>> findAllByUserId(@Param("userId") int userId);

    @Select("SELECT COUNT(*) FROM problem_favorite WHERE problem_id = #{problemId}")
    int countByProblemId(@Param("problemId") String problemId);
} 