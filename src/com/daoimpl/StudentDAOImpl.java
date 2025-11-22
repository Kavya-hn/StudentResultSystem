package com.daoimpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.dao.StudentDAO;
import com.model.Student;
import com.util.DBConnection;

public class StudentDAOImpl implements StudentDAO {
    
    
    public boolean addStudent(Student s) {
        String sql = "INSERT INTO students (name, marks1, marks2, marks3, total, percentage, grade) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            s.calculateResults();
            ps.setString(1, s.getName());
            ps.setInt(2, s.getMarks1());
            ps.setInt(3, s.getMarks2());
            ps.setInt(4, s.getMarks3());
            ps.setInt(5, s.getTotal());
            ps.setDouble(6, s.getPercentage());
            ps.setString(7, s.getGrade());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding student: " + e.getMessage());
            return false;
        }
    }

    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery();) {
            while (rs.next()) {
                list.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("marks1"),
                        rs.getInt("marks2"),
                        rs.getInt("marks3"),
                        rs.getInt("total"),
                        rs.getDouble("percentage"),
                        rs.getString("grade")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching students: " + e.getMessage());
        }
        return list;
    }

    public Student getStudentById(int id) {
        String sql = "SELECT * FROM students WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("marks1"),
                        rs.getInt("marks2"),
                        rs.getInt("marks3"),
                        rs.getInt("total"),
                        rs.getDouble("percentage"),
                        rs.getString("grade")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error fetching student by ID: " + e.getMessage());
        }
        return null;
    }

    public boolean updateStudent(Student s) {
        String sql = "UPDATE students SET name=?, marks1=?, marks2=?, marks3=?, total=?, percentage=?, grade=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);) {
            s.calculateResults();
            ps.setString(1, s.getName());
            ps.setInt(2, s.getMarks1());
            ps.setInt(3, s.getMarks2());
            ps.setInt(4, s.getMarks3());
            ps.setInt(5, s.getTotal());
            ps.setDouble(6, s.getPercentage());
            ps.setString(7, s.getGrade());
            ps.setInt(8, s.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating student: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting student: " + e.getMessage());
            return false;
        }
    }
}
