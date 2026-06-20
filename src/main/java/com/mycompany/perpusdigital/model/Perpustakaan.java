package com.mycompany.perpusdigital.model;

import java.util.ArrayList;
import java.util.List;
import com.mycompany.perpusdigital.dao.AnggotaDAO;
import com.mycompany.perpusdigital.dao.BukuDAO;
import com.mycompany.perpusdigital.dao.PeminjamanDAO;

public class Perpustakaan {

    // 1. ATRIBUT AGREGASI (Sesuai Class Diagram)
    private List<Buku> daftarBuku;
    private List<Anggota> daftarAnggota;
    private List<Peminjaman> daftarPeminjaman;

    // Jembatan ke dunia nyata (Database MySQL)
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

    // --- METHOD 1 DARI CLASS DIAGRAM: tampilkanBuku() ---
    // Mengambil dari DB, menyimpannya di memori agregasi, lalu dikirim ke GUI
    public List<Buku> tampilkanBuku() {
        this.daftarBuku = bukuDAO.getSemuaBuku();
        return this.daftarBuku;
    }

    // --- METHOD 2 DARI CLASS DIAGRAM: cariBuku() ---
    // Menjawab Kebutuhan Fungsional (h) - Fitur Pencarian Buku
    public List<Buku> cariBuku(String keyword) {
        List<Buku> hasilPencarian = new ArrayList<>();
        
        // Pastikan memori lokal ter-update dari database terlebih dulu
        tampilkanBuku();

        if (keyword == null || keyword.trim().isEmpty()) {
            return this.daftarBuku; // Kalau kotak pencarian kosong, tampilkan semua
        }

        String kw = keyword.toLowerCase().trim();
        for (Buku b : this.daftarBuku) {
            // Cari berdasarkan Judul ATAU Penulis ATAU Kategori
            if (b.getJudul().toLowerCase().contains(kw) || 
                b.getPenulis().toLowerCase().contains(kw) || 
                b.getKategori().toLowerCase().contains(kw)) {
                
                hasilPencarian.add(b);
            }
        }
        return hasilPencarian;
    }

    // --- METHOD TAMBAHAN AGAR KELAS INI BENAR-BENAR JADI PUSAT AGREGASI ---

    public List<Anggota> getDaftarAnggota() {
        this.daftarAnggota = anggotaDAO.getSemuaAnggota();
        return this.daftarAnggota;
    }

    public List<Peminjaman> getDaftarPeminjaman() {
        this.daftarPeminjaman = peminjamanDAO.getSemuaPeminjaman();
        return this.daftarPeminjaman;
    }
}