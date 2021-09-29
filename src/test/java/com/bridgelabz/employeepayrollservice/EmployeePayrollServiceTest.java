package com.bridgelabz.employeepayrollservice;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.bridgelabz.employeepayrollservice.EmployeePayrollService.*;

public class EmployeePayrollServiceTest {

    private static EmployeePayrollService employeePayrollService;
    private static List<Employee> employeePayrollList;

    @BeforeClass
    public static void beforeClass() {
        employeePayrollService = new EmployeePayrollService();
        try {
            employeePayrollList = employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
        } catch (EmployeePayrollException e) {
            assertEquals(EmployeePayrollException.ExceptionType.UNKNOWN_DATABASE, e.type);
            e.printStackTrace();
        }
    }

    @Test
    public void givenDatabase_WhenWrong_ShouldThrowCustomException() {
        try {
            employeePayrollList = employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
        } catch (EmployeePayrollException e) {
            assertEquals(EmployeePayrollException.ExceptionType.UNKNOWN_DATABASE, e.type);
            e.printStackTrace();
        }
    }

    @Test
    public void given3EmployeesWhenWrittenToFileShouldMatchEmployeeEntries() {
        Employee[] arrayOfEmps = {
                new Employee(1, "Jeff Bezos", 100000.0),
                new Employee(2, "Bill Gates", 200000.0),
                new Employee(3, "Mark Zuckerberg", 300000.0)
        };
        EmployeePayrollService employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmps));
        employeePayrollService.writeEmployeePayrollData(EmployeePayrollService.IOService.FILE_IO);
        employeePayrollService.printData(EmployeePayrollService.IOService.FILE_IO);
        long entries = employeePayrollService.countEntries(EmployeePayrollService.IOService.FILE_IO);
        assertEquals(3, entries);
    }

    @Test
    public void givenFileOnReadingFromFileShouldMatchEmployeeCount() throws EmployeePayrollException {
        List<Employee> employee = employeePayrollService.readEmployeePayrollData(IOService.FILE_IO);
        assertEquals(3, employee.size());
    }

    @Test
    public void givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchEmployeeCount() {
        assertEquals(3, employeePayrollList.size());

    }

    @Test
    public void givenNewSalaryForEmployee_WhenUpdates_ShouldSyncWithDB() {

        try {
            employeePayrollList = employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
            employeePayrollService.printData(IOService.FILE_IO);
            employeePayrollService.updateEmployeeSalary("Terisa", 400000.00);
            boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa");
            assertTrue(result);
        } catch (EmployeePayrollException e) {
            assertEquals(EmployeePayrollException.ExceptionType.UNSUCCESSFUL_UPDATE, e.type);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenDateRange_WhenRetrieved_ShouldMatchEmployeeCount() throws EmployeePayrollException {
        String startDate = "2018-01-01";
        String endDate = "2019-12-01";
        List<Employee> employeesListInDateRange = employeePayrollService.getEmployeesFromDateRange(startDate, endDate);
        assertEquals(2, employeesListInDateRange.size());
    }

    @Test
    public void givenGender_WhenRetrieved_ShouldGetSumOFSalaryBasedOnGender() {
        Map<String, Double> sum = employeePayrollService.getSalarySumBasedOnGender();
        assertEquals(110000.0, sum.get("M"), 0.0);
        assertEquals(400000, sum.get("F"), 0.0);
    }

    @Test
    public void givenGender_WhenRetrieved_ShouldGetAverageSalaryBasedOnGender() {
        Map<String, Double> average = employeePayrollService.getAverageSalaryBasedOnGender();
        assertEquals(55000.0, average.get("M"), 0.0);
        assertEquals(400000, average.get("F"), 0.0);
    }

    @Test
    public void givenGender_WhenRetrieved_ShouldGetEmployeeCountBasedOnGender() {
        Map<String, Double> count = employeePayrollService.getEmployeeCountBasedOnGender();
        assertEquals(2, count.get("M"), 0.0);
        assertEquals(1, count.get("F"), 0.0);
    }

    @Test
    public void givenGender_WhenRetrieved_ShouldGetMinimumSalaryBasedOnGender() {
        Map<String, Double> minimumSalary = employeePayrollService.getMinimumSalaryBasedOnGender();
        assertEquals(50000.0, minimumSalary.get("M"), 0.0);
        assertEquals(400000, minimumSalary.get("F"), 0.0);
    }

    @Test
    public void givenGender_WhenRetrieved_ShouldGetMaximumSalaryBasedOnGender() {
        Map<String, Double> maximumSalary = employeePayrollService.getMaximumSalaryBasedOnGender();
        assertEquals(60000.0, maximumSalary.get("M"), 0.0);
        assertEquals(400000, maximumSalary.get("F"), 0.0);
    }

    @Test
    public void givenNewEmployee_WhenAddedWithPayrollData_ShouldSyncWithDB() {
        try {
            employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
            LocalDate startDate = LocalDate.parse("2016-11-04");
            employeePayrollService.addEmployeeAndPayRoll("Strange","M",50000.0, startDate,1);
            boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Strange");
            assertTrue(result);
        } catch (EmployeePayrollException e) {
            e.printStackTrace();
        }
    }
}