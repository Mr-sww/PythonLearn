package com.demo.python_demo.repository;

import com.demo.python_demo.entity.CourseComment;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 课程评论数据访问接口
 */
@Mapper
public interface CourseCommentRepository {

    /**
     * 根据课程ID获取评论列表
     */
    @Select("SELECT cc.*, u.nickname as userName, u.avatar as userAvatar " +
            "FROM course_comment cc " +
            "LEFT JOIN user u ON cc.UserID = u.user_id " +
            "WHERE cc.CourseID = #{courseId} AND cc.ParentID IS NULL " +
            "ORDER BY cc.CreatedAt DESC")
    List<CourseComment> findByCourseId(Integer courseId);

    /**
     * 根据父评论ID获取回复列表
     */
    @Select("SELECT cc.*, u.nickname as userName, u.avatar as userAvatar " +
            "FROM course_comment cc " +
            "LEFT JOIN user u ON cc.UserID = u.user_id " +
            "WHERE cc.ParentID = #{parentId} " +
            "ORDER BY cc.CreatedAt ASC")
    List<CourseComment> findByParentId(Integer parentId);

    /**
     * 根据ID获取评论
     */
    @Select("SELECT cc.*, u.nickname as userName, u.avatar as userAvatar " +
            "FROM course_comment cc " +
            "LEFT JOIN user u ON cc.UserID = u.user_id " +
            "WHERE cc.CommentID = #{commentId}")
    CourseComment findById(Integer commentId);

    /**
     * 创建评论
     */
    @Insert("INSERT INTO course_comment (CourseID, UserID, Content, Rating, ParentID, Likes) " +
            "VALUES (#{courseId}, #{userId}, #{content}, #{rating}, #{parentId}, #{likes})")
    @Options(useGeneratedKeys = true, keyProperty = "commentId")
    int insert(CourseComment comment);

    /**
     * 更新评论
     */
    @Update("UPDATE course_comment SET Content = #{content}, Rating = #{rating}, " +
            "UpdatedAt = CURRENT_TIMESTAMP WHERE CommentID = #{commentId}")
    int update(CourseComment comment);

    /**
     * 删除评论
     */
    @Delete("DELETE FROM course_comment WHERE CommentID = #{commentId}")
    int deleteById(Integer commentId);

    /**
     * 增加点赞数
     */
    @Update("UPDATE course_comment SET Likes = Likes + 1 WHERE CommentID = #{commentId}")
    int incrementLikes(Integer commentId);

    /**
     * 减少点赞数
     */
    @Update("UPDATE course_comment SET Likes = GREATEST(Likes - 1, 0) WHERE CommentID = #{commentId}")
    int decrementLikes(Integer commentId);

    /**
     * 获取课程评论总数
     */
    @Select("SELECT COUNT(*) FROM course_comment WHERE CourseID = #{courseId}")
    int countByCourseId(Integer courseId);

    /**
     * 获取课程平均评分
     */
    @Select("SELECT AVG(Rating) FROM course_comment WHERE CourseID = #{courseId} AND Rating IS NOT NULL")
    Double getAverageRating(Integer courseId);

    /**
     * 获取用户评论列表
     */
    @Select("SELECT cc.*, c.Title as courseTitle " +
            "FROM course_comment cc " +
            "LEFT JOIN course c ON cc.CourseID = c.ArticleID " +
            "WHERE cc.UserID = #{userId} " +
            "ORDER BY cc.CreatedAt DESC")
    List<CourseComment> findByUserId(Integer userId);
} 