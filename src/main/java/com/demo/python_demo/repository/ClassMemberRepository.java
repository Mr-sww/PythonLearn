package com.demo.python_demo.repository;

import org.apache.ibatis.annotations.*;
import java.util.List;
import java.util.Map;

@Mapper
public interface ClassMemberRepository {

    @Select("SELECT c.ClassID as classId, c.Name as name, c.Description as description, c.TeacherID as teacherId, cm.JoinedAt as joinedAt " +
            "FROM class_member cm JOIN class c ON cm.ClassID = c.ClassID WHERE cm.UserID = #{userId} ORDER BY cm.JoinedAt DESC")
    List<Map<String, Object>> findJoinedClasses(Integer userId);
}


