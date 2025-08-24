package com.demo.python_demo.repository;

import com.demo.python_demo.entity.VideoWatchRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 视频观看记录Repository接口
 */
@Mapper
public interface VideoWatchRecordRepository {

    /**
     * 插入新的观看记录
     */
    @Insert("INSERT INTO video_watch_record (user_id, video_id, video_title, video_url, total_duration, status, progress, watch_time, start_time) " +
            "VALUES (#{userId}, #{videoId}, #{videoTitle}, #{videoUrl}, #{totalDuration}, #{status}, #{progress}, #{watchTime}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(VideoWatchRecord record);

    /**
     * 更新观看记录
     */
    @Update("UPDATE video_watch_record SET " +
            "watch_time = #{watchTime}, " +
            "progress = #{progress}, " +
            "status = #{status}, " +
            "end_time = #{endTime}, " +
            "last_watch_time = NOW(), " +
            "updated_at = NOW() " +
            "WHERE id = #{id}")
    int update(VideoWatchRecord record);

    /**
     * 根据用户ID和视频ID查找观看记录
     */
    @Select("SELECT * FROM video_watch_record WHERE user_id = #{userId} AND video_id = #{videoId} ORDER BY created_at DESC LIMIT 1")
    VideoWatchRecord findByUserIdAndVideoId(@Param("userId") Integer userId, @Param("videoId") Integer videoId);

    /**
     * 根据用户ID获取观看记录列表
     */
    @Select("SELECT * FROM video_watch_record WHERE user_id = #{userId} ORDER BY created_at DESC LIMIT #{limit}")
    List<VideoWatchRecord> findByUserId(@Param("userId") Integer userId, @Param("limit") Integer limit);

    /**
     * 根据用户ID获取最近的观看记录
     */
    @Select("SELECT * FROM video_watch_record WHERE user_id = #{userId} ORDER BY last_watch_time DESC LIMIT #{limit}")
    List<VideoWatchRecord> findRecentByUserId(@Param("userId") Integer userId, @Param("limit") Integer limit);

    /**
     * 统计用户的观看记录数量
     */
    @Select("SELECT COUNT(*) FROM video_watch_record WHERE user_id = #{userId}")
    int countByUserId(@Param("userId") Integer userId);

    /**
     * 统计用户完成的观看记录数量
     */
    @Select("SELECT COUNT(*) FROM video_watch_record WHERE user_id = #{userId} AND status = 'completed'")
    int countCompletedByUserId(@Param("userId") Integer userId);
}


