<?php
declare(strict_types=1);

/**
 * Sesi 2 — enkapsulasi yang menjaga invariant (PHP).
 * Bandingkan baris demi baris dengan java/Mahasiswa.java.
 */
class Mahasiswa
{
    public const float BOBOT_TUGAS = 0.30;
    public const float BOBOT_UTS   = 0.30;
    public const float BOBOT_UAS   = 0.40;

    private const float NILAI_MIN = 0;
    private const float NILAI_MAX = 100;

    /**
     * Constructor property promotion (PHP 8):
     * readonly adalah padanan `final` pada atribut Java.
     *
     * nim dan nama readonly (tidak boleh berubah setelah terdaftar).
     * nilaiTugas, nilaiUts, nilaiUas boleh berubah -> bukan readonly.
     */
    public function __construct(
        private readonly string $nim,
        private readonly string $nama,
        private float $nilaiTugas,
        private float $nilaiUts,
        private float $nilaiUas,
    ) {
        // Tolak NIM yang kosong (setelah di-trim).
        if (trim($this->nim) === '') {
            throw new InvalidArgumentException('NIM tidak boleh kosong atau hanya spasi');
        }

        // Tolak setiap komponen nilai di luar rentang 0-100.
        self::pastikanNilaiSah('tugas', $this->nilaiTugas);
        self::pastikanNilaiSah('UTS', $this->nilaiUts);
        self::pastikanNilaiSah('UAS', $this->nilaiUas);
    }

    /**
     * Validasi satu komponen nilai berada dalam rentang 0-100.
     */
    private static function pastikanNilaiSah(string $namaKomponen, float $nilai): void
    {
        if ($nilai < self::NILAI_MIN || $nilai > self::NILAI_MAX) {
            throw new InvalidArgumentException(
                sprintf(
                    'Nilai %s harus di rentang %s-%s, diberikan: %s',
                    $namaKomponen,
                    self::NILAI_MIN,
                    self::NILAI_MAX,
                    $nilai
                )
            );
        }
    }

    /** Hitung nilai akhir memakai konstanta bobot. */
    public function nilaiAkhir(): float
    {
        return ($this->nilaiTugas * self::BOBOT_TUGAS)
            + ($this->nilaiUts * self::BOBOT_UTS)
            + ($this->nilaiUas * self::BOBOT_UAS);
    }

    /** Kembalikan huruf mutu. */
    public function hurufMutu(): string
    {
        $akhir = $this->nilaiAkhir();

        return match (true) {
            $akhir >= 80 => 'A',
            $akhir >= 70 => 'B',
            $akhir >= 60 => 'C',
            $akhir >= 50 => 'D',
            default      => 'E',
        };
    }

    // Getter seperlunya. Tidak ada setNim() agar NIM tetap immutable.
    public function getNim(): string  { return $this->nim; }
    public function getNama(): string { return $this->nama; }

    public function __toString(): string
    {
        return sprintf('%-10s %-18s akhir=%6.2f  mutu=%s',
            $this->nim, $this->nama, $this->nilaiAkhir(), $this->hurufMutu());
    }
}