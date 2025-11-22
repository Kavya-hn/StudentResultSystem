package com.dao;

import java.util.List;

import com.model.Student;

public interface StudentDAO {
boolean addStudent(Student s);
List<Student> getAllStudents();
Student getStudentById(int id);
boolean updateStudent(Student s);
boolean deleteStudent(int id);

}
