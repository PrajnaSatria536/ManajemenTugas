/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import model.Tugas.Prioritas;
import model.Tugas.Status;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Kelas utilitas untuk tampilan UI
 */
public class UIHelper {

    // Warna utama aplikasi
    public static final Color PRIMARY      = new Color(37, 99, 235);
    public static final Color PRIMARY_DARK = new Color(29, 78, 216);
    public static final Color SUCCESS      = new Color(22, 163, 74);
    public static final Color WARNING      = new Color(234, 179, 8);
    public static final Color DANGER       = new Color(220, 38, 38);
    public static final Color INFO         = new Color(6, 182, 212);
    public static final Color LIGHT_BG     = new Color(248, 250, 252);
    public static final Color BORDER       = new Color(226, 232, 240);
    public static final Color TEXT_DARK    = new Color(15, 23, 42);
    public static final Color TEXT_MUTED   = new Color(100, 116, 139);

    // Warna untuk prioritas
    public static Color getWarnaPrioritas(Prioritas p) {
        if (p == null) return Color.GRAY;
        return switch (p) {
            case TINGGI -> DANGER;
            case SEDANG -> WARNING;
            case RENDAH -> SUCCESS;
        };
    }

    // Warna untuk status
    public static Color getWarnaStatus(Status s) {
        if (s == null) return Color.GRAY;
        return switch (s) {
            case SELESAI -> SUCCESS;
            case PROSES  -> INFO;
            case BELUM   -> TEXT_MUTED;
        };
    }

    // Buat tombol bergaya modern
    public static JButton buatTombol(String teks, Color warna) {
        JButton btn = new JButton(teks);
        btn.setBackground(warna);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        return btn;
    }

    // Buat label judul seksi
    public static JLabel buatLabelJudul(String teks) {
        JLabel lbl = new JLabel(teks);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lbl.setForeground(TEXT_DARK);
        return lbl;
    }

    // Buat panel dengan border rounded look
    public static JPanel buatCardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        return panel;
    }

    // Renderer untuk warna baris tabel berdasarkan kondisi tugas
    public static DefaultTableCellRenderer getTugasRenderer() {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    String statusVal = table.getValueAt(row, 5) != null
                            ? table.getValueAt(row, 5).toString() : "";
                    String deadlineLabel = table.getValueAt(row, 4) != null
                            ? table.getValueAt(row, 4).toString() : "";

                    if (statusVal.equals("Selesai")) {
                        c.setBackground(new Color(240, 253, 244));
                        c.setForeground(new Color(21, 128, 61));
                    } else if (deadlineLabel.startsWith("⚠")) {
                        c.setBackground(new Color(255, 251, 235));
                        c.setForeground(new Color(146, 64, 14));
                    } else {
                        c.setBackground(Color.WHITE);
                        c.setForeground(TEXT_DARK);
                    }
                }
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                return c;
            }
        };
    }

    // Tampilkan pesan sukses
    public static void showSukses(Component parent, String pesan) {
        JOptionPane.showMessageDialog(parent, pesan, "Berhasil",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // Tampilkan pesan error
    public static void showError(Component parent, String pesan) {
        JOptionPane.showMessageDialog(parent, pesan, "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    // Konfirmasi hapus
    public static boolean konfirmasiHapus(Component parent, String nama) {
        int result = JOptionPane.showConfirmDialog(parent,
                "Yakin ingin menghapus \"" + nama + "\"?\nData tidak dapat dipulihkan.",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        return result == JOptionPane.YES_OPTION;
    }
}
