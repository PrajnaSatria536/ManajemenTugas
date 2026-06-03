/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.MataKuliahController;
import model.MataKuliah;
import util.UIHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel View untuk Mata Kuliah
 * Menerapkan konsep MVC: View layer
 */
public class MataKuliahPanel extends JPanel {

    private final MataKuliahController controller;

    // Komponen tabel
    private JTable table;
    private DefaultTableModel tableModel;

    // Komponen form
    private JTextField txtKode, txtNama, txtDosen, txtSemester, txtSearch;
    private JSpinner   spnSks;
    private JButton    btnTambah, btnUpdate, btnHapus, btnBatal;

    private int selectedId = -1;

    public MataKuliahPanel() {
        this.controller = new MataKuliahController();
        initUI();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout(0, 0));
        setBackground(UIHelper.LIGHT_BG);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // === Header ===
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));
        headerPanel.add(UIHelper.buatLabelJudul("📚 Mata Kuliah"), BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // === Split: Tabel kiri, Form kanan ===
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                buatPanelTabel(), buatPanelForm());
        splitPane.setDividerLocation(560);
        splitPane.setBorder(null);
        splitPane.setBackground(UIHelper.LIGHT_BG);
        add(splitPane, BorderLayout.CENTER);
    }

    // ===== Panel Tabel =====
    private JPanel buatPanelTabel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIHelper.BORDER),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        // Search bar
        JPanel searchPanel = new JPanel(new BorderLayout(8, 0));
        searchPanel.setOpaque(false);
        txtSearch = new JTextField();
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIHelper.BORDER),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        txtSearch.putClientProperty("JTextField.placeholderText", "Cari mata kuliah...");
        JButton btnCari = UIHelper.buatTombol("Cari", UIHelper.PRIMARY);
        btnCari.addActionListener(e -> doSearch());
        searchPanel.add(txtSearch, BorderLayout.CENTER);
        searchPanel.add(btnCari, BorderLayout.EAST);
        panel.add(searchPanel, BorderLayout.NORTH);

        // Tabel
        String[] kolom = {"ID", "Kode", "Nama Mata Kuliah", "SKS", "Dosen", "Semester"};
        tableModel = new DefaultTableModel(kolom, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(32);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(UIHelper.LIGHT_BG);
        table.setSelectionBackground(new Color(219, 234, 254));

        // Sembunyikan kolom ID
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(1).setPreferredWidth(70);
        table.getColumnModel().getColumn(2).setPreferredWidth(200);
        table.getColumnModel().getColumn(3).setPreferredWidth(40);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) isiForm();
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(UIHelper.BORDER));
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    // ===== Panel Form =====
    private JPanel buatPanelForm() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIHelper.BORDER),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
        panel.setPreferredSize(new Dimension(280, 0));

        JLabel lblForm = new JLabel("Form Mata Kuliah");
        lblForm.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblForm.setForeground(UIHelper.TEXT_DARK);
        lblForm.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));
        panel.add(lblForm, BorderLayout.NORTH);

        // Fields
        JPanel fields = new JPanel(new GridBagLayout());
        fields.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 0, 4, 0);
        gbc.weightx = 1.0;

        txtKode     = buatTextField();
        txtNama     = buatTextField();
        txtDosen    = buatTextField();
        txtSemester = buatTextField();
        spnSks      = new JSpinner(new SpinnerNumberModel(2, 1, 6, 1));
        spnSks.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        int r = 0;
        tambahFieldForm(fields, gbc, "Kode MK *", txtKode,     r++);
        tambahFieldForm(fields, gbc, "Nama MK *",  txtNama,     r++);
        tambahFieldForm(fields, gbc, "SKS *",      spnSks,      r++);
        tambahFieldForm(fields, gbc, "Dosen",      txtDosen,    r++);
        tambahFieldForm(fields, gbc, "Semester",   txtSemester, r);

        panel.add(fields, BorderLayout.CENTER);

        // Tombol aksi
        JPanel btnPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(16, 0, 0, 0));

        btnTambah = UIHelper.buatTombol("➕ Tambah",  UIHelper.SUCCESS);
        btnUpdate = UIHelper.buatTombol("✏ Update",  UIHelper.PRIMARY);
        btnHapus  = UIHelper.buatTombol("🗑 Hapus",  UIHelper.DANGER);
        btnBatal  = UIHelper.buatTombol("✖ Batal",   new Color(107, 114, 128));

        btnUpdate.setEnabled(false);
        btnHapus.setEnabled(false);

        btnTambah.addActionListener(e -> doTambah());
        btnUpdate.addActionListener(e -> doUpdate());
        btnHapus.addActionListener(e -> doHapus());
        btnBatal.addActionListener(e -> resetForm());

        btnPanel.add(btnTambah);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnHapus);
        btnPanel.add(btnBatal);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void tambahFieldForm(JPanel panel, GridBagConstraints gbc, String label,
                                  JComponent field, int row) {
        gbc.gridy = row * 2;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(UIHelper.TEXT_MUTED);
        panel.add(lbl, gbc);

        gbc.gridy = row * 2 + 1;
        panel.add(field, gbc);
    }

    private JTextField buatTextField() {
        JTextField tf = new JTextField();
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIHelper.BORDER),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        return tf;
    }

    // ===== Logic =====

    private void loadData() {
        loadData(controller.getAll());
    }

    private void loadData(List<MataKuliah> list) {
        tableModel.setRowCount(0);
        for (MataKuliah mk : list) {
            tableModel.addRow(new Object[]{
                mk.getId(), mk.getKode(), mk.getNama(),
                mk.getSks(), mk.getDosen(), mk.getSemester()
            });
        }
    }

    private void doSearch() {
        String kw = txtSearch.getText().trim();
        loadData(kw.isEmpty() ? controller.getAll() : controller.search(kw));
    }

    private void isiForm() {
        int row = table.getSelectedRow();
        if (row < 0) return;

        selectedId = (int) tableModel.getValueAt(row, 0);
        txtKode.setText((String) tableModel.getValueAt(row, 1));
        txtNama.setText((String) tableModel.getValueAt(row, 2));
        spnSks.setValue(tableModel.getValueAt(row, 3));
        txtDosen.setText((String) tableModel.getValueAt(row, 4));
        txtSemester.setText((String) tableModel.getValueAt(row, 5));

        btnTambah.setEnabled(false);
        btnUpdate.setEnabled(true);
        btnHapus.setEnabled(true);
    }

    private void doTambah() {
        if (!validasiForm()) return;
        MataKuliah mk = buildFromForm();
        if (controller.isKodeExists(mk.getKode(), 0)) {
            UIHelper.showError(this, "Kode mata kuliah sudah ada!");
            return;
        }
        if (controller.tambah(mk)) {
            UIHelper.showSukses(this, "Mata kuliah berhasil ditambahkan!");
            loadData(); resetForm();
        } else {
            UIHelper.showError(this, "Gagal menambahkan mata kuliah.");
        }
    }

    private void doUpdate() {
        if (!validasiForm()) return;
        MataKuliah mk = buildFromForm();
        mk.setId(selectedId);
        if (controller.isKodeExists(mk.getKode(), selectedId)) {
            UIHelper.showError(this, "Kode mata kuliah sudah digunakan!");
            return;
        }
        if (controller.update(mk)) {
            UIHelper.showSukses(this, "Mata kuliah berhasil diperbarui!");
            loadData(); resetForm();
        } else {
            UIHelper.showError(this, "Gagal memperbarui mata kuliah.");
        }
    }

    private void doHapus() {
        if (!UIHelper.konfirmasiHapus(this, txtNama.getText())) return;
        if (controller.hapus(selectedId)) {
            UIHelper.showSukses(this, "Mata kuliah berhasil dihapus!");
            loadData(); resetForm();
        } else {
            UIHelper.showError(this, "Gagal menghapus. Mungkin ada tugas yang terkait.");
        }
    }

    private boolean validasiForm() {
        if (txtKode.getText().trim().isEmpty()) {
            UIHelper.showError(this, "Kode mata kuliah tidak boleh kosong!");
            return false;
        }
        if (txtNama.getText().trim().isEmpty()) {
            UIHelper.showError(this, "Nama mata kuliah tidak boleh kosong!");
            return false;
        }
        return true;
    }

    private MataKuliah buildFromForm() {
        return new MataKuliah(
            txtKode.getText().trim().toUpperCase(),
            txtNama.getText().trim(),
            (int) spnSks.getValue(),
            txtDosen.getText().trim(),
            txtSemester.getText().trim()
        );
    }

    private void resetForm() {
        selectedId = -1;
        txtKode.setText(""); txtNama.setText("");
        txtDosen.setText(""); txtSemester.setText("");
        spnSks.setValue(2);
        table.clearSelection();
        btnTambah.setEnabled(true);
        btnUpdate.setEnabled(false);
        btnHapus.setEnabled(false);
    }
}
