package com.demo.python_demo.entity;
import java.util.Date;

public class UserProblemRecord {
    private Integer id;
    private Integer userId;
    private Integer problemId;
    private Date submitTime;
    private String code;
    private String result;
    private Double passRate;
    private Integer usedTime;
    private Integer usedMemory;
    private String language;
    // getter/setter
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public Integer getProblemId() { return problemId; }
    public void setProblemId(Integer problemId) { this.problemId = problemId; }
    public Date getSubmitTime() { return submitTime; }
    public void setSubmitTime(Date submitTime) { this.submitTime = submitTime; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
    public Double getPassRate() { return passRate; }
    public void setPassRate(Double passRate) { this.passRate = passRate; }
    public Integer getUsedTime() { return usedTime; }
    public void setUsedTime(Integer usedTime) { this.usedTime = usedTime; }
    public Integer getUsedMemory() { return usedMemory; }
    public void setUsedMemory(Integer usedMemory) { this.usedMemory = usedMemory; }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
} 