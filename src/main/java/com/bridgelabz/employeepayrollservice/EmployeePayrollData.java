package com.bridgelabz.employeepayrollservice;

public class EmployeePayrollData {
    public int id;
    public String name;
    public double salary;

    public EmployeePayrollData(int id, String name, double salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "id=" + id + ", name=" + name + ", salary=" + salary;
    }

}