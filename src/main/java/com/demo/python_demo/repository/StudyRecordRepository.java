package com.demo.python_demo.repository;

import com.demo.python_demo.entity.StudyRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * 学习记录Repository接口
 */
@Mapper
public interface StudyRecordRepository {

    /**
     * 根据用户ID获取学习记录列表
     */
    @Select("SELECT sr.*, c.Title as courseTitle, cl.Title as lessonTitle, " +
            "c.CoverImage as courseImage, cc.Title as chapterTitle " +
            "FROM study_record sr " +
            "LEFT JOIN course c ON sr.CourseID = c.ArticleID " +
            "LEFT JOIN course_lesson cl ON sr.LessonID = cl.LessonID " +
            "LEFT JOIN course_chapter cc ON cl.ChapterID = cc.ChapterID " +
            "WHERE sr.UserID = #{userId} " +
            "ORDER BY sr.StudyDate DESC")
    List<StudyRecord> findByUserId(Integer userId);

    /**
     * 根据用户ID和课程ID获取学习记录
     */
    @Select("SELECT sr.*, c.Title as courseTitle, cl.Title as lessonTitle, " +
            "c.CoverImage as courseImage, cc.Title as chapterTitle " +
            "FROM study_record sr " +
            "LEFT JOIN course c ON sr.CourseID = c.ArticleID " +
            "LEFT JOIN course_lesson cl ON sr.LessonID = cl.LessonID " +
            "LEFT JOIN course_chapter cc ON cl.ChapterID = cc.ChapterID " +
            "WHERE sr.UserID = #{userId} AND sr.CourseID = #{courseId} " +
            "ORDER BY sr.StudyDate DESC")
    List<StudyRecord> findByUserIdAndCourseId(Integer userId, Integer courseId);

    /**
     * 插入学习记录
     */
    @Insert("INSERT INTO study_record (UserID, CourseID, LessonID, StudyTime, Progress, Completed, StudyDate) " +
            "VALUES (#{userId}, #{courseId}, #{lessonId}, #{studyTime}, #{progress}, #{completed}, #{studyDate})")
    @Options(useGeneratedKeys = true, keyProperty = "recordId")
    int insert(StudyRecord record);

    /**
     * 更新学习记录
     */
    @Update("UPDATE study_record SET StudyTime = #{studyTime}, Progress = #{progress}, " +
            "Completed = #{completed}, StudyDate = #{studyDate} " +
            "WHERE RecordID = #{recordId}")
    int update(StudyRecord record);

    /**
     * 删除学习记录
     */
    @Delete("DELETE FROM study_record WHERE RecordID = #{recordId}")
    int deleteById(Integer recordId);

    /**
     * 获取用户最近的学习记录
     */
    @Select("SELECT sr.*, c.Title as courseTitle, cl.Title as lessonTitle, " +
            "c.CoverImage as courseImage, cc.Title as chapterTitle " +
            "FROM study_record sr " +
            "LEFT JOIN course c ON sr.CourseID = c.ArticleID " +
            "LEFT JOIN course_lesson cl ON sr.LessonID = cl.LessonID " +
            "LEFT JOIN course_chapter cc ON cl.ChapterID = cc.ChapterID " +
            "WHERE sr.UserID = #{userId} " +
            "ORDER BY sr.StudyDate DESC " +
            "LIMIT #{limit}")
    List<StudyRecord> findRecentRecords(Integer userId, Integer limit);

    /**
     * 获取用户学习统计
     */
    @Select("SELECT COUNT(DISTINCT CourseID) as totalCourses, " +
            "COUNT(*) as totalRecords, " +
            "COALESCE(SUM(StudyTime), 0) as totalStudyTime, " +
            "COUNT(CASE WHEN Completed = 1 THEN 1 END) as completedLessons " +
            "FROM study_record WHERE UserID = #{userId}")
    @Results({
            @Result(property = "totalCourses", column = "totalCourses"),
            @Result(property = "totalRecords", column = "totalRecords"),
            @Result(property = "totalStudyTime", column = "totalStudyTime"),
            @Result(property = "completedLessons", column = "completedLessons")
    })
    Map<String, Object> getUserStatistics(Integer userId);

    /**
     * 获取用户连续学习天数
     */
    @Select("SELECT COUNT(DISTINCT DATE(StudyDate)) as continuousDays " +
            "FROM study_record " +
            "WHERE UserID = #{userId} " +
            "AND StudyDate >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)")
    int getContinuousDays(Integer userId);
}
