package view;

import model.Task;
import model.CategoryList;
import model.Category;
import model.TaskList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TaskDetailGUI extends JFrame {
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JTextField dueDateField;
    private JComboBox<String> priorityComboBox;
    private JComboBox<Category> categoryComboBox;
    private JButton saveButton;
    private JButton closeButton;

    private Task task;

    public TaskDetailGUI(Task task) {
        this.task = task;
        setTitle("Edit Task Details");
        initWidgets();
        pack();
        setLocationRelativeTo(null);
    }

    private void initWidgets() {
        titleField = new JTextField(task.getTitle(), 20);
        descriptionArea = new JTextArea(task.getDescription(), 5, 20);
        dueDateField = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(task.getDueDate()), 10);
        priorityComboBox = new JComboBox<>(new String[]{"High", "Medium", "Low"});
        priorityComboBox.setSelectedItem(task.getPriority());
        categoryComboBox = new JComboBox<>();
        CategoryList.getInstance().getCategories().forEach(categoryComboBox::addItem);
        categoryComboBox.setSelectedItem(task.getCategory());

        saveButton = new JButton("Save");
        closeButton = new JButton("Close");

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        add(createRow("Title:", titleField));
        add(createRow("Description:", new JScrollPane(descriptionArea)));
        add(createRow("Due Date:", dueDateField));
        add(createRow("Priority:", priorityComboBox));
        add(createRow("Category:", categoryComboBox));

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonsPanel.add(saveButton);
        buttonsPanel.add(closeButton);
        add(buttonsPanel);

        saveButton.addActionListener(this::onSaveTask);
        closeButton.addActionListener(e -> dispose());
    }
    private JPanel createRow(String labelText, JComponent component) {
        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rowPanel.add(new JLabel(labelText));
        rowPanel.add(component);
        return rowPanel;
    }
    private void onSaveTask(ActionEvent e) {
        // Validate and update the task details
        String title = titleField.getText();
        String description = descriptionArea.getText();
        String dueDateString = dueDateField.getText();
        String priority = (String) priorityComboBox.getSelectedItem();
        Category category = (Category) categoryComboBox.getSelectedItem();

        try {
            Date dueDate = new SimpleDateFormat("yyyy-MM-dd").parse(dueDateString);
            task.setTitle(title);
            task.setDescription(description);
            task.setDueDate(dueDate);
            task.setPriority(priority);
            task.setCategory(category);

            // Notify the user that the task was updated
            JOptionPane.showMessageDialog(this, "Task updated successfully.", "Task Updated", JOptionPane.INFORMATION_MESSAGE);
        } catch (ParseException parseException) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Please use yyyy-MM-dd.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
