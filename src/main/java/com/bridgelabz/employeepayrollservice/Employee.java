package com.bridgelabz.employeepayrollservice;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Employee {
    private int employeeId;
    private String employeeName;
    private double salary;
    private String gender;
    private String address;
    private long phoneNumber;
    private LocalDate startDate;
    private Company company;
    private Payroll payroll;
    private List<Department> departments;

    public Employee(int employeeId, String employeeName,double salary)
    {

        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.salary = salary;

    }


    public Employee(int employeeId, String employeeName, double salary,LocalDate startDate)
    {
        this(employeeId, employeeName, salary);
        this.startDate = startDate;
    }


    public Employee(int employeeId, String employeeName, String gender,String address, long phoneNumber, double salary, LocalDate startDate,
                    Company company, Payroll payroll, List<Department> departments)
    {

        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.gender = gender;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.salary = salary;
        this.startDate = startDate;
        this.company = company;
        this.payroll = payroll;
        this.departments = departments;
    }


    public int getEmployeeId()
    {
        return employeeId;
    }

    public String getEmployeeName()
    {
        return employeeName;
    }

    public void setEmployeeName(String employeeName)
    {
        this.employeeName = employeeName;
    }

    public String getGender()
    {
        return gender;
    }

    public void setGender(String gender)
    {
        this.gender = gender;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public double getSalary()
    {
        return salary;
    }

    public void setSalary(double salary)
    {
        this.salary = salary;
    }

    public LocalDate getStartDate()
    {
        return startDate;
    }

    public void setStartDate(LocalDate startDate)
    {
        this.startDate = startDate;
    }


    public Payroll getPayroll()
    {
        return payroll;
    }


    public void setPayroll(Payroll payroll)
    {
        this.payroll = payroll;
    }


    public void setEmployeeId(int employeeId)
    {
        this.employeeId = employeeId;
    }

    public List<Department> getDepartments()
    {
        return departments;
    }

    public void setDepartments(List<Department> departments)
    {
        this.departments = departments;
    }


    public Company getCompany()
    {
        return company;
    }


    public void setCompany(Company company)
    {
        this.company = company;
    }



    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Employee other = (Employee) obj;
        return Objects.equals(company, other.company) && Objects.equals(departments, other.departments)
                && employeeId == other.employeeId && Objects.equals(employeeName, other.employeeName);
    }


    @Override
    public String toString() {
        return "id=" + employeeId + ", name=" + employeeName + ", salary=" + salary;
    }

}
