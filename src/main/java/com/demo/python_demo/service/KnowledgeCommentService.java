package com.demo.python_demo.service;

import com.demo.python_demo.entity.KnowledgeComment;

import java.util.List;

public interface KnowledgeCommentService {
    List<KnowledgeComment> listByKnowledgeId(Integer knowledgeId, int page, int pageSize);
    void addComment(KnowledgeComment comment);
    void changeLikes(Integer commentId, long delta);
}


