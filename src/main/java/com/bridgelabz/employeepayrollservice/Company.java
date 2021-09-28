package com.bridgelabz.employeepayrollservice;

import java.util.List;

public class Company {
    private int companyId;
    private String companyName;
    private List<Employee> employeeList;

    public Company(int companyId, String companyName, List<Employee> employeeList) {
        super();
        this.companyId = companyId;
        this.companyName = companyName;
        this.employeeList = employeeList;
    }
    public int getCompanyId()
    {
        return companyId;
    }
    public void setCompanyId(int companyId)
    {
        this.companyId = companyId;
    }
    public String getCompanyName()
    {
        return companyName;
    }
    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
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
