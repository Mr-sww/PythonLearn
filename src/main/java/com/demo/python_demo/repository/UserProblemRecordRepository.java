package com.demo.python_demo.repository;

import com.demo.python_demo.entity.UserProblemRecord;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface UserProblemRecordRepository {
    @Insert("INSERT INTO user_problem_record (user_id, problem_id, submit_time, code, result, pass_rate, used_time, used_memory, language) " +
            "VALUES (#{userId}, #{problemId}, GETDATE(), #{code}, #{result}, #{passRate}, #{usedTime}, #{usedMemory}, #{language})")
    int insert(UserProblemRecord record);

    @Select("SELECT COUNT(*) FROM user_problem_record WHERE user_id = #{userId}")
    int countTotalSubmissions(Integer userId);

    @Select("SELECT COUNT(DISTINCT problem_id) FROM user_problem_record WHERE user_id = #{userId} AND result = N'通过'")
    int countPassedProblems(Integer userId);

    @Select("SELECT ISNULL(SUM(CASE WHEN result = N'通过' THEN 1 ELSE 0 END),0) * 1.0 / COUNT(*) FROM user_problem_record WHERE user_id = #{userId}")
    Double getAccuracy(Integer userId);

    @Select("SELECT TOP 1 * FROM user_problem_record WHERE user_id = #{userId} ORDER BY submit_time DESC")
    UserProblemRecord getLastSubmission(Integer userId);

    // 总做题数（去重）
    @Select("SELECT COUNT(DISTINCT problem_id) FROM user_problem_record WHERE user_id = #{userId} AND problem_id != 0")
    int countDistinctProblems(Integer userId);

    // 总用时
    @Select("SELECT ISNULL(SUM(used_time),0) FROM user_problem_record WHERE user_id = #{userId} AND problem_id != 0")
    int sumUsedTime(Integer userId);

    // 平均用时
    @Select("SELECT ISNULL(AVG(used_time),0) FROM user_problem_record WHERE user_id = #{userId} AND problem_id != 0")
    double avgUsedTime(Integer userId);

    // 判题结果分布
    @Select("SELECT result, COUNT(*) as count FROM user_problem_record WHERE user_id = #{userId} AND problem_id != 0 GROUP BY result")
    List<java.util.Map<String, Object>> resultDistribution(Integer userId);

    // 每日做题趋势
    @Select("SELECT CONVERT(varchar(10), submit_time, 120) as date, COUNT(*) as count FROM user_problem_record WHERE user_id = #{userId} AND problem_id != 0 GROUP BY CONVERT(varchar(10), submit_time, 120) ORDER BY date")
    List<java.util.Map<String, Object>> dailySubmissionTrend(Integer userId);
} 