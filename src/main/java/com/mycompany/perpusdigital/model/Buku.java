package com.mycompany.perpusdigital.model;

public class Buku {
    private String idBuku;
    private String judul;
    private String penulis;
    private String kategori;
    private int tahunTerbit;
    private String status;

    public Buku(String idBuku, String judul, String penulis, String kategori, int tahunTerbit, String status) {
        this.idBuku = idBuku;
        this.judul = judul;
        this.penulis = penulis;
        this.kategori = kategori;
        this.tahunTerbit = tahunTerbit;
        this.status = status;
    }

    public String getIdBuku() { return idBuku; }
    public String getJudul() { return judul; }
    public String getPenulis() { return penulis; }
    public String getKategori() { return kategori; }
    public int getTahunTerbit() { return tahunTerbit; }
    public String getStatus() { return status; }
}