package com.mycompany.perpusdigital.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.perpusdigital.config.Koneksi;
import com.mycompany.perpusdigital.model.Anggota;

public class AnggotaDAO {

    // 1. READ: Tarik data gabungan dari tabel user dan anggota
    public List<Anggota> getSemuaAnggota() {
        List<Anggota> list = new ArrayList<>();
        String sql = "SELECT u.id_user, u.nama, u.username, a.no_anggota, a.alamat, a.no_telepon " +
                     "FROM user u JOIN anggota a ON u.id_user = a.id_user " +
                     "WHERE u.tipe_user = 'Anggota'";

        try (Connection conn = Koneksi.configDB();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Anggota(
                    rs.getString("id_user"),
                    rs.getString("nama"),
                    rs.getString("username"),
                    "", // Password dikosongkan demi keamanan memori
                    "Anggota",
                    rs.getString("no_anggota"),
                    rs.getString("alamat"),
                    rs.getString("no_telepon")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error getSemuaAnggota: " + e.getMessage());
        }
        return list;
    }

    // 2. DELETE: Cukup hapus dari tabel induk (user), tabel anak otomatis terhapus berkat Cascade
    public boolean hapusAnggota(String idUser) {
        String sql = "DELETE FROM user WHERE id_user = ? AND tipe_user = 'Anggota'";
        try (Connection conn = Koneksi.configDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idUser);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error hapusAnggota: " + e.getMessage());
            return false;
        }
    }
    // 3. PROMOTE: Memindahkan user dari kasta Anggota menjadi kasta Admin
    public boolean jadikanAdmin(String idUser, String jabatan) {
        Connection conn = null;
        try {
            conn = Koneksi.configDB();
            conn.setAutoCommit(false); // Mulai SQL Transaction

            // Jurus 1: Naikkan pangkatnya di tabel Induk
            String sql1 = "UPDATE user SET tipe_user = 'Admin' WHERE id_user = ?";
            PreparedStatement ps1 = conn.prepareStatement(sql1);
            ps1.setString(1, idUser);
            ps1.executeUpdate();

            // Jurus 2: Cabut datanya dari tabel Anggota
            String sql2 = "DELETE FROM anggota WHERE id_user = ?";
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            ps2.setString(1, idUser);
            ps2.executeUpdate();

            // Jurus 3: Daftarkan dia ke tabel Admin beserta jabatannya
            String sql3 = "INSERT INTO admin (id_user, jabatan) VALUES (?, ?)";
            PreparedStatement ps3 = conn.prepareStatement(sql3);
            ps3.setString(1, idUser);
            ps3.setString(2, jabatan);
            ps3.executeUpdate();

            // Jika ketiga jurus sukses, kunci permanen!
            conn.commit();
            return true;

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback(); // Batalkan semua kalau ada 1 yang gagal
            } catch (SQLException ex) { ex.printStackTrace(); }
            System.err.println("Error jadikanAdmin: " + e.getMessage());
            return false;
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException ex) { ex.printStackTrace(); }
        }
    }
}