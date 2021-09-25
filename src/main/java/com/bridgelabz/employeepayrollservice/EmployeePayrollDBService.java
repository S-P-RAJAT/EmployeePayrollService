package com.bridgelabz.employeepayrollservice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollDBService {

    public List<EmployeePayrollData> readData() throws SQLException {
        List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        return employeePayrollList;
    }
}