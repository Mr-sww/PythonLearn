package com.demo.python_demo.service;

import com.demo.python_demo.entity.Course;

import java.util.List;
import java.util.Optional;

/**
 * 课程服务接口
 */
public interface CourseService {

    /**
     * 获取所有课程
     */
    List<Course> getAllCourses();

    /**
     * 根据ID获取课程
     */
    Optional<Course> getCourseById(Integer articleId);

    /**
     * 根据分类获取课程
     */
    List<Course> getCoursesByCategory(String category);

    /**
     * 根据作者获取课程
     */
    List<Course> getCoursesByAuthor(String author);

    /**
     * 搜索课程
     */
    List<Course> searchCourses(String keyword);

    /**
     * 获取热门课程
     */
    List<Course> getPopularCourses(Integer limit);

    /**
     * 获取最新课程
     */
    List<Course> getLatestCourses(Integer limit);

    /**
     * 创建课程
     */
    Course createCourse(Course course);

    /**
     * 更新课程
     */
    Optional<Course> updateCourse(Integer articleId, Course course);

    /**
     * 删除课程
     */
    boolean deleteCourse(Integer articleId);

    /**
     * 增加课程浏览量
     */
    boolean incrementViews(Integer articleId);

    /**
     * 获取课程总数
     */
    int getCourseCount();

    /**
     * 分页获取课程
     */
    List<Course> getCoursesWithPagination(int page, int size);

    /**
     * 获取分类统计
     */
    List<Object> getCategoryStatistics();

    /**
     * 获取推荐课程
     */
    List<Course> getRecommendedCourses(Integer userId, Integer limit);
} 