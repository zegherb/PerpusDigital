package com.mycompany.perpusdigital.view;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import com.mycompany.perpusdigital.dao.BukuDAO;
import com.mycompany.perpusdigital.dao.PeminjamanDAO;
import com.mycompany.perpusdigital.model.Anggota;
import com.mycompany.perpusdigital.model.Buku;
import com.mycompany.perpusdigital.model.Peminjaman;
import com.mycompany.perpusdigital.model.Perpustakaan;
import com.mycompany.perpusdigital.service.ReportService;

public class DashboardAnggota extends JFrame {

    private Anggota anggotaLogin;
    private CardLayout cardLayout;
    private JPanel contentPanel;
    
    private JTable tabelKatalog;
    private DefaultTableModel modelKatalog;
    private JTable tabelRakSaya;
    private DefaultTableModel modelRakSaya;

    private BukuDAO bukuDAO;
    private PeminjamanDAO peminjamanDAO;

    public DashboardAnggota(Anggota anggota) {
        this.anggotaLogin = anggota;
        this.bukuDAO = new BukuDAO();
        this.peminjamanDAO = new PeminjamanDAO();
        initComponents();
    }

    private void initComponents() {
        setTitle("Portal Anggota - Perpustakaan Digital");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(new Color(30, 41, 59)); 
        sidebar.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.weightx = 1.0;

        JLabel lblNama = new JLabel("<html><div style='text-align:center;'>Halo,<br><b>" + anggotaLogin.getNama() + "</b></div></html>");
        lblNama.setForeground(Color.WHITE);
        lblNama.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        
        JLabel lblNo = new JLabel("ID: " + anggotaLogin.getNoAnggota());
        lblNo.setForeground(new Color(148, 163, 184));
        lblNo.setFont(new Font("Segoe UI", Font.ITALIC, 12));

        JButton btnKatalog = createBtn("Katalog Buku");
        JButton btnRakSaya = createBtn("Rak Buku Saya");
        JButton btnLogout = createBtn("Logout");
        btnLogout.setBackground(new Color(229, 56, 59));
        btnLogout.setForeground(Color.WHITE); 

        gbc.gridy = 0; sidebar.add(lblNama, gbc);
        gbc.gridy = 1; sidebar.add(lblNo, gbc);
        gbc.gridy = 2; sidebar.add(new JSeparator(), gbc);
        gbc.gridy = 3; sidebar.add(btnKatalog, gbc);
        gbc.gridy = 4; sidebar.add(btnRakSaya, gbc);
        
        gbc.gridy = 5; gbc.weighty = 1.0; sidebar.add(new JLabel(""), gbc); 
        gbc.gridy = 6; gbc.weighty = 0; sidebar.add(btnLogout, gbc);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        contentPanel.add(buatPanelKatalog(), "KATALOG");
        contentPanel.add(buatPanelRakSaya(), "RAK_SAYA");

        btnKatalog.addActionListener(e -> cardLayout.show(contentPanel, "KATALOG"));
        btnRakSaya.addActionListener(e -> cardLayout.show(contentPanel, "RAK_SAYA"));
        btnLogout.addActionListener(e -> {
            new LoginForm().setVisible(true);
            this.dispose();
        });

        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
        
        muatUlangRakSaya();
    }

    private JButton createBtn(String teks) {
        JButton b = new JButton(teks);
        b.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        b.setFocusPainted(false);
        return b;
    }

