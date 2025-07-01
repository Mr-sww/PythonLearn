package com.demo.python_demo.repository;

import com.demo.python_demo.entity.PythonVideo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface PythonVideoRepository {
    @Select("SELECT ID, Title, URL, ImageURL, PlayCount, PublishDate FROM PythonVideos")
    List<PythonVideo> findAll();
} 