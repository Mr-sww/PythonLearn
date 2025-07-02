package com.demo.python_demo.repository;

import com.demo.python_demo.entity.ProblemComment;
import org.apache.ibatis.annotations.*;
import java.util.List;
import java.util.Map;

@Mapper
public interface ProblemCommentRepository {
    @Select("SELECT * FROM problem_comment WHERE problem_id = #{problemId} ORDER BY created_at DESC")
    List<ProblemComment> findByProblemId(String problemId);

    @Insert("INSERT INTO problem_comment (problem_id, user_id, content, parent_id, likes, created_at, updated_at) VALUES (#{problemId}, #{userId}, #{content}, #{parentId}, 0, GETDATE(), GETDATE())")
    void insert(ProblemComment comment);

    @Update("UPDATE problem_comment SET likes = likes + 1 WHERE comment_id = #{commentId}")
    void likeComment(Integer commentId);

    @Select("SELECT c.*, u.nickname, u.avatar FROM problem_comment c LEFT JOIN [user] u ON c.user_id = u.user_id WHERE c.problem_id = #{problemId} ORDER BY c.created_at DESC OFFSET #{offset} ROWS FETCH NEXT #{pageSize} ROWS ONLY")
    List<Map<String, Object>> findPagedByProblemId(@Param("problemId") String problemId, @Param("offset") int offset, @Param("pageSize") int pageSize);

    @Select("SELECT COUNT(*) FROM problem_comment WHERE problem_id = #{problemId}")
    int countByProblemId(String problemId);
} 