    private JPanel buatPanelKatalog() {
        JPanel p = new JPanel(new BorderLayout(0, 10));
        Perpustakaan perpusObj = new Perpustakaan();

        JPanel northPanel = new JPanel(new BorderLayout(10, 0));
        JLabel judul = new JLabel("Katalog Buku Digital");
        judul.setFont(new Font("Segoe UI", Font.BOLD, 20));
        
        JPanel searchBox = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JTextField txtCari = new JTextField(18);
        JButton btnCari = new JButton("Cari");
        searchBox.add(new JLabel("Filter: "));
        searchBox.add(txtCari);
        searchBox.add(btnCari);

        northPanel.add(judul, BorderLayout.WEST);
        northPanel.add(searchBox, BorderLayout.EAST);
        p.add(northPanel, BorderLayout.NORTH);

        String[] kol = {"ID Buku", "Judul Buku", "Penulis", "Kategori", "Tahun", "Status"};
        modelKatalog = new DefaultTableModel(kol, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabelKatalog = new JTable(modelKatalog);
        tabelKatalog.setRowHeight(25);
        p.add(new JScrollPane(tabelKatalog), BorderLayout.CENTER);

        JButton btnPinjam = new JButton("PINJAM BUKU TERPILIH");
        btnPinjam.setBackground(new Color(42, 157, 143));
        btnPinjam.setForeground(Color.WHITE);
        btnPinjam.setFont(new Font("Segoe UI", Font.BOLD, 14));

        btnCari.addActionListener(e -> {
            modelKatalog.setRowCount(0); 
            List<Buku> hasil = perpusObj.cariBuku(txtCari.getText()); 
            for (Buku b : hasil) {
                modelKatalog.addRow(new Object[]{
                    b.getIdBuku(), b.getJudul(), b.getPenulis(), 
                    b.getKategori(), b.getTahunTerbit(), b.getStatus()
                });
            }
        });
        txtCari.addActionListener(btnCari.getActionListeners()[0]);

        btnPinjam.addActionListener(e -> {
            int row = tabelKatalog.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Pilih buku dari tabel dulu bro!", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String idBuku = modelKatalog.getValueAt(row, 0).toString();
            String judulBuku = modelKatalog.getValueAt(row, 1).toString();
            String statusBuku = modelKatalog.getValueAt(row, 5).toString();

            if ("Dipinjam".equals(statusBuku)) {
                JOptionPane.showMessageDialog(this, 
                    "Sabar bro, e-book '" + judulBuku + "' sedang dipinjam orang lain. Tunggu dikembalikan dulu!", 
                    "Buku Tidak Tersedia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (JOptionPane.showConfirmDialog(this, "Pinjam digital buku '" + judulBuku + "'? (Maks 7 Hari)", "Pinjam", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                if (peminjamanDAO.catatPeminjaman(anggotaLogin.getIdUser(), idBuku)) {
                    JOptionPane.showMessageDialog(this, "BERHASIL! Buku dipindahkan ke Rak Anda.");
                    btnCari.doClick();  
                    muatUlangRakSaya(); 
                }
            }
        });

        p.add(btnPinjam, BorderLayout.SOUTH);
        btnCari.doClick(); 
        return p;
    }

    private JPanel buatPanelRakSaya() {
        JPanel p = new JPanel(new BorderLayout(0, 10));
        JLabel judul = new JLabel("Rak Buku Digital Saya");
        judul.setFont(new Font("Segoe UI", Font.BOLD, 20));
        p.add(judul, BorderLayout.NORTH);

        String[] kol = {"Kode Transaksi", "ID Buku", "Judul Buku", "Tgl Pinjam", "Jatuh Tempo", "Status"};
        modelRakSaya = new DefaultTableModel(kol, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabelRakSaya = new JTable(modelRakSaya);
        tabelRakSaya.setRowHeight(25);
        p.add(new JScrollPane(tabelRakSaya), BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new BorderLayout());
        JLabel info = new JLabel("Catatan: Pengembalian buku dilakukan secara otomatis atau melalui loket Admin.");
        info.setFont(new Font("Segoe UI", Font.ITALIC, 11));

        JPanel panelTombolKanan = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        

        JButton btnCetakStruk = new JButton("Cetak Bukti Pinjam (PDF)");
        btnCetakStruk.setBackground(new Color(42, 157, 143)); 
        btnCetakStruk.setForeground(Color.WHITE);
        btnCetakStruk.setFont(new Font("Segoe UI", Font.BOLD, 12));

        btnCetakStruk.addActionListener(e -> {
            int row = tabelRakSaya.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Klik dulu satu buku di tabel rak lu yang mau dicetak buktinya!");
                return;
            }
            new ReportService().cetakStrukPeminjaman(modelRakSaya.getValueAt(row, 0).toString());
        });

       
        panelTombolKanan.add(btnCetakStruk);

        southPanel.add(info, BorderLayout.WEST);
        southPanel.add(panelTombolKanan, BorderLayout.EAST);
        p.add(southPanel, BorderLayout.SOUTH);

        return p;
    }

    private void muatUlangRakSaya() {
        if (modelRakSaya != null) {
            modelRakSaya.setRowCount(0); 
            List<Peminjaman> riwayat = peminjamanDAO.getPeminjamanByAnggota(anggotaLogin.getIdUser());
            for (Peminjaman p : riwayat) {
                modelRakSaya.addRow(new Object[]{
                    p.getIdPinjam(), p.getIdBuku(), p.getJudulBuku(), 
                    p.getTanggalPinjam(), p.getTanggalKembali(), p.getStatusPinjam()
                });
            }
        }
    }
}