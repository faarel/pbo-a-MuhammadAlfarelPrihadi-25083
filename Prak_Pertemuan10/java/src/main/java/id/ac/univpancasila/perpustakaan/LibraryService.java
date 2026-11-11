package id.ac.univpancasila.perpustakaan;

import id.ac.univpancasila.perpustakaan.exception.AnggotaTidakDitemukanException;
import id.ac.univpancasila.perpustakaan.exception.BatasPinjamTerlampauiException;
import id.ac.univpancasila.perpustakaan.exception.BukuSedangDipinjamException;
import id.ac.univpancasila.perpustakaan.exception.BukuTidakDitemukanException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Sesi 10 Langkah 2 - HASIL REFACTORING dari LibraryServiceKotor.
 *
 * Method pinjam() memakai guard clause: setiap syarat yang tidak terpenuhi
 * langsung melempar exception yang menjelaskan alasannya. Logika utama
 * ada di paling bawah dan rata di kiri (tidak ada if bersarang).
 */
public class LibraryService {

    public static final int BATAS_PINJAM = 3;

    // LinkedHashMap supaya urutan buku tetap sama saat ditampilkan/diekspor.
    private final Map<String, Integer> stok = new LinkedHashMap<>();
    private final Map<String, String> judul = new LinkedHashMap<>();
    private final Map<String, Integer> pinjaman = new LinkedHashMap<>();

    public void tambahBuku(String isbn, String judulBuku, int eksemplar) {
        // Kesalahan pemrogram (bukan kesalahan pengguna): App sudah memvalidasi,
        // jadi pelanggaran di sini berarti ada bug di pemanggil.
        if (isbn == null || isbn.isBlank()) {
            throw new IllegalArgumentException("ISBN tidak boleh kosong");
        }
        if (judulBuku == null || judulBuku.isBlank()) {
            throw new IllegalArgumentException("Judul tidak boleh kosong");
        }
        if (eksemplar <= 0) {
            throw new IllegalArgumentException("Jumlah eksemplar harus lebih dari 0, diterima: " + eksemplar);
        }
        stok.merge(isbn, eksemplar, Integer::sum);
        judul.put(isbn, judulBuku);
    }

    public void daftarkanAnggota(String idAnggota) {
        if (idAnggota == null || idAnggota.isBlank()) {
            throw new IllegalArgumentException("ID anggota tidak boleh kosong");
        }
        pinjaman.putIfAbsent(idAnggota, 0);
    }

    /**
     * Meminjam satu eksemplar buku.
     *
     * @throws AnggotaTidakDitemukanException jika anggota belum terdaftar
     * @throws BukuTidakDitemukanException    jika ISBN tidak terdaftar
     * @throws BukuSedangDipinjamException    jika stok buku habis
     * @throws BatasPinjamTerlampauiException jika anggota sudah meminjam sebanyak batas
     */
    public void pinjam(String idAnggota, String isbn) {
        if (!pinjaman.containsKey(idAnggota)) {
            throw new AnggotaTidakDitemukanException(idAnggota);
        }
        if (!stok.containsKey(isbn)) {
            throw new BukuTidakDitemukanException(isbn);
        }
        if (stok.get(isbn) <= 0) {
            throw new BukuSedangDipinjamException(isbn, judul.get(isbn));
        }
        if (pinjaman.get(idAnggota) >= BATAS_PINJAM) {
            throw new BatasPinjamTerlampauiException(idAnggota, BATAS_PINJAM);
        }

        // Logika utama: semua syarat sudah lolos.
        stok.merge(isbn, -1, Integer::sum);
        pinjaman.merge(idAnggota, 1, Integer::sum);
    }

    public int sisaEksemplar(String isbn) {
        return stok.getOrDefault(isbn, 0);
    }

    public String judulDari(String isbn) {
        return judul.get(isbn);
    }

    public int jumlahPinjaman(String idAnggota) {
        return pinjaman.getOrDefault(idAnggota, 0);
    }

    /** Daftar ISBN sesuai urutan buku ditambahkan. */
    public List<String> daftarIsbn() {
        return List.copyOf(stok.keySet());
    }
}
