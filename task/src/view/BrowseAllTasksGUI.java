package view;

import model.Category;
import model.CategoryList;
import model.Task;
import model.TaskList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

public class BrowseAllTasksGUI extends JFrame {
    private JTable taskTable;
    private JTextField searchTextField;
    private JButton selectTaskButton;
    private JButton backButton;
    private JButton toggleCompleteButton;

    private JButton deleteButton;
    private TaskList taskList;
    private DefaultTableModel tableModel;

    private JTextField deleteCategoryField; // Field to input the category name to be deleted
    private JButton deleteCategoryButton;
    public BrowseAllTasksGUI(TaskList taskList) {
        this.taskList = taskList;
        this.taskList.getTasks().sort((a, b) -> {
            if (a.getDueDate().before(b.getDueDate())) {
                return -1;
            } else if (a.getDueDate().after(b.getDueDate())) {
                return 1;
            } else {
                return 0;
            }
        });
        setTitle("Browse All Tasks");
        initWidgets();
        pack();
        setLocationRelativeTo(null);
    }

    private void initWidgets() {
//        searchTextField = new JTextField(20);
        taskTable = new JTable();
        selectTaskButton = new JButton("Edit Task");
        backButton = new JButton("Back");
        toggleCompleteButton = new JButton("Toggle Complete");
        deleteButton = new JButton("Delete");
        deleteCategoryField = new JTextField(20);
        deleteCategoryButton = new JButton("Delete Category");
        // Table setup
        String[] columnNames = {"ID", "Title", "Description", "Due Date", "Priority", "Category", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        taskTable.setModel(tableModel);
        showAllTasks();

        // Search Panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        buttonPanel.add(selectTaskButton);
        buttonPanel.add(toggleCompleteButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel deleteCategoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        deleteCategoryPanel.add(new JLabel("Delete Category:"));
        deleteCategoryPanel.add(deleteCategoryField);
        deleteCategoryPanel.add(deleteCategoryButton);
        // Set up layout and add components
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(new JScrollPane(taskTable));
        add(deleteCategoryPanel);
        add(buttonPanel);

        // Action listener for the select button
        selectTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editTask();
            }
        });

        // Action listener for the toggle complete button
        toggleCompleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleTaskStatus();
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteTask();
            }
        });
        deleteCategoryButton.addActionListener(this::onDeleteCategory);
        // Action listener for the back button
        backButton.addActionListener(e -> dispose());
    }
    private void onDeleteCategory(ActionEvent e) {
        String categoryName = deleteCategoryField.getText();
        if (categoryName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a category name.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        CategoryList categoryList = CategoryList.getInstance();
        boolean deleted = categoryList.deleteCategory(categoryName);
        if (deleted) {
            JOptionPane.showMessageDialog(this, "Category deleted successfully.", "Category Deleted", JOptionPane.INFORMATION_MESSAGE);

        } else {
            JOptionPane.showMessageDialog(this, "Category deleted unsuccessfully", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    // helper methods
    private void showAllTasks() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (Task task : taskList.getTasks()) {
            Object[] row = new Object[]{
                    task.getTaskID(),
                    task.getTitle(),
                    task.getDescription(),
                    dateFormat.format(task.getDueDate()),
                    task.getPriority(),
                    task.getCategory().getName(),
                    task.getStatus()
            };
            tableModel.addRow(row);
        }
    }

    private void updateTableRow(int row) {
        Task task = taskList.getTaskById(taskTable.getValueAt(row, 0).toString());
        if (task != null) {
            tableModel.setValueAt(task.getTaskID(), row, 0);
            tableModel.setValueAt(task.getTitle(), row, 1);
            tableModel.setValueAt(task.getDescription(), row, 2);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            tableModel.setValueAt(dateFormat.format(task.getDueDate()), row, 3);
            tableModel.setValueAt(task.getPriority(), row, 4);
            tableModel.setValueAt(task.getCategory().getName(), row, 5);
            tableModel.setValueAt(task.getStatus(), row, 6);
        }
    }

    private void editTask() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow >= 0) {
            String taskId = taskTable.getValueAt(selectedRow, 0).toString();
            Task selectedTask = taskList.getTaskById(taskId);
            if (selectedTask != null) {
                TaskDetailGUI detailGUI = new TaskDetailGUI(selectedTask);
                detailGUI.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                        SwingUtilities.invokeLater(() -> updateTableRow(selectedRow));
                    }
                });
                detailGUI.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(BrowseAllTasksGUI.this, "Task not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(BrowseAllTasksGUI.this, "Please select a task first.", "No Task Selected", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void toggleTaskStatus() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow >= 0) {
            String taskId = taskTable.getValueAt(selectedRow, 0).toString();
            Task selectedTask = taskList.getTaskById(taskId);
            if (selectedTask != null) {
                selectedTask.toggleStatus();
                tableModel.setValueAt(selectedTask.getStatus(), selectedRow, 6);
            } else {
                JOptionPane.showMessageDialog(BrowseAllTasksGUI.this, "Task not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(BrowseAllTasksGUI.this, "Please select a task first.", "No Task Selected", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteTask() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow >= 0) {
            int confirm = JOptionPane.showConfirmDialog(
                    BrowseAllTasksGUI.this,
                    "Are you sure you want to delete this task?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                String taskId = taskTable.getValueAt(selectedRow, 0).toString();
                Task deleteTask = taskList.getTaskById(taskId);
                taskList.removeTask(deleteTask);
                tableModel.removeRow(selectedRow);
            }
        } else {
            JOptionPane.showMessageDialog(BrowseAllTasksGUI.this, "Please select a task first.", "No Task Selected", JOptionPane.WARNING_MESSAGE);
        }
    }
}
