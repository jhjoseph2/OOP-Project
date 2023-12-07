package model;

import model.Category;
import model.CategoryList;

import java.util.Date;

public class Task {
    public enum Status {
        INCOMPLETE, COMPLETE
    }
    public static int TASK_ID = 1;
    private int taskID;
    private String title;
    private String description;
    private Date dueDate;
    private String priority;
    private Status status;
    private Category category;

    public Task(String title, String description, Date dueDate, String priority, String cName) {
        this.taskID = TASK_ID++;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.status = Status.INCOMPLETE;
        CategoryList cl = CategoryList.getInstance();
        if (cl.getCategoryByName(cName).isPresent()) {
            this.category = cl.getCategoryByName(cName).get();
        } else {
            this.category = new Category(cName);
        }
    }

//    public static int getTaskId() {
//        return TASK_ID;
//    }

    public int getTaskID() {
        return taskID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Status getStatus() {
        return status;
    }

    public void toggleStatus() {
        if (this.status == Status.INCOMPLETE) {
            this.status = Status.COMPLETE;
        } else {
            this.status = Status.INCOMPLETE;
        }
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
