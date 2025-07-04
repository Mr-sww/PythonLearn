package com.demo.python_demo.service.impl;

import com.demo.python_demo.entity.KnowledgePoint;
import com.demo.python_demo.repository.KnowledgePointRepository;
import com.demo.python_demo.service.KnowledgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KnowledgeServiceImpl implements KnowledgeService {
    @Autowired
    private KnowledgePointRepository knowledgePointRepository;

    @Override
    public KnowledgePoint getById(Integer id) {
        return knowledgePointRepository.findById(id);
    }
} 