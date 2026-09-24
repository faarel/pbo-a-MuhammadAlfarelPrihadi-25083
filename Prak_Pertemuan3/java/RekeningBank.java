/**
 * Sesi 3 — constructor berdelegasi, anggota statis, dan konstanta.
 *
 * Invariant:
 *   1. saldo tidak pernah negatif
 *   2. nomor rekening tidak berubah setelah objek dibuat
 *   3. setoran dan penarikan selalu bernilai positif
 */
public class RekeningBank {

    // TODO 1 (selesai): konstanta bernama.
    public static final double BUNGA_TAHUNAN          = 0.025;
    public static final double BIAYA_ADMINISTRASI     = 5000;
    public static final double BATAS_PENARIKAN_SEKALI = 5000000;

    // TODO 2 (selesai): field statis penghitung jumlah rekening.
    private static int jumlahRekening = 0;

    private final String nomor;
    private final String pemilik;
    private double saldo;

    /**
     * Constructor ringkas.
     * TODO 3 (diperbaiki): delegasikan ke constructor lengkap dengan this(...).
     */
    public RekeningBank(String nomor, String pemilik) {
        this(nomor, pemilik, 0);
    }

    /** Constructor lengkap — SATU-SATUNYA tempat validasi berada. */
    public RekeningBank(String nomor, String pemilik, double saldoAwal) {
        // TODO 4 (diperbaiki): tolak nomor kosong dan saldo awal negatif.
        if (nomor == null || nomor.isBlank()) {
            throw new IllegalArgumentException("Nomor rekening tidak boleh kosong atau null");
        }
        if (saldoAwal < 0) {
            throw new IllegalArgumentException("Saldo awal tidak boleh negatif: " + saldoAwal);
        }

        this.nomor = nomor;
        this.pemilik = pemilik;
        this.saldo = saldoAwal;

        // TODO 5 (diperbaiki): baris ini HARUS di dalam constructor, bukan mengambang
        // di luar method. Ditaruh di sini saja (bukan di constructor ringkas) supaya
        // tiap rekening cuma dihitung sekali, karena constructor ringkas selalu
        // mendelegasikan ke sini lewat this(...).
        jumlahRekening++;
    }

    public void setor(double jumlah) {
        // TODO 6 (diperbaiki): kurung dan kurawal if harus lengkap.
        if (jumlah <= 0) {
            throw new IllegalArgumentException("Jumlah setoran harus positif");
        }
        saldo += jumlah;
    }

    public void tarik(double jumlah) {
        if (jumlah <= 0) {
            throw new IllegalArgumentException("Jumlah penarikan harus positif");
        }
        if (jumlah > saldo) {
            throw new IllegalArgumentException("Saldo tidak mencukupi");
        }
        // TODO 7 (diperbaiki): nama konstanta harus persis BATAS_PENARIKAN_SEKALI
        // (huruf besar semua, sesuai deklarasi di atas) — Java case-sensitive.
        if (jumlah > BATAS_PENARIKAN_SEKALI) {
            throw new IllegalArgumentException("Melebihi batas penarikan sekali transaksi");
        }
        saldo -= jumlah;
    }

    /** TODO 8 (diperbaiki): nama konstanta harus BIAYA_ADMINISTRASI, bukan "administrasi". */
    public void potongBiayaAdmin() {
        saldo = Math.max(0, saldo - BIAYA_ADMINISTRASI);
    }

    /** TODO 9 (selesai): method statis — kembalikan jumlah rekening yang pernah dibuat. */
    public static int getJumlahRekening() {
        return jumlahRekening;
    }

    /** TODO 10 (diperbaiki): hitung bunga setahun dari pokok. */
    public static double bungaSetahun(double pokok) {
        return pokok * BUNGA_TAHUNAN;
    }

    public double getSaldo()  { return saldo; }
    public String getNomor()  { return nomor; }

    @Override
    public String toString() {
        return String.format("Rekening[%s] %-14s Rp%,.2f", nomor, pemilik, saldo);
    }
}