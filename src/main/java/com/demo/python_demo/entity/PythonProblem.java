package com.demo.python_demo.entity;

public class PythonProblem {
    private String id;
    private String title;
    private String description;
    private String inputFormat;
    private String outputFormat;
    private String note;
    private String samples;
    private String background;
    private String createTime;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getInputFormat() { return inputFormat; }
    public void setInputFormat(String inputFormat) { this.inputFormat = inputFormat; }
    public String getOutputFormat() { return outputFormat; }
    public void setOutputFormat(String outputFormat) { this.outputFormat = outputFormat; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public String getSamples() { return samples; }
    public void setSamples(String samples) { this.samples = samples; }
    public String getBackground() { return background; }
    public void setBackground(String background) { this.background = background; }
    public String getCreateTime() { return createTime; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }

    // 可选：静态工厂方法
    public static PythonProblem ofId(String id) {
        PythonProblem p = new PythonProblem();
        p.setId(id);
        return p;
    }
} 