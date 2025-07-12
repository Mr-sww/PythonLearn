package com.demo.python_demo.repository;

import com.demo.python_demo.entity.UserFavorite;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserFavoriteRepository {
    
    /**
     * 查询用户收藏的课程列表
     */
    @Select("SELECT uf.id, uf.user_id, uf.course_id, uf.create_time, " +
            "c.ArticleID, c.Title, c.URL, c.PublicationDate, c.Content, c.Author, c.Category, c.Tags, c.Views, c.CreatedAt, c.UpdatedAt " +
            "FROM user_favorite uf " +
            "LEFT JOIN course c ON uf.course_id = c.ArticleID " +
            "WHERE uf.user_id = #{userId} " +
            "ORDER BY uf.create_time DESC")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "userId", column = "user_id"),
        @Result(property = "courseId", column = "course_id"),
        @Result(property = "createTime", column = "create_time"),
        @Result(property = "course.articleId", column = "ArticleID"),
        @Result(property = "course.title", column = "Title"),
        @Result(property = "course.url", column = "URL"),
        @Result(property = "course.publicationDate", column = "PublicationDate"),
        @Result(property = "course.content", column = "Content"),
        @Result(property = "course.author", column = "Author"),
        @Result(property = "course.category", column = "Category"),
        @Result(property = "course.tags", column = "Tags"),
        @Result(property = "course.views", column = "Views"),
        @Result(property = "course.createdAt", column = "CreatedAt"),
        @Result(property = "course.updatedAt", column = "UpdatedAt")
    })
    List<UserFavorite> findByUserId(Integer userId);
    
    /**
     * 添加收藏
     */
    @Insert("INSERT INTO user_favorite (user_id, course_id) VALUES (#{userId}, #{courseId})")
    int insert(UserFavorite userFavorite);
    
    /**
     * 取消收藏
     */
    @Delete("DELETE FROM user_favorite WHERE user_id = #{userId} AND course_id = #{courseId}")
    int deleteByUserIdAndCourseId(@Param("userId") Integer userId, @Param("courseId") Integer courseId);
    
    /**
     * 检查是否已收藏
     */
    @Select("SELECT COUNT(*) FROM user_favorite WHERE user_id = #{userId} AND course_id = #{courseId}")
    int countByUserIdAndCourseId(@Param("userId") Integer userId, @Param("courseId") Integer courseId);
    
    /**
     * 获取用户收藏数量
     */
    @Select("SELECT COUNT(*) FROM user_favorite WHERE user_id = #{userId}")
    int countByUserId(Integer userId);
} 