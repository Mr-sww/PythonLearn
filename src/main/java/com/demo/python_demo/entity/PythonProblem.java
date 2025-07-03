package com.demo.python_demo.entity;

public class PythonProblem {
    private String Id;
    private String Title;
    private String Description;
    private String InputFormat;
    private String OutputFormat;
    private String Note;
    private String Samples;
    private String Background;
    private java.util.Date CreateTime;
    private Integer dif;

    public String getId() { return Id; }
    public void setId(String Id) { this.Id = Id; }
    public String getTitle() { return Title; }
    public void setTitle(String Title) { this.Title = Title; }
    public String getDescription() { return Description; }
    public void setDescription(String Description) { this.Description = Description; }
    public String getInputFormat() { return InputFormat; }
    public void setInputFormat(String InputFormat) { this.InputFormat = InputFormat; }
    public String getOutputFormat() { return OutputFormat; }
    public void setOutputFormat(String OutputFormat) { this.OutputFormat = OutputFormat; }
    public String getNote() { return Note; }
    public void setNote(String Note) { this.Note = Note; }
    public String getSamples() { return Samples; }
    public void setSamples(String Samples) { this.Samples = Samples; }
    public String getBackground() { return Background; }
    public void setBackground(String Background) { this.Background = Background; }
    public java.util.Date getCreateTime() { return CreateTime; }
    public void setCreateTime(java.util.Date CreateTime) { this.CreateTime = CreateTime; }
    public Integer getDif() { return dif; }
    public void setDif(Integer dif) { this.dif = dif; }
} 