package com.bridgelabz.employeepayrollservice;

import java.sql.SQLException;

public class EmployeePayrollException extends SQLException {

    enum ExceptionType {
        UNKNOWN_DATABASE, SQL_EXCEPTION,INVALID_TABLE,UNSUCCESFUL_UPDATE;
    }

    ExceptionType type;

    public EmployeePayrollException(ExceptionType type, String message) {
        super(message);
        this.type = type;
    }
}