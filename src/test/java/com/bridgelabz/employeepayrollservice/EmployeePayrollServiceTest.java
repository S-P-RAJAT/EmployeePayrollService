package com.bridgelabz.employeepayrollservice;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.bridgelabz.employeepayrollservice.EmployeePayrollService.*;

public class EmployeePayrollServiceTest {

    private static EmployeePayrollService employeePayrollService;

    @BeforeClass
    public static void beforeClass() {
        employeePayrollService = new EmployeePayrollService();

    }

    @Test
    public void given3EmployeesWhenWrittenToFileShouldMatchEmployeeEntries() {
        EmployeePayrollData[] arrayOfEmps = {
                new EmployeePayrollData(1, "Jeff Bezos", 100000.0),
                new EmployeePayrollData(2, "Bill Gates", 200000.0),
                new EmployeePayrollData(3, "Mark Zuckerberg", 300000.0)
        };
        EmployeePayrollService employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmps));
        employeePayrollService.writeEmployeePayrollData(EmployeePayrollService.IOService.FILE_IO);
        employeePayrollService.printData(EmployeePayrollService.IOService.FILE_IO);
        long entries = employeePayrollService.countEntries(EmployeePayrollService.IOService.FILE_IO);
        assertEquals(3, entries);
    }

    @Test
    public void givenFileOnReadingFromFileShouldMatchEmployeeCount() {
        List<EmployeePayrollData> employeePayrollData= employeePayrollService.readEmployeePayrollData(IOService.FILE_IO);
        assertEquals(3, employeePayrollData.size());
    }
    @Test
    public void givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchEmployeeCount() {
        List<EmployeePayrollData> employeePayrollData= employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
        assertEquals(4, employeePayrollData.size());
    }
    @Test
    public void givenNewSalaryForEmployee_WhenUpdates_ShouldSyncWithDB() {

        employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
        employeePayrollService.printData(IOService.FILE_IO);
        employeePayrollService.updateEmployeeSalary("Terisa", 400000.00);
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa");
        assertTrue(result);
    }

    @Test
    public void givenDateRange_WhenRetrieved_ShouldMatchEmployeeCount() {
        employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
        String startDate = "2018-01-01";
        String endDate = "2019-12-01";
        List<EmployeePayrollData> employeesListInDateRange = employeePayrollService.getEmployeesFromDateRange(startDate,endDate);
        assertEquals(2, employeesListInDateRange.size());
    }

    @Test
    public void givenGender_WhenRetrieved_ShouldGetSumOFSalaryBasedOnGender() {
        double sum = employeePayrollService.getSalarySumBasedOnGender('M');
        assertEquals(4000000, sum,0.0);
        sum = employeePayrollService.getSalarySumBasedOnGender('F');
        assertEquals(400000, sum,0.0);
    }

    @Test
    public void givenGender_WhenRetrieved_ShouldGetAverageSalaryBasedOnGender() {
        double average = employeePayrollService.getAverageSalaryBasedOnGender('M');
        assertEquals(2000000, average,0.0);
        average = employeePayrollService.getAverageSalaryBasedOnGender('F');
        assertEquals(400000, average,0.0);
    }

    @Test
    public void givenGender_WhenRetrieved_ShouldGetEmployeeCountBasedOnGender() {
        int count = employeePayrollService.getEmployeeCountBasedOnGender('M');
        assertEquals(2, count,0.0);
        count = employeePayrollService.getEmployeeCountBasedOnGender('F');
        assertEquals(1, count,0.0);
    }

    @Test
    public void givenGender_WhenRetrieved_ShouldGetMinimumSalaryBasedOnGender() {
        double minimumSalary = employeePayrollService.getMinimumSalaryBasedOnGender('M');
        assertEquals(1000000, minimumSalary,0.0);
        minimumSalary = employeePayrollService.getMinimumSalaryBasedOnGender('F');
        assertEquals(400000, minimumSalary,0.0);
    }
    @Test
    public void givenGender_WhenRetrieved_ShouldGetMaximumSalaryBasedOnGender() {
        double maximumSalary = employeePayrollService.getMaximumSalaryBasedOnGender('M');
        assertEquals(3000000, maximumSalary,0.0);
        maximumSalary = employeePayrollService.getMaximumSalaryBasedOnGender('F');
        assertEquals(400000, maximumSalary,0.0);
    }
}