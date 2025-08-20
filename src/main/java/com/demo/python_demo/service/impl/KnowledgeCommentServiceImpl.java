package com.demo.python_demo.service.impl;

import com.demo.python_demo.entity.KnowledgeComment;
import com.demo.python_demo.repository.KnowledgeCommentRepository;
import com.demo.python_demo.service.KnowledgeCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KnowledgeCommentServiceImpl implements KnowledgeCommentService {

    @Autowired
    private KnowledgeCommentRepository repository;

    @Override
    public List<KnowledgeComment> listByKnowledgeId(Integer knowledgeId, int page, int pageSize) {
        int offset = Math.max(0, (page - 1) * pageSize);
        return repository.findByKnowledgeId(knowledgeId, offset, pageSize);
    }

    @Override
    public void addComment(KnowledgeComment comment) {
        repository.insert(comment);
    }

    @Override
    public void changeLikes(Integer commentId, long delta) {
        repository.changeLikes(commentId, delta);
    }
}


