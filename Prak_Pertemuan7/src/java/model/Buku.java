package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Sesi 7 — KOMPOSISI.
 *
 * Tiga hal yang membuat sebuah relasi benar-benar komposisi:
 *   1. tidak ada constructor atau method yang MENERIMA objek bagian dari luar
 *   2. objek bagian dibuat DI DALAM induk
 *   3. getter mengembalikan salinan yang tidak bisa diubah
 */
public class Buku {

    private final String nomorInduk;
    private final String judul;
    private final String penulis;
    private int jumlahEksemplar;
    private final List<Bab> bab = new ArrayList<>();

    public Buku(String nomorInduk, String judul, String penulis, int jumlahEksemplar) {
        // TODO 1: tolak jumlah eksemplar negatif.
        if (jumlahEksemplar < 0) {
            throw new IllegalArgumentException(
                    "Jumlah eksemplar tidak boleh negatif: " + jumlahEksemplar);
        }
        this.nomorInduk = nomorInduk;
        this.judul = judul;
        this.penulis = penulis;
        this.jumlahEksemplar = jumlahEksemplar;
    }

    /**
     * TODO 2: satu-satunya cara Bab lahir.
     *         Parameternya nomor dan judul, BUKAN objek Bab, sehingga
     *         Bab selalu dibuat di dalam Buku (komposisi, bukan agregasi).
     */
    public void tambahBab(int nomor, String judulBab) {
        if (nomor <= 0) {
            throw new IllegalArgumentException("Nomor bab harus positif: " + nomor);
        }
        if (judulBab == null || judulBab.isBlank()) {
            throw new IllegalArgumentException("Judul bab tidak boleh kosong");
        }
        bab.add(new Bab(nomor, judulBab));
    }

    /**
     * TODO 3: kembalikan salinan yang tidak bisa diubah.
     *         List.copyOf menghasilkan list immutable, sehingga pemanggil
     *         tidak bisa menambah/menghapus bab lewat getter ini.
     */
    public List<Bab> getBab() {
        return List.copyOf(bab);
    }

    public boolean tersedia() { return jumlahEksemplar > 0; }

    /** TODO 4: kurangi stok; tolak bila sudah nol. */
    public void kurangiStok() {
        if (jumlahEksemplar == 0) {
            throw new IllegalStateException(
                    "Stok buku '" + judul + "' sudah habis");
        }
        jumlahEksemplar--;
    }

    public void tambahStok() { jumlahEksemplar++; }

    public String getNomorInduk()  { return nomorInduk; }
    public String getJudul()       { return judul; }
    public int getJumlahEksemplar() { return jumlahEksemplar; }

    @Override
    public String toString() {
        return String.format("%-32s %-16s [%d eks, %d bab]",
                judul, penulis, jumlahEksemplar, bab.size());
    }

    /**
     * Bab adalah BAGIAN dari Buku — kelasnya bersarang untuk menegaskan itu,
     * dan constructor-nya privat agar hanya Buku yang boleh membuatnya.
     */
    public static final class Bab {
        private final int nomor;
        private final String judul;

        private Bab(int nomor, String judul) {
            this.nomor = nomor;
            this.judul = judul;
        }

        public int getNomor()    { return nomor; }
        public String getJudul() { return judul; }

        @Override public String toString() { return "Bab " + nomor + ": " + judul; }
    }
}