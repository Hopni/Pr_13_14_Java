package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.zip.DataFormatException;

public class MainFrame extends JFrame {
    private JPanel listsPanel;
    private JList leftList, rightList;
    private StudentComparator studentComparator;
    private StudentList modelLeft, modelRight;

    public MainFrame(){
        super("Student List");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLayout(new BorderLayout());
        setBounds(500, 200, 500, 500);

        studentComparator = new StudentComparator();
        createListPanel();

        this.add(listsPanel, BorderLayout.CENTER);
        
    }

    private void createListPanel() {
        JButton openFile = new JButton("Open");
        modelLeft = new StudentList();
        modelRight = new StudentList();
        openFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                if (fileChooser.showDialog(null, "Open") == JFileChooser.APPROVE_OPTION) {
                    try {
                        File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                        Scanner scanner = new Scanner(file);
                        while (scanner.hasNextLine()) {
                            try {
                                modelLeft.addElement(new Student(scanner.nextLine()));
                            } catch (DataFormatException dfe) {
                                JOptionPane.showMessageDialog(null, "Incorrect data");
                            }
                        }
                    } catch (FileNotFoundException fnfe) {
                        JOptionPane.showMessageDialog(null, "File not found");
                    }
                    for(Student s : modelLeft){
                        if(s.isHighAchiever()) {
                            modelRight.addElement(s);
                        }
                    }
                    modelRight.sort();
                }
            }
        });
        listsPanel = new JPanel(new BorderLayout());
        JPanel centerListPanel = new JPanel(new BorderLayout());
        listsPanel.add(centerListPanel, BorderLayout.CENTER);
        leftList = new JList<Student>(modelLeft);
        rightList = new JList<Student>(modelRight);
        leftList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        rightList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        leftList.setLayoutOrientation(JList.VERTICAL);
        rightList.setLayoutOrientation(JList.VERTICAL);
        JScrollPane leftScroller = new JScrollPane(leftList);
        leftScroller.setPreferredSize(new Dimension(150, 50));
        JScrollPane rightScroller = new JScrollPane(rightList);
        rightScroller.setPreferredSize(new Dimension(150, 50));
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(leftScroller, BorderLayout.CENTER);
        leftPanel.add(new JLabel("Student list"), BorderLayout.NORTH);
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(rightScroller, BorderLayout.CENTER);
        rightPanel.add(new JLabel("High achievers"), BorderLayout.NORTH);
        listsPanel.add(leftPanel, BorderLayout.WEST);
        listsPanel.add(rightPanel, BorderLayout.EAST);
        JTextArea studentInformation = new JTextArea();
        studentInformation.setEditable(false);
        JScrollPane scroll = new JScrollPane(studentInformation);

        leftList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(!rightList.isSelectionEmpty()) {
                    rightList.clearSelection();
                }
                if(!modelLeft.isEmpty() && (modelLeft.get(leftList.getSelectedIndex()) != null)) {
                    studentInformation.setText(modelLeft.get(leftList.getSelectedIndex()).getInformation());
                }
            }
        });
        rightList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(!leftList.isSelectionEmpty()){
                    leftList.clearSelection();
                }
                if(!modelRight.isEmpty() && (modelRight.get(rightList.getSelectedIndex()) != null)) {
                    studentInformation.setText(modelRight.get(rightList.getSelectedIndex()).getInformation());
                }
            }
        });
        JButton addStudent = new JButton("Add Student");
        addStudent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddingDialog addingDialog = new AddingDialog(modelLeft, modelRight);
                addingDialog.setVisible(true);
            }
        });
        centerListPanel.add(addStudent, BorderLayout.SOUTH);
        centerListPanel.add(openFile, BorderLayout.NORTH);
        centerListPanel.add(scroll, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame();
        mainFrame.setVisible(true);
    }
}
