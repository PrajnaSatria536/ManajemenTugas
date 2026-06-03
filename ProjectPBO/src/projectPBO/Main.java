package projectPBO;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
import view.MainFrame;

import javax.swing.*;


public class Main {

    public static void main(String[] args) {
        // Set Look and Feel ke sistem OS agar tampilan lebih native
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Gunakan default jika gagal
        }

        // Jalankan di Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
