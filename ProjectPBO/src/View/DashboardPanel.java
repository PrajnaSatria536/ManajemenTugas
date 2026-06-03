/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.TugasController;
import model.Tugas.Status;
import util.UIHelper;

import javax.swing.*;
import java.awt.*;

/**
 * Panel Dashboard - menampilkan ringkasan & statistik
 */
public class DashboardPanel extends JPanel {

    private final TugasController tugasCtrl;

    private JLabel lblBelum, lblProses, lblSelesai, lblOverdue;

    public DashboardPanel() {
        this.tugasCtrl = new TugasController();
        initUI();
        refreshData();
    }

    private void initUI() {
        setLayout(new BorderLayout(0, 20));
        setBackground(UIHelper.LIGHT_BG);
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        // === Header ===
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel lblJudul = UIHelper.buatLabelJudul("📊 Dashboard");
        JLabel lblSub   = new JLabel("Ringkasan status tugas kuliah kamu");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(UIHelper.TEXT_MUTED);

        headerPanel.add(lblJudul, BorderLayout.NORTH);
        headerPanel.add(lblSub, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // === Kartu Statistik ===
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 16, 0));
        statsPanel.setOpaque(false);

        lblBelum   = new JLabel("0", SwingConstants.CENTER);
        lblProses  = new JLabel("0", SwingConstants.CENTER);
        lblSelesai = new JLabel("0", SwingConstants.CENTER);
        lblOverdue = new JLabel("0", SwingConstants.CENTER);

        statsPanel.add(buatKartuStat("Belum Dikerjakan", lblBelum,  UIHelper.TEXT_MUTED, "📋"));
        statsPanel.add(buatKartuStat("Sedang Dikerjakan", lblProses, UIHelper.INFO,       "⚙"));
        statsPanel.add(buatKartuStat("Selesai",          lblSelesai, UIHelper.SUCCESS,    "✅"));
        statsPanel.add(buatKartuStat("Terlambat",        lblOverdue, UIHelper.DANGER,     "⚠"));

        add(statsPanel, BorderLayout.CENTER);

        // === Info panel bawah ===
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIHelper.BORDER),
            BorderFactory.createEmptyBorder(16, 20, 16, 20)
        ));

        JLabel lblTips = new JLabel("<html><b>💡 Tips:</b> Prioritaskan tugas dengan label <font color='red'>Tinggi</font> "
                + "dan yang mendekati deadline. Klik tab <b>Daftar Tugas</b> untuk melihat semua tugas.</html>");
        lblTips.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblTips.setForeground(UIHelper.TEXT_DARK);

        infoPanel.add(lblTips, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.SOUTH);
    }

    private JPanel buatKartuStat(String judul, JLabel lblAngka, Color warna, String ikon) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIHelper.BORDER),
            BorderFactory.createEmptyBorder(20, 16, 20, 16)
        ));

        JLabel lblIkon = new JLabel(ikon, SwingConstants.CENTER);
        lblIkon.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        lblIkon.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblAngka.setFont(new Font("Segoe UI", Font.BOLD, 40));
        lblAngka.setForeground(warna);
        lblAngka.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblJdl = new JLabel(judul, SwingConstants.CENTER);
        lblJdl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblJdl.setForeground(UIHelper.TEXT_MUTED);
        lblJdl.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(lblIkon);
        card.add(Box.createVerticalStrut(8));
        card.add(lblAngka);
        card.add(Box.createVerticalStrut(4));
        card.add(lblJdl);

        return card;
    }

    public void refreshData() {
        lblBelum.setText(String.valueOf(tugasCtrl.countByStatus(Status.BELUM)));
        lblProses.setText(String.valueOf(tugasCtrl.countByStatus(Status.PROSES)));
        lblSelesai.setText(String.valueOf(tugasCtrl.countByStatus(Status.SELESAI)));
        lblOverdue.setText(String.valueOf(tugasCtrl.countOverdue()));
    }
}
