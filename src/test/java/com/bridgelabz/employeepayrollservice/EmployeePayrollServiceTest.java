package com.bridgelabz.employeepayrollservice;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.bridgelabz.employeepayrollservice.EmployeePayrollService.*;

public class EmployeePayrollServiceTest {

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
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData= employeePayrollService.readEmployeePayrollData(IOService.FILE_IO);
        assertEquals(3, employeePayrollData.size());
    }
    @Test
    public void givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchEmployeeCount() {
        EmployeePayrollService employeePayrollService=new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData= employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
        assertEquals(4, employeePayrollData.size());
    }
    @Test
    public void givenNewSalaryForEmployee_WhenUpdates_ShouldSyncWithDB() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();

        employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
        employeePayrollService.printData(IOService.FILE_IO);
        employeePayrollService.updateEmployeeSalary("Terisa", 400000.00);
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa");
        assertTrue(result);
    }

    @Test
    public void givenDateRange_WhenRetrieved_ShouldMatchEmployeeCount() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
        String startDate = "2018-01-01";
        String endDate = "2019-12-01";
        List<EmployeePayrollData> employeesListInDateRange = employeePayrollService.getEmployeesFromDateRange(startDate,endDate);
        assertEquals(2, employeesListInDateRange.size());
    }
}