package com.mycompany.perpusdigital.view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import com.mycompany.perpusdigital.config.Koneksi;

public class SignUpForm extends javax.swing.JFrame {

    private javax.swing.JTextField txtNama;
    private javax.swing.JTextField txtUsername;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtNoAnggota;
    private javax.swing.JTextField txtAlamat;
    private javax.swing.JTextField txtTelepon;
    private javax.swing.JButton btnDaftar;
    private javax.swing.JButton btnKeLogin;

    public SignUpForm() {
        initComponents();
    }

    private void initComponents() {
        javax.swing.JLabel lblJudul = new javax.swing.JLabel("DAFTAR ANGGOTA BARU");
        javax.swing.JLabel lblNama = new javax.swing.JLabel("Nama Lengkap:");
        javax.swing.JLabel lblUsername = new javax.swing.JLabel("Username:");
        javax.swing.JLabel lblPassword = new javax.swing.JLabel("Password:");
        javax.swing.JLabel lblNoAnggota = new javax.swing.JLabel("No. Anggota:");
        javax.swing.JLabel lblAlamat = new javax.swing.JLabel("Alamat:");
        javax.swing.JLabel lblTelepon = new javax.swing.JLabel("No. Telepon:");

        txtNama = new javax.swing.JTextField(20);
        txtUsername = new javax.swing.JTextField(20);
        txtPassword = new javax.swing.JPasswordField(20);
        txtNoAnggota = new javax.swing.JTextField(20);
        txtAlamat = new javax.swing.JTextField(20);
        txtTelepon = new javax.swing.JTextField(20);

        btnDaftar = new javax.swing.JButton("Daftar");
        btnKeLogin = new javax.swing.JButton("Kembali ke Login");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(true);
        setTitle("Registrasi Anggota");
        lblJudul.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));

        btnDaftar.addActionListener(evt -> btnDaftarActionPerformed(evt));
        btnKeLogin.addActionListener(evt -> {
            new LoginForm().setVisible(true);
            this.dispose(); // Tutup form signup
        });

        javax.swing.JPanel signPanel = new javax.swing.JPanel();
        signPanel.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.insets = new java.awt.Insets(8, 10, 8, 10);
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = java.awt.GridBagConstraints.CENTER;
        signPanel.add(lblJudul, gbc);

        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 1; signPanel.add(lblNama, gbc);
        gbc.gridx = 1; gbc.gridy = 1; signPanel.add(txtNama, gbc);

       
        gbc.gridx = 0; gbc.gridy = 2; signPanel.add(lblUsername, gbc);
        gbc.gridx = 1; gbc.gridy = 2; signPanel.add(txtUsername, gbc);

       
        gbc.gridx = 0; gbc.gridy = 3; signPanel.add(lblPassword, gbc);
        gbc.gridx = 1; gbc.gridy = 3; signPanel.add(txtPassword, gbc);

        
        gbc.gridx = 0; gbc.gridy = 4; signPanel.add(lblNoAnggota, gbc);
        gbc.gridx = 1; gbc.gridy = 4; signPanel.add(txtNoAnggota, gbc);

        
        gbc.gridx = 0; gbc.gridy = 5; signPanel.add(lblAlamat, gbc);
        gbc.gridx = 1; gbc.gridy = 5; signPanel.add(txtAlamat, gbc);

        
        gbc.gridx = 0; gbc.gridy = 6; signPanel.add(lblTelepon, gbc);
        gbc.gridx = 1; gbc.gridy = 6; signPanel.add(txtTelepon, gbc);

        
        javax.swing.JPanel buttonPanel = new javax.swing.JPanel();
        buttonPanel.add(btnDaftar);
        buttonPanel.add(btnKeLogin);

        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        gbc.fill = java.awt.GridBagConstraints.NONE;
        signPanel.add(buttonPanel, gbc);

       
        getContentPane().setLayout(new java.awt.GridBagLayout());
        getContentPane().add(signPanel);

        pack();
        setLocationRelativeTo(null); 
    }

    
    private void btnDaftarActionPerformed(java.awt.event.ActionEvent evt) {
       
        if (txtNama.getText().isEmpty() || txtUsername.getText().isEmpty() || 
            new String(txtPassword.getPassword()).isEmpty() || txtNoAnggota.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Harap isi semua kolom wajib!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Connection conn = null;
        try {
            conn = Koneksi.configDB();
            conn.setAutoCommit(false); 

            
            String idUser = "USR" + (System.currentTimeMillis() % 100000);

           
            String sqlUser = "INSERT INTO user (id_user, nama, username, password, tipe_user) VALUES (?, ?, ?, ?, 'Anggota')";
            PreparedStatement pstUser = conn.prepareStatement(sqlUser);
            pstUser.setString(1, idUser);
            pstUser.setString(2, txtNama.getText());
            pstUser.setString(3, txtUsername.getText());
            pstUser.setString(4, new String(txtPassword.getPassword()));
            pstUser.executeUpdate();

            
            String sqlAnggota = "INSERT INTO anggota (id_user, no_anggota, alamat, no_telepon) VALUES (?, ?, ?, ?)";
            PreparedStatement pstAnggota = conn.prepareStatement(sqlAnggota);
            pstAnggota.setString(1, idUser);
            pstAnggota.setString(2, txtNoAnggota.getText());
            pstAnggota.setString(3, txtAlamat.getText());
            pstAnggota.setString(4, txtTelepon.getText());
            pstAnggota.executeUpdate();

           
            conn.commit();
            
            JOptionPane.showMessageDialog(this, "Registrasi Berhasil! Silakan Login.");
            
            
            new LoginForm().setVisible(true);
            this.dispose();

        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback(); 
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            JOptionPane.showMessageDialog(this, "Registrasi Gagal: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true); 
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new SignUpForm().setVisible(true);
        });
    }
}