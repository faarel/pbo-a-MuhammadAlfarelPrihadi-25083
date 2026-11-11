package id.ac.univpancasila.perpustakaan;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Sesi 10 Langkah 5 - ekspor daftar buku ke berkas teks dengan
 * try-with-resources, sehingga berkas ditutup otomatis walau terjadi exception.
 */
public class EksporBuku {

    public static final int TANPA_GAGAL = -1;

    /**
     * Berkas teks sebagai resource. Method close() dipanggil otomatis oleh
     * try-with-resources; statusnya dicetak sebagai bukti berkas tertutup.
     */
    private static class BerkasTeks implements AutoCloseable {
        private final Path jalur;
        private final BufferedWriter penulis;

        BerkasTeks(Path jalur) throws IOException {
            this.jalur = jalur;
            this.penulis = Files.newBufferedWriter(jalur);
        }

        void tulisBaris(String teks) throws IOException {
            penulis.write(teks);
            penulis.newLine();
        }

        @Override
        public void close() throws IOException {
            penulis.close();
            System.out.println("[INFO] Berkas ditutup otomatis (try-with-resources): " + jalur);
        }
    }

    /**
     * Menulis daftar buku ke berkas.
     *
     * @param gagalSetelahBaris untuk pembuktian: bila >= 0, sengaja melempar
     *                          IOException setelah sekian baris buku ditulis.
     *                          Pakai TANPA_GAGAL untuk ekspor normal.
     * @return jumlah buku yang ditulis
     * @throws IOException bila berkas tidak dapat dibuka atau ditulis
     */
    public int eksporKeBerkas(LibraryService service, Path tujuan, int gagalSetelahBaris) throws IOException {
        List<String> daftarIsbn = service.daftarIsbn();
        try (BerkasTeks berkas = new BerkasTeks(tujuan)) {
            berkas.tulisBaris("DAFTAR BUKU PERPUSTAKAAN");
            berkas.tulisBaris("Diekspor: " + LocalDateTime.now(ZoneId.of("Asia/Jakarta")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            berkas.tulisBaris("No | ISBN | Judul | Sisa Eksemplar");

            int nomor = 0;
            for (String isbn : daftarIsbn) {
                if (nomor == gagalSetelahBaris) {
                    throw new IOException("Simulasi kegagalan di tengah penulisan (setelah "
                            + nomor + " baris buku)");
                }
                nomor++;
                berkas.tulisBaris(nomor + " | " + isbn + " | " + service.judulDari(isbn)
                        + " | " + service.sisaEksemplar(isbn));
            }
            berkas.tulisBaris("Total judul: " + nomor);
            return nomor;
        }
    }
}
