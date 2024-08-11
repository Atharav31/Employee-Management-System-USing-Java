import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmployeeManagementSystem {
    private List<Employee> employees = new ArrayList<>();
    private int nextId = 1;
    private static final String FILE_NAME = "employees.csv";

    public static void main(String[] args) {
        EmployeeManagementSystem system = new EmployeeManagementSystem();
        system.loadEmployees();  // Load employees from file at startup
        system.menu();
    }

    private void menu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nEmployee Management System");
            System.out.println("1. Add Employee");
            System.out.println("2. View Employees");
            System.out.println("3. Edit Employee");
            System.out.println("4. Delete Employee");
            System.out.println("5. Save and Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addEmployee(scanner);
                    break;
                case 2:
                    viewEmployees();
                    break;
                case 3:
                    editEmployee(scanner);
                    break;
                case 4:
                    deleteEmployee(scanner);
                    break;
                case 5:
                    saveEmployees();  // Save employees to file
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addEmployee(Scanner scanner) {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter department: ");
        String department = scanner.nextLine();
        System.out.print("Enter salary: ");
        double salary = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        if (validateEmail(email) && salary > 0) {
            Employee employee = new Employee(nextId++, name, email, department, salary);
            employees.add(employee);
            System.out.println("Employee added successfully.");
        } else {
            System.out.println("Invalid input. Please try again.");
        }
    }

    private void viewEmployees() {
        if (employees.isEmpty()) {
            System.out.println("No employees found.");
        } else {
            System.out.println("Employee List:");
            for (Employee employee : employees) {
                System.out.println(employee);
            }
        }
    }

    private void editEmployee(Scanner scanner) {
        System.out.print("Enter employee ID to edit: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Employee employee = findEmployeeById(id);
        if (employee != null) {
            System.out.print("Enter new name (current: " + employee.getName() + "): ");
            String name = scanner.nextLine();
            System.out.print("Enter new email (current: " + employee.getEmail() + "): ");
            String email = scanner.nextLine();
            System.out.print("Enter new department (current: " + employee.getDepartment() + "): ");
            String department = scanner.nextLine();
            System.out.print("Enter new salary (current: " + employee.getSalary() + "): ");
            double salary = scanner.nextDouble();
            scanner.nextLine(); // Consume newline

            if (validateEmail(email) && salary > 0) {
                employee.setName(name);
                employee.setEmail(email);
                employee.setDepartment(department);
                employee.setSalary(salary);
                System.out.println("Employee updated successfully.");
            } else {
                System.out.println("Invalid input. Please try again.");
            }
        } else {
            System.out.println("Employee not found.");
        }
    }

    private void deleteEmployee(Scanner scanner) {
        System.out.print("Enter employee ID to delete: ");
        int id = scanner.nextInt();

        Employee employee = findEmployeeById(id);
        if (employee != null) {
            employees.remove(employee);
            System.out.println("Employee deleted successfully.");
        } else {
            System.out.println("Employee not found.");
        }
    }

    private Employee findEmployeeById(int id) {
        for (Employee employee : employees) {
            if (employee.getId() == id) {
                return employee;
            }
        }
        return null;
    }

    private boolean validateEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private void saveEmployees() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Employee employee : employees) {
                writer.println(employee.getId() + "," + employee.getName() + "," +
                               employee.getEmail() + "," + employee.getDepartment() + "," +
                               employee.getSalary());
            }
            System.out.println("Employees saved to file.");
        } catch (IOException e) {
            System.out.println("An error occurred while saving employees.");
            e.printStackTrace();
        }
    }

    private void loadEmployees() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                int id = Integer.parseInt(fields[0]);
                String name = fields[1];
                String email = fields[2];
                String department = fields[3];
                double salary = Double.parseDouble(fields[4]);
                Employee employee = new Employee(id, name, email, department, salary);
                employees.add(employee);
                nextId = Math.max(nextId, id + 1);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while loading employees.");
            e.printStackTrace();
        }
    }
}
