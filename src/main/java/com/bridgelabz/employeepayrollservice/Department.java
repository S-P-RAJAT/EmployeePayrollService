package com.bridgelabz.employeepayrollservice;

import java.util.List;

public class Department {
    private int departmentId;
    private String departmentName;
    private List<Employee> employeeList;

    public Department(int departmentId, String departmentName, List<Employee> employeeList)
    {

        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.employeeList = employeeList;
    }
    public int getDepartmentId()
    {
        return departmentId;
    }
    public void setDepartmentId(int departmentId)
    {
        this.departmentId = departmentId;
    }
    public String getDepartmentName()
    {
        return departmentName;
    }
    public void setDepartmentName(String departmentName)
    {
        this.departmentName = departmentName;
    }
    public List<Employee> getEmployeeList()
    {
        return employeeList;
    }
    public void setEmployeeList(List<Employee> employeeList)
    {
        this.employeeList = employeeList;
    }
}
