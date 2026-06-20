package com.mycompany.perpusdigital.model;

public abstract class User {
    protected String idUser;
    protected String nama;
    protected String username;
    protected String password;
    protected String tipeUser;

    public User(String idUser, String nama, String username, String password, String tipeUser) {
        this.idUser = idUser;
        this.nama = nama;
        this.username = username;
        this.password = password;
        this.tipeUser = tipeUser;
    }

    // --- GETTER LENGKAP ---
    public String getIdUser() { return idUser; }
    public String getNama() { return nama; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getTipeUser() { return tipeUser; }
    
    public abstract void login();
    public abstract void logout();
}