package com.mycompany.perpusdigital.model;

import java.util.ArrayList;
import java.util.List;
import com.mycompany.perpusdigital.dao.AnggotaDAO;
import com.mycompany.perpusdigital.dao.BukuDAO;
import com.mycompany.perpusdigital.dao.PeminjamanDAO;

public class Perpustakaan {

    private List<Buku> daftarBuku;
    private List<Anggota> daftarAnggota;
    private List<Peminjaman> daftarPeminjaman;

    private BukuDAO bukuDAO;
    private AnggotaDAO anggotaDAO;
    private PeminjamanDAO peminjamanDAO;

    public Perpustakaan() {
        this.daftarBuku = new ArrayList<>();
        this.daftarAnggota = new ArrayList<>();
        this.daftarPeminjaman = new ArrayList<>();

        this.bukuDAO = new BukuDAO();
        this.anggotaDAO = new AnggotaDAO();
        this.peminjamanDAO = new PeminjamanDAO();
    }

    public List<Buku> tampilkanBuku() {
        this.daftarBuku = bukuDAO.getSemuaBuku();
        return this.daftarBuku;
    }

    public List<Buku> cariBuku(String keyword) {
        List<Buku> hasilPencarian = new ArrayList<>();
        
        tampilkanBuku();

        if (keyword == null || keyword.trim().isEmpty()) {
            return this.daftarBuku; 
        }

        String kw = keyword.toLowerCase().trim();
        for (Buku b : this.daftarBuku) {
           
            if (b.getJudul().toLowerCase().contains(kw) || 
                b.getPenulis().toLowerCase().contains(kw) || 
                b.getKategori().toLowerCase().contains(kw)) {
                
                hasilPencarian.add(b);
            }
        }
        return hasilPencarian;
    }

    public List<Anggota> getDaftarAnggota() {
        this.daftarAnggota = anggotaDAO.getSemuaAnggota();
        return this.daftarAnggota;
    }

    public List<Peminjaman> getDaftarPeminjaman() {
        this.daftarPeminjaman = peminjamanDAO.getSemuaPeminjaman();
        return this.daftarPeminjaman;
    }
}