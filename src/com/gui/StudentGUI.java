package com.gui;

import com.dao.StudentDAO;
import com.daoimpl.StudentDAOImpl;
import com.model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

@SuppressWarnings("serial")
public class StudentGUI extends JFrame {

    private StudentDAO studentDAO = new StudentDAOImpl();
    private JTable studentTable;
    private DefaultTableModel tableModel;

    public StudentGUI() {
        setTitle("Student Result Management System");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Student");
        JButton viewButton = new JButton("View All Students");
        JButton updateButton = new JButton("Update Student");
        JButton deleteButton = new JButton("Delete Student");

        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        // Table
        tableModel = new DefaultTableModel();
        studentTable = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(studentTable);
        tableModel.setColumnIdentifiers(new Object[]{"ID", "Name", "Marks1", "Marks2", "Marks3", "Total", "Percentage", "Grade"});

        add(buttonPanel, BorderLayout.NORTH);
        add(tableScroll, BorderLayout.CENTER);

        // Button Actions
        addButton.addActionListener(e -> openAddStudentDialog());
        viewButton.addActionListener(e -> loadStudentData());
        updateButton.addActionListener(e -> openUpdateStudentDialog());
        deleteButton.addActionListener(e -> deleteSelectedStudent());
    }

    private void openAddStudentDialog() {
        JDialog dialog = new JDialog(this, "Add Student", true);
        dialog.setSize(300, 300);
        dialog.setLayout(new GridLayout(6, 2));

        JTextField nameField = new JTextField();
        JTextField marks1Field = new JTextField();
        JTextField marks2Field = new JTextField();
        JTextField marks3Field = new JTextField();

        dialog.add(new JLabel("Name:"));
        dialog.add(nameField);
        dialog.add(new JLabel("Marks 1:"));
        dialog.add(marks1Field);
        dialog.add(new JLabel("Marks 2:"));
        dialog.add(marks2Field);
        dialog.add(new JLabel("Marks 3:"));
        dialog.add(marks3Field);

        JButton saveButton = new JButton("Save");
        dialog.add(new JLabel());
        dialog.add(saveButton);

        saveButton.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                int m1 = Integer.parseInt(marks1Field.getText().trim());
                int m2 = Integer.parseInt(marks2Field.getText().trim());
                int m3 = Integer.parseInt(marks3Field.getText().trim());

                // Validation
                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Name cannot be empty!");
                    return;
                }
                if (m1 < 0 || m1 > 100 || m2 < 0 || m2 > 100 || m3 < 0 || m3 > 100) {
                    JOptionPane.showMessageDialog(dialog, "Marks must be between 0 and 100!");
                    return;
                }

                int total = m1 + m2 + m3;
                float percentage = total / 3f;
                String grade = (percentage >= 90) ? "A" : (percentage >= 75) ? "B" : (percentage >= 60) ? "C" : "D";

                Student s = new Student(name, m1, m2, m3, total, percentage, grade);
                boolean added = studentDAO.addStudent(s);
                if (added) {
                    JOptionPane.showMessageDialog(dialog, "Student added successfully!");
                    dialog.dispose();
                    loadStudentData();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Error adding student!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter valid numeric marks!");
            }
        });

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void openUpdateStudentDialog() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student to update.");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String name = (String) tableModel.getValueAt(selectedRow, 1);
        int m1 = (int) tableModel.getValueAt(selectedRow, 2);
        int m2 = (int) tableModel.getValueAt(selectedRow, 3);
        int m3 = (int) tableModel.getValueAt(selectedRow, 4);

        JDialog dialog = new JDialog(this, "Update Student", true);
        dialog.setSize(300, 300);
        dialog.setLayout(new GridLayout(6, 2));

        JTextField nameField = new JTextField(name);
        JTextField marks1Field = new JTextField(String.valueOf(m1));
        JTextField marks2Field = new JTextField(String.valueOf(m2));
        JTextField marks3Field = new JTextField(String.valueOf(m3));

        dialog.add(new JLabel("Name:"));
        dialog.add(nameField);
        dialog.add(new JLabel("Marks 1:"));
        dialog.add(marks1Field);
        dialog.add(new JLabel("Marks 2:"));
        dialog.add(marks2Field);
        dialog.add(new JLabel("Marks 3:"));
        dialog.add(marks3Field);

        JButton updateButton = new JButton("Update");
        dialog.add(new JLabel());
        dialog.add(updateButton);

        updateButton.addActionListener(e -> {
            try {
                String newName = nameField.getText().trim();
                int newM1 = Integer.parseInt(marks1Field.getText().trim());
                int newM2 = Integer.parseInt(marks2Field.getText().trim());
                int newM3 = Integer.parseInt(marks3Field.getText().trim());

                // Validation
                if (newName.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Name cannot be empty!");
                    return;
                }
                if (newM1 < 0 || newM1 > 100 || newM2 < 0 || newM2 > 100 || newM3 < 0 || newM3 > 100) {
                    JOptionPane.showMessageDialog(dialog, "Marks must be between 0 and 100!");
                    return;
                }

                int total = newM1 + newM2 + newM3;
                float percentage = total / 3f;
                String grade = (percentage >= 90) ? "A" : (percentage >= 75) ? "B" : (percentage >= 60) ? "C" : "D";

                Student s = new Student(id, newName, newM1, newM2, newM3, total, percentage, grade);
                boolean updated = studentDAO.updateStudent(s);
                if (updated) {
                    JOptionPane.showMessageDialog(dialog, "Student updated successfully!");
                    dialog.dispose();
                    loadStudentData();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Error updating student!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter valid numeric marks!");
            }
        });

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void deleteSelectedStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student to delete.");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this student?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean deleted = studentDAO.deleteStudent(id);
            if (deleted) {
                JOptionPane.showMessageDialog(this, "Student deleted successfully!");
                loadStudentData();
            } else {
                JOptionPane.showMessageDialog(this, "Error deleting student!");
            }
        }
    }

    private void loadStudentData() {
        tableModel.setRowCount(0);
        List<Student> students = studentDAO.getAllStudents();
        for (Student s : students) {
            tableModel.addRow(new Object[]{
                    s.getId(),
                    s.getName(),
                    s.getMarks1(),
                    s.getMarks2(),
                    s.getMarks3(),
                    s.getTotal(),
                    s.getPercentage(),
                    s.getGrade()
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentGUI().setVisible(true));
    }
}
