package id.ac.univpancasila.perpustakaan.exception;

/**
 * Dilempar ketika anggota sudah meminjam sebanyak batas maksimum.
 * Membawa idAnggota dan batas. Menyertakan batas di dalam exception
 * memudahkan pemanggil menyusun pesan tanpa harus mengetahui aturannya
 * sendiri.
 */
public class BatasPinjamTerlampauiException extends PerpustakaanException {

    private final String idAnggota;
    private final int batas;

    public BatasPinjamTerlampauiException(String idAnggota, int batas) {
        super("Anggota " + idAnggota + " sudah mencapai batas peminjaman maksimum (" + batas + " buku)");
        this.idAnggota = idAnggota;
        this.batas = batas;
    }

    public String getIdAnggota() {
        return idAnggota;
    }

    public int getBatas() {
        return batas;
    }
}
