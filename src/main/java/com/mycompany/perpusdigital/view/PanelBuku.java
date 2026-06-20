package com.mycompany.perpusdigital.view;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.mycompany.perpusdigital.dao.BukuDAO;
import com.mycompany.perpusdigital.model.Buku;

public class PanelBuku extends JPanel {

    private JTextField txtIdBuku, txtJudul, txtPenulis, txtKategori, txtTahun;
    private JComboBox<String> cmbStatus;
    private JTable tabelBuku;
    private DefaultTableModel tableModel;
    private BukuDAO bukuDAO;

    public PanelBuku() {
        bukuDAO = new BukuDAO();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. BAGIAN ATAS: Form Input (GridBagLayout)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Form Input / Edit Buku"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtIdBuku = new JTextField(15);
        txtJudul = new JTextField(25);
        txtPenulis = new JTextField(20);
        txtKategori = new JTextField(15);
        txtTahun = new JTextField(10);
        cmbStatus = new JComboBox<>(new String[]{"Tersedia", "Dipinjam"});

        addFormField(formPanel, gbc, "ID Buku (Unik):", txtIdBuku, 0);
        addFormField(formPanel, gbc, "Judul Buku:", txtJudul, 1);
        addFormField(formPanel, gbc, "Penulis:", txtPenulis, 2);
        addFormField(formPanel, gbc, "Kategori:", txtKategori, 3);
        addFormField(formPanel, gbc, "Tahun Terbit:", txtTahun, 4);
        addFormField(formPanel, gbc, "Status:", cmbStatus, 5);

        // 2. BAGIAN TENGAH: Deretan Tombol Aksi
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnTambah = new JButton("Tambah");
        JButton btnUbah = new JButton("Ubah");
        JButton btnHapus = new JButton("Hapus");
        JButton btnReset = new JButton("Bersihkan Form");

        buttonPanel.add(btnTambah);
        buttonPanel.add(btnUbah);
        buttonPanel.add(btnHapus);
        buttonPanel.add(btnReset);

        // Gabung Form dan Tombol ke panel utara
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(formPanel, BorderLayout.CENTER);
        northPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(northPanel, BorderLayout.NORTH);

        // 3. BAGIAN BAWAH: Tabel Data
        String[] kolom = {"ID Buku", "Judul", "Penulis", "Kategori", "Tahun", "Status"};
        tableModel = new DefaultTableModel(kolom, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; } // Kunci sel tabel biar gak bisa diedit langsung
        };
        tabelBuku = new JTable(tableModel);
        add(new JScrollPane(tabelBuku), BorderLayout.CENTER);

        // --- EVENT LISTENERS ---
        
        // Event Klik Baris Tabel -> Masukkan data ke Form
        tabelBuku.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = tabelBuku.getSelectedRow();
                if (row >= 0) {
                    txtIdBuku.setText(tableModel.getValueAt(row, 0).toString());
                    txtJudul.setText(tableModel.getValueAt(row, 1).toString());
                    txtPenulis.setText(tableModel.getValueAt(row, 2).toString());
                    txtKategori.setText(tableModel.getValueAt(row, 3).toString());
                    txtTahun.setText(tableModel.getValueAt(row, 4).toString());
                    cmbStatus.setSelectedItem(tableModel.getValueAt(row, 5).toString());
                    txtIdBuku.setEditable(false); // Kunci ID pas mode edit
                }
            }
        });

        // Tombol Tambah
        btnTambah.addActionListener(e -> {
            if (validateInput()) {
                Buku bukuBaru = getBukuFromForm();
                if (bukuDAO.tambahBuku(bukuBaru)) {
                    JOptionPane.showMessageDialog(this, "Buku berhasil ditambahkan!");
                    loadDataBuku();
                    resetForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal menambah buku. Cek apakah ID sudah dipakai.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Tombol Ubah
        btnUbah.addActionListener(e -> {
            if (txtIdBuku.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Pilih buku dari tabel dulu yang mau diubah!");
                return;
            }
            if (validateInput()) {
                if (bukuDAO.ubahBuku(getBukuFromForm())) {
                    JOptionPane.showMessageDialog(this, "Data buku berhasil diperbarui!");
                    loadDataBuku();
                    resetForm();
                }
            }
        });

        // Tombol Hapus
        btnHapus.addActionListener(e -> {
            String id = txtIdBuku.getText();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Pilih buku dari tabel yang mau dihapus!");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin mau hapus buku ID: " + id + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (bukuDAO.hapusBuku(id)) {
                    JOptionPane.showMessageDialog(this, "Buku berhasil dihapus!");
                    loadDataBuku();
                    resetForm();
                }
            }
        });

        // Tombol Reset
        btnReset.addActionListener(e -> resetForm());

        // Muat data saat panel pertama kali dirender
        loadDataBuku();
    }

    // Method pembantu pasang field ke GridBag
    private void addFormField(JPanel panel, GridBagConstraints gbc, String label, JComponent field, int y) {
        gbc.gridx = 0; gbc.gridy = y; gbc.weightx = 0; panel.add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.gridy = y; gbc.weightx = 1; panel.add(field, gbc);
    }

    // Ambil semua objek dari MySQL lalu tembak ke JTable
    private void loadDataBuku() {
        tableModel.setRowCount(0); // Kosongkan tabel
        List<Buku> list = bukuDAO.getSemuaBuku();
        for (Buku b : list) {
            tableModel.addRow(new Object[]{
                b.getIdBuku(), b.getJudul(), b.getPenulis(), b.getKategori(), b.getTahunTerbit(), b.getStatus()
            });
        }
    }

    private Buku getBukuFromForm() {
        return new Buku(
            txtIdBuku.getText(), txtJudul.getText(), txtPenulis.getText(),
            txtKategori.getText(), Integer.parseInt(txtTahun.getText()), cmbStatus.getSelectedItem().toString()
        );
    }

    private void resetForm() {
        txtIdBuku.setText(""); txtJudul.setText(""); txtPenulis.setText("");
        txtKategori.setText(""); txtTahun.setText(""); cmbStatus.setSelectedIndex(0);
        txtIdBuku.setEditable(true);
        tabelBuku.clearSelection();
    }

    private boolean validateInput() {
        if (txtIdBuku.getText().isEmpty() || txtJudul.getText().isEmpty() || txtTahun.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID Buku, Judul, dan Tahun tidak boleh kosong!");
            return false;
        }
        try {
            Integer.parseInt(txtTahun.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Tahun terbit harus berupa angka bulat!");
            return false;
        }
        return true;
    }
}