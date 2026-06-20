package com.mycompany.perpusdigital.view;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.mycompany.perpusdigital.dao.AnggotaDAO;
import com.mycompany.perpusdigital.model.Anggota;

public class PanelAnggota extends JPanel {

    private JTable tabelAnggota;
    private DefaultTableModel tableModel;
    private AnggotaDAO anggotaDAO;

    public PanelAnggota() {
        anggotaDAO = new AnggotaDAO();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblJudul = new JLabel("Manajemen Anggota Terdaftar");
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(lblJudul, BorderLayout.NORTH);

        String[] kolom = {"ID User", "No. Anggota", "Nama Lengkap", "Username", "Alamat", "No. Telepon"};
        tableModel = new DefaultTableModel(kolom, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; } 
        };
        tabelAnggota = new JTable(tableModel);
        tabelAnggota.setRowHeight(25); 
        add(new JScrollPane(tabelAnggota), BorderLayout.CENTER);

        // --- DERETAN TOMBOL ---
        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnPromosi = new JButton("Promosikan Jadi Admin");
        JButton btnHapus = new JButton("Hapus Anggota");
        JButton btnRefresh = new JButton("Muat Ulang Data");

        // Warna Teal/Emerald Green untuk tombol Promosi
        btnPromosi.setBackground(new Color(42, 157, 143));
        btnPromosi.setForeground(Color.WHITE);
        btnPromosi.setFont(new Font("Segoe UI", Font.BOLD, 12));

        // Warna Merah untuk tombol Hapus
        btnHapus.setBackground(new Color(229, 56, 59));
        btnHapus.setForeground(Color.WHITE);
        btnHapus.setFont(new Font("Segoe UI", Font.BOLD, 12));

        panelTombol.add(btnPromosi);
        panelTombol.add(btnHapus);
        panelTombol.add(btnRefresh);
        add(panelTombol, BorderLayout.SOUTH);

        // --- EVENT LISTENER TOMBOL PROMOSI ---
        btnPromosi.addActionListener(e -> {
            int baris = tabelAnggota.getSelectedRow();
            if (baris < 0) {
                JOptionPane.showMessageDialog(this, "Pilih dulu satu anggota di tabel yang mau diangkat jadi Admin!", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String idUser = tableModel.getValueAt(baris, 0).toString();
            String nama = tableModel.getValueAt(baris, 2).toString();

            // Munculkan kotak input teks untuk mengetik jabatan
            String inputJabatan = JOptionPane.showInputDialog(this, 
                "Masukkan Jabatan baru untuk '" + nama + "':\n(Contoh: Petugas Sirkulasi, Pustakawan, Staf IT)", 
                "Promosi Anggota ke Admin", JOptionPane.QUESTION_MESSAGE);

            // Cek apakah user mengetik sesuatu (tidak pencet cancel)
            if (inputJabatan != null && !inputJabatan.trim().isEmpty()) {
                String jabatanBersih = inputJabatan.trim();
                
                int konfirm = JOptionPane.showConfirmDialog(this, 
                    "Yakin angkat '" + nama + "' menjadi Admin dengan jabatan: " + jabatanBersih + "?", 
                    "Konfirmasi", JOptionPane.YES_NO_OPTION);

                if (konfirm == JOptionPane.YES_OPTION) {
                    if (anggotaDAO.jadikanAdmin(idUser, jabatanBersih)) {
                        JOptionPane.showMessageDialog(this, "SELAMAT! '" + nama + "' resmi menjadi Admin (" + jabatanBersih + ").");
                        loadDataAnggota(); // Refresh tabel
                    } else {
                        JOptionPane.showMessageDialog(this, "Gagal melakukan promosi jabatan!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Event Hapus & Refresh
        btnHapus.addActionListener(e -> hapusAnggotaTerpilih());
        btnRefresh.addActionListener(e -> loadDataAnggota());

        loadDataAnggota();
    }

    private void hapusAnggotaTerpilih() {
        int baris = tabelAnggota.getSelectedRow();
        if (baris < 0) return;
        String idUser = tableModel.getValueAt(baris, 0).toString();
        String nama = tableModel.getValueAt(baris, 2).toString();

        if (JOptionPane.showConfirmDialog(this, "Hapus anggota '" + nama + "'?", "Hapus", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (anggotaDAO.hapusAnggota(idUser)) loadDataAnggota();
        }
    }

    private void loadDataAnggota() {
        tableModel.setRowCount(0);
        List<Anggota> list = anggotaDAO.getSemuaAnggota();
        for (Anggota a : list) {
            tableModel.addRow(new Object[]{ a.getIdUser(), a.getNoAnggota(), a.getNama(), a.getUsername(), a.getAlamat(), a.getNoTelepon() });
        }
    }
}