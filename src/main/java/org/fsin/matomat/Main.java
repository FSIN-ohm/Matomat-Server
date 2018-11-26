package org.fsin.matomat;


import java.sql.Connection;
import java.sql.DriverManager;

public class Main {
    public static void main(String[] argv) {
        System.out.println("hallo welt");
        try {
            Connection connect = DriverManager
                    .getConnection("jdbc:mysql://127.0.0.1/matohmat", "root", "root");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
