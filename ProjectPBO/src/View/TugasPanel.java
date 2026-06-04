/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.MataKuliahController;
import controller.TugasController;
import model.MataKuliah;
import model.Tugas;
import model.Tugas.Prioritas;
import model.Tugas.Status;
import util.UIHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class TugasPanel extends JPanel {

    private final TugasController     tugasCtrl;
    private final MataKuliahController mkCtrl;

    private JTable             table;
    private DefaultTableModel  tableModel;

    private JComboBox<MataKuliah> cmbMataKuliah;
    private JTextField            txtJudul, txtSearch;
    private JTextArea             txtDeskripsi, txtCatatan;
    private JSpinner              spnDeadline;
    private JComboBox<String>     cmbPrioritas, cmbStatus, cmbFilterStatus;
    private JButton               btnTambah, btnUpdate, btnHapus, btnBatal, btnSelesai;

    private int selectedId = -1;

    public TugasPanel() {
        this.tugasCtrl = new TugasController();
        this.mkCtrl    = new MataKuliahController();
        initUI();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout(0, 0));
        setBackground(UIHelper.LIGHT_BG);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));
        header.add(UIHelper.buatLabelJudul("📝 Daftar Tugas"), BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                buatPanelTabel(), buatPanelForm());
        split.setDividerLocation(580);
        split.setBorder(null);
        split.setBackground(UIHelper.LIGHT_BG);
        add(split, BorderLayout.CENTER);
    }

    private JPanel buatPanelTabel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIHelper.BORDER),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        JPanel toolbar = new JPanel(new BorderLayout(8, 0));
        toolbar.setOpaque(false);

        txtSearch = new JTextField();
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIHelper.BORDER),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));

        String[] filterOpts = {"Semua Status", "Belum Dikerjakan", "Sedang Dikerjakan", "Selesai"};
        cmbFilterStatus = new JComboBox<>(filterOpts);
        cmbFilterStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cmbFilterStatus.setPreferredSize(new Dimension(160, 34));
        cmbFilterStatus.addActionListener(e -> loadData());

        JButton btnCari = UIHelper.buatTombol("Cari", UIHelper.PRIMARY);
        btnCari.addActionListener(e -> doSearch());

        JPanel rightTools = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        rightTools.setOpaque(false);
        rightTools.add(cmbFilterStatus);
        rightTools.add(btnCari);

        toolbar.add(txtSearch, BorderLayout.CENTER);
        toolbar.add(rightTools, BorderLayout.EAST);
        panel.add(toolbar, BorderLayout.NORTH);

        String[] kolom = {"ID", "Mata Kuliah", "Judul Tugas", "Prioritas", "Deadline", "Status", "Sisa"};
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
        table.getColumnModel().getColumn(1).setPreferredWidth(130);
        table.getColumnModel().getColumn(2).setPreferredWidth(180);
        table.getColumnModel().getColumn(3).setPreferredWidth(70);
        table.getColumnModel().getColumn(4).setPreferredWidth(90);
        table.getColumnModel().getColumn(5).setPreferredWidth(130);
        table.getColumnModel().getColumn(6).setPreferredWidth(110);

        DefaultTableCellRenderer renderer = UIHelper.getTugasRenderer();
        for (int i = 1; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        table.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, value, sel, foc, row, col);
                if (!sel && value != null) {
                    Prioritas p = Prioritas.fromString(value.toString());
                    setForeground(UIHelper.getWarnaPrioritas(p));
                    setFont(new Font("Segoe UI", Font.BOLD, 12));
                }
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                return this;
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) isiForm();
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(UIHelper.BORDER));
        panel.add(scroll, BorderLayout.CENTER);

        btnSelesai = UIHelper.buatTombol("✅ Tandai Selesai", UIHelper.SUCCESS);
        btnSelesai.setEnabled(false);
        btnSelesai.addActionListener(e -> doTandaiSelesai());
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomBar.setOpaque(false);
        bottomBar.add(btnSelesai);
        panel.add(bottomBar, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel buatPanelForm() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIHelper.BORDER),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
        panel.setPreferredSize(new Dimension(300, 0));

        JLabel lblForm = new JLabel("Form Tugas");
        lblForm.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblForm.setForeground(UIHelper.TEXT_DARK);
        lblForm.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));
        panel.add(lblForm, BorderLayout.NORTH);

        JPanel fields = new JPanel(new GridBagLayout());
        fields.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 0, 3, 0);
        gbc.weightx = 1.0;

        cmbMataKuliah = new JComboBox<>();
        cmbMataKuliah.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        refreshMataKuliah();

        txtJudul = buatTextField();

        txtDeskripsi = new JTextArea(3, 1);
        txtDeskripsi.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtDeskripsi.setLineWrap(true);
        txtDeskripsi.setWrapStyleWord(true);
        txtDeskripsi.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIHelper.BORDER),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));

        SpinnerDateModel dateModel = new SpinnerDateModel();
        spnDeadline = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spnDeadline, "dd/MM/yyyy");
        spnDeadline.setEditor(dateEditor);
        spnDeadline.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        String[] prioritasOpts = {"Rendah", "Sedang", "Tinggi"};
        cmbPrioritas = new JComboBox<>(prioritasOpts);
        cmbPrioritas.setSelectedIndex(1);
        cmbPrioritas.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        String[] statusOpts = {"Belum Dikerjakan", "Sedang Dikerjakan", "Selesai"};
        cmbStatus = new JComboBox<>(statusOpts);
        cmbStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        txtCatatan = new JTextArea(2, 1);
        txtCatatan.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtCatatan.setLineWrap(true);
        txtCatatan.setWrapStyleWord(true);
        txtCatatan.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIHelper.BORDER),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));

        int r = 0;
        tambahField(fields, gbc, "Mata Kuliah *",  cmbMataKuliah,                       r++);
        tambahField(fields, gbc, "Judul Tugas *",  txtJudul,                            r++);
        tambahField(fields, gbc, "Deskripsi",      new JScrollPane(txtDeskripsi),       r++);
        tambahField(fields, gbc, "Deadline *",     spnDeadline,                         r++);
        tambahField(fields, gbc, "Prioritas",      cmbPrioritas,                        r++);
        tambahField(fields, gbc, "Status",         cmbStatus,                           r++);
        tambahField(fields, gbc, "Catatan",        new JScrollPane(txtCatatan),         r);

        JScrollPane scrollForm = new JScrollPane(fields);
        scrollForm.setBorder(null);
        scrollForm.setOpaque(false);
        scrollForm.getViewport().setOpaque(false);
        panel.add(scrollForm, BorderLayout.CENTER);

        // Tombol
        JPanel btnPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));

        btnTambah = UIHelper.buatTombol("➕ Tambah", UIHelper.SUCCESS);
        btnUpdate = UIHelper.buatTombol("✏ Update",  UIHelper.PRIMARY);
        btnHapus  = UIHelper.buatTombol("🗑 Hapus",  UIHelper.DANGER);
        btnBatal  = UIHelper.buatTombol("✖ Batal",   new Color(107, 114, 128));

        btnUpdate.setEnabled(false);
        btnHapus.setEnabled(false);

        btnTambah.addActionListener(e -> doTambah());
        btnUpdate.addActionListener(e -> doUpdate());
        btnHapus.addActionListener(e -> doHapus());
        btnBatal.addActionListener(e -> resetForm());

        btnPanel.add(btnTambah); btnPanel.add(btnUpdate);
        btnPanel.add(btnHapus);  btnPanel.add(btnBatal);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void tambahField(JPanel p, GridBagConstraints gbc, String lbl, JComponent comp, int row) {
        gbc.gridy = row * 2;
        JLabel label = new JLabel(lbl);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        label.setForeground(UIHelper.TEXT_MUTED);
        p.add(label, gbc);
        gbc.gridy = row * 2 + 1;
        p.add(comp, gbc);
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


    public void loadData() {
        String filter = cmbFilterStatus != null
            ? (String) cmbFilterStatus.getSelectedItem() : "Semua Status";

        List<Tugas> list;
        if ("Semua Status".equals(filter)) {
            list = tugasCtrl.getAll();
        } else {
            list = tugasCtrl.getByStatus(Status.fromString(filter));
        }
        tampilkanData(list);
    }

    private void doSearch() {
        String kw = txtSearch.getText().trim();
        tampilkanData(kw.isEmpty() ? tugasCtrl.getAll() : tugasCtrl.search(kw));
    }

    private void tampilkanData(List<Tugas> list) {
        tableModel.setRowCount(0);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (Tugas t : list) {
            tableModel.addRow(new Object[]{
                t.getId(),
                t.getNamaMataKuliah(),
                t.getJudul(),
                t.getPrioritas().getLabel(),
                t.getDeadlineFormatted(),
                t.getStatus().getLabel(),
                t.getSisaHariLabel()
            });
        }
    }

    private void isiForm() {
        int row = table.getSelectedRow();
        if (row < 0) return;

        selectedId = (int) tableModel.getValueAt(row, 0);
        Tugas t = tugasCtrl.getById(selectedId);
        if (t == null) return;

        for (int i = 0; i < cmbMataKuliah.getItemCount(); i++) {
            if (cmbMataKuliah.getItemAt(i).getId() == t.getMataKuliahId()) {
                cmbMataKuliah.setSelectedIndex(i);
                break;
            }
        }

        txtJudul.setText(t.getJudul());
        txtDeskripsi.setText(t.getDeskripsi() != null ? t.getDeskripsi() : "");
        txtCatatan.setText(t.getCatatan() != null ? t.getCatatan() : "");

        java.util.Date d = java.sql.Date.valueOf(t.getDeadline());
        spnDeadline.setValue(d);

        cmbPrioritas.setSelectedItem(t.getPrioritas().getLabel());
        cmbStatus.setSelectedItem(t.getStatus().getLabel());

        btnTambah.setEnabled(false);
        btnUpdate.setEnabled(true);
        btnHapus.setEnabled(true);
        btnSelesai.setEnabled(t.getStatus() != Status.SELESAI);
    }

    private void doTambah() {
        if (!validasi()) return;
        Tugas t = buildFromForm();
        if (tugasCtrl.tambah(t)) {
            UIHelper.showSukses(this, "Tugas berhasil ditambahkan!");
            loadData(); resetForm();
        } else {
            UIHelper.showError(this, "Gagal menambahkan tugas.");
        }
    }

    private void doUpdate() {
        if (!validasi()) return;
        Tugas t = buildFromForm();
        t.setId(selectedId);
        if (tugasCtrl.update(t)) {
            UIHelper.showSukses(this, "Tugas berhasil diperbarui!");
            loadData(); resetForm();
        } else {
            UIHelper.showError(this, "Gagal memperbarui tugas.");
        }
    }

    private void doHapus() {
        if (!UIHelper.konfirmasiHapus(this, txtJudul.getText())) return;
        if (tugasCtrl.hapus(selectedId)) {
            UIHelper.showSukses(this, "Tugas berhasil dihapus!");
            loadData(); resetForm();
        } else {
            UIHelper.showError(this, "Gagal menghapus tugas.");
        }
    }

    private void doTandaiSelesai() {
        if (selectedId < 0) return;
        if (tugasCtrl.updateStatus(selectedId, Status.SELESAI)) {
            UIHelper.showSukses(this, "Tugas ditandai selesai! 🎉");
            loadData(); resetForm();
        }
    }

    private boolean validasi() {
        if (cmbMataKuliah.getSelectedItem() == null) {
            UIHelper.showError(this, "Pilih mata kuliah terlebih dahulu!");
            return false;
        }
        if (txtJudul.getText().trim().isEmpty()) {
            UIHelper.showError(this, "Judul tugas tidak boleh kosong!");
            return false;
        }
        return true;
    }

    private Tugas buildFromForm() {
        MataKuliah mk = (MataKuliah) cmbMataKuliah.getSelectedItem();
        java.util.Date d = (java.util.Date) spnDeadline.getValue();
        LocalDate deadline = d.toInstant()
            .atZone(java.time.ZoneId.systemDefault()).toLocalDate();

        Tugas t = new Tugas();
        t.setMataKuliahId(mk.getId());
        t.setJudul(txtJudul.getText().trim());
        t.setDeskripsi(txtDeskripsi.getText().trim());
        t.setDeadline(deadline);
        t.setPrioritas(Prioritas.fromString((String) cmbPrioritas.getSelectedItem()));
        t.setStatus(Status.fromString((String) cmbStatus.getSelectedItem()));
        t.setCatatan(txtCatatan.getText().trim());
        return t;
    }

    private void refreshMataKuliah() {
        cmbMataKuliah.removeAllItems();
        for (MataKuliah mk : mkCtrl.getAll()) {
            cmbMataKuliah.addItem(mk);
        }
    }

    private void resetForm() {
        selectedId = -1;
        refreshMataKuliah();
        txtJudul.setText("");
        txtDeskripsi.setText("");
        txtCatatan.setText("");
        spnDeadline.setValue(new java.util.Date());
        cmbPrioritas.setSelectedIndex(1);
        cmbStatus.setSelectedIndex(0);
        table.clearSelection();
        btnTambah.setEnabled(true);
        btnUpdate.setEnabled(false);
        btnHapus.setEnabled(false);
        btnSelesai.setEnabled(false);
    }
}
