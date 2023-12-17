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
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class BrowseAllTasksGUI extends JFrame {
    private JTable taskTable;
    private JButton selectTaskButton;
    private JButton backButton;
    private JButton toggleCompleteButton;
    private JButton deleteButton;
    private TaskList taskList;
    private DefaultTableModel tableModel;
    private JComboBox<String> categoryFilterComboBox;
    private JComboBox<String> priorityFilterComboBox;
    private JButton filterButton;


    private JRadioButton categoryRadioButton;


    private JRadioButton priorityRadioButton;
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
        initFilterComponents();
        pack();
        setLocationRelativeTo(null);
    }

    private void initWidgets() {
        taskTable = new JTable() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        selectTaskButton = new JButton("Edit Task");
        backButton = new JButton("Back");
        toggleCompleteButton = new JButton("Toggle Complete");
        deleteButton = new JButton("Delete");

        String[] columnNames = {"ID", "Title", "Description", "Due Date", "Priority", "Category", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        taskTable.setModel(tableModel);
        showAllTasks();

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        buttonPanel.add(selectTaskButton);
        buttonPanel.add(toggleCompleteButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(new JScrollPane(taskTable));
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

        // Action listener for the back button
        backButton.addActionListener(e -> dispose());
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

    private void initFilterComponents() {
        categoryRadioButton = new JRadioButton("Category", true);
        priorityRadioButton = new JRadioButton("Priority");
        ButtonGroup group = new ButtonGroup();
        group.add(categoryRadioButton);
        group.add(priorityRadioButton);
        categoryFilterComboBox = new JComboBox<>();
        categoryFilterComboBox.addItem("All Categories");
        CategoryList.getInstance().getCategories().forEach(category ->
                categoryFilterComboBox.addItem(category.getName())
        );

        priorityFilterComboBox = new JComboBox<>(new String[]{"All Priorities", "High", "Medium", "Low"});
        priorityFilterComboBox.setEnabled(false);
        categoryRadioButton.addActionListener(e -> toggleFilterOptions(true));
        priorityRadioButton.addActionListener(e -> toggleFilterOptions(false));

        filterButton = new JButton("Apply Filters");
        filterButton.addActionListener(e -> applyFilters());

        JPanel filterOptionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterOptionsPanel.add(categoryRadioButton);
        filterOptionsPanel.add(priorityRadioButton);
        filterOptionsPanel.add(new JLabel("Filter by:"));
        filterOptionsPanel.add(categoryFilterComboBox);
        filterOptionsPanel.add(priorityFilterComboBox);
        JPanel applyButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        applyButtonPanel.add(filterButton);
        JPanel filterPanel = new JPanel(new BorderLayout());
        filterPanel.add(filterOptionsPanel, BorderLayout.CENTER);
        filterPanel.add(applyButtonPanel, BorderLayout.EAST);
        add(filterPanel, 0);
    }

    private void toggleFilterOptions(boolean isCategorySelected) {
        categoryFilterComboBox.setEnabled(isCategorySelected);
        priorityFilterComboBox.setEnabled(!isCategorySelected);
    }

    private void applyFilters() {
        String selectedCategoryName = categoryFilterComboBox.isEnabled() ? (String) categoryFilterComboBox.getSelectedItem() : "All Categories";
        String selectedPriority = priorityFilterComboBox.isEnabled() ? (String) priorityFilterComboBox.getSelectedItem() : "All Priorities";

        List<Task> filteredTasks;

        if (!"All Categories".equals(selectedCategoryName)) {
            filteredTasks = taskList.getTasksByCategory(selectedCategoryName);
        } else if (!"All Priorities".equals(selectedPriority)) {
            filteredTasks = taskList.getTasksByPriority(selectedPriority);
        } else {
            filteredTasks = taskList.getTasks();
        }
        tableModel.setRowCount(0);
        updateTaskTable(filteredTasks);
    }

    private void updateTaskTable(List<Task> tasks) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (Task task : tasks) {
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
