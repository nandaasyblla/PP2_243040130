package p08;

//untuk memanggil liblary
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Dimension;

//untuk menyediakan komponen GUI serta layout manager
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutFocusTraversalPolicy;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        //memastikan proses pembuatan tampulan GUI dijaalankan melalui EDT
        SwingUtilities.invokeLater(() -> {
            buatGUI();
        });

    }

    private static void buatGUI() {

        // Membuat jendela utama
        JFrame frame = new JFrame("Formulir Pendaftaran Kegiatan");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        // ==============================
        // 1. HEADER
        // ==============================

        JLabel judul = new JLabel(
                "FORMULIR PENDAFTARAN KEGIATAN",
                JLabel.CENTER
        );

        judul.setBorder(
                BorderFactory.createEmptyBorder(15, 10, 15, 10)
        );

        // ==============================
        // 2. IDENTITAS PESERTA
        // ==============================

        //mengelompokkan komponen berdasarkan bagian formulir, seperti identitas pilihan, kegiatan dan catatan
        JPanel identitasPanel = new JPanel(new GridBagLayout());

        identitasPanel.setBorder(
                BorderFactory.createTitledBorder("Identitas Peserta")
        );

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(7, 8, 7, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        //menyediakan kolom untuk memasukkan data nama peserta 
        JTextField namaField = new JTextField(25);
        JTextField nimField = new JTextField(25);
        JTextField emailField = new JTextField(25);

        gbc.gridx = 0;
        gbc.gridy = 0;
        identitasPanel.add(new JLabel("Nama Lengkap"), gbc);

        gbc.gridx = 1;
        identitasPanel.add(namaField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        identitasPanel.add(new JLabel("NIM"), gbc);

        gbc.gridx = 1;
        identitasPanel.add(nimField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        identitasPanel.add(new JLabel("Email"), gbc);

        gbc.gridx = 1;
        identitasPanel.add(emailField, gbc);

        // ==============================
        // 3. PILIHAN KEGIATAN
        // ==============================

        JPanel kegiatanPanel = new JPanel();

        kegiatanPanel.setLayout(
                new BoxLayout(kegiatanPanel, BoxLayout.Y_AXIS)
        );

        kegiatanPanel.setBorder(
                BorderFactory.createTitledBorder("Pilihan Kegiatan")
        );

        JPanel pilihanPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT)
        );

        pilihanPanel.add(new JLabel("Jenis Kegiatan:"));

        String[] daftarKegiatan = {
                "Seminar Teknologi",
                "Workshop Programming",
                "Pelatihan Cyber Security",
                "Webinar Nasional"
        };

        //menampilkan daftar kegiatan yang bisa dipilih
        JComboBox<String> kegiatanCombo =
                new JComboBox<>(daftarKegiatan);

        pilihanPanel.add(kegiatanCombo);

        JPanel pesertaPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT)
        );

        pesertaPanel.add(new JLabel("Jenis Peserta:"));

        //membuat pilihan jenis peserta yg hanya bisa dipilih sakah satu
        JRadioButton pesertaRadio =
                new JRadioButton("Peserta");

        JRadioButton panitiaRadio =
                new JRadioButton("Panitia");

        ButtonGroup kelompokRadio = new ButtonGroup();

        kelompokRadio.add(pesertaRadio);
        kelompokRadio.add(panitiaRadio);

        pesertaPanel.add(pesertaRadio);
        pesertaPanel.add(panitiaRadio);

        pesertaRadio.setSelected(true);

        JPanel persetujuanPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT)
        );

        //untuk persetujuan mengikuti kegiatan
        JCheckBox persetujuanCheck =
                new JCheckBox("Saya bersedia mengikuti kegiatan");

        persetujuanPanel.add(persetujuanCheck);

        kegiatanPanel.add(pilihanPanel);
        kegiatanPanel.add(pesertaPanel);
        kegiatanPanel.add(persetujuanPanel);

        // ==============================
        // 4. CATATAN TAMBAHAN
        // ==============================

        JPanel catatanPanel = new JPanel(new BorderLayout(5, 5));

        catatanPanel.setBorder(
                BorderFactory.createTitledBorder("Catatan Tambahan")
        );

        //untuk memasukkan catatan tambahan dengan lebih dari satu baris
        JTextArea catatanArea = new JTextArea(4, 25);

        catatanArea.setLineWrap(true);
        catatanArea.setWrapStyleWord(true);

        JScrollPane scrollCatatan =
                new JScrollPane(catatanArea);

        catatanPanel.add(scrollCatatan, BorderLayout.CENTER);

        // ==============================
        // 5. MENGGABUNGKAN FORM
        // ==============================

        JPanel isiPanel = new JPanel();

        isiPanel.setLayout(
                new BoxLayout(isiPanel, BoxLayout.Y_AXIS)
        );

        isiPanel.add(identitasPanel);
        isiPanel.add(kegiatanPanel);
        isiPanel.add(catatanPanel);

        isiPanel.setBorder(
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        );

        JScrollPane scrollForm = new JScrollPane(isiPanel);

        scrollForm.setBorder(null);
        scrollForm.getVerticalScrollBar().setUnitIncrement(15);

        // ==============================
        // 6. TOMBOL
        // ==============================

        JButton daftarButton = new JButton("Daftar");
        JButton resetButton = new JButton("Reset");

        JLabel statusLabel = new JLabel(
                "Silakan lengkapi formulir pendaftaran."
        );

        JPanel tombolPanel = new JPanel(
                new BorderLayout(10, 5)
        );

        JPanel tombolKanan = new JPanel(
                new FlowLayout(FlowLayout.RIGHT)
        );

        tombolKanan.add(resetButton);
        tombolKanan.add(daftarButton);

        tombolPanel.add(statusLabel, BorderLayout.CENTER);
        tombolPanel.add(tombolKanan, BorderLayout.EAST);

        tombolPanel.setBorder(
                BorderFactory.createEmptyBorder(5, 10, 10, 10)
        );

        // ==============================
        // 7. EVENT TOMBOL DAFTAR
        // ==============================

        //menjalankan perintah ketika tombol Daftar ditekan
        daftarButton.addActionListener(e -> {

            String nama = namaField.getText().trim();
            String nim = nimField.getText().trim();
            String email = emailField.getText().trim();

            if (nama.isEmpty()) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Nama lengkap wajib diisi!"
                );
                namaField.requestFocusInWindow();
                return;
            }

            if (nim.isEmpty()) {
                JOptionPane.showMessageDialog(
                        frame,
                        "NIM wajib diisi!"
                );
                nimField.requestFocusInWindow();
                return;
            }

            if (email.isEmpty()) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Email wajib diisi!"
                );
                emailField.requestFocusInWindow();
                return;
            }

            if (!persetujuanCheck.isSelected()) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Silakan centang persetujuan terlebih dahulu!"
                );
                persetujuanCheck.requestFocusInWindow();
                return;
            }

            String jenisPeserta = pesertaRadio.isSelected()
                    ? "Peserta" : "Panitia";

            String hasil =
                    "Pendaftaran Berhasil!\n\n" +
                    "Nama: " + nama + "\n" +
                    "NIM: " + nim + "\n" +
                    "Email: " + email + "\n" +
                    "Kegiatan: " + kegiatanCombo.getSelectedItem() + "\n" +
                    "Jenis Peserta: " + jenisPeserta;

            JOptionPane.showMessageDialog(
                    frame,
                    hasil,
                    "Informasi Pendaftaran",
                    JOptionPane.INFORMATION_MESSAGE
            );

            statusLabel.setText("Formulir berhasil divalidasi.");
        });

        // ==============================
        // 8. EVENT TOMBOL RESET
        // ==============================

        resetButton.addActionListener(e -> {

            namaField.setText("");
            nimField.setText("");
            emailField.setText("");

            kegiatanCombo.setSelectedIndex(0);

            pesertaRadio.setSelected(true);

            persetujuanCheck.setSelected(false);

            catatanArea.setText("");

            statusLabel.setText(
                    "Silakan lengkapi formulir pendaftaran."
            );

            namaField.requestFocusInWindow();
        });

        // ==============================
        // 9. MENAMPILKAN GUI
        // ==============================

        frame.add(judul, BorderLayout.NORTH);
        frame.add(scrollForm, BorderLayout.CENTER);
        frame.add(tombolPanel, BorderLayout.SOUTH);

        frame.setFocusCycleRoot(true);
        frame.setFocusTraversalPolicy(
                new LayoutFocusTraversalPolicy()
        );

        //menyesuaikan ukuran jendela dengan komponen yang ada di dalamnya
        frame.pack();

        frame.setMinimumSize(new Dimension(620, 560));

        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
    }
}