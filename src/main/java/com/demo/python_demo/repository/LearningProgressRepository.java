package com.demo.python_demo.repository;

import com.demo.python_demo.entity.LearningProgress;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 学习进度数据访问接口
 */
@Mapper
public interface LearningProgressRepository {

    /**
     * 根据用户ID和课程ID获取学习进度
     */
    @Select("SELECT lp.*, c.Title as courseTitle, ch.Title as chapterTitle, cl.Title as lessonTitle " +
            "FROM learning_progress lp " +
            "LEFT JOIN course c ON lp.CourseID = c.ArticleID " +
            "LEFT JOIN course_chapter ch ON lp.ChapterID = ch.ChapterID " +
            "LEFT JOIN course_lesson cl ON lp.LessonID = cl.LessonID " +
            "WHERE lp.UserID = #{userId} AND lp.CourseID = #{courseId}")
    LearningProgress findByUserAndCourse(Integer userId, Integer courseId);

    /**
     * 根据用户ID获取所有学习进度
     */
    @Select("SELECT lp.*, c.Title as courseTitle, ch.Title as chapterTitle, cl.Title as lessonTitle " +
            "FROM learning_progress lp " +
            "LEFT JOIN course c ON lp.CourseID = c.ArticleID " +
            "LEFT JOIN course_chapter ch ON lp.ChapterID = ch.ChapterID " +
            "LEFT JOIN course_lesson cl ON lp.LessonID = cl.LessonID " +
            "WHERE lp.UserID = #{userId} " +
            "ORDER BY lp.LastStudyTime DESC")
    List<LearningProgress> findByUserId(Integer userId);

    /**
     * 创建学习进度
     */
    @Insert("INSERT INTO learning_progress (UserID, CourseID, ChapterID, LessonID, Progress, Status, TimeSpent) " +
            "VALUES (#{userId}, #{courseId}, #{chapterId}, #{lessonId}, #{progress}, #{status}, #{timeSpent})")
    @Options(useGeneratedKeys = true, keyProperty = "progressId")
    int insert(LearningProgress progress);

    /**
     * 更新学习进度
     */
    @Update("UPDATE learning_progress SET ChapterID = #{chapterId}, LessonID = #{lessonId}, " +
            "Progress = #{progress}, Status = #{status}, TimeSpent = #{timeSpent}, " +
            "LastStudyTime = CURRENT_TIMESTAMP, UpdatedAt = CURRENT_TIMESTAMP " +
            "WHERE ProgressID = #{progressId}")
    int update(LearningProgress progress);

    /**
     * 更新或创建学习进度（SQL Server实现）
     */
    @Insert({
        "<script>",
        "IF EXISTS (SELECT 1 FROM learning_progress WHERE UserID = #{userId} AND CourseID = #{courseId})",
        "BEGIN",
        "   UPDATE learning_progress SET",
        "       ChapterID = #{chapterId},",
        "       LessonID = #{lessonId},",
        "       Progress = #{progress},",
        "       Status = #{status},",
        "       TimeSpent = #{timeSpent},",
        "       LastStudyTime = CURRENT_TIMESTAMP,",
        "       UpdatedAt = CURRENT_TIMESTAMP",
        "   WHERE UserID = #{userId} AND CourseID = #{courseId}",
        "END",
        "ELSE",
        "BEGIN",
        "   INSERT INTO learning_progress (UserID, CourseID, ChapterID, LessonID, Progress, Status, TimeSpent)",
        "   VALUES (#{userId}, #{courseId}, #{chapterId}, #{lessonId}, #{progress}, #{status}, #{timeSpent})",
        "END",
        "</script>"
    })
    int insertOrUpdate(LearningProgress progress);

    /**
     * 删除学习进度
     */
    @Delete("DELETE FROM learning_progress WHERE ProgressID = #{progressId}")
    int deleteById(Integer progressId);

    /**
     * 获取用户最近学习的课程（SQL Server实现）
     */
    @Select("SELECT TOP (${limit}) lp.*, c.Title as courseTitle " +
            "FROM learning_progress lp " +
            "LEFT JOIN course c ON lp.CourseID = c.ArticleID " +
            "WHERE lp.UserID = #{userId} " +
            "ORDER BY lp.LastStudyTime DESC")
    List<LearningProgress> findRecentCourses(Integer userId, Integer limit);

    /**
     * 获取用户学习统计
     */
    @Select("SELECT COUNT(*) as totalCourses, " +
            "SUM(CASE WHEN Status = 'completed' THEN 1 ELSE 0 END) as completedCourses, " +
            "SUM(TimeSpent) as totalTimeSpent, " +
            "AVG(Progress) as averageProgress " +
            "FROM learning_progress " +
            "WHERE UserID = #{userId}")
    Object getUserStatistics(Integer userId);

    /**
     * 获取课程学习人数
     */
    @Select("SELECT COUNT(*) FROM learning_progress WHERE CourseID = #{courseId}")
    int countByCourseId(Integer courseId);

    /**
     * 获取课程完成人数
     */
    @Select("SELECT COUNT(*) FROM learning_progress WHERE CourseID = #{courseId} AND Status = 'completed'")
    int countCompletedByCourseId(Integer courseId);
} 