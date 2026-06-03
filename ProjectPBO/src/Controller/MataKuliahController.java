/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import database.DatabaseConnection;
import model.MataKuliah;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller untuk operasi CRUD Mata Kuliah
 * Menerapkan konsep MVC: Controller layer
 */
public class MataKuliahController {

    private final Connection conn;

    public MataKuliahController() {
        this.conn = DatabaseConnection.getInstance().getConnection();
    }

    // ===== CREATE =====
    public boolean tambah(MataKuliah mk) {
        String sql = "INSERT INTO mata_kuliah (kode, nama, sks, dosen, semester) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, mk.getKode());
            ps.setString(2, mk.getNama());
            ps.setInt(3, mk.getSks());
            ps.setString(4, mk.getDosen());
            ps.setString(5, mk.getSemester());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error tambah mata kuliah: " + e.getMessage());
            return false;
        }
    }

    // ===== READ ALL =====
    public List<MataKuliah> getAll() {
        List<MataKuliah> list = new ArrayList<>();
        String sql = "SELECT * FROM mata_kuliah ORDER BY kode";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getAll mata kuliah: " + e.getMessage());
        }
        return list;
    }

    // ===== READ BY ID =====
    public MataKuliah getById(int id) {
        String sql = "SELECT * FROM mata_kuliah WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.err.println("Error getById mata kuliah: " + e.getMessage());
        }
        return null;
    }

    // ===== UPDATE =====
    public boolean update(MataKuliah mk) {
        String sql = "UPDATE mata_kuliah SET kode=?, nama=?, sks=?, dosen=?, semester=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, mk.getKode());
            ps.setString(2, mk.getNama());
            ps.setInt(3, mk.getSks());
            ps.setString(4, mk.getDosen());
            ps.setString(5, mk.getSemester());
            ps.setInt(6, mk.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error update mata kuliah: " + e.getMessage());
            return false;
        }
    }

    // ===== DELETE =====
    public boolean hapus(int id) {
        String sql = "DELETE FROM mata_kuliah WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error hapus mata kuliah: " + e.getMessage());
            return false;
        }
    }

    // ===== SEARCH =====
    public List<MataKuliah> search(String keyword) {
        List<MataKuliah> list = new ArrayList<>();
        String sql = "SELECT * FROM mata_kuliah WHERE nama LIKE ? OR kode LIKE ? ORDER BY kode";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            String kw = "%" + keyword + "%";
            ps.setString(1, kw);
            ps.setString(2, kw);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("Error search mata kuliah: " + e.getMessage());
        }
        return list;
    }

    // ===== Cek apakah kode sudah ada =====
    public boolean isKodeExists(String kode, int excludeId) {
        String sql = "SELECT COUNT(*) FROM mata_kuliah WHERE kode = ? AND id != ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kode);
            ps.setInt(2, excludeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("Error isKodeExists: " + e.getMessage());
        }
        return false;
    }

    // ===== Helper: Map ResultSet ke Object =====
    private MataKuliah mapRow(ResultSet rs) throws SQLException {
        return new MataKuliah(
            rs.getInt("id"),
            rs.getString("kode"),
            rs.getString("nama"),
            rs.getInt("sks"),
            rs.getString("dosen"),
            rs.getString("semester")
        );
    }
}
