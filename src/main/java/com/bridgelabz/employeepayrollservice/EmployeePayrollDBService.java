package com.bridgelabz.employeepayrollservice;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeePayrollDBService {

    private PreparedStatement employeePayrollDataStatement;
    private PreparedStatement employeePayrollUpdateDataStatement;
    private static EmployeePayrollDBService employeePayrollDBService;

    public static EmployeePayrollDBService getInstance() {
        if (employeePayrollDBService == null)
            employeePayrollDBService = new EmployeePayrollDBService();
        return employeePayrollDBService;
    }

    private EmployeePayrollDBService() {

    }

    private Connection getConnection() throws SQLException {
        String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
        String userName = "rajat";
        String password = "Mysql@3q#";
        Connection connection;

        try {
            connection = DriverManager.getConnection(jdbcURL, userName, password);
            System.out.println("Connection is successfull" + connection);
        } catch (SQLSyntaxErrorException e) {
            throw new EmployeePayrollException(EmployeePayrollException.ExceptionType.UNKNOWN_DATABASE, "Entered is a " + e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new EmployeePayrollException(EmployeePayrollException.ExceptionType.SQL_EXCEPTION, "Invalid credentials! - " + e.getMessage());
        }

        return connection;
    }

    public List<EmployeePayrollData> getEmployeePayrollDataFromDB(String sql) throws EmployeePayrollException {
        List<EmployeePayrollData> employeePayrollList;
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            employeePayrollList = this.getEmployeePayrollData(result);

        } catch (SQLException e) {
            throw new EmployeePayrollException(EmployeePayrollException.ExceptionType.INVALID_TABLE, "Invalid table name! - " + e.getMessage());
        }
        return employeePayrollList;
    }

    public List<EmployeePayrollData> readData() throws EmployeePayrollException {
        String sql = "SELECT * FROM employee_payroll";
        return this.getEmployeePayrollDataFromDB(sql);

    }

    public int updateEmployeeData(String name, double salary) {
        return this.updateEmployeeDataUsingPreparedStatement(name, salary);
    }

    private int updateEmployeeDataUsingStatement(String name, double salary) {
        String sql = String.format("update employee_payroll set basic_pay= %.2f where name ='%s';", salary, name);
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int updateEmployeeDataUsingPreparedStatement(String name, double salary) {
        if (this.employeePayrollUpdateDataStatement == null)
            this.prepareStatementToUpdateEmployeeData();
        try {
            employeePayrollUpdateDataStatement.setString(1, String.format("%.2f", salary));
            employeePayrollUpdateDataStatement.setString(2, name);

            return employeePayrollUpdateDataStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<EmployeePayrollData> getEmployeePayrollData(String name) {
        List<EmployeePayrollData> employeePayrollList = null;
        if (this.employeePayrollDataStatement == null)
            this.prepareStatementForEmployeeData();
        try {
            employeePayrollDataStatement.setString(1, name);
            ResultSet resultSet = employeePayrollDataStatement.executeQuery();
            employeePayrollList = this.getEmployeePayrollData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollList;
    }

    private List<EmployeePayrollData> getEmployeePayrollData(ResultSet resultSet) {
        List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double salary = resultSet.getDouble("basic_pay");
                employeePayrollList.add(new EmployeePayrollData(id, name, salary));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollList;
    }

    private void prepareStatementForEmployeeData() {
        try {
            Connection connection = this.getConnection();
            String sql = "select id, name, basic_pay from employee_payroll where name=?";
            employeePayrollDataStatement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void prepareStatementToUpdateEmployeeData() {
        try {
            Connection connection = this.getConnection();
            String sql = "update employee_payroll set basic_pay=? where name =?";
            employeePayrollUpdateDataStatement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<EmployeePayrollData> getEmployeesFromDateRange(String startDate, String endDate) throws EmployeePayrollException {
        String sql = String.format("SELECT id, name, basic_pay, start FROM employee_payroll "
                + "WHERE start BETWEEN CAST('%s' AS DATE) AND CAST('%s' AS DATE);", startDate, endDate);
        return this.getEmployeePayrollDataFromDB(sql);

    }

    public Map<String, Double> applyAggregateFunction(EmployeePayrollService.aggregateFunction function) {
        String sql = String.format("SELECT  gender, %s(DISTINCT basic_pay) AS RESULT FROM employee_payroll GROUP BY gender;", function.name);
        Map<String, Double> genderToResultMap = new HashMap<String, Double>();
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                double result = resultSet.getDouble("RESULT");
                genderToResultMap.put(gender, result);
            }
            return genderToResultMap;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}