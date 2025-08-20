package com.demo.python_demo.repository;

import com.demo.python_demo.entity.KnowledgeComment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface KnowledgeCommentRepository {

    @Select("SELECT kc.id, kc.knowledge_id AS knowledgeId, kc.user_id AS userId, " +
            "COALESCE(kc.nickname, u.nickname) AS nickname, " +
            "u.avatar AS avatar, " +
            "0 AS parentId, 0 AS rootId, 0 AS replyToUserId, " +
            "kc.content, IFNULL(kc.likes, 0) AS likes, 0 AS replyCount, kc.created_at AS createdAt, kc.updated_at AS updatedAt " +
            "FROM knowledge_comment kc LEFT JOIN user u ON kc.user_id = u.user_id " +
            "WHERE kc.knowledge_id = #{knowledgeId} ORDER BY kc.id DESC LIMIT #{limit} OFFSET #{offset}")
    List<KnowledgeComment> findByKnowledgeId(@Param("knowledgeId") Integer knowledgeId,
                                              @Param("offset") int offset,
                                              @Param("limit") int limit);

    @Insert("INSERT INTO knowledge_comment (knowledge_id, user_id, nickname, content, created_at, updated_at) " +
            "VALUES (#{knowledgeId}, #{userId}, #{nickname}, #{content}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(KnowledgeComment comment);

    @Update("UPDATE knowledge_comment SET likes = likes + #{delta} WHERE id = #{id}")
    int changeLikes(@Param("id") Integer id, @Param("delta") long delta);
}


