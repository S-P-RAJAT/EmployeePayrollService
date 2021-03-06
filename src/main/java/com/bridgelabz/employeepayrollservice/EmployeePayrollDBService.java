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

    private Connection getConnection() throws EmployeePayrollException {
        String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
        String userName = "rajat";
        String password = "Mysql@3q#";
        Connection connection;

        try {
            connection = DriverManager.getConnection(jdbcURL, userName, password);
            System.out.println("Connection is successful" + connection);
        } catch (SQLSyntaxErrorException e) {
            throw new EmployeePayrollException(EmployeePayrollException.ExceptionType.UNKNOWN_DATABASE,
                    "Entered is a " + e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new EmployeePayrollException(EmployeePayrollException.ExceptionType.SQL_EXCEPTION,
                    "Invalid credentials! - " + e.getMessage());
        }
        return connection;
    }
    public List<Employee> readData() throws EmployeePayrollException {
        String sql = "SELECT * FROM employee where is_active=true;";
        return this.getEmployeePayrollDataFromDB(sql);

    }

    public List<Employee> getEmployeePayrollData(String name) {
        List<Employee> employeePayrollList = null;
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
    private List<Employee> getEmployeePayrollData(ResultSet resultSet) {
        List<Employee> employeePayrollList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("employee_name");
                double salary = resultSet.getDouble("basic_pay");
                employeePayrollList.add(new Employee(id, name, salary));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollList;
    }

    public List<Employee> getEmployeePayrollDataFromDB(String sql) throws EmployeePayrollException {
        List<Employee> employeePayrollList;
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            employeePayrollList = this.getEmployeePayrollData(result);

        } catch (SQLException e) {
            throw new EmployeePayrollException(EmployeePayrollException.ExceptionType.INVALID_TABLE,
                    "Invalid table name! - " + e.getMessage());
        }
        return employeePayrollList;
    }

    public List<Employee> getEmployeesFromDateRange(String startDate, String endDate)
            throws EmployeePayrollException {
        String sql = String.format("SELECT id, employee_name, basic_pay, start_date FROM employee "
                + "WHERE start_date BETWEEN CAST('%s' AS DATE) AND CAST('%s' AS DATE);", startDate, endDate);
        return this.getEmployeePayrollDataFromDB(sql);

    }

    public Map<String, Double> applyAggregateFunction(EmployeePayrollService.aggregateFunction function) {
        String sql = String.format("SELECT  gender, %s(DISTINCT basic_pay) AS RESULT FROM employee GROUP BY gender;",
                function.name);
        Map<String, Double> genderToResultMap = new HashMap<>();
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

    private void prepareStatementForEmployeeData() {
        try {
            Connection connection = this.getConnection();
            String sql = "select id, employee_name, basic_pay from employee where employee_name=?";
            employeePayrollDataStatement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void prepareStatementToUpdateEmployeeData() {
        try {
            Connection connection = this.getConnection();
            String sql = "update employee set basic_pay=? where employee_name =?";
            employeePayrollUpdateDataStatement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int updateEmployeeData(String name, double salary) throws SQLException {
        return this.updateEmployeeDataUsingPreparedStatement(name, salary);
    }

    private int updateEmployeeDataUsingPreparedStatement(String name, double salary) throws SQLException {
        if (this.employeePayrollUpdateDataStatement == null)
            this.prepareStatementToUpdateEmployeeData();
        Connection connection = employeePayrollUpdateDataStatement.getConnection();
        try {
            connection.setAutoCommit(false);
            employeePayrollUpdateDataStatement.setString(1, String.format("%.2f", salary));
            employeePayrollUpdateDataStatement.setString(2, name);

            List<Employee> employeeList = getEmployeePayrollData(name);
            int id =  employeeList.get(0).getEmployeeId();
            removeEmployeePayroll(id,connection);
            insertToPayrollTable(id,salary,connection);
            int result = employeePayrollUpdateDataStatement.executeUpdate();
            connection.commit();
            return result;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new EmployeePayrollException(EmployeePayrollException.ExceptionType.UPDATE_FAILED, e.getMessage());

        }
    }

    public Employee addEmployeeToPayroll(String employeeName, String gender, double salary, LocalDate startDate,
                                         int companyId) throws EmployeePayrollException {
        int employeeId = -1;
        Employee employeeData;
        try (Connection connection = this.getConnection()) {
            connection.setAutoCommit(false);
            employeeId = insertToEmployeeTable(
                    employeeName, gender, salary, startDate, companyId, employeeId, connection);
            employeeData = new Employee(employeeId, employeeName, salary, startDate);

            employeeData.setPayroll(insertToPayrollTable(employeeId, salary, connection));
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw new EmployeePayrollException(EmployeePayrollException.ExceptionType.ADD_FAILED, e.getMessage());

        }
        return employeeData;
    }

    private int insertToEmployeeTable(String employeeName, String gender, double salary,
                                      LocalDate startDate, int companyId, int employeeId,
                                      Connection connection) throws EmployeePayrollException {
        try (Statement statement = connection.createStatement()) {
            String sql = String.format("INSERT INTO employee(employee_name,gender,basic_pay,start_date,company_id)"
                    + " VALUES ('%s','%s','%s','%s','%s');", employeeName, gender, salary, startDate, companyId);
            int rowAffected = statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
            if (rowAffected == 1) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) employeeId = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new EmployeePayrollException(EmployeePayrollException.ExceptionType.ADD_FAILED, e.getMessage());
        }
        return employeeId;
    }

    private Payroll insertToPayrollTable(int employeeId, double salary,Connection connection)
            throws EmployeePayrollException {
        Payroll payroll = null;

        try (Statement statement = connection.createStatement()) {
            double deductions = salary * 0.2;
            double taxablePay = salary - deductions;
            double tax = taxablePay * 0.1;
            double netPay = salary - tax;

            String sql = String.format("INSERT INTO payroll(id,basic_pay,deductions,taxable_pay,tax,net_pay) VALUES"
                    + " ('%s','%s','%s','%s','%s','%s');", employeeId, salary, deductions, taxablePay, tax, netPay);
            int rowAffected = statement.executeUpdate(sql);
            if (rowAffected == 1) {
                payroll = new Payroll(salary, deductions, taxablePay, tax, netPay);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new EmployeePayrollException(EmployeePayrollException.ExceptionType.ADD_FAILED, e.getMessage());
        }
        return payroll;
    }

    public List<Employee> removeEmployeeAndPayroll(int id) throws EmployeePayrollException {

        try (Connection connection = this.getConnection()) {
            connection.setAutoCommit(false);
            removeEmployeePayroll(id, connection);
            setEmployeeInActive(id, connection);
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw new EmployeePayrollException(EmployeePayrollException.ExceptionType.ADD_FAILED, e.getMessage());

        }
        return this.readData();
    }

    private void setEmployeeInActive(int id, Connection connection) throws EmployeePayrollException {
        String sql = String.format("UPDATE employee SET is_active=false WHERE id = '%d';", id);
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new EmployeePayrollException(EmployeePayrollException.ExceptionType.REMOVE_FAILED, e.getMessage());

        }
    }

    private void removeEmployeePayroll(int id, Connection connection) throws EmployeePayrollException {
        String sql = String.format("DELETE FROM payroll WHERE id = '%d';", id);
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new EmployeePayrollException(EmployeePayrollException.ExceptionType.REMOVE_FAILED, e.getMessage());
        }
    }
}