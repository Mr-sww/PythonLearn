package com.demo.python_demo.repository;

import com.demo.python_demo.entity.Course;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 课程数据访问接口
 */
@Mapper
public interface CourseRepository {

    /**
     * 获取所有课程
     */
    @Select("SELECT * FROM course ORDER BY createdAt DESC")
    List<Course> findAll();

    /**
     * 根据ID获取课程
     */
    @Select("SELECT * FROM course WHERE ArticleID = #{articleId}")
    Course findById(Integer articleId);

    /**
     * 根据分类获取课程
     */
    @Select("SELECT * FROM course WHERE Category = #{category} ORDER BY createdAt DESC")
    List<Course> findByCategory(String category);

    /**
     * 根据作者获取课程
     */
    @Select("SELECT * FROM course WHERE Author = #{author} ORDER BY createdAt DESC")
    List<Course> findByAuthor(String author);

    /**
     * 搜索课程（标题、内容、标签）
     */
    @Select("SELECT * FROM course WHERE Title LIKE '%' + #{keyword} + '%' " +
            "OR Content LIKE '%' + #{keyword} + '%' " +
            "OR Tags LIKE '%' + #{keyword} + '%' " +
            "ORDER BY createdAt DESC")
    List<Course> searchByKeyword(String keyword);

    /**
     * 获取热门课程（按浏览量排序，SQL Server实现）
     */
    @Select("SELECT TOP (${limit}) * FROM course ORDER BY Views DESC")
    List<Course> findPopularCourses(Integer limit);

    /**
     * 获取最新课程（SQL Server实现）
     */
    @Select("SELECT TOP (${limit}) * FROM course ORDER BY PublicationDate DESC")
    List<Course> findLatestCourses(Integer limit);

    /**
     * 创建课程
     */
    @Insert("INSERT INTO course (Title, URL, PublicationDate, Content, Author, Category, Tags, Views) " +
            "VALUES (#{title}, #{url}, #{publicationDate}, #{content}, #{author}, #{category}, #{tags}, #{views})")
    @Options(useGeneratedKeys = true, keyProperty = "articleId")
    int insert(Course course);

    /**
     * 更新课程
     */
    @Update("UPDATE course SET Title = #{title}, URL = #{url}, PublicationDate = #{publicationDate}, " +
            "Content = #{content}, Author = #{author}, Category = #{category}, Tags = #{tags}, " +
            "Views = #{views}, UpdatedAt = CURRENT_TIMESTAMP WHERE ArticleID = #{articleId}")
    int update(Course course);

    /**
     * 删除课程
     */
    @Delete("DELETE FROM course WHERE ArticleID = #{articleId}")
    int deleteById(Integer articleId);

    /**
     * 增加浏览量
     */
    @Update("UPDATE course SET Views = Views + 1 WHERE ArticleID = #{articleId}")
    int incrementViews(Integer articleId);

    /**
     * 获取课程总数
     */
    @Select("SELECT COUNT(*) FROM course")
    int count();

    /**
     * 分页查询课程（SQL Server实现）
     */
    @Select("SELECT * FROM course ORDER BY createdAt DESC OFFSET #{offset} ROWS FETCH NEXT #{limit} ROWS ONLY")
    List<Course> findWithPagination(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 根据分类统计课程数量
     */
    @Select("SELECT Category, COUNT(*) as count FROM course GROUP BY Category")
    List<Object> countByCategory();
} 