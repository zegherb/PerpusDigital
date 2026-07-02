package com.mycompany.perpusdigital.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.perpusdigital.config.Koneksi;
import com.mycompany.perpusdigital.model.Buku;

public class BukuDAO {

    public List<Buku> getSemuaBuku() {
        List<Buku> listBuku = new ArrayList<>();
        String sql = "SELECT * FROM buku";
        
        try (Connection conn = Koneksi.configDB();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                listBuku.add(new Buku(
                    rs.getString("id_buku"),
                    rs.getString("judul"),
                    rs.getString("penulis"),
                    rs.getString("kategori"),
                    rs.getInt("tahun_terbit"),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error getSemuaBuku: " + e.getMessage());
        }
        return listBuku;
    }

    public boolean tambahBuku(Buku b) {
        String sql = "INSERT INTO buku (id_buku, judul, penulis, kategori, tahun_terbit, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = Koneksi.configDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, b.getIdBuku());
            ps.setString(2, b.getJudul());
            ps.setString(3, b.getPenulis());
            ps.setString(4, b.getKategori());
            ps.setInt(5, b.getTahunTerbit());
            ps.setString(6, b.getStatus());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error tambahBuku: " + e.getMessage());
            return false;
        }
    }

    public boolean ubahBuku(Buku b) {
        String sql = "UPDATE buku SET judul=?, penulis=?, kategori=?, tahun_terbit=?, status=? WHERE id_buku=?";
        try (Connection conn = Koneksi.configDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, b.getJudul());
            ps.setString(2, b.getPenulis());
            ps.setString(3, b.getKategori());
            ps.setInt(4, b.getTahunTerbit());
            ps.setString(5, b.getStatus());
            ps.setString(6, b.getIdBuku());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error ubahBuku: " + e.getMessage());
            return false;
        }
    }

    public boolean hapusBuku(String idBuku) {
        String sql = "DELETE FROM buku WHERE id_buku=?";
        try (Connection conn = Koneksi.configDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, idBuku);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error hapusBuku: " + e.getMessage());
            return false;
        }
    }
}