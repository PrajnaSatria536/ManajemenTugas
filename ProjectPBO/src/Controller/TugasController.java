/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import database.DatabaseConnection;
import model.Tugas;
import model.Tugas.Prioritas;
import model.Tugas.Status;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class TugasController {

    private final Connection conn;

    public TugasController() {
        this.conn = DatabaseConnection.getInstance().getConnection();
    }

    
    public boolean tambah(Tugas tugas) {
        String sql = "INSERT INTO tugas (mata_kuliah_id, judul, deskripsi, deadline, prioritas, status, catatan) "
                   + "VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tugas.getMataKuliahId());
            ps.setString(2, tugas.getJudul());
            ps.setString(3, tugas.getDeskripsi());
            ps.setDate(4, Date.valueOf(tugas.getDeadline()));
            ps.setString(5, tugas.getPrioritas().getLabel());
            ps.setString(6, tugas.getStatus().getLabel());
            ps.setString(7, tugas.getCatatan());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error tambah tugas: " + e.getMessage());
            return false;
        }
    }


    public List<Tugas> getAll() {
        List<Tugas> list = new ArrayList<>();
        String sql = "SELECT t.*, mk.nama AS nama_mk FROM tugas t "
                   + "JOIN mata_kuliah mk ON t.mata_kuliah_id = mk.id "
                   + "ORDER BY t.deadline ASC";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("Error getAll tugas: " + e.getMessage());
        }
        return list;
    }


    public Tugas getById(int id) {
        String sql = "SELECT t.*, mk.nama AS nama_mk FROM tugas t "
                   + "JOIN mata_kuliah mk ON t.mata_kuliah_id = mk.id WHERE t.id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.err.println("Error getById tugas: " + e.getMessage());
        }
        return null;
    }


    public List<Tugas> getByMataKuliah(int mataKuliahId) {
        List<Tugas> list = new ArrayList<>();
        String sql = "SELECT t.*, mk.nama AS nama_mk FROM tugas t "
                   + "JOIN mata_kuliah mk ON t.mata_kuliah_id = mk.id "
                   + "WHERE t.mata_kuliah_id = ? ORDER BY t.deadline ASC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, mataKuliahId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("Error getByMataKuliah: " + e.getMessage());
        }
        return list;
    }


    public List<Tugas> getByStatus(Status status) {
        List<Tugas> list = new ArrayList<>();
        String sql = "SELECT t.*, mk.nama AS nama_mk FROM tugas t "
                   + "JOIN mata_kuliah mk ON t.mata_kuliah_id = mk.id "
                   + "WHERE t.status = ? ORDER BY t.deadline ASC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.getLabel());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("Error getByStatus: " + e.getMessage());
        }
        return list;
    }


    public boolean update(Tugas tugas) {
        String sql = "UPDATE tugas SET mata_kuliah_id=?, judul=?, deskripsi=?, deadline=?, "
                   + "prioritas=?, status=?, catatan=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tugas.getMataKuliahId());
            ps.setString(2, tugas.getJudul());
            ps.setString(3, tugas.getDeskripsi());
            ps.setDate(4, Date.valueOf(tugas.getDeadline()));
            ps.setString(5, tugas.getPrioritas().getLabel());
            ps.setString(6, tugas.getStatus().getLabel());
            ps.setString(7, tugas.getCatatan());
            ps.setInt(8, tugas.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error update tugas: " + e.getMessage());
            return false;
        }
    }


    public boolean updateStatus(int id, Status status) {
        String sql = "UPDATE tugas SET status = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.getLabel());
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updateStatus: " + e.getMessage());
            return false;
        }
    }


    public boolean hapus(int id) {
        String sql = "DELETE FROM tugas WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error hapus tugas: " + e.getMessage());
            return false;
        }
    }


    public List<Tugas> search(String keyword) {
        List<Tugas> list = new ArrayList<>();
        String sql = "SELECT t.*, mk.nama AS nama_mk FROM tugas t "
                   + "JOIN mata_kuliah mk ON t.mata_kuliah_id = mk.id "
                   + "WHERE t.judul LIKE ? OR mk.nama LIKE ? ORDER BY t.deadline ASC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            String kw = "%" + keyword + "%";
            ps.setString(1, kw);
            ps.setString(2, kw);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("Error search tugas: " + e.getMessage());
        }
        return list;
    }

    public int countByStatus(Status status) {
        String sql = "SELECT COUNT(*) FROM tugas WHERE status = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.getLabel());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Error countByStatus: " + e.getMessage());
        }
        return 0;
    }

    public int countOverdue() {
        String sql = "SELECT COUNT(*) FROM tugas WHERE deadline < CURDATE() AND status != 'Selesai'";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Error countOverdue: " + e.getMessage());
        }
        return 0;
    }

    private Tugas mapRow(ResultSet rs) throws SQLException {
        return new Tugas(
            rs.getInt("id"),
            rs.getInt("mata_kuliah_id"),
            rs.getString("nama_mk"),
            rs.getString("judul"),
            rs.getString("deskripsi"),
            rs.getDate("deadline").toLocalDate(),
            Prioritas.fromString(rs.getString("prioritas")),
            Status.fromString(rs.getString("status")),
            rs.getString("catatan")
        );
    }
}
