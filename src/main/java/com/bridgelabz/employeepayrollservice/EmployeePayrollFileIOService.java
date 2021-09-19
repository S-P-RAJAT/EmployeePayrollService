package com.bridgelabz.employeepayrollservice;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollFileIOService {

    public static String PAYROLL_FILE_NAME = "payroll-file.txt";

    public void writeData(List<EmployeePayrollData> employeePayrollList) {
        StringBuffer empBuffer = new StringBuffer();
        employeePayrollList.forEach(employee -> {
            String employeeDataString = employee.toString().concat("\n");
            empBuffer.append(employeeDataString);
        });

        try {
            Files.write(Paths.get(PAYROLL_FILE_NAME), empBuffer.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printData() {
        try {
            Files.lines(new File(PAYROLL_FILE_NAME).toPath()).forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public long countEntries() {
        long enteries = 0;
        try {
            enteries = Files.lines(new File(PAYROLL_FILE_NAME).toPath()).count();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return enteries;
    }

    public List<EmployeePayrollData> readData() {
        List<EmployeePayrollData> employeePayrollDatas = new ArrayList<EmployeePayrollData>();
        try {
            Files.lines(new File(PAYROLL_FILE_NAME).toPath()).map(String::trim).forEach(line -> {
                EmployeePayrollData tempEmp = new EmployeePayrollData(
                        Integer.parseInt(line.split(",")[0].split("=")[1]), line.split(",")[1].split("=")[1],
                        Double.parseDouble(line.split(",")[2].split("=")[1]));
                employeePayrollDatas.add(tempEmp);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return employeePayrollDatas;
    }
}