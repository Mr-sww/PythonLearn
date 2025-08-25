package com.demo.python_demo.repository;

import com.demo.python_demo.entity.KnowledgePoint;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface KnowledgePointRepository {
    @Select("SELECT * FROM runoobpython3install ORDER BY stage ASC")
    List<KnowledgePoint> findAllOrderByStage();

    @Select("SELECT * FROM runoobpython3install WHERE title = #{title}")
    KnowledgePoint findByTitle(String title);

    @Select("SELECT * FROM runoobpython3install WHERE id = #{id}")
    KnowledgePoint findById(Integer id);

    @Insert("INSERT INTO runoobpython3install (title, content, question, url, stage) VALUES (#{title}, #{content}, #{question}, #{url}, #{stage})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    KnowledgePoint insert(KnowledgePoint point);

    @Update("UPDATE runoobpython3install SET title = #{title}, content = #{content}, question = #{question}, url = #{url}, stage = #{stage} WHERE id = #{id}")
    int update(KnowledgePoint point);

    @Delete("DELETE FROM runoobpython3install WHERE id = #{id}")
    int deleteById(Integer id);
}
