package com.mycompany.perpusdigital.view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import com.mycompany.perpusdigital.config.Koneksi;

public class LoginForm extends javax.swing.JFrame {

    // Deklarasi Komponen Global
    private javax.swing.JLabel Username;
    private javax.swing.JLabel Password;
    private javax.swing.JTextField txtUsername;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JButton btnLogin;
    private javax.swing.JButton btnSignUp;

    // Constructor
    public LoginForm() {
        initComponents();
    }

    // Method Layout Responsif
    private void initComponents() {
        Username = new javax.swing.JLabel("Username: ");
        Password = new javax.swing.JLabel("Password: ");
        txtUsername = new javax.swing.JTextField(20);
        txtPassword = new javax.swing.JPasswordField(20);
        jLabel3 = new javax.swing.JLabel("MENU LOGIN");
        btnLogin = new javax.swing.JButton("Login");
        btnSignUp = new javax.swing.JButton("Belum punya akun? Daftar");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(true);
        setTitle("Aplikasi Perpustakaan Digital");
        jLabel3.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));

        // Event Listener untuk tombol
        btnLogin.addActionListener(evt -> btnLoginActionPerformed(evt));
        btnSignUp.addActionListener(evt -> {
            new SignUpForm().setVisible(true);
            this.dispose();
        });

        // Setup Panel dan GridBagLayout
        javax.swing.JPanel loginPanel = new javax.swing.JPanel();
        loginPanel.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.insets = new java.awt.Insets(10, 10, 10, 10);
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = java.awt.GridBagConstraints.CENTER;
        loginPanel.add(jLabel3, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1; loginPanel.add(Username, gbc);
        gbc.gridx = 1; gbc.gridy = 1; loginPanel.add(txtUsername, gbc);

        gbc.gridx = 0; gbc.gridy = 2; loginPanel.add(Password, gbc);
        gbc.gridx = 1; gbc.gridy = 2; loginPanel.add(txtPassword, gbc);

        // Sub-panel untuk mensejajarkan tombol Login dan SignUp
        javax.swing.JPanel buttonPanel = new javax.swing.JPanel();
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnSignUp);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.fill = java.awt.GridBagConstraints.NONE;
        loginPanel.add(buttonPanel, gbc);

        getContentPane().setLayout(new java.awt.GridBagLayout());
        getContentPane().add(loginPanel);

        pack();
        setLocationRelativeTo(null); // Memastikan tampil di tengah layar
    }

    // Logika Autentikasi Login
    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            Connection conn = Koneksi.configDB();
            String sql = "SELECT * FROM user WHERE username = ? AND password = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            
            pst.setString(1, txtUsername.getText());
            pst.setString(2, new String(txtPassword.getPassword()));
            
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                String tipe = rs.getString("tipe_user");
                
                if ("Admin".equals(tipe)) {
                    // Ambil nama dari database buat dikirim ke dashboard
                    String namaLengkap = rs.getString("nama"); 
                    JOptionPane.showMessageDialog(this, "Login Berhasil sebagai Admin!");
    
                    // Buka Dashboard Admin dan kirim variabel nama
                    new DashboardAdmin(namaLengkap).setVisible(true); 
                    this.dispose(); // Tutup form login
                    
                } else if ("Anggota".equals(tipe)) {
                    // 1. Tangkap data dari tabel user
                    String id = rs.getString("id_user");
                    String nama = rs.getString("nama");
                    String user = rs.getString("username");
                    String pass = rs.getString("password");

                    // 2. Kita harus kueri JOIN sedikit ke tabel anggota buat ngambil 'no_anggota'
                    String sqlAnggota = "SELECT no_anggota, alamat, no_telepon FROM anggota WHERE id_user = ?";
                    PreparedStatement psAnggota = conn.prepareStatement(sqlAnggota);
                    psAnggota.setString(1, id);
                    ResultSet rsAng = psAnggota.executeQuery();

                    String noAng = ""; String alamat = ""; String telp = "";
                    if (rsAng.next()) {
                        noAng = rsAng.getString("no_anggota");
                        alamat = rsAng.getString("alamat");
                        telp = rsAng.getString("no_telepon");
                    }

                    // 3. Wujudkan objek Anggota!
                    com.mycompany.perpusdigital.model.Anggota anggotaLogin = 
                        new com.mycompany.perpusdigital.model.Anggota(id, nama, user, pass, tipe, noAng, alamat, telp);

                    JOptionPane.showMessageDialog(this, "Selamat Datang, " + nama + "!");

                    // 4. LEMPAR KE DASHBOARD KHUSUS ANGGOTA
                    new DashboardAnggota(anggotaLogin).setVisible(true);
                    this.dispose();
                }
                this.dispose(); 
            } else {
                JOptionPane.showMessageDialog(this, "Username atau Password salah!", "Akses Ditolak", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error Database: " + e.getMessage());
        }
    }

    // Main Method untuk testing run form langsung
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    }
}