package p09;

// Import library untuk mengatur tampilan dan layout aplikasi
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

// Import komponen Java Swing
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

// Import listener untuk mendeteksi perubahan pada input teks
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class Main {

    // Konstanta untuk nama setiap halaman pada CardLayout
    private static final String FORM = "FORM";
    private static final String RINGKASAN = "RINGKASAN";
    private static final String BANTUAN = "BANTUAN";

    // Membuat jendela utama aplikasi
    private final JFrame frame =
            new JFrame("Aplikasi Pendaftaran Kegiatan");

    // Membuat CardLayout untuk mengatur perpindahan halaman
    private final CardLayout cardLayout = new CardLayout();

    // Panel utama yang menampung seluruh halaman
    private final JPanel cards = new JPanel(cardLayout);

    // Komponen input pada formulir pendaftaran
    private final JTextField namaField = new JTextField(24);
    private final JTextField emailField = new JTextField(24);
    private final JTextField umurField = new JTextField(24);

    // Membuat pilihan kegiatan menggunakan JComboBox
    private final JComboBox<String> kegiatanCombo =
            new JComboBox<>(new String[]{
                "-- Pilih kegiatan --",
                "Seminar Teknologi",
                "Workshop Pemrograman",
                "Pelatihan Cybersecurity",
                "Kompetisi IT"
            });

    // Label untuk menampilkan informasi atau pesan pada formulir
    private final JLabel pesanLabel = new JLabel(" ");

    // Area teks untuk menampilkan hasil ringkasan pendaftaran
    private final JTextArea ringkasanArea = new JTextArea(10, 38);

    // Menandai apakah terdapat perubahan data yang belum ditampilkan
    private boolean dirty = false;

    // Mencegah perubahan otomatis saat program melakukan reset
    private boolean updatingForm = false;


    // Constructor untuk menginisialisasi seluruh tampilan aplikasi
    private Main() {

        // Menambahkan halaman formulir ke CardLayout
        cards.add(buatPanelForm(), FORM);

        // Menambahkan halaman ringkasan ke CardLayout
        cards.add(buatPanelRingkasan(), RINGKASAN);

        // Menambahkan halaman bantuan ke CardLayout
        cards.add(buatPanelBantuan(), BANTUAN);

        // Menjadikan CardLayout sebagai tampilan utama JFrame
        frame.setContentPane(cards);

        // Mencegah aplikasi langsung tertutup ketika tombol X ditekan
        // Penutupan akan ditangani melalui konfirmasi keluar
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // Menambahkan event ketika pengguna menekan tombol X
        frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent event) {

                // Memanggil metode konfirmasi keluar
                konfirmasiKeluar();
            }
        });

        // Memasang listener untuk mendeteksi perubahan input
        pasangPemantauPerubahan();

        // Mengatur ukuran minimum jendela
        frame.setMinimumSize(new Dimension(620, 500));

        // Menyesuaikan ukuran jendela dengan isi komponennya
        frame.pack();

        // Menempatkan jendela di tengah layar
        frame.setLocationRelativeTo(null);

        // Menampilkan halaman formulir saat aplikasi pertama kali dibuka
        cardLayout.show(cards, FORM);
    }


    // =====================================================
    // MEMBUAT HALAMAN FORMULIR PENDAFTARAN
    // =====================================================

    private JPanel buatPanelForm() {

        // Membuat panel utama dengan BorderLayout
        JPanel panel = panelDasar();

        // Membuat judul halaman
        JLabel judul = judul("Formulir Pendaftaran");

        // Membuat keterangan di bawah judul
        JLabel subjudul = new JLabel(
                "Silakan lengkapi data diri dan pilih kegiatan."
        );

        subjudul.setForeground(new Color(95, 105, 120));

        // Menggabungkan judul dan subjudul
        JPanel header = new JPanel(new BorderLayout(0, 5));
        header.setOpaque(false);
        header.add(judul, BorderLayout.NORTH);
        header.add(subjudul, BorderLayout.SOUTH);

        // Membuat panel untuk menampung seluruh input
        JPanel fields = new JPanel(new GridBagLayout());
        fields.setOpaque(false);

        // Mengatur posisi dan jarak setiap komponen input
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(9, 4, 9, 12);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Menambahkan label dan komponen input ke formulir
        tambahField(fields, gbc, 0, "Nama lengkap", namaField);
        tambahField(fields, gbc, 1, "Email", emailField);
        tambahField(fields, gbc, 2, "Umur", umurField);
        tambahField(fields, gbc, 3, "Pilihan kegiatan", kegiatanCombo);

        // Mengatur tampilan pesan validasi
        pesanLabel.setForeground(new Color(190, 45, 45));
        pesanLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));

        // Membuat tombol-tombol pada halaman formulir
        JButton simpanButton = tombolUtama("Simpan / Tampilkan");
        JButton resetButton = tombol("Reset");
        JButton bantuanButton = tombol("Bantuan");
        JButton keluarButton = tombol("Keluar");

        // Event tombol Simpan / Tampilkan
        // Memanggil metode validasi sebelum menampilkan ringkasan
        simpanButton.addActionListener(event ->
                simpanDanTampilkan()
        );

        // Event tombol Reset
        resetButton.addActionListener(event ->
                resetForm()
        );

        // Event tombol Bantuan
        bantuanButton.addActionListener(event ->
                cardLayout.show(cards, BANTUAN)
        );

        // Event tombol Keluar
        keluarButton.addActionListener(event ->
                konfirmasiKeluar()
        );

        // Membuat panel untuk menampung tombol
        JPanel actions = new JPanel();
        actions.setOpaque(false);

        actions.add(simpanButton);
        actions.add(resetButton);
        actions.add(bantuanButton);
        actions.add(keluarButton);

        // Membuat panel bagian bawah
        JPanel bottom = new JPanel(new BorderLayout(0, 8));
        bottom.setOpaque(false);

        // Menampilkan pesan validasi
        bottom.add(pesanLabel, BorderLayout.NORTH);

        // Menampilkan tombol-tombol
        bottom.add(actions, BorderLayout.SOUTH);

        // Menyusun seluruh komponen pada halaman formulir
        panel.add(header, BorderLayout.NORTH);
        panel.add(fields, BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);

        return panel;
    }


    // =====================================================
    // MEMBUAT HALAMAN RINGKASAN
    // =====================================================

    private JPanel buatPanelRingkasan() {

        // Membuat panel utama halaman ringkasan
        JPanel panel = panelDasar();

        // Menambahkan judul halaman
        panel.add(
                judul("Ringkasan Pendaftaran"),
                BorderLayout.NORTH
        );

        // Mengatur area teks agar tidak dapat diedit
        ringkasanArea.setEditable(false);

        // Mengaktifkan pemotongan teks otomatis
        ringkasanArea.setLineWrap(true);
        ringkasanArea.setWrapStyleWord(true);

        // Mengatur jenis dan ukuran font
        ringkasanArea.setFont(
                new Font("Monospaced", Font.PLAIN, 14)
        );

        // Memberikan jarak pada area ringkasan
        ringkasanArea.setBorder(
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        );

        // Membuat tombol kembali ke formulir
        JButton kembaliButton = tombolUtama("Kembali ke Formulir");

        // Event tombol Kembali
        kembaliButton.addActionListener(event -> {

            // Menampilkan kembali halaman formulir
            cardLayout.show(cards, FORM);

            // Mengarahkan kursor ke input nama
            namaField.requestFocusInWindow();
        });

        // Menambahkan area ringkasan dengan kemampuan scroll
        panel.add(
                new JScrollPane(ringkasanArea),
                BorderLayout.CENTER
        );

        // Menambahkan tombol kembali
        panel.add(kembaliButton, BorderLayout.SOUTH);

        return panel;
    }


    // =====================================================
    // MEMBUAT HALAMAN BANTUAN
    // =====================================================

    private JPanel buatPanelBantuan() {

        // Membuat panel utama halaman bantuan
        JPanel panel = panelDasar();

        // Menambahkan judul halaman
        panel.add(
                judul("Bantuan Penggunaan"),
                BorderLayout.NORTH
        );

        // Membuat teks petunjuk penggunaan aplikasi
        JTextArea bantuanArea = new JTextArea(
                "PETUNJUK PENGGUNAAN APLIKASI\n\n"

                + "1. Isi nama lengkap, email, dan umur.\n"
                + "2. Pilih salah satu kegiatan yang tersedia.\n"
                + "3. Tekan Simpan / Tampilkan untuk memeriksa data.\n"
                + "4. Jika data valid, ringkasan akan ditampilkan.\n"
                + "5. Tekan Kembali untuk melihat atau mengubah data.\n"
                + "6. Tombol Reset digunakan untuk mengosongkan formulir.\n"
                + "7. Tombol Keluar digunakan untuk menutup aplikasi.\n\n"

                + "ATURAN VALIDASI\n"
                + "- Nama wajib diisi.\n"
                + "- Email harus memiliki format yang benar.\n"
                + "- Umur harus berupa bilangan bulat 15-100.\n"
                + "- Kegiatan wajib dipilih.\n\n"

                + "Catatan: Data hanya disimpan sementara "
                + "selama aplikasi berjalan."
        );

        // Mengatur area bantuan agar tidak dapat diedit
        bantuanArea.setEditable(false);

        // Mengaktifkan pemotongan teks otomatis
        bantuanArea.setLineWrap(true);
        bantuanArea.setWrapStyleWord(true);

        // Mengatur font teks bantuan
        bantuanArea.setFont(
                new Font("SansSerif", Font.PLAIN, 14)
        );

        // Memberikan jarak pada area teks
        bantuanArea.setBorder(
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        );

        // Membuat tombol kembali ke formulir
        JButton kembaliButton = tombolUtama("Kembali ke Formulir");

        // Event tombol kembali
        kembaliButton.addActionListener(event ->
                cardLayout.show(cards, FORM)
        );

        // Menambahkan area bantuan dengan scroll
        panel.add(
                new JScrollPane(bantuanArea),
                BorderLayout.CENTER
        );

        // Menambahkan tombol kembali
        panel.add(kembaliButton, BorderLayout.SOUTH);

        return panel;
    }


    // =====================================================
    // METODE UNTUK MENAMBAHKAN INPUT FORMULIR
    // =====================================================

    private void tambahField(
            JPanel panel,
            GridBagConstraints gbc,
            int baris,
            String label,
            java.awt.Component field
    ) {

        // Mengatur posisi label
        gbc.gridx = 0;
        gbc.gridy = baris;
        gbc.weightx = 0.30;

        // Menambahkan label ke panel
        panel.add(new JLabel(label + ":"), gbc);

        // Mengatur posisi komponen input
        gbc.gridx = 1;
        gbc.weightx = 0.70;

        // Mengatur font input teks
        if (field instanceof JTextField) {
            ((JTextField) field).setFont(
                    new Font("SansSerif", Font.PLAIN, 14)
            );
        }

        // Mengatur ukuran komponen input
        field.setPreferredSize(new Dimension(300, 34));

        // Menambahkan komponen input ke panel
        panel.add(field, gbc);
    }


    // =====================================================
    // METODE UNTUK MENGATUR TAMPILAN PANEL
    // =====================================================

    private JPanel panelDasar() {

        // Membuat panel dengan BorderLayout
        JPanel panel = new JPanel(
                new BorderLayout(18, 22)
        );

        // Mengatur jarak tepi panel
        panel.setBorder(
                BorderFactory.createEmptyBorder(28, 32, 28, 32)
        );

        // Mengatur warna latar belakang
        panel.setBackground(new Color(247, 249, 252));

        return panel;
    }


    // Membuat label judul dengan format tertentu
    private JLabel judul(String teks) {

        JLabel label = new JLabel(teks);

        // Mengatur jenis, ukuran, dan ketebalan font
        label.setFont(
                new Font("SansSerif", Font.BOLD, 22)
        );

        // Mengatur warna teks
        label.setForeground(new Color(35, 65, 105));

        return label;
    }


    // Membuat tombol biasa
    private JButton tombol(String teks) {

        JButton button = new JButton(teks);

        // Menghilangkan efek fokus bawaan tombol
        button.setFocusPainted(false);

        return button;
    }


    // Membuat tombol utama dengan warna khusus
    private JButton tombolUtama(String teks) {

        JButton button = tombol(teks);

        // Mengatur warna latar belakang tombol
        button.setBackground(new Color(43, 103, 173));

        // Mengatur warna tulisan tombol
        button.setForeground(Color.WHITE);

        return button;
    }


    // =====================================================
    // EVENT PEMANTAU PERUBAHAN INPUT
    // =====================================================

    private void pasangPemantauPerubahan() {

        // Membuat DocumentListener untuk memantau perubahan teks
        DocumentListener listener = new DocumentListener() {

            // Metode yang dijalankan ketika teks berubah
            private void berubah() {

                // Memeriksa apakah perubahan berasal dari pengguna
                if (!updatingForm) {

                    // Menandai bahwa ada perubahan yang belum ditampilkan
                    dirty = true;

                    // Menampilkan informasi perubahan
                    pesanLabel.setText(
                            "Ada perubahan yang belum disimpan."
                    );
                }
            }

            // Event ketika teks ditambahkan
            @Override
            public void insertUpdate(DocumentEvent e) {
                berubah();
            }

            // Event ketika teks dihapus
            @Override
            public void removeUpdate(DocumentEvent e) {
                berubah();
            }

            // Event ketika atribut dokumen berubah
            @Override
            public void changedUpdate(DocumentEvent e) {
                berubah();
            }
        };

        // Memasang listener pada setiap input teks
        namaField.getDocument().addDocumentListener(listener);
        emailField.getDocument().addDocumentListener(listener);
        umurField.getDocument().addDocumentListener(listener);

        // Memantau perubahan pilihan kegiatan
        kegiatanCombo.addActionListener(event -> {

            if (!updatingForm) {

                // Menandai adanya perubahan data
                dirty = true;

                pesanLabel.setText(
                        "Ada perubahan yang belum disimpan."
                );
            }
        });
    }


    // =====================================================
    // VALIDASI INPUT DAN MENAMPILKAN RINGKASAN
    // =====================================================

    private void simpanDanTampilkan() {

        // Mengambil seluruh nilai dari komponen input
        String nama = namaField.getText().trim();
        String email = emailField.getText().trim();
        String teksUmur = umurField.getText().trim();

        // Mengambil pilihan kegiatan
        Object pilihan = kegiatanCombo.getSelectedItem();


        // VALIDASI 1: NAMA WAJIB DIISI

        if (nama.isEmpty()) {

            tampilkanKesalahan(
                    "Nama lengkap wajib diisi.",
                    namaField
            );

            // Menghentikan proses jika nama kosong
            return;
        }


        // VALIDASI 2: FORMAT EMAIL

        // Memeriksa apakah email memiliki format yang wajar
        if (email.isEmpty()
                || !email.matches(
                "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
                )) {

            tampilkanKesalahan(
                    "Format email belum benar. Contoh: nama@email.com.",
                    emailField
            );

            // Menghentikan proses jika email tidak valid
            return;
        }


        // VALIDASI 3: UMUR WAJIB DIISI

        if (teksUmur.isEmpty()) {

            tampilkanKesalahan(
                    "Umur wajib diisi dengan bilangan bulat 15-100.",
                    umurField
            );

            return;
        }


        // VALIDASI 4: UMUR HARUS BERUPA BILANGAN BULAT

        final int umur;

        try {

            // Mengubah teks umur menjadi tipe data integer
            umur = Integer.parseInt(teksUmur);

        } catch (NumberFormatException e) {

            // Menampilkan pesan jika input bukan angka bulat
            tampilkanKesalahan(
                    "Umur harus berupa bilangan bulat. Contoh: 20.",
                    umurField
            );

            return;
        }


        // VALIDASI 5: BATAS UMUR 15-100

        if (umur < 15 || umur > 100) {

            tampilkanKesalahan(
                    "Umur harus berada dalam rentang 15-100 tahun.",
                    umurField
            );

            return;
        }


        // VALIDASI 6: KEGIATAN WAJIB DIPILIH

        if (pilihan == null
                || pilihan.toString().startsWith("--")) {

            tampilkanKesalahan(
                    "Pilihan kegiatan wajib ditentukan.",
                    kegiatanCombo
            );

            return;
        }


        // MEMBUAT RINGKASAN SETELAH SEMUA VALIDASI BERHASIL

        String ringkasan =
                "DATA PENDAFTARAN KEGIATAN\n"
                + "====================================\n"
                + "Nama lengkap : " + nama + "\n"
                + "Email        : " + email + "\n"
                + "Umur         : " + umur + " tahun\n"
                + "Kegiatan     : " + pilihan + "\n\n"
                + "Status: Data valid dan ditampilkan. "
                + "Data berada sementara di memori aplikasi.";

        // Menampilkan hasil ringkasan pada JTextArea
        ringkasanArea.setText(ringkasan);

        // Mengatur posisi teks ke bagian paling atas
        ringkasanArea.setCaretPosition(0);

        // Menandai bahwa data sudah berhasil diproses
        dirty = false;

        // Menghapus pesan perubahan pada formulir
        pesanLabel.setText(" ");

        // Berpindah ke halaman ringkasan
        cardLayout.show(cards, RINGKASAN);
    }


    // =====================================================
    // METODE MENAMPILKAN PESAN KESALAHAN
    // =====================================================

    private void tampilkanKesalahan(
            String pesan,
            java.awt.Component komponen
    ) {

        // Menampilkan dialog pesan kesalahan
        JOptionPane.showMessageDialog(
                frame,
                pesan,
                "Validasi Input",
                JOptionPane.WARNING_MESSAGE
        );

        // Memastikan halaman formulir tetap ditampilkan
        cardLayout.show(cards, FORM);

        // Mengarahkan fokus ke komponen yang salah
        komponen.requestFocusInWindow();

        // Jika komponen berupa JTextField, pilih seluruh teks
        if (komponen instanceof JTextField) {

            ((JTextField) komponen).selectAll();
        }
    }


    // =====================================================
    // EVENT TOMBOL RESET
    // =====================================================

    private void resetForm() {

        // Meminta konfirmasi sebelum mengosongkan formulir
        int pilihan = JOptionPane.showConfirmDialog(
                frame,
                "Kosongkan seluruh isian formulir?",
                "Konfirmasi Reset",
                JOptionPane.YES_NO_OPTION
        );

        // Formulir direset jika pengguna memilih YES
        if (pilihan == JOptionPane.YES_OPTION) {

            // Mengaktifkan penanda agar perubahan program
            // tidak dianggap sebagai perubahan langsung pengguna
            updatingForm = true;

            // Mengosongkan seluruh input teks
            namaField.setText("");
            emailField.setText("");
            umurField.setText("");

            // Mengembalikan pilihan kegiatan ke kondisi awal
            kegiatanCombo.setSelectedIndex(0);

            // Menonaktifkan penanda perubahan otomatis
            updatingForm = false;

            // Menandai bahwa formulir mengalami perubahan
            dirty = true;

            // Menampilkan informasi bahwa formulir telah direset
            pesanLabel.setText(
                    "Formulir telah direset. Perubahan belum disimpan."
            );

            // Mengarahkan fokus ke input nama
            namaField.requestFocusInWindow();
        }
    }


    // =====================================================
    // KONFIRMASI SEBELUM KELUAR
    // =====================================================

    private void konfirmasiKeluar() {

        // Memeriksa apakah terdapat perubahan yang belum diproses
        if (dirty) {

            // Menampilkan dialog konfirmasi keluar
            int pilihan = JOptionPane.showConfirmDialog(
                    frame,
                    "Ada perubahan yang belum disimpan. "
                    + "Yakin ingin keluar?",
                    "Konfirmasi Keluar",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            // Jika pengguna memilih NO atau menutup dialog,
            // aplikasi tidak jadi ditutup
            if (pilihan != JOptionPane.YES_OPTION) {
                return;
            }
        }

        // Menutup jendela aplikasi
        frame.dispose();
    }


    // =====================================================
    // METHOD UTAMA PROGRAM
    // =====================================================

    public static void main(String[] args) {

        // Menjalankan aplikasi Swing melalui Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {

            // Membuat objek aplikasi
            Main aplikasi = new Main();

            // Menampilkan jendela aplikasi
            aplikasi.frame.setVisible(true);
        });
    }
}