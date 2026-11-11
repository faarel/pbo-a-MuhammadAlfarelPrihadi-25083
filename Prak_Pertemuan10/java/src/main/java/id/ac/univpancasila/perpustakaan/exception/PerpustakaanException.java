package id.ac.univpancasila.perpustakaan.exception;

/**
 * Sesi 10 - induk seluruh exception domain perpustakaan.
 *
 * Manfaat punya induk bersama: pemanggil bisa menangkap SELURUH kegagalan
 * domain sekaligus dengan satu blok catch, tanpa ikut menelan kesalahan
 * pemrograman yang lain.
 *
 * Keputusan (TODO 1): diturunkan dari RuntimeException (unchecked), karena
 * kegagalan ini menandakan permintaan yang tidak sah (ISBN salah, anggota
 * tidak ada, dan sebagainya), bukan keadaan luar yang wajar dipulihkan di
 * setiap lapisan. Alasan lengkap ada di skenario-uji.md.
 */
public abstract class PerpustakaanException extends RuntimeException {

    protected PerpustakaanException(String pesan) {
        super(pesan);
    }

    protected PerpustakaanException(String pesan, Throwable penyebab) {
        super(pesan, penyebab);
    }
}
