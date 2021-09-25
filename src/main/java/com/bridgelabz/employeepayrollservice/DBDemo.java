package com.bridgelabz.employeepayrollservice;

import java.sql.Connection;

public class DBDemo {

    public static void main(String[] args) {

        String jdbcURL="jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
        String userName="rajat";
        String password="Mysql@3q#";
        Connection connection;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded!");
        }catch(ClassNotFoundException e) {
            throw new IllegalStateException("lol",e);
        }
    }

}