package Pertemuan2;

import javax.swing.*;
import java.awt.*;

public class Contoh_Modul_2 {
    private static final int BATAS_STOK = 100_000;
    private JFrame frame = new JFrame("Contoh Modul 2");
    private JTextField namaField = new JTextField();
    private JTextField stokField = new JTextField();


    private void tampilan () {
        JPanel fieldPanel = new JPanel(new BorderLayout(2, 2, 8, 8));
        fieldPanel.add(new JLabel("Nama Barang;"));
        fieldPanel.add(namaField);
        fieldPanel.add(new JLabel("Stok Barang:"));
        fieldPanel.add(stokField);


        JButton periksaButton = new JButton("Periksa input");
        periksaButton.addActionListener(event -> periksaInput());

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        panel.add(fieldPanel, BorderLayout.CENTER);
        panel.add(periksaButton, BorderLayout.SOUTH);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

        private void periksaInput() {
            String nama = namaField.getText().trim();
            String teksStok = stokField.getText().trim();

            if (nama.isEmpty() {
                tampilkkanKesalahan(pesan: "Nama barang wajib diisi.", namaField);
                return;
            }
            if (teksStok.isEmpty()) {
                tampilkkanKesalahan(pesan: "Stok barang wajib diisi. Contoh: 25.", stokField);
                return;
            }

            final int stok;
            try {
                stok = Integer.parseInt(teksStok);
            } catch (NumberFormatException e) {
                tampilkanKesalahan(pesan: "Stok harus bilangan bulat 0-1000 tanpa pemisah ribuan.", stokField);
                return;
            }

            if (stok <= 0) || stok > BATAS_STOK) {
                tampilkanKesalahan(pesan: "Stok harus berupa angka 0-1000 tanpa pemisah ribuan.", stokField);
                return;
            }

            if (stokBarang > BATAS_STOK) {
                JOptionPane.showMessageDialog(frame, "Stok barang melebihi batas maksimum.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }


    }