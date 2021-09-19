package com.bridgelabz.employeepayrollservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmployeePayrollService {

    public EmployeePayrollService() { }

    public enum IOService {
        CONSOLE_IO, FILE_IO, DB_IO, REST_IO
    }

    private List<EmployeePayrollData> employeePayrollList;

    public EmployeePayrollService(List<EmployeePayrollData> employeePayrollList) {
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
    public long readEmployeePayrollData(IOService ioservice) {
        if (ioservice.equals(IOService.FILE_IO)) {
            this.employeePayrollList = new EmployeePayrollFileIOService().readData();
            System.out.println("PARSED DATA FROM FILE: ");
            this.employeePayrollList.forEach(System.out::println);
        }
        return this.employeePayrollList.size();
    }

    public static void main(String[] args) {
        ArrayList<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        EmployeePayrollService employeePayrollService = new EmployeePayrollService(employeePayrollList);
        Scanner consoleInputReader = new Scanner(System.in);
        employeePayrollService.readEmployeePayrollData(consoleInputReader);
        employeePayrollService.writeEmployeePayrollData(IOService.CONSOLE_IO);
    }
}