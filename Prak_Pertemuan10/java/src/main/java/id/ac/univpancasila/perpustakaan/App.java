package id.ac.univpancasila.perpustakaan;

import id.ac.univpancasila.perpustakaan.exception.AnggotaTidakDitemukanException;
import id.ac.univpancasila.perpustakaan.exception.BatasPinjamTerlampauiException;
import id.ac.univpancasila.perpustakaan.exception.BukuSedangDipinjamException;
import id.ac.univpancasila.perpustakaan.exception.BukuTidakDitemukanException;
import id.ac.univpancasila.perpustakaan.exception.PerpustakaanException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

/**
 * Antarmuka konsol Sistem Perpustakaan (Sesi 10 Langkah 3-5).
 * Program tidak boleh berhenti karena stack trace: setiap kegagalan
 * menghasilkan pesan yang bisa dibaca dan dicatat ke berkas log.
 */
public class App {

    private static final int MAKS_EKSEMPLAR = 1000;
    private static final String EKSPOR_DEFAULT = "hasil-ekspor/daftar-buku.txt";
    private static final String EKSPOR_SIMULASI = "hasil-ekspor/simulasi-gagal.txt";

    private final LibraryService service = new LibraryService();
    private final PencatatLog log = new PencatatLog(Path.of("logs", "perpustakaan.log"));
    private final EksporBuku eksporBuku = new EksporBuku();
    private final BufferedReader masukan = new BufferedReader(new InputStreamReader(System.in));

    /** Penanda bahwa masukan sudah habis (Ctrl+D / Ctrl+Z atau input dari berkas selesai). */
    private static class MasukanSelesai extends RuntimeException {
    }

    public static void main(String[] args) {
        new App().jalankan();
    }

    private void jalankan() {
        siapkanData();
        System.out.println("=== SISTEM PERPUSTAKAAN ===");
        System.out.println("Anggota terdaftar: A001, A002, A003 (batas pinjam " + LibraryService.BATAS_PINJAM + " buku)");

        try {
            while (true) {
                tampilkanMenu();
                int pilihan = bacaAngka("Pilih menu (0-5): ", 0, 5);
                if (pilihan == 0) {
                    System.out.println("Terima kasih. Program selesai.");
                    return;
                }
                try {
                    prosesMenu(pilihan);
                } catch (PerpustakaanException e) {
                    // Satu catch untuk SELURUH kegagalan domain (Langkah 4).
                    tanganiKegagalanDomain(e);
                } catch (IOException | InvalidPathException e) {
                    String pesan = "Ekspor gagal (" + e.getClass().getSimpleName() + "): " + e.getMessage();
                    System.out.println("[GAGAL] " + pesan);
                    log.catat(e.getClass().getSimpleName(), "detail=" + e.getMessage(), pesan);
                } catch (MasukanSelesai e) {
                    throw e;
                } catch (RuntimeException e) {
                    // Lapisan paling luar: jaring pengaman untuk bug yang tidak terduga.
                    String pesan = "Terjadi kesalahan tak terduga: " + e.getMessage();
                    System.out.println("[GAGAL] " + pesan);
                    log.catat(e.getClass().getSimpleName(), "tidak diketahui", pesan);
                }
            }
        } catch (MasukanSelesai e) {
            System.out.println();
            System.out.println("Masukan habis. Program selesai.");
        }
    }

    private void siapkanData() {
        service.tambahBuku("978-1", "Pemrograman Berorientasi Objek", 3);
        service.tambahBuku("978-2", "Basis Data", 1);
        service.tambahBuku("978-3", "Algoritma dan Struktur Data", 5);
        service.tambahBuku("978-4", "Jaringan Komputer", 2);
        service.daftarkanAnggota("A001");
        service.daftarkanAnggota("A002");
        service.daftarkanAnggota("A003");
        try {
            Files.createDirectories(Path.of("hasil-ekspor"));
        } catch (IOException e) {
            System.err.println("[PERINGATAN] Folder hasil-ekspor tidak dapat dibuat: " + e.getMessage());
        }
    }

    private void tampilkanMenu() {
        System.out.println();
        System.out.println("--- MENU ---");
        System.out.println("1. Pinjam buku");
        System.out.println("2. Tambah buku");
        System.out.println("3. Lihat daftar buku");
        System.out.println("4. Ekspor daftar buku ke berkas");
        System.out.println("5. Uji ekspor gagal di tengah penulisan (simulasi)");
        System.out.println("0. Keluar");
    }

    private void prosesMenu(int pilihan) throws IOException {
        switch (pilihan) {
            case 1 -> pinjamBuku();
            case 2 -> tambahBuku();
            case 3 -> lihatDaftarBuku();
            case 4 -> eksporDaftarBuku();
            case 5 -> simulasiEksporGagal();
            default -> throw new IllegalStateException("Menu tidak dikenal: " + pilihan);
        }
    }

    // ---------------------------------------------------------------- menu

    private void pinjamBuku() {
        String idAnggota = bacaTeks("ID anggota: ");
        String isbn = bacaTeks("ISBN buku: ");
        service.pinjam(idAnggota, isbn);
        System.out.println("[OK] " + idAnggota + " berhasil meminjam \"" + service.judulDari(isbn) + "\". "
                + "Sisa eksemplar: " + service.sisaEksemplar(isbn)
                + ". Total pinjaman " + idAnggota + ": " + service.jumlahPinjaman(idAnggota));
    }

