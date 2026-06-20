package com.mycompany.perpusdigital.dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.perpusdigital.config.Koneksi;
import com.mycompany.perpusdigital.model.Peminjaman;

public class PeminjamanDAO {

    // 1. READ: Ambil semua riwayat transaksi beserta nama peminjam & judul buku
    public List<Peminjaman> getSemuaPeminjaman() {
        List<Peminjaman> list = new ArrayList<>();
        String sql = "SELECT p.id_pinjam, p.id_user_anggota, u.nama AS nama_peminjam, " +
                     "p.id_buku, b.judul AS judul_buku, p.tanggal_pinjam, p.tanggal_kembali, p.status_pinjam " +
                     "FROM peminjaman p " +
                     "JOIN user u ON p.id_user_anggota = u.id_user " +
                     "JOIN buku b ON p.id_buku = b.id_buku " +
                     "ORDER BY p.tanggal_pinjam DESC";

        try (Connection conn = Koneksi.configDB();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Peminjaman(
                    rs.getString("id_pinjam"),
                    rs.getString("id_user_anggota"),
                    rs.getString("nama_peminjam"),
                    rs.getString("id_buku"),
                    rs.getString("judul_buku"),
                    rs.getDate("tanggal_pinjam"),
                    rs.getDate("tanggal_kembali"),
                    rs.getString("status_pinjam")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error getSemuaPeminjaman: " + e.getMessage());
        }
        return list;
    }

    // 2. CREATE: Catat Pinjam (Insert ke peminjaman + Update buku jadi 'Dipinjam')
    public boolean catatPeminjaman(String idAnggota, String idBuku) {
        Connection conn = null;
        try {
            conn = Koneksi.configDB();
            conn.setAutoCommit(false); // Mulai SQL Transaction

            // Generate ID Pinjam (PJM + 4 digit acak dari waktu)
            String idPinjam = "PJM" + (System.currentTimeMillis() % 10000);
            
            // Tanggal Pinjam hari ini, Tanggal Kembali default +7 Hari
            LocalDate tglPinjam = LocalDate.now();
            LocalDate tglKembali = tglPinjam.plusDays(7);

            // Langkah 1: Masukkan ke tabel peminjaman
            String sql1 = "INSERT INTO peminjaman (id_pinjam, id_user_anggota, id_buku, tanggal_pinjam, tanggal_kembali, status_pinjam) VALUES (?, ?, ?, ?, ?, 'Dipinjam')";
            PreparedStatement ps1 = conn.prepareStatement(sql1);
            ps1.setString(1, idPinjam);
            ps1.setString(2, idAnggota);
            ps1.setString(3, idBuku);
            ps1.setDate(4, Date.valueOf(tglPinjam));
            ps1.setDate(5, Date.valueOf(tglKembali));
            ps1.executeUpdate();

            // Langkah 2: Kunci buku jadi 'Dipinjam'
            String sql2 = "UPDATE buku SET status = 'Dipinjam' WHERE id_buku = ?";
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            ps2.setString(1, idBuku);
            ps2.executeUpdate();

            conn.commit();
            return true;

        } catch (SQLException e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
            System.err.println("Error catatPeminjaman: " + e.getMessage());
            return false;
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException ex) {}
        }
    }

    // 3. UPDATE: Proses Pengembalian (Ubah status transaksi jadi 'Dikembalikan' + Buku jadi 'Tersedia')
    public boolean kembalikanBuku(String idPinjam, String idBuku) {
        Connection conn = null;
        try {
            conn = Koneksi.configDB();
            conn.setAutoCommit(false);

            // Langkah 1: Tandai peminjaman selesai (tanggal kembali dicatat hari saat dia ngembaliin)
            String sql1 = "UPDATE peminjaman SET status_pinjam = 'Dikembalikan', tanggal_kembali = ? WHERE id_pinjam = ?";
            PreparedStatement ps1 = conn.prepareStatement(sql1);
            ps1.setDate(1, Date.valueOf(LocalDate.now()));
            ps1.setString(2, idPinjam);
            ps1.executeUpdate();

            // Langkah 2: Lepas buku kembali jadi 'Tersedia'
            String sql2 = "UPDATE buku SET status = 'Tersedia' WHERE id_buku = ?";
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            ps2.setString(1, idBuku);
            ps2.executeUpdate();

            conn.commit();
            return true;

        } catch (SQLException e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
            System.err.println("Error kembalikanBuku: " + e.getMessage());
            return false;
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException ex) {}
        }
    }
    // 4. READ KHUSUS ANGGOTA: Ambil riwayat milik 1 user spesifik
    public List<Peminjaman> getPeminjamanByAnggota(String idUserAnggota) {
        List<Peminjaman> list = new ArrayList<>();
        String sql = "SELECT p.id_pinjam, p.id_user_anggota, u.nama AS nama_peminjam, " +
                     "p.id_buku, b.judul AS judul_buku, p.tanggal_pinjam, p.tanggal_kembali, p.status_pinjam " +
                     "FROM peminjaman p " +
                     "JOIN user u ON p.id_user_anggota = u.id_user " +
                     "JOIN buku b ON p.id_buku = b.id_buku " +
                     "WHERE p.id_user_anggota = ? " +
                     "ORDER BY p.tanggal_pinjam DESC";

        try (Connection conn = Koneksi.configDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idUserAnggota);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Peminjaman(
                    rs.getString("id_pinjam"), rs.getString("id_user_anggota"), rs.getString("nama_peminjam"),
                    rs.getString("id_buku"), rs.getString("judul_buku"),
                    rs.getDate("tanggal_pinjam"), rs.getDate("tanggal_kembali"), rs.getString("status_pinjam")
                ));
            }
        } catch (SQLException e) { System.err.println("Error getPeminjamanByAnggota: " + e.getMessage()); }
        return list;
    }
}