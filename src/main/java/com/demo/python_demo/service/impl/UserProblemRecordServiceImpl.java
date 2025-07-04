package com.demo.python_demo.service.impl;

import com.demo.python_demo.entity.UserProblemRecord;
import com.demo.python_demo.repository.UserProblemRecordRepository;
import com.demo.python_demo.service.UserProblemRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class UserProblemRecordServiceImpl implements UserProblemRecordService {
    @Autowired
    private UserProblemRecordRepository repository;

    @Override
    public void saveRecord(UserProblemRecord record) {
        repository.insert(record);
    }

    @Override
    public int getTotalSubmissions(Integer userId) {
        int count = repository.countTotalSubmissions(userId);
        if (count == 0) {
            // 插入一条默认记录
            UserProblemRecord record = new UserProblemRecord();
            record.setUserId(userId);
            record.setProblemId(0);
            record.setCode("");
            record.setResult("");
            record.setPassRate(0.0);
            record.setUsedTime(0);
            record.setUsedMemory(0);
            record.setLanguage("");
            repository.insert(record);
            return 0;
        }
        return count;
    }

    @Override
    public int getPassedProblems(Integer userId) {
        int count = repository.countPassedProblems(userId);
        if (repository.countTotalSubmissions(userId) == 0) {
            // 插入一条默认记录
            UserProblemRecord record = new UserProblemRecord();
            record.setUserId(userId);
            record.setProblemId(0);
            record.setCode("");
            record.setResult("");
            record.setPassRate(0.0);
            record.setUsedTime(0);
            record.setUsedMemory(0);
            record.setLanguage("");
            repository.insert(record);
            return 0;
        }
        return count;
    }

    @Override
    public double getAccuracy(Integer userId) {
        Double acc = repository.getAccuracy(userId);
        if (repository.countTotalSubmissions(userId) == 0) {
            // 插入一条默认记录
            UserProblemRecord record = new UserProblemRecord();
            record.setUserId(userId);
            record.setProblemId(0);
            record.setCode("");
            record.setResult("");
            record.setPassRate(0.0);
            record.setUsedTime(0);
            record.setUsedMemory(0);
            record.setLanguage("");
            repository.insert(record);
            return 0.0;
        }
        return acc != null ? acc : 0.0;
    }

    @Override
    public Map<String, Object> getUserStatistics(Integer userId) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalSubmissions", repository.countTotalSubmissions(userId));
        stats.put("passedProblems", repository.countPassedProblems(userId));
        stats.put("accuracy", Optional.ofNullable(repository.getAccuracy(userId)).orElse(0.0));
        stats.put("distinctProblems", repository.countDistinctProblems(userId));
        stats.put("sumUsedTime", repository.sumUsedTime(userId));
        stats.put("avgUsedTime", repository.avgUsedTime(userId));
        stats.put("resultDistribution", repository.resultDistribution(userId));
        stats.put("dailyTrend", repository.dailySubmissionTrend(userId));
        return stats;
    }

    @Override
    public int getContinuousDays(Integer userId) {
        return repository.getContinuousDays(userId);
    }
} 