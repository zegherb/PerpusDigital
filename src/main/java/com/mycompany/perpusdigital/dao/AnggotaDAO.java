package com.mycompany.perpusdigital.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.perpusdigital.config.Koneksi;
import com.mycompany.perpusdigital.model.Anggota;

public class AnggotaDAO {

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
                    "", 
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

    public boolean jadikanAdmin(String idUser, String jabatan) {
        Connection conn = null;
        try {
            conn = Koneksi.configDB();
            conn.setAutoCommit(false); 

            String sql1 = "UPDATE user SET tipe_user = 'Admin' WHERE id_user = ?";
            PreparedStatement ps1 = conn.prepareStatement(sql1);
            ps1.setString(1, idUser);
            ps1.executeUpdate();

            String sql2 = "DELETE FROM anggota WHERE id_user = ?";
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            ps2.setString(1, idUser);
            ps2.executeUpdate();

            String sql3 = "INSERT INTO admin (id_user, jabatan) VALUES (?, ?)";
            PreparedStatement ps3 = conn.prepareStatement(sql3);
            ps3.setString(1, idUser);
            ps3.setString(2, jabatan);
            ps3.executeUpdate();

            conn.commit();
            return true;

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback(); 
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