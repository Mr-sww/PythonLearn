package com.demo.python_demo.repository;

import com.demo.python_demo.entity.UserProblemRecord;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface UserProblemRecordRepository {
    @Insert("INSERT INTO user_problem_record (user_id, problem_id, submit_time, code, result, pass_rate, used_time, used_memory, language, execution_time, memory_usage, score) " +
            "VALUES (#{userId}, #{problemId}, NOW(), #{code}, #{result}, #{passRate}, #{usedTime}, #{usedMemory}, #{language}, #{executionTime}, #{memoryUsage}, #{score})")
    int insert(UserProblemRecord record);

    @Select("SELECT COUNT(*) FROM user_problem_record WHERE user_id = #{userId}")
    int countTotalSubmissions(Integer userId);
    
    @Select("SELECT COUNT(*) FROM user_problem_record")
    int countAllSubmissions();

    @Select("SELECT COUNT(DISTINCT problem_id) FROM user_problem_record WHERE user_id = #{userId} AND result = '通过'")
    int countPassedProblems(Integer userId);

    @Select("SELECT COALESCE(SUM(CASE WHEN result = '通过' THEN 1 ELSE 0 END),0) * 1.0 / COUNT(*) FROM user_problem_record WHERE user_id = #{userId}")
    Double getAccuracy(Integer userId);

    @Select("SELECT * FROM user_problem_record WHERE user_id = #{userId} ORDER BY submit_time DESC LIMIT 1")
    UserProblemRecord getLastSubmission(Integer userId);

    // 总做题数（去重）
    @Select("SELECT COUNT(DISTINCT problem_id) FROM user_problem_record WHERE user_id = #{userId} AND problem_id != 0")
    int countDistinctProblems(Integer userId);

    // 总用时
    @Select("SELECT COALESCE(SUM(used_time),0) FROM user_problem_record WHERE user_id = #{userId} AND problem_id != 0")
    int sumUsedTime(Integer userId);

    // 平均用时
    @Select("SELECT COALESCE(AVG(used_time),0) FROM user_problem_record WHERE user_id = #{userId} AND problem_id != 0")
    double avgUsedTime(Integer userId);

    // 判题结果分布
    @Select("SELECT result, COUNT(*) as count FROM user_problem_record WHERE user_id = #{userId} AND problem_id != 0 GROUP BY result")
    List<java.util.Map<String, Object>> resultDistribution(Integer userId);

    // 每日做题趋势
    @Select("SELECT DATE(submit_time) as date, COUNT(*) as count FROM user_problem_record WHERE user_id = #{userId} AND problem_id != 0 GROUP BY DATE(submit_time) ORDER BY date")
    List<java.util.Map<String, Object>> dailySubmissionTrend(Integer userId);

    // 统计用户连续刷题天数（简化实现）
    @Select("""
        SELECT COALESCE(MAX(continuous_days), 0) as continuous_days
        FROM (
            SELECT COUNT(*) as continuous_days
            FROM (
                SELECT d, DATE_SUB(d, INTERVAL rn DAY) AS grp
                FROM (
                    SELECT d, (@rn := @rn + 1) as rn
                    FROM (
                        SELECT DISTINCT DATE(submit_time) AS d
                        FROM user_problem_record
                        WHERE user_id = #{userId}
                        ORDER BY d DESC
                    ) dates, (SELECT @rn := 0) r
                ) ranked
            ) date_groups
            GROUP BY grp
        ) result
    """)
    int getContinuousDays(Integer userId);

    // 获取用户做题记录（分页，包含题目信息）
    @Select("""
        SELECT r.*, p.Title as problem_title, p.dif as difficulty
        FROM user_problem_record r
        LEFT JOIN pythonproblems p ON CONCAT('P', r.problem_id) = p.Id
        WHERE r.user_id = #{userId} AND r.problem_id != 0
        ORDER BY r.submit_time DESC
        LIMIT #{offset}, #{size}
    """)
    List<java.util.Map<String, Object>> getUserProblemRecordsWithPagination(Integer userId, int offset, int size);

    // 获取用户做题记录总数
    @Select("SELECT COUNT(*) FROM user_problem_record WHERE user_id = #{userId} AND problem_id != 0")
    int getUserProblemRecordsCount(Integer userId);

    // 根据结果筛选用户做题记录（分页，包含题目信息）
    @Select("""
        SELECT r.*, p.Title as problem_title, p.dif as difficulty
        FROM user_problem_record r
        LEFT JOIN pythonproblems p ON CONCAT('P', r.problem_id) = p.Id
        WHERE r.user_id = #{userId} AND r.problem_id != 0 AND r.result = #{result}
        ORDER BY r.submit_time DESC
        LIMIT #{offset}, #{size}
    """)
    List<java.util.Map<String, Object>> getUserProblemRecordsByResultWithPagination(Integer userId, String result, int offset, int size);

    // 根据结果筛选用户做题记录总数
    @Select("SELECT COUNT(*) FROM user_problem_record WHERE user_id = #{userId} AND problem_id != 0 AND result = #{result}")
    int getUserProblemRecordsByResultCount(Integer userId, String result);
} 