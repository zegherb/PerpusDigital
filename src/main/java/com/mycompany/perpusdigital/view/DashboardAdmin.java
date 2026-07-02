package com.mycompany.perpusdigital.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class DashboardAdmin extends JFrame {

    
    private JPanel sidebarPanel;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    
    private String namaAdmin;

    public DashboardAdmin(String nama) {
        this.namaAdmin = nama;
        initComponents();
    }

    private void initComponents() {
        setTitle("Dashboard Admin - Sistem Manajemen Perpustakaan");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600); 
        setMinimumSize(new Dimension(800, 500));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        sidebarPanel = new JPanel();
        sidebarPanel.setPreferredSize(new Dimension(220, 0));
        sidebarPanel.setBackground(new Color(41, 50, 65)); 
        sidebarPanel.setLayout(new GridBagLayout());
        
        GridBagConstraints gbcSide = new GridBagConstraints();
        gbcSide.insets = new Insets(10, 10, 10, 10);
        gbcSide.fill = GridBagConstraints.HORIZONTAL;
        gbcSide.gridx = 0;
        gbcSide.weightx = 1.0;

        JLabel lblWelcome = new JLabel("Halo, " + namaAdmin);
        lblWelcome.setForeground(Color.WHITE);
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbcSide.gridy = 0;
        sidebarPanel.add(lblWelcome, gbcSide);

        JButton btnBeranda = createSidebarButton("Beranda");
        JButton btnBuku = createSidebarButton("Kelola Buku");
        JButton btnAnggota = createSidebarButton("Kelola Anggota");
        JButton btnTransaksi = createSidebarButton("Transaksi Peminjaman");
        JButton btnLogout = createSidebarButton("Logout");
        btnLogout.setBackground(new Color(229, 56, 59)); 

        gbcSide.gridy = 1; sidebarPanel.add(btnBeranda, gbcSide);
        gbcSide.gridy = 2; sidebarPanel.add(btnBuku, gbcSide);
        gbcSide.gridy = 3; sidebarPanel.add(btnAnggota, gbcSide);
        gbcSide.gridy = 4; sidebarPanel.add(btnTransaksi, gbcSide);
        
        gbcSide.gridy = 5; 
        gbcSide.weighty = 1.0; 
        sidebarPanel.add(new JLabel(""), gbcSide); 
        
        gbcSide.gridy = 6; 
        gbcSide.weighty = 0;
        sidebarPanel.add(btnLogout, gbcSide);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); 

        JPanel panelBeranda = createPanelBeranda();
        JPanel panelBuku = new PanelBuku();
        JPanel panelAnggota = new PanelAnggota();
        JPanel panelTransaksi = new PanelTransaksi();

        contentPanel.add(panelBeranda, "BERANDA");
        contentPanel.add(panelBuku, "BUKU");
        contentPanel.add(panelAnggota, "ANGGOTA");
        contentPanel.add(panelTransaksi, "TRANSAKSI");

        btnBeranda.addActionListener(e -> cardLayout.show(contentPanel, "BERANDA"));
        btnBuku.addActionListener(e -> cardLayout.show(contentPanel, "BUKU"));
        btnAnggota.addActionListener(e -> cardLayout.show(contentPanel, "ANGGOTA"));
        btnTransaksi.addActionListener(e -> cardLayout.show(contentPanel, "TRANSAKSI"));
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin keluar?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                new LoginForm().setVisible(true);
                this.dispose();
            }
        });

        add(sidebarPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    private JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setFocusPainted(false);
        return btn;
    }

    private JPanel createPanelBeranda() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel judul = new JLabel("Selamat Datang di Sistem Manajemen Perpustakaan");
        judul.setFont(new Font("Segoe UI", Font.BOLD, 24));
        panel.add(judul, BorderLayout.NORTH);
        
        JLabel info = new JLabel("Pilih menu di sidebar sebelah kiri untuk mulai mengelola data.");
        panel.add(info, BorderLayout.CENTER);
        return panel;
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new DashboardAdmin("Admin ku").setVisible(true);
        });
    }
}