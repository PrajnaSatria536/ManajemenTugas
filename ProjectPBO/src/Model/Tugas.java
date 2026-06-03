/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Model untuk entitas Tugas
 * Menerapkan konsep OOP: Encapsulation + Method utility
 */
public class Tugas {

    // Enum untuk Prioritas
    public enum Prioritas {
        RENDAH("Rendah"), SEDANG("Sedang"), TINGGI("Tinggi");
        private final String label;
        Prioritas(String label) { this.label = label; }
        public String getLabel() { return label; }
        public static Prioritas fromString(String s) {
            for (Prioritas p : values()) if (p.label.equalsIgnoreCase(s)) return p;
            return SEDANG;
        }
    }

    // Enum untuk Status
    public enum Status {
        BELUM("Belum Dikerjakan"), PROSES("Sedang Dikerjakan"), SELESAI("Selesai");
        private final String label;
        Status(String label) { this.label = label; }
        public String getLabel() { return label; }
        public static Status fromString(String s) {
            for (Status st : values()) if (st.label.equalsIgnoreCase(s)) return st;
            return BELUM;
        }
    }

    private int        id;
    private int        mataKuliahId;
    private String     namaMataKuliah; // join field
    private String     judul;
    private String     deskripsi;
    private LocalDate  deadline;
    private Prioritas  prioritas;
    private Status     status;
    private String     catatan;

    // Constructor kosong
    public Tugas() {}

    // Constructor lengkap
    public Tugas(int id, int mataKuliahId, String namaMataKuliah,
                 String judul, String deskripsi, LocalDate deadline,
                 Prioritas prioritas, Status status, String catatan) {
        this.id              = id;
        this.mataKuliahId    = mataKuliahId;
        this.namaMataKuliah  = namaMataKuliah;
        this.judul           = judul;
        this.deskripsi       = deskripsi;
        this.deadline        = deadline;
        this.prioritas       = prioritas;
        this.status          = status;
        this.catatan         = catatan;
    }

    // ===== Business Logic Methods =====

    /** Hitung sisa hari menuju deadline */
    public long getSisaHari() {
        if (deadline == null) return -1;
        return ChronoUnit.DAYS.between(LocalDate.now(), deadline);
    }

    /** Cek apakah tugas sudah melewati deadline */
    public boolean isOverdue() {
        return deadline != null
            && LocalDate.now().isAfter(deadline)
            && status != Status.SELESAI;
    }

    /** Format deadline menjadi string tampilan */
    public String getDeadlineFormatted() {
        if (deadline == null) return "-";
        return deadline.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    /** Label status sisa hari untuk tampilan */
    public String getSisaHariLabel() {
        long sisa = getSisaHari();
        if (status == Status.SELESAI) return "✓ Selesai";
        if (sisa < 0)  return "⚠ Terlambat " + Math.abs(sisa) + " hari";
        if (sisa == 0) return "⚠ Hari ini!";
        if (sisa <= 3) return "⚠ " + sisa + " hari lagi";
        return sisa + " hari lagi";
    }

    // ===== Getters =====
    public int       getId()             { return id; }
    public int       getMataKuliahId()   { return mataKuliahId; }
    public String    getNamaMataKuliah() { return namaMataKuliah; }
    public String    getJudul()          { return judul; }
    public String    getDeskripsi()      { return deskripsi; }
    public LocalDate getDeadline()       { return deadline; }
    public Prioritas getPrioritas()      { return prioritas; }
    public Status    getStatus()         { return status; }
    public String    getCatatan()        { return catatan; }

    // ===== Setters =====
    public void setId(int id)                        { this.id             = id; }
    public void setMataKuliahId(int mataKuliahId)    { this.mataKuliahId   = mataKuliahId; }
    public void setNamaMataKuliah(String nama)        { this.namaMataKuliah = nama; }
    public void setJudul(String judul)               { this.judul          = judul; }
    public void setDeskripsi(String deskripsi)       { this.deskripsi      = deskripsi; }
    public void setDeadline(LocalDate deadline)      { this.deadline       = deadline; }
    public void setPrioritas(Prioritas prioritas)    { this.prioritas      = prioritas; }
    public void setStatus(Status status)             { this.status         = status; }
    public void setCatatan(String catatan)           { this.catatan        = catatan; }

    @Override
    public String toString() {
        return "[" + prioritas.getLabel() + "] " + judul + " - " + getDeadlineFormatted();
    }
}
