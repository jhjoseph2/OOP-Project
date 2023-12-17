package view;

import model.TaskList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainGUI extends JFrame {
    private JButton addNewTaskButton;
    private JButton browseAllTasksButton;

    private JButton manageCategoryButton;
    private TaskList taskList;

    public MainGUI() {
        taskList = new TaskList(0);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Smart Todo List Application");
        initWidgets();
        setPreferredSize(new Dimension(300, 200));
        pack();
        setLocationRelativeTo(null);
    }

    private void initWidgets() {
        addNewTaskButton = new JButton("Add New Task");
        browseAllTasksButton = new JButton("Browse All Tasks");
        manageCategoryButton = new JButton("Manage Category");

        manageCategoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showManageGUI();
            }
        });
        addNewTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddNewGUI();
            }
        });

        browseAllTasksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showBrowseGUI();
            }
        });

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        add(addNewTaskButton, gbc);
        add(browseAllTasksButton, gbc);
        add(manageCategoryButton, gbc);
    }
    private void showManageGUI(){
        CategoryGUI categoryGUI = new CategoryGUI();
        categoryGUI.setVisible(true);
    }
    private void showAddNewGUI() {
        AddNewTaskGUI addNewTaskGUI = new AddNewTaskGUI(taskList);
        addNewTaskGUI.setVisible(true);
    }

    private void showBrowseGUI() {
        BrowseAllTasksGUI browseAllTasksGUI = new BrowseAllTasksGUI(taskList);
        browseAllTasksGUI.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainGUI().setVisible(true);
            }
        });
    }
}

