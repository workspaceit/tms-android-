package com.apper.sarwar.tmsapp.viewmodel;

public class MyTaskList {

    private String taskId;
    private String taskTitle;
    private String taskDate;

    public MyTaskList(String taskId,String taskTitle, String taskDate) {
        this.taskId = taskId;
        this.taskTitle = taskTitle;
        this.taskDate = taskDate;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }
}