    private void tambahBuku() {
        String isbn = bacaTeks("ISBN: ");
        String judul = bacaTeks("Judul: ");
        int eksemplar = bacaAngka("Jumlah eksemplar (1-" + MAKS_EKSEMPLAR + "): ", 1, MAKS_EKSEMPLAR);
        service.tambahBuku(isbn, judul, eksemplar);
        System.out.println("[OK] Buku \"" + judul + "\" ditambahkan. Sisa eksemplar: " + service.sisaEksemplar(isbn));
    }

    private void lihatDaftarBuku() {
        System.out.println("No | ISBN | Judul | Sisa Eksemplar");
        int nomor = 0;
        for (String isbn : service.daftarIsbn()) {
            nomor++;
            System.out.println(nomor + " | " + isbn + " | " + service.judulDari(isbn)
                    + " | " + service.sisaEksemplar(isbn));
        }
    }

    private void eksporDaftarBuku() throws IOException {
        String nama = bacaBaris("Nama berkas (Enter = " + EKSPOR_DEFAULT + "): ").trim();
        Path tujuan = Path.of(nama.isEmpty() ? EKSPOR_DEFAULT : nama);
        int jumlah = eksporBuku.eksporKeBerkas(service, tujuan, EksporBuku.TANPA_GAGAL);
        System.out.println("[OK] " + jumlah + " buku diekspor ke " + tujuan);
    }

    private void simulasiEksporGagal() throws IOException {
        Path tujuan = Path.of(EKSPOR_SIMULASI);
        eksporBuku.eksporKeBerkas(service, tujuan, 2);
        System.out.println("[OK] Seharusnya tidak sampai sini.");
    }

    // ----------------------------------------------- penanganan kegagalan

    private void tanganiKegagalanDomain(PerpustakaanException e) {
        // Pemeriksaan tipe untuk mengambil data spesifik dari exception tertentu.
        String data;
        String saran;
        switch (e) {
            case BukuTidakDitemukanException b -> {
                data = "isbn=" + b.getIsbn();
                saran = "Periksa ISBN pada menu 3 (Lihat daftar buku).";
            }
            case AnggotaTidakDitemukanException a -> {
                data = "idAnggota=" + a.getIdAnggota();
                saran = "Gunakan ID anggota yang terdaftar: A001, A002, A003.";
            }
            case BukuSedangDipinjamException s -> {
                data = "isbn=" + s.getIsbn() + ", judul=" + s.getJudul();
                saran = "Coba buku lain atau tunggu ada yang mengembalikan.";
            }
            case BatasPinjamTerlampauiException t -> {
                data = "idAnggota=" + t.getIdAnggota() + ", batas=" + t.getBatas();
                saran = "Anggota harus mengembalikan buku lebih dulu.";
            }
            default -> {
                data = "-";
                saran = "";
            }
        }
        System.out.println("[GAGAL] " + e.getMessage() + ". " + saran);
        log.catat(e.getClass().getSimpleName(), data, e.getMessage());
    }

    // --------------------------------------------------------- baca masukan

    private String bacaBaris(String prompt) {
        System.out.print(prompt);
        try {
            String baris = masukan.readLine();
            if (baris == null) {
                throw new MasukanSelesai();
            }
            return baris;
        } catch (IOException e) {
            throw new MasukanSelesai();
        }
    }

    /** Meminta teks sampai pengguna mengisi sesuatu (kosong dan spasi saja ditolak). */
    private String bacaTeks(String prompt) {
        while (true) {
            String teks = bacaBaris(prompt).trim();
            if (!teks.isEmpty()) {
                return teks;
            }
            String pesan = "Masukan tidak boleh kosong atau hanya spasi. Silakan ulangi.";
            System.out.println("[MASUKAN] " + pesan);
            log.catat("MasukanKosong", "masukan kosong/spasi", pesan);
        }
    }

    /** Meminta bilangan bulat dalam rentang [min, max]; salah input -> diminta ulang. */
    private int bacaAngka(String prompt, int min, int max) {
        while (true) {
            String teks = bacaBaris(prompt).trim();
            if (teks.isEmpty()) {
                String pesan = "Masukan tidak boleh kosong atau hanya spasi. Silakan ulangi.";
                System.out.println("[MASUKAN] " + pesan);
                log.catat("MasukanKosong", "masukan kosong/spasi", pesan);
                continue;
            }
            try {
                int angka = Integer.parseInt(teks);
                if (angka >= min && angka <= max) {
                    return angka;
                }
                String pesan = "Angka harus antara " + min + " dan " + max + " (negatif tidak diterima). Silakan ulangi.";
                System.out.println("[MASUKAN] " + pesan);
                log.catat("MasukanDiLuarRentang", "masukan=" + teks, pesan);
            } catch (NumberFormatException e) {
                String pesan = "\"" + teks + "\" bukan bilangan bulat yang valid (huruf, desimal, atau terlalu besar). Silakan ulangi.";
                System.out.println("[MASUKAN] " + pesan);
                log.catat(e.getClass().getSimpleName(), "masukan=" + teks, pesan);
            }
        }
    }
}
