package com.company;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.zip.DataFormatException;

public class MainFrame extends JFrame {
    private JPanel listsPanel;
    private JList leftList, rightList;
    private StudentList modelLeft, modelRight;
    private JLabel studentsCount;
    private JLabel highAchieversCount;

    public MainFrame(){
        super("Student List");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLayout(new BorderLayout());
        setBounds(500, 200, 500, 500);

        createListPanel();

        this.add(listsPanel, BorderLayout.CENTER);
        
    }


    private void createListPanel() {
        JButton openFile = new JButton("Open");
        modelLeft = new StudentList();
        modelRight = new StudentList();
        studentsCount = new JLabel("Students");
        highAchieversCount = new JLabel("High achievers");
        openFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                if (fileChooser.showDialog(null, "Open") == JFileChooser.APPROVE_OPTION) {
                    try {
                        File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                        if (file.getName().substring(file.getName().lastIndexOf(".")).compareTo(".xml") == 0) {
                            try (StaxStreamProcessor processor = new StaxStreamProcessor(Files.newInputStream(file.toPath()))) {
                                XMLStreamReader reader = processor.getReader();
                                while (processor.hasElement()) {
                                    try {
                                        modelLeft.addElement(new Student(reader.getElementText()));
                                    } catch (DataFormatException dfe) {
                                        JOptionPane.showMessageDialog(null, "Incorrect data");
                                    }
                                }
                                reader.close();
                            } catch (XMLStreamException | IOException ignored) {
                                JOptionPane.showMessageDialog(null, "Opening error");
                            }
                        } else {
                            Scanner scanner = new Scanner(file);
                            while (scanner.hasNextLine()) {
                                try {
                                    modelLeft.addElement(new Student(scanner.nextLine()));
                                } catch (DataFormatException dfe) {
                                    JOptionPane.showMessageDialog(null, "Incorrect data");
                                }
                            }
                        }
                    } catch (FileNotFoundException fnfe) {
                        JOptionPane.showMessageDialog(null, "File not found");
                    }
                    for (Student s : modelLeft) {
                        if (s.isHighAchiever()) {
                            modelRight.addElement(s);
                        }
                    }
                    modelRight.sort();
                    studentsCount.setText("Students " + modelLeft.getSize());
                    highAchieversCount.setText("High achievers " + modelRight.getSize());
                }
            }
        });
        JButton saveInFile = new JButton("Save");
        saveInFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                if (fileChooser.showDialog(null, "Save") == JFileChooser.APPROVE_OPTION) {
                    try {
                        FileWriter file = new FileWriter(fileChooser.getSelectedFile().getAbsolutePath());
//                        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
//                        Element students = document.createElement("students");
//                        document.appendChild(students);
                        file.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                        file.write("<students>\n");
                        for (Student s : modelLeft) {
                            file.write("<student>");
                            file.write(s.toXMLString());
                            file.write("</student>\n");
//                            Element student = document.createElement("student");
//                            student.setTextContent(s.toXMLString());
//                            students.appendChild(student);
                        }
                        file.write("</students>");
                        file.close();
//                        Transformer transformer = TransformerFactory.newInstance().newTransformer();
//                        DOMSource source = new DOMSource(document);
//                        StreamResult result = new StreamResult(file);
//                        transformer.transform(source, result);
                    } catch (IOException ioe){ JOptionPane.showMessageDialog(null, "Writing Error");}
//                    } catch (ParserConfigurationException | TransformerException ex) {
//                        JOptionPane.showMessageDialog(null, "Writing Error");
//                    }
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
        leftPanel.add(studentsCount, BorderLayout.NORTH);
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(rightScroller, BorderLayout.CENTER);
        rightPanel.add(highAchieversCount, BorderLayout.NORTH);
        listsPanel.add(leftPanel, BorderLayout.WEST);
        listsPanel.add(rightPanel, BorderLayout.EAST);
        JTextArea studentInformation = new JTextArea();
        studentInformation.setEditable(false);
        JScrollPane scroll = new JScrollPane(studentInformation);

        leftList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!rightList.isSelectionEmpty()) {
                    rightList.clearSelection();
                }
                if (!modelLeft.isEmpty() && !leftList.isSelectionEmpty() && (modelLeft.get(leftList.getSelectedIndex()) != null)) {
                    studentInformation.setText(modelLeft.get(leftList.getSelectedIndex()).getInformation());
                }
            }
        });

        rightList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!leftList.isSelectionEmpty()) {
                    leftList.clearSelection();
                }
                if (!modelRight.isEmpty() && !rightList.isSelectionEmpty() && (modelRight.get(rightList.getSelectedIndex()) != null)) {
                    studentInformation.setText(modelRight.get(rightList.getSelectedIndex()).getInformation());
                }
            }
        });
        AddingDialog addingDialog = new AddingDialog(this);
        JButton addStudent = new JButton("Add Student");
        addStudent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addingDialog.setVisible(true);
            }
        });
        JPanel filePanel = new JPanel(new GridLayout(1, 2));
        filePanel.add(openFile);
        filePanel.add(saveInFile);
        centerListPanel.add(addStudent, BorderLayout.SOUTH);
        centerListPanel.add(filePanel, BorderLayout.NORTH);
        centerListPanel.add(scroll, BorderLayout.CENTER);
    }

    public StudentList getModelLeft() {
        return modelLeft;
    }

    public StudentList getModelRight(){
        return modelRight;
    }

    public JLabel getStudentsCount() {
        return studentsCount;
    }

    public JLabel getHighAchieversCount() {
        return highAchieversCount;
    }

    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame();
        mainFrame.setVisible(true);
    }
}
