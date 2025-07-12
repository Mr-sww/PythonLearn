package com.demo.python_demo.repository;

import com.demo.python_demo.entity.PythonVideo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface PythonVideoRepository {
    @Select("SELECT ID, Title, URL, ImageURL, PlayCount, PublishDate, Tags FROM PythonVideos")
    List<PythonVideo> findAll();

    @Select("SELECT ID, Title, URL, ImageURL, PlayCount, PublishDate, Tags FROM PythonVideos WHERE ID = #{id}")
    PythonVideo findById(Integer id);

    @Select("SELECT TOP 10 ID, Title, URL, ImageURL, PlayCount, PublishDate, Tags FROM PythonVideos ORDER BY ImportDate DESC")
    List<PythonVideo> findRecommend();

    @Select({
        "<script>",
        "SELECT ID, Title, URL, ImageURL, PlayCount, PublishDate, Tags FROM PythonVideos",
        "<where>",
        "<if test='categories != null and categories.size > 0'>",
        "<foreach collection='categories' item='cat' open='(' separator='OR' close=')'>",
        "Tags LIKE CONCAT('%', #{cat}, '%')",
        "</foreach>",
        "</if>",
        "<if test='excluded != null and excluded.size > 0'>",
        "<foreach collection='excluded' item='ex'>",
        "AND Tags NOT LIKE CONCAT('%', #{ex}, '%')",
        "</foreach>",
        "</if>",
        "</where>",
        "</script>"
    })
    List<PythonVideo> findByCategories(@Param("categories") List<String> categories, @Param("excluded") List<String> excluded);

    @Select("SELECT TOP 3 ID, Title, URL, ImageURL, PlayCount, PublishDate, Tags FROM PythonVideos WHERE ImageURL IS NOT NULL AND ImageURL <> '' ORDER BY NEWID()")
    List<PythonVideo> findRandomWithImage();
} 