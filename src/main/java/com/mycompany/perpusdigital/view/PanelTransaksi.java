package com.mycompany.perpusdigital.view;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.mycompany.perpusdigital.dao.AnggotaDAO;
import com.mycompany.perpusdigital.dao.BukuDAO;
import com.mycompany.perpusdigital.dao.PeminjamanDAO;
import com.mycompany.perpusdigital.model.Anggota;
import com.mycompany.perpusdigital.model.Buku;
import com.mycompany.perpusdigital.model.Peminjaman;

public class PanelTransaksi extends JPanel {

    private JComboBox<String> cmbAnggota;
    private JComboBox<String> cmbBuku;
    private JTable tabelTransaksi;
    private DefaultTableModel tableModel;
    
    private PeminjamanDAO peminjamanDAO;
    private AnggotaDAO anggotaDAO;
    private BukuDAO bukuDAO;

    public PanelTransaksi() {
        peminjamanDAO = new PeminjamanDAO();
        anggotaDAO = new AnggotaDAO();
        bukuDAO = new BukuDAO();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 1. BAGIAN ATAS: Form Input Transaksi
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Catat Peminjaman Baru (Maks 7 Hari)"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cmbAnggota = new JComboBox<>();
        cmbBuku = new JComboBox<>();
        JButton btnPinjam = new JButton("Proses Pinjam Buku");
        btnPinjam.setBackground(new Color(42, 157, 143));
        btnPinjam.setForeground(Color.WHITE);
        btnPinjam.setFont(new Font("Segoe UI", Font.BOLD, 12));

        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("Pilih Peminjam:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; formPanel.add(cmbAnggota, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; formPanel.add(new JLabel("Pilih Buku:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0; formPanel.add(cmbBuku, gbc);

        gbc.gridx = 1; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(btnPinjam, gbc);

        add(formPanel, BorderLayout.NORTH);

        // 2. BAGIAN TENGAH: Tabel Riwayat
        String[] kolom = {"ID Pinjam", "Peminjam", "ID Buku", "Judul Buku", "Tgl Pinjam", "Jatuh Tempo", "Status"};
        tableModel = new DefaultTableModel(kolom, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabelTransaksi = new JTable(tableModel);
        tabelTransaksi.setRowHeight(25);
        add(new JScrollPane(tabelTransaksi), BorderLayout.CENTER);

        // 3. BAGIAN BAWAH: Tombol Aksi Pengembalian
        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnKembali = new JButton("Konfirmasi Buku Dikembalikan");
        JButton btnRefresh = new JButton("Refresh Data");
        JButton btnCetakLaporan = new JButton("Cetak Laporan Transaksi (PDF)");

        btnKembali.setBackground(new Color(229, 56, 59));
        btnKembali.setForeground(Color.WHITE);
        btnKembali.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCetakLaporan.setBackground(new Color(67, 97, 238));
        btnCetakLaporan.setForeground(Color.WHITE); // Teksnya jadi putih
        btnCetakLaporan.setFont(new Font("Segoe UI", Font.BOLD, 12));
        

        panelTombol.add(btnKembali);
        panelTombol.add(btnRefresh);
        panelTombol.add(btnCetakLaporan);
        add(panelTombol, BorderLayout.SOUTH);

        // --- EVENT LISTENERS ---

        // Tombol Proses Pinjam
        btnPinjam.addActionListener(e -> {
            if (cmbAnggota.getItemCount() == 0 || cmbBuku.getItemCount() == 0) {
                JOptionPane.showMessageDialog(this, "Data Anggota atau Buku Tersedia masih kosong!");
                return;
            }

            // Trik membelah string dropdown "ID - Nama" untuk mengambil ID-nya saja
            String idAnggota = cmbAnggota.getSelectedItem().toString().split(" - ")[0];
            String idBuku = cmbBuku.getSelectedItem().toString().split(" - ")[0];

            if (peminjamanDAO.catatPeminjaman(idAnggota, idBuku)) {
                JOptionPane.showMessageDialog(this, "Peminjaman Berhasil Dicatat!");
                refreshSemuaUI();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal mencatat peminjaman!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Tombol Buku Dikembalikan
        btnKembali.addActionListener(e -> {
            int baris = tabelTransaksi.getSelectedRow();
            if (baris < 0) {
                JOptionPane.showMessageDialog(this, "Klik dulu satu transaksi di tabel yang mau dikembalikan!");
                return;
            }

            String idPinjam = tableModel.getValueAt(baris, 0).toString();
            String peminjam = tableModel.getValueAt(baris, 1).toString();
            String idBuku = tableModel.getValueAt(baris, 2).toString();
            String judul = tableModel.getValueAt(baris, 3).toString();
            String status = tableModel.getValueAt(baris, 6).toString();

            if ("Dikembalikan".equals(status)) {
                JOptionPane.showMessageDialog(this, "Buku ini kan sudah dikembalikan sebelumnya bro!", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if (JOptionPane.showConfirmDialog(this, "Konfirmasi pengembalian buku '" + judul + "' oleh " + peminjam + "?", "Kembalikan", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                if (peminjamanDAO.kembalikanBuku(idPinjam, idBuku)) {
                    JOptionPane.showMessageDialog(this, "Buku berhasil dikembalikan ke rak perpustakaan.");
                    refreshSemuaUI();
                }
            }
        });
        btnCetakLaporan.addActionListener(e -> {
            new com.mycompany.perpusdigital.service.ReportService().cetakLaporanAdmin();
        });

        btnRefresh.addActionListener(e -> refreshSemuaUI());

        refreshSemuaUI();
    }

    // Method untuk menyegarkan Dropdown dan Tabel sekaligus
    private void refreshSemuaUI() {
        // 1. Refresh Dropdown Anggota
        cmbAnggota.removeAllItems();
        for (Anggota a : anggotaDAO.getSemuaAnggota()) {
            cmbAnggota.addItem(a.getIdUser() + " - " + a.getNama());
        }

        // 2. Refresh Dropdown Buku (HANYA AMBIL YANG STATUSNYA 'Tersedia')
        cmbBuku.removeAllItems();
        for (Buku b : bukuDAO.getSemuaBuku()) {
            if ("Tersedia".equals(b.getStatus())) {
                cmbBuku.addItem(b.getIdBuku() + " - " + b.getJudul());
            }
        }

        // 3. Refresh Tabel Riwayat
        tableModel.setRowCount(0);
        for (Peminjaman p : peminjamanDAO.getSemuaPeminjaman()) {
            tableModel.addRow(new Object[]{
                p.getIdPinjam(), p.getNamaAnggota(), p.getIdBuku(), 
                p.getJudulBuku(), p.getTanggalPinjam(), p.getTanggalKembali(), p.getStatusPinjam()
            });
        }
    }
}