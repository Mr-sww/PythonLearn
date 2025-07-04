package com.demo.python_demo.repository;

import com.demo.python_demo.entity.KnowledgePoint;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface KnowledgePointRepository {
    @Select("SELECT * FROM RunoobPython3Install ORDER BY stage ASC")
    List<KnowledgePoint> findAllOrderByStage();

    @Select("SELECT * FROM RunoobPython3Install WHERE title = #{title}")
    KnowledgePoint findByTitle(String title);

    @Select("SELECT * FROM RunoobPython3Install WHERE id = #{id}")
    KnowledgePoint findById(Integer id);
}
