package view;

import model.Category;
import model.CategoryList;
import model.Task;
import model.TaskList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddNewTaskGUI extends JFrame {
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JTextField dueDateField;
    private JComboBox<String> priorityComboBox;
    private JComboBox<Category> categoryComboBox;
    private JButton backButton;

    private JButton saveButton;
    private CategoryList categoryList;

    private TaskList taskList;

    public AddNewTaskGUI(TaskList taskList) {
        this.taskList = taskList;
        setTitle("Add New Task");
        categoryList = CategoryList.getInstance();
        initWidgets();
        pack();
        setLocationRelativeTo(null);
    }

    private void initWidgets() {
        saveButton = new JButton("Save");
        titleField = new JTextField(20);
        descriptionArea = new JTextArea(5, 20);
        dueDateField = new JTextField(10);
        priorityComboBox = new JComboBox<>(new String[]{"High", "Medium", "Low"});
        categoryComboBox = new JComboBox<>(); // This should be populated with actual categories
        backButton = new JButton("Back");
        for(Category category: categoryList.getCategories()){
            categoryComboBox.addItem(category);
        }

        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // Title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.add(new JLabel("Title:"));
        titlePanel.add(titleField);
        getContentPane().add(titlePanel);

        // Description
        JPanel descriptionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        descriptionPanel.add(new JLabel("Description:"));
        descriptionPanel.add(new JScrollPane(descriptionArea));
        getContentPane().add(descriptionPanel);

        // Due Date
        JPanel dueDatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dueDatePanel.add(new JLabel("Due Date:"));
        dueDatePanel.add(dueDateField);
        getContentPane().add(dueDatePanel);

        // Priority
        JPanel priorityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        priorityPanel.add(new JLabel("Priority:"));
        priorityPanel.add(priorityComboBox);
        getContentPane().add(priorityPanel);

        // Category
        JPanel categoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        categoryPanel.add(new JLabel("Category:"));
        categoryPanel.add(categoryComboBox);
        getContentPane().add(categoryPanel);

        // Back button
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        backPanel.add(backButton);
        getContentPane().add(backPanel);

        // Add action listener to backButton
        backButton.addActionListener(e -> dispose());

        JPanel savePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        savePanel.add(saveButton);
        getContentPane().add(savePanel);

        // Add action listener to saveButton
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSaveTask();
            }
        });

        // Add action listener to backButton
        backButton.addActionListener(e -> dispose());
    }

    private void onSaveTask() {
        // Validate and gather input from fields
        String title = titleField.getText();
        String description = descriptionArea.getText();
        String dueDateString = dueDateField.getText();
        String priority = (String) priorityComboBox.getSelectedItem();
        Category category = (Category) categoryComboBox.getSelectedItem();

        // Parse the due date
        Date dueDate = null;
        try {
            dueDate = new SimpleDateFormat("yyyy-MM-dd").parse(dueDateString);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid due date in the format yyyy-MM-dd.",
                    "Invalid Date Format", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create a new Task object
        Task newTask = new Task(title, description, dueDate, priority, category.getName());

        // Add the new task to the task list
        taskList.addTask(newTask);


         clearFields();
         dispose();

        // Inform the user that the task was saved
        JOptionPane.showMessageDialog(this, "Task saved successfully.", "Task Saved", JOptionPane.INFORMATION_MESSAGE);
    }

    private void clearFields() {
        titleField.setText("");
        descriptionArea.setText("");
        dueDateField.setText("");
        priorityComboBox.setSelectedIndex(0);
        categoryComboBox.setSelectedIndex(0);
    }
}

