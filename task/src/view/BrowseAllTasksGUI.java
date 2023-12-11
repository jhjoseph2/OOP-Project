package view;

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



    public BrowseAllTasksGUI(TaskList taskList) {
        this.taskList = taskList;
        Collections.sort(this.taskList.getTasks(), (a, b) -> {
            if(a.getDueDate().before(b.getDueDate())){
                return -1;
            }else if(a.getDueDate().after(b.getDueDate())){
                return 1;
            }else{
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
        // Table setup
        String[] columnNames = {"ID", "Title", "Description", "Due Date", "Priority", "Category", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        taskTable.setModel(tableModel);
        populateTableWithTasks();

        // Search Panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 0)); // Changed to GridLayout
        buttonPanel.add(selectTaskButton);
        buttonPanel.add(toggleCompleteButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Set up layout and add components
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(new JScrollPane(taskTable));
        add(buttonPanel);

        // Action listener for the select button
        selectTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = taskTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String taskId = taskTable.getValueAt(selectedRow, 0).toString(); // Assuming the ID is in the first column
                    Task selectedTask = taskList.getTaskById(taskId); // Assuming getTaskById method exists
                    if (selectedTask != null) {
                        new TaskDetailGUI(selectedTask).setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(BrowseAllTasksGUI.this, "Task not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(BrowseAllTasksGUI.this, "Please select a task first.", "No Task Selected", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // Action listener for the toggle complete button
        toggleCompleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = taskTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String taskId = taskTable.getValueAt(selectedRow, 0).toString(); // Assuming the ID is in the first column
                    Task selectedTask = taskList.getTaskById(taskId); // Assuming getTaskById method exists
                    if (selectedTask != null) {
                        selectedTask.toggleStatus(); // Call the toggleStatus method on the task
                        // Optionally, update the table row to reflect the new status
                        tableModel.setValueAt(selectedTask.getStatus(), selectedRow, 6); // Assuming you know the column index of the status
                    } else {
                        JOptionPane.showMessageDialog(BrowseAllTasksGUI.this, "Task not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(BrowseAllTasksGUI.this, "Please select a task first.", "No Task Selected", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = taskTable.getSelectedRow();
                if (selectedRow >= 0) {
                    // Confirm before deleting
                    int confirm = JOptionPane.showConfirmDialog(
                            BrowseAllTasksGUI.this,
                            "Are you sure you want to delete this task?",
                            "Confirm Deletion",
                            JOptionPane.YES_NO_OPTION
                    );

                    if (confirm == JOptionPane.YES_OPTION) {
                        String taskId = taskTable.getValueAt(selectedRow, 0).toString(); // Assuming the ID is in the first column
                        Task deleteTask = taskList.getTaskById(taskId);
                        taskList.removeTask(deleteTask);
                        tableModel.removeRow(selectedRow); // Remove the row from the table model
                    }
                } else {
                    JOptionPane.showMessageDialog(BrowseAllTasksGUI.this, "Please select a task first.", "No Task Selected", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // Action listener for the back button
        backButton.addActionListener(e -> dispose());
    }

    private void populateTableWithTasks() {
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

}
