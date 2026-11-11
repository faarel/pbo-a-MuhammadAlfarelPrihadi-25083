package id.ac.univpancasila.perpustakaan.exception;

/**
 * Dilempar ketika seluruh eksemplar buku sedang dipinjam (stok habis).
 * Membawa DUA data: isbn dan judul. Judul disertakan agar pesan untuk
 * pengguna lebih berarti; isbn disertakan agar sistem dapat mengolahnya.
 */
public class BukuSedangDipinjamException extends PerpustakaanException {

    private final String isbn;
    private final String judul;

    public BukuSedangDipinjamException(String isbn, String judul) {
        super("Buku \"" + judul + "\" (ISBN " + isbn + ") sedang dipinjam semua, stok habis");
        this.isbn = isbn;
        this.judul = judul;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getJudul() {
        return judul;
    }
}
