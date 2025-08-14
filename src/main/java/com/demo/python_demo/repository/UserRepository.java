package com.demo.python_demo.repository;

import com.demo.python_demo.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Delete;
import java.util.List;

@Mapper
public interface UserRepository {
    
    @Select("SELECT user_id AS userId, phone, account, password, nickname, avatar, group_type AS groupType, intest_types AS intestTypes, create_time AS createTime, update_time AS updateTime, email, status FROM user WHERE account = #{account}")
    User findByAccount(String account);

    @Select("SELECT user_id AS userId, phone, account, password, nickname, avatar, group_type AS groupType, intest_types AS intestTypes, create_time AS createTime, update_time AS updateTime, email, status FROM user WHERE phone = #{phone}")
    User findByPhone(String phone);

    @Insert("INSERT INTO user (phone, account, password, nickname, avatar, group_type, intest_types, email, status) VALUES (#{phone}, #{account}, #{password}, #{nickname}, #{avatar}, #{groupType}, #{intestTypes}, #{email}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int insert(User user);

    @Select("SELECT user_id AS userId, phone, account, password, nickname, avatar, group_type AS groupType, intest_types AS intestTypes, create_time AS createTime, update_time AS updateTime, email, status FROM user WHERE user_id = #{userId}")
    User findById(Integer userId);

    @Update("UPDATE user SET phone=#{phone}, account=#{account}, password=#{password}, nickname=#{nickname}, avatar=#{avatar}, group_type=#{groupType}, intest_types=#{intestTypes}, email=#{email}, status=#{status} WHERE user_id=#{userId}")
    int update(User user);

    @Update("UPDATE user SET group_type=#{groupType} WHERE user_id=#{userId}")
    int updateGroupType(@Param("userId") Integer userId, @Param("groupType") Integer groupType);

    @Update("UPDATE user SET intest_types=#{intestTypes} WHERE user_id=#{userId}")
    int updateIntestTypes(@Param("userId") Integer userId, @Param("intestTypes") String intestTypes);

    // 管理员功能
    @Select("SELECT user_id AS userId, phone, account, password, nickname, avatar, group_type AS groupType, intest_types AS intestTypes, create_time AS createTime, update_time AS updateTime, email, status FROM user WHERE group_type = #{groupType}")
    List<User> findByGroupType(Integer groupType);

    @Select("SELECT user_id AS userId, phone, account, password, nickname, avatar, group_type AS groupType, intest_types AS intestTypes, create_time AS createTime, update_time AS updateTime, email, status FROM user WHERE group_type = 1 AND FIND_IN_SET(#{majorType}, intest_types)")
    List<User> findStudentsByMajor(Integer majorType);

    @Select("SELECT user_id AS userId, phone, account, password, nickname, avatar, group_type AS groupType, intest_types AS intestTypes, create_time AS createTime, update_time AS updateTime, email, status FROM user")
    List<User> getAllUsers();

    @Select("SELECT COUNT(*) FROM user")
    int countAllUsers();

    @Select("SELECT COUNT(*) FROM user WHERE status = #{status}")
    int countUsersByStatus(String status);

    @Update("UPDATE user SET status = #{status} WHERE user_id = #{userId}")
    int updateUserStatus(@Param("userId") Integer userId, @Param("status") String status);

    @Delete("DELETE FROM user WHERE user_id = #{userId}")
    int deleteById(Integer userId);

    @Select("""
        SELECT group_type as major, COUNT(*) as count 
        FROM user 
        GROUP BY group_type
    """)
    List<Object> getMajorStatistics();

    @Select("""
        SELECT 
            CASE 
                WHEN group_type = 1 THEN '学生'
                WHEN group_type = 2 THEN '教师'
                WHEN group_type = 3 THEN '管理员'
                ELSE '未知'
            END as role,
            COUNT(*) as count
        FROM user 
        GROUP BY group_type
    """)
    List<Object> getRoleStatistics();

    @Select("""
        SELECT user_id AS userId, phone, account, password, nickname, avatar, group_type AS groupType, intest_types AS intestTypes, create_time AS createTime, update_time AS updateTime, email, status 
        FROM user 
        WHERE (#{keyword} IS NULL OR account LIKE CONCAT('%', #{keyword}, '%') OR nickname LIKE CONCAT('%', #{keyword}, '%'))
        AND (#{groupType} IS NULL OR group_type = #{groupType})
        AND (#{status} IS NULL OR status = #{status})
        ORDER BY create_time DESC
    """)
    List<User> searchUsers(@Param("keyword") String keyword, @Param("groupType") Integer groupType, @Param("status") String status);
} 