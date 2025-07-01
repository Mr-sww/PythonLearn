package com.demo.python_demo.service.impl;

import com.demo.python_demo.entity.Course;
import com.demo.python_demo.repository.CourseRepository;
import com.demo.python_demo.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 课程服务实现类
 */
@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public Optional<Course> getCourseById(Integer articleId) {
        Course course = courseRepository.findById(articleId);
        return Optional.ofNullable(course);
    }

    @Override
    public List<Course> getCoursesByCategory(String category) {
        return courseRepository.findByCategory(category);
    }

    @Override
    public List<Course> getCoursesByAuthor(String author) {
        return courseRepository.findByAuthor(author);
    }

    @Override
    public List<Course> searchCourses(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllCourses();
        }
        return courseRepository.searchByKeyword(keyword.trim());
    }

    @Override
    public List<Course> getPopularCourses(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        return courseRepository.findPopularCourses(limit);
    }

    @Override
    public List<Course> getLatestCourses(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        return courseRepository.findLatestCourses(limit);
    }

    @Override
    public Course createCourse(Course course) {
        if (course == null) {
            throw new IllegalArgumentException("课程信息不能为空");
        }
        
        // 设置默认值
        if (course.getViews() == null) {
            course.setViews(0);
        }
        if (course.getPublicationDate() == null) {
            course.setPublicationDate(LocalDateTime.now());
        }
        
        int result = courseRepository.insert(course);
        if (result > 0) {
            return course;
        }
        return null;
    }

    @Override
    public Optional<Course> updateCourse(Integer articleId, Course course) {
        if (articleId == null || course == null) {
            return Optional.empty();
        }
        
        Course existingCourse = courseRepository.findById(articleId);
        if (existingCourse == null) {
            return Optional.empty();
        }
        
        // 更新字段
        if (course.getTitle() != null) {
            existingCourse.setTitle(course.getTitle());
        }
        if (course.getUrl() != null) {
            existingCourse.setUrl(course.getUrl());
        }
        if (course.getContent() != null) {
            existingCourse.setContent(course.getContent());
        }
        if (course.getAuthor() != null) {
            existingCourse.setAuthor(course.getAuthor());
        }
        if (course.getCategory() != null) {
            existingCourse.setCategory(course.getCategory());
        }
        if (course.getTags() != null) {
            existingCourse.setTags(course.getTags());
        }
        if (course.getViews() != null) {
            existingCourse.setViews(course.getViews());
        }
        
        int result = courseRepository.update(existingCourse);
        if (result > 0) {
            return Optional.of(existingCourse);
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteCourse(Integer articleId) {
        if (articleId == null) {
            return false;
        }
        int result = courseRepository.deleteById(articleId);
        return result > 0;
    }

    @Override
    public boolean incrementViews(Integer articleId) {
        if (articleId == null) {
            return false;
        }
        int result = courseRepository.incrementViews(articleId);
        return result > 0;
    }

    @Override
    public int getCourseCount() {
        return courseRepository.count();
    }

    @Override
    public List<Course> getCoursesWithPagination(int page, int size) {
        if (page < 0) page = 0;
        if (size <= 0) size = 10;
        
        int offset = page * size;
        return courseRepository.findWithPagination(offset, size);
    }

    @Override
    public List<Object> getCategoryStatistics() {
        return courseRepository.countByCategory();
    }

    @Override
    public List<Course> getRecommendedCourses(Integer userId, Integer limit) {
        // 简单的推荐逻辑：返回热门课程
        // 在实际项目中，这里可以实现更复杂的推荐算法
        if (limit == null || limit <= 0) {
            limit = 6;
        }
        return getPopularCourses(limit);
    }
} 