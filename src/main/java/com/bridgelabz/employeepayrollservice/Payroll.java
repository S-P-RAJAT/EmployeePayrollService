package com.bridgelabz.employeepayrollservice;

public class Payroll {
    private double basicPay;
    private double deductions;
    private double taxablePay;
    private double tax;
    private double netPay;

    public Payroll(double basicPay, double deductions, double taxablePay, double tax, double netPay)
    {
        this.basicPay = basicPay;
        this.deductions = deductions;
        this.taxablePay = taxablePay;
        this.tax = tax;
        this.netPay = netPay;
    }
    public double getBasicPay()
    {
        return basicPay;
    }
    public void setBasicPay(double basicPay)
    {
        this.basicPay = basicPay;
    }
    public double getDeductions()
    {
        return deductions;
    }
    public void setDeductions(double deductions)
    {
        this.deductions = deductions;
    }
    public double getTaxablePay()
    {
        return taxablePay;
    }
    public void setTaxablePay(double taxablePay)
    {
        this.taxablePay = taxablePay;
    }
    public double getTax()
    {
        return tax;
    }
    public void setTax(double tax)
    {
        this.tax = tax;
    }
    public double getNetPay()
    {
        return netPay;
    }
    public void setNetPay(double netPay)
    {
        this.netPay = netPay;
    }
}
