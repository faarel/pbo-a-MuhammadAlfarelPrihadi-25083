package service;

import model.Buku;
import model.Jurusan.Mahasiswa;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Kerangka lapisan layanan.
 * Pada sesi 7 cukup struktur dan tanda tangan method; isinya boleh minimal.
 * Lapisan ini akan diperdalam pada sesi 10 (exception) dan 12 (SOLID).
 */
public class PerpustakaanService {

    // TODO 1: Layanan ini AGREGASI terhadap Buku dan Mahasiswa (anggota).
    //         Alasannya: layanan tidak membuat Buku/Mahasiswa; objeknya
    //         datang dari luar dan tetap hidup walau layanan dibuang.
    //         (Bandingkan dengan Buku -> Bab yang komposisi.)
    //         Disimpan dalam Map berkunci nomor induk / NIM agar pencarian cepat
    //         dan tidak ada duplikat.
    private final Map<String, Buku> koleksi = new HashMap<>();
    private final Map<String, Mahasiswa> anggota = new HashMap<>();

    // Catatan peminjaman: nomor anggota -> kumpulan nomor induk buku yang sedang dipinjam.
    private final Map<String, Set<String>> pinjaman = new HashMap<>();

    /** Pelengkap: pinjam() butuh tahu siapa saja anggota yang sah. */
    public void daftarkanAnggota(Mahasiswa m) {
        if (m == null) {
            throw new IllegalArgumentException("Anggota tidak boleh null");
        }
        anggota.putIfAbsent(m.getNim(), m);
    }

    // TODO 2
    public void tambahKoleksi(Buku buku) {
        if (buku == null) {
            throw new IllegalArgumentException("Buku tidak boleh null");
        }
        if (koleksi.containsKey(buku.getNomorInduk())) {
            throw new IllegalStateException(
                    "Nomor induk sudah terdaftar: " + buku.getNomorInduk());
        }
        koleksi.put(buku.getNomorInduk(), buku);
    }

    // TODO 3: mengembalikan null bila tidak ditemukan (akan diganti exception di sesi 10).
    public Buku cariKoleksi(String nomorInduk) {
        return koleksi.get(nomorInduk);
    }

    // TODO 4: aturan minimal: anggota dan buku harus ada, stok harus tersedia,
    //         dan anggota tidak boleh meminjam judul yang sama dua kali.
    //         Aturan lengkap dari deskripsi-kebutuhan.md disempurnakan di sesi 10.
    public void pinjam(String nomorAnggota, String nomorInduk) {
        if (!anggota.containsKey(nomorAnggota)) {
            throw new IllegalArgumentException("Anggota tidak ditemukan: " + nomorAnggota);
        }
        Buku buku = cariKoleksi(nomorInduk);
        if (buku == null) {
            throw new IllegalArgumentException("Buku tidak ditemukan: " + nomorInduk);
        }
        Set<String> dipinjam = pinjaman.computeIfAbsent(nomorAnggota, k -> new HashSet<>());
        if (dipinjam.contains(nomorInduk)) {
            throw new IllegalStateException("Buku ini sedang Anda pinjam: " + nomorInduk);
        }
        if (!buku.tersedia()) {
            throw new IllegalStateException("Stok buku habis: " + buku.getJudul());
        }
        buku.kurangiStok();
        dipinjam.add(nomorInduk);
    }

    // TODO 5
    public void kembalikan(String nomorAnggota, String nomorInduk) {
        Set<String> dipinjam = pinjaman.get(nomorAnggota);
        if (dipinjam == null || !dipinjam.contains(nomorInduk)) {
            throw new IllegalStateException(
                    "Anggota " + nomorAnggota + " tidak sedang meminjam buku " + nomorInduk);
        }
        Buku buku = cariKoleksi(nomorInduk);
        buku.tambahStok();
        dipinjam.remove(nomorInduk);
    }
}