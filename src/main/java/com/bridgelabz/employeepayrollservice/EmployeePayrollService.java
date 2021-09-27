package com.bridgelabz.employeepayrollservice;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmployeePayrollService {

    private EmployeePayrollDBService employeePayrollDBService;


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

    private List<EmployeePayrollData> employeePayrollList;

    public EmployeePayrollService(List<EmployeePayrollData> employeePayrollList) {
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
        employeePayrollList.add(new EmployeePayrollData(id, name, salary));
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

    public List<EmployeePayrollData> readEmployeePayrollData(IOService ioservice) throws EmployeePayrollException {
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
            throw new EmployeePayrollException(EmployeePayrollException.ExceptionType.UNSUCCESFUL_UPDATE, "Update operation failed! - ");
        EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name);
        if (employeePayrollData != null)
            employeePayrollData.salary = salary;
    }
    private EmployeePayrollData getEmployeePayrollData(String name) {
        return this.employeePayrollList
                .stream()
                .filter(employeePayrollDataItem -> employeePayrollDataItem.name.equals(name))
                .findFirst()
                .orElse(null);
    }
    public boolean checkEmployeePayrollInSyncWithDB(String name) {
        List<EmployeePayrollData> employeePayrollDataList = employeePayrollDBService.getEmployeePayrollData(name);
        return employeePayrollDataList.get(0).equals(getEmployeePayrollData(name));
    }

    public List<EmployeePayrollData> getEmployeesFromDateRange(String startDate, String endDate) {
         return employeePayrollDBService.getEmployeesFromDateRange(startDate, endDate);
    }

    public double getSalarySumBasedOnGender(char gender) {
        return employeePayrollDBService.applyAggregateFunction(aggregateFunction.SUM,gender);
    }

    public double getAverageSalaryBasedOnGender(char gender) {
        return employeePayrollDBService.applyAggregateFunction(aggregateFunction.AVERAGE,gender);
    }

    public int getEmployeeCountBasedOnGender(char gender) {
        return (int) employeePayrollDBService.applyAggregateFunction(aggregateFunction.COUNT,gender);
    }

    public double getMinimumSalaryBasedOnGender(char gender) {
        return employeePayrollDBService.applyAggregateFunction(aggregateFunction.MIN,gender);
    }
    public double getMaximumSalaryBasedOnGender(char gender) {
        return employeePayrollDBService.applyAggregateFunction(aggregateFunction.MAX,gender);
    }
    public static void main(String[] args) {
        ArrayList<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        EmployeePayrollService employeePayrollService = new EmployeePayrollService(employeePayrollList);
        Scanner consoleInputReader = new Scanner(System.in);
        employeePayrollService.readEmployeePayrollData(consoleInputReader);
        employeePayrollService.writeEmployeePayrollData(IOService.CONSOLE_IO);
    }
}