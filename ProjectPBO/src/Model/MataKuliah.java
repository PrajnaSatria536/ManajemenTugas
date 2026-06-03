/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 * Model untuk entitas Mata Kuliah
 * Menerapkan konsep OOP: Encapsulation
 */
public class MataKuliah {

    private int    id;
    private String kode;
    private String nama;
    private int    sks;
    private String dosen;
    private String semester;

    // Constructor kosong
    public MataKuliah() {}

    // Constructor lengkap
    public MataKuliah(int id, String kode, String nama, int sks, String dosen, String semester) {
        this.id       = id;
        this.kode     = kode;
        this.nama     = nama;
        this.sks      = sks;
        this.dosen    = dosen;
        this.semester = semester;
    }

    // Constructor tanpa id (untuk insert baru)
    public MataKuliah(String kode, String nama, int sks, String dosen, String semester) {
        this.kode     = kode;
        this.nama     = nama;
        this.sks      = sks;
        this.dosen    = dosen;
        this.semester = semester;
    }

    // ===== Getters =====
    public int    getId()       { return id; }
    public String getKode()     { return kode; }
    public String getNama()     { return nama; }
    public int    getSks()      { return sks; }
    public String getDosen()    { return dosen; }
    public String getSemester() { return semester; }

    // ===== Setters =====
    public void setId(int id)             { this.id       = id; }
    public void setKode(String kode)      { this.kode     = kode; }
    public void setNama(String nama)      { this.nama     = nama; }
    public void setSks(int sks)           { this.sks      = sks; }
    public void setDosen(String dosen)    { this.dosen    = dosen; }
    public void setSemester(String sem)   { this.semester = sem; }

    @Override
    public String toString() {
        return kode + " - " + nama;
    }
}
