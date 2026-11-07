package p02;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class Main {
    private static final String FORM = "FORM";
    private static final String RINGKASAN = "RINGKASAN";
    private static final String BANTUAN = "BANTUAN";

    private final JFrame frame = new JFrame("Pendaftaran Kegiatan");
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cards = new JPanel(cardLayout);

    private final JTextField namaField = new JTextField(24);
    private final JTextField emailField = new JTextField(24);
    private final JTextField umurField = new JTextField(24);
    private final JComboBox<String> kegiatanCombo = new JComboBox<>(
            new String[]{"-- Pilih kegiatan --", "Seminar Teknologi",
                         "Workshop Pemrograman", "Pelatihan Cybersecurity",
                         "Kompetisi IT"}
    );

    private final JLabel pesanLabel = new JLabel(" ");
    private final JTextArea ringkasanArea = new JTextArea(10, 38);
    private boolean dirty = false;
    private boolean updatingForm = false;

    private Main() {
        cards.add(buatPanelForm(), FORM);
        cards.add(buatPanelRingkasan(), RINGKASAN);
        cards.add(buatPanelBantuan(), BANTUAN);

        frame.setContentPane(cards);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                konfirmasiKeluar();
            }
        });

        pasangPemantauPerubahan();
        frame.setMinimumSize(new Dimension(620, 500));
        frame.pack();
        frame.setLocationRelativeTo(null);
        cardLayout.show(cards, FORM);
    }

    private JPanel buatPanelForm() {
        JPanel panel = panelDasar();
        JLabel judul = judul("Formulir Pendaftaran");
        JLabel subjudul = new JLabel("Silakan lengkapi data diri dan pilih kegiatan.");
        subjudul.setForeground(new Color(95, 105, 120));

        JPanel header = new JPanel(new BorderLayout(0, 5));
        header.setOpaque(false);
        header.add(judul, BorderLayout.NORTH);
        header.add(subjudul, BorderLayout.SOUTH);

        JPanel fields = new JPanel(new GridBagLayout());
        fields.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(9, 4, 9, 12);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        tambahField(fields, gbc, 0, "Nama lengkap", namaField);
        tambahField(fields, gbc, 1, "Email", emailField);
        tambahField(fields, gbc, 2, "Umur", umurField);
        tambahField(fields, gbc, 3, "Pilihan kegiatan", kegiatanCombo);

        pesanLabel.setForeground(new Color(190, 45, 45));
        pesanLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));

        JButton simpanButton = tombolUtama("Simpan / Tampilkan");
        JButton resetButton = tombol("Reset");
        JButton bantuanButton = tombol("Bantuan");
        JButton keluarButton = tombol("Keluar");

        simpanButton.addActionListener(event -> simpanDanTampilkan());
        resetButton.addActionListener(event -> resetForm());
        bantuanButton.addActionListener(event -> {
            cardLayout.show(cards, BANTUAN);
        });
        keluarButton.addActionListener(event -> konfirmasiKeluar());

        JPanel actions = new JPanel();
        actions.setOpaque(false);
        actions.add(simpanButton);
        actions.add(resetButton);
        actions.add(bantuanButton);
        actions.add(keluarButton);

        JPanel bottom = new JPanel(new BorderLayout(0, 8));
        bottom.setOpaque(false);
        bottom.add(pesanLabel, BorderLayout.NORTH);
        bottom.add(actions, BorderLayout.SOUTH);

        panel.add(header, BorderLayout.NORTH);
        panel.add(fields, BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buatPanelRingkasan() {
        JPanel panel = panelDasar();
        panel.add(judul("Ringkasan Pendaftaran"), BorderLayout.NORTH);

        ringkasanArea.setEditable(false);
        ringkasanArea.setLineWrap(true);
        ringkasanArea.setWrapStyleWord(true);
        ringkasanArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        ringkasanArea.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JButton kembaliButton = tombolUtama("Kembali ke Formulir");
        kembaliButton.addActionListener(event -> {
            cardLayout.show(cards, FORM);
            namaField.requestFocusInWindow();
        });

        panel.add(new JScrollPane(ringkasanArea), BorderLayout.CENTER);
        panel.add(kembaliButton, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buatPanelBantuan() {
        JPanel panel = panelDasar();
        panel.add(judul("Bantuan Penggunaan"), BorderLayout.NORTH);

        JTextArea bantuanArea = new JTextArea(
                "PETUNJUK PENGGUNAAN APLIKASI\n\n"
                + "1. Isi nama lengkap, email, dan umur pada formulir.\n"
                + "2. Pilih salah satu kegiatan yang tersedia.\n"
                + "3. Tekan tombol Simpan / Tampilkan untuk memeriksa data.\n"
                + "4. Jika data valid, aplikasi menampilkan halaman ringkasan.\n"
                + "5. Tekan Kembali ke Formulir untuk melihat atau mengubah data.\n"
                + "6. Tombol Reset digunakan untuk mengosongkan formulir.\n"
                + "7. Tombol Keluar digunakan untuk menutup aplikasi.\n\n"
                + "ATURAN VALIDASI\n"
                + "- Nama tidak boleh kosong.\n"
                + "- Email harus menggunakan format yang wajar, misalnya nama@email.com.\n"
                + "- Umur harus berupa bilangan bulat antara 15 sampai 100.\n"
                + "- Kegiatan wajib dipilih.\n\n"
                + "Catatan: Data pendaftaran pada aplikasi latihan ini disimpan sementara "
                + "di memori aplikasi, bukan ke database atau berkas."
        );
        bantuanArea.setEditable(false);
        bantuanArea.setLineWrap(true);
        bantuanArea.setWrapStyleWord(true);
        bantuanArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        bantuanArea.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JButton kembaliButton = tombolUtama("Kembali ke Formulir");
        kembaliButton.addActionListener(event -> cardLayout.show(cards, FORM));

        panel.add(new JScrollPane(bantuanArea), BorderLayout.CENTER);
        panel.add(kembaliButton, BorderLayout.SOUTH);
        return panel;
    }

    private void tambahField(JPanel panel, GridBagConstraints gbc,
                             int baris, String label, java.awt.Component field) {
        gbc.gridx = 0;
        gbc.gridy = baris;
        gbc.weightx = 0.30;
        panel.add(new JLabel(label + ":"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.70;
        if (field instanceof JTextField) {
            ((JTextField) field).setFont(new Font("SansSerif", Font.PLAIN, 14));
        }
        field.setPreferredSize(new Dimension(300, 34));
        panel.add(field, gbc);
    }

    private JPanel panelDasar() {
        JPanel panel = new JPanel(new BorderLayout(18, 22));
        panel.setBorder(BorderFactory.createEmptyBorder(28, 32, 28, 32));
        panel.setBackground(new Color(247, 249, 252));
        return panel;
    }

    private JLabel judul(String teks) {
        JLabel label = new JLabel(teks);
        label.setFont(new Font("SansSerif", Font.BOLD, 22));
        label.setForeground(new Color(35, 65, 105));
        return label;
    }

    private JButton tombol(String teks) {
        JButton button = new JButton(teks);
        button.setFocusPainted(false);
        return button;
    }

    private JButton tombolUtama(String teks) {
        JButton button = tombol(teks);
        button.setBackground(new Color(43, 103, 173));
        button.setForeground(Color.WHITE);
        return button;
    }

    private void pasangPemantauPerubahan() {
        DocumentListener listener = new DocumentListener() {
            private void berubah() {
                if (!updatingForm) {
                    dirty = true;
                    pesanLabel.setText("Ada perubahan yang belum disimpan.");
                }
            }
            @Override public void insertUpdate(DocumentEvent e) { berubah(); }
            @Override public void removeUpdate(DocumentEvent e) { berubah(); }
            @Override public void changedUpdate(DocumentEvent e) { berubah(); }
        };

        namaField.getDocument().addDocumentListener(listener);
        emailField.getDocument().addDocumentListener(listener);
        umurField.getDocument().addDocumentListener(listener);
        kegiatanCombo.addActionListener(event -> {
            if (!updatingForm) {
                dirty = true;
                pesanLabel.setText("Ada perubahan yang belum disimpan.");
            }
        });
    }

    private void simpanDanTampilkan() {
        String nama = namaField.getText().trim();
        String email = emailField.getText().trim();
        String teksUmur = umurField.getText().trim();
        Object pilihan = kegiatanCombo.getSelectedItem();

        if (nama.isEmpty()) {
            tampilkanKesalahan("Nama lengkap wajib diisi.", namaField);
            return;
        }

        if (email.isEmpty() || !email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            tampilkanKesalahan("Format email belum benar. Contoh: nama@email.com.", emailField);
            return;
        }

        if (teksUmur.isEmpty()) {
            tampilkanKesalahan("Umur wajib diisi dengan bilangan bulat 15–100.", umurField);
            return;
        }

        final int umur;
        try {
            umur = Integer.parseInt(teksUmur);
        } catch (NumberFormatException e) {
            tampilkanKesalahan("Umur harus berupa bilangan bulat. Contoh: 20.", umurField);
            return;
        }

        if (umur < 15 || umur > 100) {
            tampilkanKesalahan("Umur harus berada dalam rentang 15–100 tahun.", umurField);
            return;
        }

        if (pilihan == null || pilihan.toString().startsWith("--")) {
            tampilkanKesalahan("Pilihan kegiatan wajib ditentukan.", kegiatanCombo);
            return;
        }

        String ringkasan = "DATA PENDAFTARAN KEGIATAN\n"
                + "====================================\n"
                + "Nama lengkap : " + nama + "\n"
                + "Email        : " + email + "\n"
                + "Umur         : " + umur + " tahun\n"
                + "Kegiatan     : " + pilihan + "\n\n"
                + "Status: Data valid dan tersimpan sementara selama aplikasi berjalan.";
        ringkasanArea.setText(ringkasan);
        ringkasanArea.setCaretPosition(0);
        dirty = false;
        pesanLabel.setText(" ");
        cardLayout.show(cards, RINGKASAN);
    }

    private void tampilkanKesalahan(String pesan, java.awt.Component komponen) {
        JOptionPane.showMessageDialog(frame, pesan, "Validasi Input",
                                      JOptionPane.WARNING_MESSAGE);
        cardLayout.show(cards, FORM);
        komponen.requestFocusInWindow();
        if (komponen instanceof JTextField) {
            ((JTextField) komponen).selectAll();
        }
    }

    private void resetForm() {
        int pilihan = JOptionPane.showConfirmDialog(
                frame, "Kosongkan seluruh isian formulir?",
                "Konfirmasi Reset", JOptionPane.YES_NO_OPTION
        );
        if (pilihan == JOptionPane.YES_OPTION) {
            updatingForm = true;
            namaField.setText("");
            emailField.setText("");
            umurField.setText("");
            kegiatanCombo.setSelectedIndex(0);
            updatingForm = false;
            dirty = true;
            pesanLabel.setText("Formulir telah direset. Perubahan belum disimpan.");
            namaField.requestFocusInWindow();
        }
    }

    private void konfirmasiKeluar() {
        if (dirty) {
            int pilihan = JOptionPane.showConfirmDialog(
                    frame,
                    "Ada perubahan yang belum disimpan. Yakin ingin keluar?",
                    "Konfirmasi Keluar",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            if (pilihan != JOptionPane.YES_OPTION) {
                return;
            }
        }
        frame.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main aplikasi = new Main();
            aplikasi.frame.setVisible(true);
        });
    }
}
