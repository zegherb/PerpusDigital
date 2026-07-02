package com.mycompany.perpusdigital.model;

public class Admin extends User {
    private String jabatan;

    public Admin(String idUser, String nama, String username, String password, String tipeUser, String jabatan) {
        super(idUser, nama, username, password, tipeUser);
        this.jabatan = jabatan;
    }

    
    public String getJabatan() { return jabatan; }

    @Override
    public void login() {
        System.out.println("Admin " + this.nama + " berhasil login.");
    }

    @Override
    public void logout() {
        System.out.println("Admin logout.");
    }
}