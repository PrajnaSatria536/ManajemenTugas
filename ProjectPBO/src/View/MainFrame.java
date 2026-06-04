/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import util.UIHelper;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private DashboardPanel  dashboardPanel;
    private TugasPanel      tugasPanel;
    private MataKuliahPanel mataKuliahPanel;
    private JTabbedPane     tabbedPane;

    public MainFrame() {
        initUI();
    }

    private void initUI() {
        setTitle("📚 Sistem Manajemen Tugas Kuliah");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 720);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(UIHelper.PRIMARY);
        topBar.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JLabel lblApp = new JLabel("📚 TaskKuliah");
        lblApp.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblApp.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Sistem Manajemen Tugas Kuliah");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblSub.setForeground(new Color(191, 219, 254));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        titlePanel.add(lblApp);
        titlePanel.add(lblSub);

        topBar.add(titlePanel, BorderLayout.WEST);
        add(topBar, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabbedPane.setBackground(UIHelper.LIGHT_BG);

        dashboardPanel  = new DashboardPanel();
        tugasPanel      = new TugasPanel();
        mataKuliahPanel = new MataKuliahPanel();

        tabbedPane.addTab("  Dashboard  ",   null, dashboardPanel,  "Lihat ringkasan tugas");
        tabbedPane.addTab("  Daftar Tugas  ", null, tugasPanel,      "Kelola semua tugas");
        tabbedPane.addTab("  Mata Kuliah  ",  null, mataKuliahPanel, "Kelola mata kuliah");

        tabbedPane.addChangeListener(e -> {
            int idx = tabbedPane.getSelectedIndex();
            if (idx == 0) dashboardPanel.refreshData();
            if (idx == 1) tugasPanel.loadData();
        });

        add(tabbedPane, BorderLayout.CENTER);

        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 4));
        statusBar.setBackground(new Color(241, 245, 249));
        statusBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UIHelper.BORDER));

        JLabel lblStatus = new JLabel("✅ Terhubung ke database  |  " +
                "Klik baris pada tabel untuk mengedit  |  Kelompok PA - Sistem Manajemen Tugas Kuliah");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblStatus.setForeground(UIHelper.TEXT_MUTED);
        statusBar.add(lblStatus);

        add(statusBar, BorderLayout.SOUTH);
    }
}
