package com.demo.python_demo.repository;

import com.demo.python_demo.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserRepository {
    
    @Select("SELECT user_id AS userId, phone, account, password, nickname, avatar, group_type AS groupType, intest_types AS intestTypes, create_time AS createTime, update_time AS updateTime, email FROM [user] WHERE account = #{account}")
    User findByAccount(String account);

    @Select("SELECT user_id AS userId, phone, account, password, nickname, avatar, group_type AS groupType, intest_types AS intestTypes, create_time AS createTime, update_time AS updateTime, email FROM [user] WHERE phone = #{phone}")
    User findByPhone(String phone);

    @Insert("INSERT INTO [user] (phone, account, password, nickname, avatar, group_type, intest_types, email) VALUES (#{phone}, #{account}, #{password}, #{nickname}, #{avatar}, #{groupType}, #{intestTypes}, #{email})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int insert(User user);

    @Select("SELECT user_id AS userId, phone, account, password, nickname, avatar, group_type AS groupType, intest_types AS intestTypes, create_time AS createTime, update_time AS updateTime, email FROM [user] WHERE user_id = #{userId}")
    User findById(Integer userId);

    @Update("UPDATE [user] SET phone=#{phone}, account=#{account}, password=#{password}, nickname=#{nickname}, avatar=#{avatar}, group_type=#{groupType}, intest_types=#{intestTypes}, email=#{email} WHERE user_id=#{userId}")
    int update(User user);

    @Update("UPDATE [user] SET group_type=#{groupType} WHERE user_id=#{userId}")
    int updateGroupType(@Param("userId") Integer userId, @Param("groupType") Integer groupType);

    @Update("UPDATE [user] SET intest_types=#{intestTypes} WHERE user_id=#{userId}")
    int updateIntestTypes(@Param("userId") Integer userId, @Param("intestTypes") String intestTypes);
} 