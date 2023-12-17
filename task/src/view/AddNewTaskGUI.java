package view;

import model.Category;
import model.CategoryList;
import model.Task;
import model.TaskList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
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
        categoryComboBox = new JComboBox<>();
        backButton = new JButton("Back");
        for (Category category : categoryList.getCategories()) {
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
        dueDatePanel.add(new JLabel("Due Date (yyyy-mm-dd):"));
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

        // Back and Save button
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonsPanel.add(saveButton);
        buttonsPanel.add(backButton);
        getContentPane().add(buttonsPanel);

        // Add action listener to backButton
        backButton.addActionListener(e -> dispose());

        // Add action listener to saveButton
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTask();
            }
        });


    }



    private void addTask() {
        String title = titleField.getText();
        String description = descriptionArea.getText();
        String dueDateString = dueDateField.getText();
        String priority = (String) priorityComboBox.getSelectedItem();
        Category category = (Category) categoryComboBox.getSelectedItem();

        Date dueDate;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            dueDate = dateFormat.parse(dueDateString);
            Date today = new Date();
            if (dueDate.before(today)) {
                JOptionPane.showMessageDialog(this, "The due date must be later than today.",
                        "Invalid Due Date", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid due date in the format yyyy-MM-dd.",
                    "Invalid Date Format", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Task newTask = new Task(title, description, dueDate, priority, category != null ? category.getName() : "");

        taskList.addTask(newTask);

        clearFields();
        dispose();

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


