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
    @Select("SELECT ArticleID as articleId, Title as title, URL as url, PublicationDate as publicationDate, " +
            "Content as content, Author as author, Category as category, Tags as tags, Views as views, " +
            "Rating as rating, Duration as duration, Lessons as lessons, Difficulty as difficulty, " +
            "CoverImage as coverImage, AuthorAvatar as authorAvatar, Status as status, " +
            "CreatedAt as createdAt, UpdatedAt as updatedAt, FromRequestID as fromRequestId, " +
            "reviewComment, reviewedAt, reviewedBy " +
            "FROM course ORDER BY CreatedAt DESC")
    List<Course> findAll();

    /**
     * 根据ID获取课程
     */
    @Select("SELECT ArticleID as articleId, Title as title, URL as url, PublicationDate as publicationDate, " +
            "Content as content, Author as author, Category as category, Tags as tags, Views as views, " +
            "Rating as rating, Duration as duration, Lessons as lessons, Difficulty as difficulty, " +
            "CoverImage as coverImage, AuthorAvatar as authorAvatar, Status as status, " +
            "CreatedAt as createdAt, UpdatedAt as updatedAt, FromRequestID as fromRequestId, " +
            "reviewComment, reviewedAt, reviewedBy " +
            "FROM course WHERE ArticleID = #{articleId}")
    Course findById(Integer articleId);

    /**
     * 根据分类获取课程
     */
    @Select("SELECT ArticleID as articleId, Title as title, URL as url, PublicationDate as publicationDate, " +
            "Content as content, Author as author, Category as category, Tags as tags, Views as views, " +
            "Rating as rating, Duration as duration, Lessons as lessons, Difficulty as difficulty, " +
            "CoverImage as coverImage, AuthorAvatar as authorAvatar, Status as status, " +
            "CreatedAt as createdAt, UpdatedAt as updatedAt, reviewComment, reviewedAt, reviewedBy " +
            "FROM course WHERE Category = #{category} ORDER BY CreatedAt DESC")
    List<Course> findByCategory(String category);

    /**
     * 根据作者获取课程
     */
    @Select("SELECT ArticleID as articleId, Title as title, URL as url, PublicationDate as publicationDate, " +
            "Content as content, Author as author, Category as category, Tags as tags, Views as views, " +
            "Rating as rating, Duration as duration, Lessons as lessons, Difficulty as difficulty, " +
            "CoverImage as coverImage, AuthorAvatar as authorAvatar, Status as status, " +
            "CreatedAt as createdAt, UpdatedAt as updatedAt, reviewComment, reviewedAt, reviewedBy " +
            "FROM course WHERE Author = #{author} ORDER BY CreatedAt DESC")
    List<Course> findByAuthor(String author);

    /**
     * 根据状态获取课程
     */
    @Select("SELECT ArticleID as articleId, Title as title, URL as url, PublicationDate as publicationDate, " +
            "Content as content, Author as author, Category as category, Tags as tags, Views as views, " +
            "Rating as rating, Duration as duration, Lessons as lessons, Difficulty as difficulty, " +
            "CoverImage as coverImage, AuthorAvatar as authorAvatar, Status as status, " +
            "CreatedAt as createdAt, UpdatedAt as updatedAt, FromRequestID as fromRequestId, " +
            "reviewComment, reviewedAt, reviewedBy " +
            "FROM course WHERE Status = #{status} ORDER BY CreatedAt DESC")
    List<Course> findByStatus(String status);

    /**
     * 获取已审核课程（审核历史）
     */
    @Select("SELECT ArticleID as articleId, Title as title, URL as url, PublicationDate as publicationDate, " +
            "Content as content, Author as author, Category as category, Tags as tags, Views as views, " +
            "Rating as rating, Duration as duration, Lessons as lessons, Difficulty as difficulty, " +
            "CoverImage as coverImage, AuthorAvatar as authorAvatar, Status as status, " +
            "CreatedAt as createdAt, UpdatedAt as updatedAt, reviewComment, reviewedAt, reviewedBy " +
            "FROM course WHERE Status IN ('approved', 'rejected') AND reviewedBy IS NOT NULL ORDER BY reviewedAt DESC")
    List<Course> findReviewedCourses();

    /**
     * 搜索课程（标题、内容、标签）
     */
    @Select("SELECT ArticleID as articleId, Title as title, URL as url, PublicationDate as publicationDate, " +
            "Content as content, Author as author, Category as category, Tags as tags, Views as views, " +
            "Rating as rating, Duration as duration, Lessons as lessons, Difficulty as difficulty, " +
            "CoverImage as coverImage, AuthorAvatar as authorAvatar, Status as status, " +
            "CreatedAt as createdAt, UpdatedAt as updatedAt, reviewComment, reviewedAt, reviewedBy " +
            "FROM course WHERE Title LIKE CONCAT('%', #{keyword}, '%') " +
            "OR Content LIKE CONCAT('%', #{keyword}, '%') " +
            "OR Tags LIKE CONCAT('%', #{keyword}, '%') " +
            "ORDER BY CreatedAt DESC")
    List<Course> searchByKeyword(String keyword);

    /**
     * 获取热门课程（按浏览量排序）
     */
    @Select("SELECT ArticleID as articleId, Title as title, URL as url, PublicationDate as publicationDate, " +
            "Content as content, Author as author, Category as category, Tags as tags, Views as views, " +
            "Rating as rating, Duration as duration, Lessons as lessons, Difficulty as difficulty, " +
            "CoverImage as coverImage, AuthorAvatar as authorAvatar, Status as status, " +
            "CreatedAt as createdAt, UpdatedAt as updatedAt, reviewComment, reviewedAt, reviewedBy " +
            "FROM course ORDER BY Views DESC LIMIT #{limit}")
    List<Course> findPopularCourses(Integer limit);

    /**
     * 获取最新课程
     */
    @Select("SELECT ArticleID as articleId, Title as title, URL as url, PublicationDate as publicationDate, " +
            "Content as content, Author as author, Category as category, Tags as tags, Views as views, " +
            "Rating as rating, Duration as duration, Lessons as lessons, Difficulty as difficulty, " +
            "CoverImage as coverImage, AuthorAvatar as authorAvatar, Status as status, " +
            "CreatedAt as createdAt, UpdatedAt as updatedAt, reviewComment, reviewedAt, reviewedBy " +
            "FROM course ORDER BY PublicationDate DESC LIMIT #{limit}")
    List<Course> findLatestCourses(Integer limit);

    /**
     * 创建课程
     */
    @Insert("INSERT INTO course (Title, PublicationDate, Content, Author, Category, Tags, Views, Rating, Duration, Lessons, Difficulty, CoverImage) " +
            "VALUES (#{title}, #{publicationDate}, #{content}, #{author}, #{category}, #{tags}, #{views}, #{rating}, #{duration}, #{lessons}, #{difficulty}, #{coverImage})")
    @Options(useGeneratedKeys = true, keyProperty = "articleId")
    int insert(Course course);

    /**
     * 更新课程
     */
    @Update("UPDATE course SET Title = #{title}, PublicationDate = #{publicationDate}, " +
            "Content = #{content}, Author = #{author}, Category = #{category}, Tags = #{tags}, " +
            "Views = #{views}, Rating = #{rating}, Duration = #{duration}, Lessons = #{lessons}, " +
            "Difficulty = #{difficulty}, CoverImage = #{coverImage}, Status = #{status}, " +
            "reviewComment = #{reviewComment}, reviewedAt = #{reviewedAt}, reviewedBy = #{reviewedBy}, " +
            "UpdatedAt = #{updatedAt} " +
            "WHERE ArticleID = #{articleId}")
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
     * 分页查询课程
     */
    @Select("SELECT ArticleID as articleId, Title as title, URL as url, PublicationDate as publicationDate, " +
            "Content as content, Author as author, Category as category, Tags as tags, Views as views, " +
            "Rating as rating, Duration as duration, Lessons as lessons, Difficulty as difficulty, " +
            "CoverImage as coverImage, AuthorAvatar as authorAvatar, Status as status, " +
            "CreatedAt as createdAt, UpdatedAt as updatedAt, reviewComment, reviewedAt, reviewedBy " +
            "FROM course ORDER BY CreatedAt DESC LIMIT #{offset}, #{limit}")
    List<Course> findWithPagination(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 根据分类统计课程数量
     */
    @Select("SELECT Category, COUNT(*) as count FROM course GROUP BY Category")
    List<Object> countByCategory();
} 