package com.mycompany.perpusdigital.service;

import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;
import com.mycompany.perpusdigital.config.Koneksi;

public class ReportService {

    // 1. Eksekutor Laporan Semua Transaksi (Admin)
    public void cetakLaporanAdmin() {
        try (Connection conn = Koneksi.configDB()) {
            // Membaca file jrxml yang ada di folder resources tadi
            InputStream reportStream = getClass().getResourceAsStream("/report/laporan_admin.jrxml");
            if (reportStream == null) {
                JOptionPane.showMessageDialog(null, "Template laporan_admin.jrxml tidak ditemukan!");
                return;
            }

            // Proses Kompilasi on-the-fly dari XML -> PDF
            JasperReport jr = JasperCompileManager.compileReport(reportStream);
            JasperPrint jp = JasperFillManager.fillReport(jr, null, conn);
            
            // Memunculkan Jendela Preview PDF (false = agar saat PDF diclose, aplikasi utama gak mati)
            JasperViewer.viewReport(jp, false);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal mencetak laporan: " + e.getMessage(), "Error Jasper", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // 2. Eksekutor Cetak Struk per Transaksi (Anggota)
    public void cetakStrukPeminjaman(String idPinjam) {
        try (Connection conn = Koneksi.configDB()) {
            InputStream reportStream = getClass().getResourceAsStream("/report/struk_peminjaman.jrxml");
            if (reportStream == null) {
                JOptionPane.showMessageDialog(null, "Template struk_peminjaman.jrxml tidak ditemukan!");
                return;
            }

            // Melempar parameter ID Pinjam ke dalam kueri XML
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("id_pinjam_param", idPinjam);

            JasperReport jr = JasperCompileManager.compileReport(reportStream);
            JasperPrint jp = JasperFillManager.fillReport(jr, parameters, conn);

            JasperViewer.viewReport(jp, false);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal mencetak struk: " + e.getMessage(), "Error Jasper", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}