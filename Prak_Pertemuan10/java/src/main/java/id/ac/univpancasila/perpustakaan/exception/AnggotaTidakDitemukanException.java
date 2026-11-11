package id.ac.univpancasila.perpustakaan.exception;

/**
 * Dilempar ketika ID anggota belum terdaftar.
 * Membawa idAnggota agar pemanggil tahu anggota mana yang dicari.
 */
public class AnggotaTidakDitemukanException extends PerpustakaanException {

    private final String idAnggota;

    public AnggotaTidakDitemukanException(String idAnggota) {
        super("Anggota dengan ID " + idAnggota + " belum terdaftar");
        this.idAnggota = idAnggota;
    }

    public String getIdAnggota() {
        return idAnggota;
    }
}
