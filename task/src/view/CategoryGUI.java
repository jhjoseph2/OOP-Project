package view;

import model.Category;
import model.CategoryList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class CategoryGUI extends JFrame {


    private JTextField newCategoryField;
    private JButton createCategoryButton;
    private JButton deleteCategoryButton;
    private CategoryList categoryList;

    private JButton backButton;
    private JComboBox<String> deleteCategoryComboBox;
    public CategoryGUI() {
        categoryList = CategoryList.getInstance();
        setTitle("Manage Category");
        initWidgets();
        pack();
        setLocationRelativeTo(null); // Center the window

    }

    private void initWidgets() {
        deleteCategoryComboBox = new JComboBox<>();
        deleteCategoryComboBox.addItem("Select a category"); // Moved up before adding categories
        for (Category category : categoryList.getCategories()) {
            deleteCategoryComboBox.addItem(category.getName());
        }

        newCategoryField = new JTextField(20);
        createCategoryButton = new JButton("Create Category");
        deleteCategoryButton = new JButton("Delete Category");
        backButton = new JButton("Back");
        JPanel deleteCategoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        deleteCategoryPanel.add(new JLabel("Delete Category:"));
        deleteCategoryPanel.add(deleteCategoryComboBox);
        deleteCategoryPanel.add(deleteCategoryButton);

        JPanel newCategoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        newCategoryPanel.add(new JLabel("New Category:"));
        newCategoryPanel.add(newCategoryField);
        newCategoryPanel.add(createCategoryButton);

        createCategoryButton.addActionListener(this::addCategory);
        deleteCategoryButton.addActionListener(this::deleteCategory);

        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS)); // Set layout
        getContentPane().add(newCategoryPanel); // Add panels to the frame
        getContentPane().add(deleteCategoryPanel);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonsPanel.add(backButton);
        getContentPane().add(buttonsPanel);
        backButton.addActionListener(e -> dispose());
    }

    private void addCategory(ActionEvent e) {
        String categoryName = newCategoryField.getText().trim();
        if (categoryName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a category name.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean created = CategoryList.createCategory(categoryName);
        if (!created) {
            JOptionPane.showMessageDialog(this, "Category created failed", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }


        newCategoryField.setText("");

        JOptionPane.showMessageDialog(this, "Category created successfully.", "Category Created", JOptionPane.INFORMATION_MESSAGE);
        deleteCategoryComboBox.addItem(categoryName);
    }

    private void deleteCategory(ActionEvent e) {
        String categoryName = (String) deleteCategoryComboBox.getSelectedItem();
        if (categoryName.equals("Select a category")) {
            JOptionPane.showMessageDialog(this, "Please select a category to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        CategoryList categoryList = CategoryList.getInstance();
        boolean deleted = categoryList.deleteCategory(categoryName);
        if (deleted) {
            JOptionPane.showMessageDialog(this, "Category deleted successfully.", "Category Deleted", JOptionPane.INFORMATION_MESSAGE);
            deleteCategoryComboBox.removeItem(categoryName);
        } else {
            JOptionPane.showMessageDialog(this, "Category deleted unsuccessfully", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


}
