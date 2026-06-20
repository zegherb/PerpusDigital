package com.mycompany.perpusdigital.model;

import java.sql.Date;

public class Peminjaman {
    private String idPinjam;
    private String idUserAnggota;
    private String namaAnggota; // Tambahan untuk UI
    private String idBuku;
    private String judulBuku;   // Tambahan untuk UI
    private Date tanggalPinjam;
    private Date tanggalKembali;
    private String statusPinjam;

    public Peminjaman(String idPinjam, String idUserAnggota, String namaAnggota, String idBuku, String judulBuku, Date tanggalPinjam, Date tanggalKembali, String statusPinjam) {
        this.idPinjam = idPinjam;
        this.idUserAnggota = idUserAnggota;
        this.namaAnggota = namaAnggota;
        this.idBuku = idBuku;
        this.judulBuku = judulBuku;
        this.tanggalPinjam = tanggalPinjam;
        this.tanggalKembali = tanggalKembali;
        this.statusPinjam = statusPinjam;
    }

    public String getIdPinjam() { return idPinjam; }
    public String getIdUserAnggota() { return idUserAnggota; }
    public String getNamaAnggota() { return namaAnggota; }
    public String getIdBuku() { return idBuku; }
    public String getJudulBuku() { return judulBuku; }
    public Date getTanggalPinjam() { return tanggalPinjam; }
    public Date getTanggalKembali() { return tanggalKembali; }
    public String getStatusPinjam() { return statusPinjam; }
}