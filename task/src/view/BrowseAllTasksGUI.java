package view;

import model.Task;
import model.TaskList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

public class BrowseAllTasksGUI extends JFrame {
    private JTable taskTable;
    private JTextField searchTextField;
    private JButton selectTaskButton;
    private JButton backButton;

    private TaskList taskList;
    private DefaultTableModel tableModel;

    public BrowseAllTasksGUI(TaskList taskList) {
        this.taskList = taskList;
        setTitle("Browse All Tasks");
        initWidgets();
        pack();
        setLocationRelativeTo(null);
    }

    private void initWidgets() {
//        searchTextField = new JTextField(20);
        taskTable = new JTable();
        selectTaskButton = new JButton("Select Task");
        backButton = new JButton("Back");

        // Table setup
        String[] columnNames = {"ID", "Title", "Description", "Due Date", "Priority", "Category", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        taskTable.setModel(tableModel);
        populateTableWithTasks();

        // Search Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(selectTaskButton);
        buttonPanel.add(backButton);

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
