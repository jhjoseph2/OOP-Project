package model;

import model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskList {
    private List<Task> tasks;
    private int userID;

    public TaskList(int userID) {
        this.userID = userID;
        this.tasks = new ArrayList<>();
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
    }

    public List<Task> getTasksByCategory(String categoryName) {
        return tasks.stream()
                .filter(task -> task.getCategory().getName().equals(categoryName))
                .collect(Collectors.toList());
    }

    public List<Task> getTasksByPriority(String priority) {
        return tasks.stream()
                .filter(task -> task.getPriority().equals(priority))
                .collect(Collectors.toList());
    }

    public Task getTaskById(String taskId) {
        for (Task task : tasks) {
            if (task.getTaskID() == Integer.parseInt(taskId)) {
                return task;
            }
        }
        return null;
    }

    public List<Task> getTasks() {
        return tasks;
    }
}
