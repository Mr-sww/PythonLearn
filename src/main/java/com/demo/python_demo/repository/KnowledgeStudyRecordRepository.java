package com.demo.python_demo.repository;

import com.demo.python_demo.entity.KnowledgeStudyRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 知识点学习记录Repository接口
 */
@Mapper
public interface KnowledgeStudyRecordRepository {

    /**
     * 插入新的学习记录
     */
    @Insert("INSERT INTO knowledge_study_record (user_id, knowledge_id, knowledge_title, status, progress, study_time, start_time) " +
            "VALUES (#{userId}, #{knowledgeId}, #{knowledgeTitle}, #{status}, #{progress}, #{studyTime}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(KnowledgeStudyRecord record);

    /**
     * 更新学习记录
     */
    @Update("UPDATE knowledge_study_record SET " +
            "study_time = #{studyTime}, " +
            "progress = #{progress}, " +
            "status = #{status}, " +
            "end_time = #{endTime}, " +
            "last_study_time = NOW(), " +
            "updated_at = NOW() " +
            "WHERE id = #{id}")
    int update(KnowledgeStudyRecord record);

    /**
     * 根据用户ID和知识点ID查找学习记录
     * 返回最新的一条记录
     */
    @Select("SELECT * FROM knowledge_study_record WHERE user_id = #{userId} AND knowledge_id = #{knowledgeId} ORDER BY id DESC LIMIT 1")
    KnowledgeStudyRecord findByUserIdAndKnowledgeId(@Param("userId") Integer userId, @Param("knowledgeId") Integer knowledgeId);

    /**
     * 根据用户ID获取学习记录列表
     */
    @Select("SELECT * FROM knowledge_study_record WHERE user_id = #{userId} ORDER BY created_at DESC LIMIT #{limit}")
    List<KnowledgeStudyRecord> findByUserId(@Param("userId") Integer userId, @Param("limit") Integer limit);

    /**
     * 根据用户ID获取最近的学习记录
     * 每个知识点只返回最新的一条记录
     */
    @Select("SELECT * FROM knowledge_study_record WHERE id IN (" +
            "SELECT MAX(id) FROM knowledge_study_record " +
            "WHERE user_id = #{userId} " +
            "GROUP BY knowledge_id" +
            ") ORDER BY last_study_time DESC LIMIT #{limit}")
    List<KnowledgeStudyRecord> findRecentByUserId(@Param("userId") Integer userId, @Param("limit") Integer limit);

    /**
     * 统计用户的学习记录数量
     */
    @Select("SELECT COUNT(*) FROM knowledge_study_record WHERE user_id = #{userId}")
    int countByUserId(@Param("userId") Integer userId);

    /**
     * 统计用户完成的学习记录数量
     */
    @Select("SELECT COUNT(*) FROM knowledge_study_record WHERE user_id = #{userId} AND status = 'completed'")
    int countCompletedByUserId(@Param("userId") Integer userId);

    /**
     * 统计用户的总学习时长（秒）
     */
    @Select("SELECT COALESCE(SUM(study_time), 0) FROM knowledge_study_record WHERE user_id = #{userId}")
    Integer sumStudyTimeByUserId(@Param("userId") Integer userId);


}
