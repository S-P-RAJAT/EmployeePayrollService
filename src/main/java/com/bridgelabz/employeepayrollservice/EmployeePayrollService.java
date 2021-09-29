package com.bridgelabz.employeepayrollservice;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class EmployeePayrollService {

    private final EmployeePayrollDBService employeePayrollDBService;


    public EmployeePayrollService() {
        employeePayrollDBService = EmployeePayrollDBService.getInstance();

    }

    public enum IOService {
        CONSOLE_IO, FILE_IO, DB_IO, REST_IO
    }
    public enum aggregateFunction {
        SUM("SUM"), AVERAGE("AVG"),COUNT("COUNT"),MAX("MAX"),MIN("MIN");

        public String name;
        aggregateFunction(String name){
            this.name = name;
        }
    }

    private List<Employee> employeePayrollList;

    public EmployeePayrollService(List<Employee> employeePayrollList) {
        this();
        this.employeePayrollList = employeePayrollList;
    }

    private void readEmployeePayrollData(Scanner consoleInputReader) {
        System.out.println("Enter Employee ID: ");
        int id = consoleInputReader.nextInt();
        System.out.println("Enter Employee Name: ");
        String name = consoleInputReader.next();
        System.out.println("Enter Employee Salary: ");
        double salary = consoleInputReader.nextDouble();
        employeePayrollList.add(new Employee(id, name, salary));
    }

    void writeEmployeePayrollData(IOService ioservice) {
        if (ioservice.equals(IOService.CONSOLE_IO))
            System.out.println("\nWriting Employee Payroll to Console\n" + employeePayrollList);
        else if (ioservice.equals(IOService.FILE_IO)) {
            new EmployeePayrollFileIOService().writeData(employeePayrollList);
        }
    }

    public void printData(IOService ioservice) {
        if (ioservice.equals(IOService.FILE_IO)) {
            new EmployeePayrollFileIOService().printData();
        }
    }

    public long countEntries(IOService ioservice) {
        if (ioservice.equals(IOService.FILE_IO)) {
            return new EmployeePayrollFileIOService().countEntries();
        }
        return 0;
    }

    public List<Employee> readEmployeePayrollData(IOService ioservice) throws EmployeePayrollException {
        if (ioservice.equals(IOService.FILE_IO)) {
            this.employeePayrollList = new EmployeePayrollFileIOService().readData();
            System.out.println("PARSED DATA FROM FILE: ");
            this.employeePayrollList.forEach(System.out::println);
        } else if (ioservice.equals(IOService.DB_IO)){
            try {
                this.employeePayrollList = employeePayrollDBService.readData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return this.employeePayrollList;
    }
    public void updateEmployeeSalary(String name, double salary) throws EmployeePayrollException {
        int result = employeePayrollDBService.updateEmployeeData(name, salary);
        if (result == 0)
            throw new EmployeePayrollException(EmployeePayrollException.ExceptionType.UNSUCCESSFUL_UPDATE, "Update operation failed! - ");
        Employee employee = this.getEmployeePayrollData(name);
        if (employee != null)
            employee.setSalary(salary);
    }
    private Employee getEmployeePayrollData(String name) {
        return this.employeePayrollList
                .stream()
                .filter(employee -> employee.getEmployeeName().equals(name))
                .findFirst()
                .orElse(null);
    }
    public boolean checkEmployeePayrollInSyncWithDB(String name) {
        List<Employee> employeeList = employeePayrollDBService.getEmployeePayrollData(name);
        return employeeList.get(0).equals(getEmployeePayrollData(name));
    }

    public List<Employee> getEmployeesFromDateRange(String startDate, String endDate) throws EmployeePayrollException {
        return employeePayrollDBService.getEmployeesFromDateRange(startDate, endDate);
    }

    public Map<String, Double> getSalarySumBasedOnGender() {
        return employeePayrollDBService.applyAggregateFunction(aggregateFunction.SUM);
    }

    public Map<String, Double> getAverageSalaryBasedOnGender() {
        return employeePayrollDBService.applyAggregateFunction(aggregateFunction.AVERAGE);
    }

    public Map<String, Double> getEmployeeCountBasedOnGender() {
        return  employeePayrollDBService.applyAggregateFunction(aggregateFunction.COUNT);
    }

    public Map<String, Double> getMinimumSalaryBasedOnGender() {
        return employeePayrollDBService.applyAggregateFunction(aggregateFunction.MIN);
    }
    public Map<String, Double> getMaximumSalaryBasedOnGender() {
        return employeePayrollDBService.applyAggregateFunction(aggregateFunction.MAX);
    }

    public void addEmployeeAndPayRoll(String employeeName, String gender, double salary, LocalDate startDate,int companyId )
    {
        employeePayrollList.add(employeePayrollDBService.addEmployeeToPayroll(employeeName, gender, salary, startDate, companyId));
    }
    public static void main(String[] args) {
        ArrayList<Employee> employeePayrollList = new ArrayList<>();
        EmployeePayrollService employeePayrollService = new EmployeePayrollService(employeePayrollList);
        Scanner consoleInputReader = new Scanner(System.in);
        employeePayrollService.readEmployeePayrollData(consoleInputReader);
        employeePayrollService.writeEmployeePayrollData(IOService.CONSOLE_IO);
    }
}