/**
 * Sesi 2 — enkapsulasi yang menjaga invariant.
 *
 * Deskripsi masalah:
 *   "Sistem akademik mencatat mahasiswa dengan NIM, nama, dan tiga komponen
 *    nilai: tugas, UTS, dan UAS. NIM tidak pernah berubah setelah mahasiswa
 *    terdaftar. Setiap komponen nilai berada dalam rentang 0 sampai 100.
 *    Nilai akhir dihitung 30% tugas, 30% UTS, 40% UAS."
 *
 * Tuliskan lebih dulu daftar invariant-nya di analisis.md,
 * baru lengkapi TODO di bawah ini.
 */
public class Mahasiswa {

    // Konstanta bobot — jangan menulis angka 0.30 dan 0.40 di dalam method.
    public static final double BOBOT_TUGAS = 0.30;
    public static final double BOBOT_UTS   = 0.30;
    public static final double BOBOT_UAS   = 0.40;

    private static final double NILAI_MIN = 0;
    private static final double NILAI_MAX = 100;

    // Atribut: nim dan nama tidak boleh berubah setelah terdaftar -> final.
    // Nilai boleh berubah (mis. saat input ulang), jadi bukan final.
    private final String nim;
    private final String nama;
    private double nilaiTugas;
    private double nilaiUts;
    private double nilaiUas;

    public Mahasiswa(String nim, String nama, double nilaiTugas, double nilaiUts, double nilaiUas) {
        // Tolak NIM yang kosong atau null.
        if (nim == null || nim.isBlank()) {
            throw new IllegalArgumentException("NIM tidak boleh kosong atau null");
        }

        // Tolak setiap komponen nilai yang di luar rentang 0-100.
        pastikanNilaiSah("tugas", nilaiTugas);
        pastikanNilaiSah("UTS", nilaiUts);
        pastikanNilaiSah("UAS", nilaiUas);

        this.nim = nim;
        this.nama = nama;
        this.nilaiTugas = nilaiTugas;
        this.nilaiUts = nilaiUts;
        this.nilaiUas = nilaiUas;
    }

    // Method privat pembantu untuk memvalidasi satu komponen nilai.
    private static void pastikanNilaiSah(String namaKomponen, double nilai) {
        if (nilai < NILAI_MIN || nilai > NILAI_MAX) {
            throw new IllegalArgumentException(
                "Nilai " + namaKomponen + " harus di rentang " + NILAI_MIN + "-" + NILAI_MAX
                + ", diberikan: " + nilai);
        }
    }

    /**
     * Hitung nilai akhir memakai konstanta bobot di atas.
     */
    public double nilaiAkhir() {
        return (nilaiTugas * BOBOT_TUGAS) + (nilaiUts * BOBOT_UTS) + (nilaiUas * BOBOT_UAS);
    }

    /**
     * Kembalikan huruf mutu berdasarkan nilai akhir.
     *   >= 80 -> "A"   >= 70 -> "B"   >= 60 -> "C"   >= 50 -> "D"   selain itu "E"
     */
    public String hurufMutu() {
        double akhir = nilaiAkhir();
        if (akhir >= 80) return "A";
        if (akhir >= 70) return "B";
        if (akhir >= 60) return "C";
        if (akhir >= 50) return "D";
        return "E";
    }

    // ── Getter ────────────────────────────────────────────────
    // Getter untuk nim dan nama sudah ada.
    // "Getter" untuk nilaiAkhir cukup lewat method nilaiAkhir() itu sendiri
    // (tidak perlu getter tambahan / setNim tidak dibuat agar NIM tetap immutable).

    public String getNim()  { return nim; }
    public String getNama() { return nama; }

    @Override
    public String toString() {
        return String.format("%-10s %-18s akhir=%6.2f  mutu=%s",
                nim, nama, nilaiAkhir(), hurufMutu());
    }
}