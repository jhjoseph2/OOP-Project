package view;

import model.TaskList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class MainGUI extends JFrame {
    private JButton addNewTaskButton;
    private JButton browseAllTasksButton;

    private TaskList taskList;

    public MainGUI() {
        taskList = new TaskList(0);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Smart Todo List Application");
        initWidgets();
        pack();
        setLocationRelativeTo(null);
    }

    private void initWidgets() {
        addNewTaskButton = new JButton("Add New Task");
        browseAllTasksButton = new JButton("Browse All Tasks");

        addNewTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAddNewTask();
            }
        });

        browseAllTasksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onBrowseAllTasks();
            }
        });

        setLayout(new FlowLayout());
        add(addNewTaskButton);
        add(browseAllTasksButton);
    }

    private void onAddNewTask() {
        // Switch to AddNewTaskGUI
        AddNewTaskGUI addNewTaskGUI = new AddNewTaskGUI(taskList);
        addNewTaskGUI.setVisible(true);
    }

    private void onBrowseAllTasks() {
        // Switch to BrowseAllTasksGUI
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

