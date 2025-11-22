package com.main;

import java.util.List;
import java.util.Scanner;

import com.daoimpl.StudentDAOImpl;
import com.model.Student;

public class Main {
    private static StudentDAOImpl dao = new StudentDAOImpl();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            try {
                System.out.println("\n=== Student Result Management System ===");
                System.out.println("1. Add Student");
                System.out.println("2. View All Students");
                System.out.println("3. Update Student");
                System.out.println("4. Delete Student");
                System.out.println("5. View Student By ID");
                System.out.println("0. Exit");
                System.out.print("Enter your choice: ");
                int choice = sc.nextInt();
                sc.nextLine(); 

                switch (choice) {
                    case 1 -> addStudent(sc);
                    case 2 -> viewAllStudents();
                    case 3 -> updateStudent(sc);
                    case 4 -> deleteStudent(sc);
                    case 5 -> viewStudentById(sc);
                    case 0 -> {
                        System.out.println("Exiting...");
                        return;
                    }
                    default -> System.out.println("Invalid choice!");
                }
            } catch (Exception e) {
                System.err.println("Input error: " + e.getMessage());
                sc.nextLine(); // clear invalid input
            }
        }
    }

    private static void addStudent(Scanner sc) {
        try {
            // Validate name
            String name;
            while (true) {
                System.out.print("Enter Name: ");
                name = sc.nextLine();
                if (validateName(name)) break;
                else System.out.println("Invalid name! Only letters and spaces allowed.");
            }

            // Validate marks
            int m1 = -1, m2 = -1, m3 = -1;
            while (true) {
                try {
                    System.out.print("Enter Marks1: ");
                    m1 = sc.nextInt();
                    System.out.print("Enter Marks2: ");
                    m2 = sc.nextInt();
                    System.out.print("Enter Marks3: ");
                    m3 = sc.nextInt();
                    sc.nextLine();
                    if (!validateMarks(m1, m2, m3)) {
                        System.err.println("Marks should be between 0 and 100!");
                        continue;
                    }
                    break;
                } catch (Exception e) {
                    System.err.println("Please enter valid integers for marks!");
                    sc.nextLine(); // clear invalid input
                }
            }

            String finalName = name; 
            Student s = new Student(finalName, m1, m2, m3);

            Thread t = new Thread(() -> {
                if (dao.addStudent(s)) System.out.println("Student added: " + finalName);
                else System.out.println("Failed to add student.");
            });
            t.start();
            t.join(); 



        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            sc.nextLine();
        }
    }

    private static void viewAllStudents() {
        Thread t = new Thread(() -> {
            List<Student> students = dao.getAllStudents();
            System.out.println("\n=== All Students ===");
            System.out.printf("%-3s | %-10s | %-10s | %-5s | %-5s | %-5s | %-7s | %-6s | %-5s\n",
                    "ID", "Name", "Marks", "M1", "M2", "M3", "Total", "%", "Grade");
            System.out.println("-------------------------------------------------------------------");
            for (Student s : students) {
                System.out.printf("%-3d | %-10s | %-10s | %-5d | %-5d | %-5d | %-7d | %-6.2f | %-5s\n",
                        s.getId(), s.getName(),
                        s.getMarks1() + "," + s.getMarks2() + "," + s.getMarks3(),
                        s.getMarks1(), s.getMarks2(), s.getMarks3(),
                        s.getTotal(), s.getPercentage(), s.getGrade());
            }
        });
        t.start();
        try {
            t.join(); // wait until thread finishes before continuing
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void updateStudent(Scanner sc) {
        try {
            System.out.print("Enter Student ID to update: ");
            int id = sc.nextInt();
            sc.nextLine();

            Student s = dao.getStudentById(id);
            if (s == null) {
                System.out.println("Student not found!");
                return;
            }

            // Validate name
            String name;
            while (true) {
                System.out.print("Enter new Name: ");
                name = sc.nextLine();
                if (validateName(name)) break;
                else System.err.println("Invalid name! Only letters and spaces allowed.");
            }

            // Validate marks
            int m1 = -1, m2 = -1, m3 = -1;
            while (true) {
                try {
                    System.out.print("Enter Marks1: ");
                    m1 = sc.nextInt();
                    System.out.print("Enter Marks2: ");
                    m2 = sc.nextInt();
                    System.out.print("Enter Marks3: ");
                    m3 = sc.nextInt();
                    sc.nextLine();
                    if (!validateMarks(m1, m2, m3)) {
                        System.err.println("Marks should be between 0 and 100!");
                        continue;
                    }
                    break;
                } catch (Exception e) {
                    System.err.println("Please enter valid integers for marks!");
                    sc.nextLine(); // clear invalid input
                }
            }

            s.setName(name);
            s.setMarks1(m1);
            s.setMarks2(m2);
            s.setMarks3(m3);

            Thread t = new Thread(() -> {
                if (dao.updateStudent(s)) System.out.println("Student updated.");
                else System.out.println("Failed to update student.");
            });
            t.start();
            try {
                t.join();  // wait until update finishes
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            sc.nextLine();
        }
    }

    private static void deleteStudent(Scanner sc) {
        try {
            System.out.print("Enter Student ID to delete: ");
            int id = sc.nextInt();
            sc.nextLine();
            Thread t = new Thread(() -> {
                if (dao.deleteStudent(id)) System.out.println("Student deleted.");
                else System.out.println("Failed to delete student.");
            });
            t.start();
            try {
                t.join();  // wait until deletion completes
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            System.err.println("Invalid input: " + e.getMessage());
            sc.nextLine();
        }
    }

    private static void viewStudentById(Scanner sc) {
        try {
            System.out.print("Enter Student ID to view: ");
            int id = sc.nextInt();
            sc.nextLine();
            Thread t = new Thread(() -> {
                Student s = dao.getStudentById(id);
                if (s != null) {
                    System.out.printf("%d | %s | %d, %d, %d | Total: %d | %%: %.2f | Grade: %s\n",
                            s.getId(), s.getName(), s.getMarks1(), s.getMarks2(), s.getMarks3(),
                            s.getTotal(), s.getPercentage(), s.getGrade());
                } else {
                    System.out.println("Student not found!");
                }
            });
            t.start();
            try {
                t.join();  // wait until view completes
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            System.err.println("Invalid input: " + e.getMessage());
            sc.nextLine();
        }
    }

    private static boolean validateName(String name) {
        return name != null && !name.trim().isEmpty() && name.matches("[a-zA-Z ]+");
    }

    private static boolean validateMarks(int m1, int m2, int m3) {
        return m1 >= 0 && m1 <= 100 &&
               m2 >= 0 && m2 <= 100 &&
               m3 >= 0 && m3 <= 100;
    }
}
