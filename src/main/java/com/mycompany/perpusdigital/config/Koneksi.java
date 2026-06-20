/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.perpusdigital.config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author aldyansyahombi
 */
public class Koneksi {
    private static Connection mysqlconfig;

    public static Connection configDB() {
        try {
            // // URL Database, ditambahkan parameter timezone agar sinkron dengan MySQL 8
            // String url = "jdbc:mysql://localhost:3306/perpustakaan_db?serverTimezone=Asia/Makassar"; 
            // String user = "root"; // Sesuaikan jika user MySQL lu berbeda
            // String pass = ""; // Isi password jika root MySQL lu menggunakan password

            String url = "jdbc:mysql://ui7pyx3cjuxf8cfm:h43GgpMfuPDJWLYut4xQ@bc9q9qfrymvvc7ewlani-mysql.services.clever-cloud.com:3306/bc9q9qfrymvvc7ewlani"; 
            String user = "ui7pyx3cjuxf8cfm"; // Sesuaikan jika user MySQL lu berbeda
            String pass = "h43GgpMfuPDJWLYut4xQ"; // Isi password jika root MySQL lu menggunakan password

            // Memanggil Driver MySQL
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            
            mysqlconfig = DriverManager.getConnection(url, user, pass);
            System.out.println("Koneksi Database Berhasil!");
        } catch (SQLException e) {
            System.err.println("Koneksi Database Gagal: " + e.getMessage());
        }
        return mysqlconfig;
    }

    // Method main ini kita buat sementara murni untuk ngetes koneksi
    public static void main(String[] args) {
        configDB();
    }
}
    

