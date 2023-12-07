package model;

import model.TaskList;

public class User {
    private int userID;
    private String username;
    private TaskList taskList;

    public User(int userID, String username) {
        this.userID = userID;
        this.username = username;
        this.taskList = new TaskList(userID);
    }

    public TaskList getTaskList() {
        return taskList;
    }
}
