package com.mycompany.perpusdigital.model;

public class Anggota extends User {
    private String noAnggota;
    private String alamat;
    private String noTelepon;

    public Anggota(String idUser, String nama, String username, String password, String tipeUser, String noAnggota, String alamat, String noTelepon) {
        super(idUser, nama, username, password, tipeUser);
        this.noAnggota = noAnggota;
        this.alamat = alamat;
        this.noTelepon = noTelepon;
    }

    // --- GETTER KHUSUS ANGGOTA ---
    public String getNoAnggota() { return noAnggota; }
    public String getAlamat() { return alamat; }
    public String getNoTelepon() { return noTelepon; }

    @Override
    public void login() {
        System.out.println("Anggota " + this.nama + " berhasil login.");
    }

    @Override
    public void logout() {
        System.out.println("Anggota logout.");
    }
}