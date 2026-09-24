<?php
declare(strict_types=1);

/**
 * Sesi 4 — hierarki pegawai (PHP).
 * Seluruh hierarki ditaruh dalam satu berkas.
 */

abstract class Pegawai
{
    public function __construct(
        protected readonly string $nip,
        protected readonly string $nama,
        protected readonly float $gajiPokok,
    ) {
        // TODO 1: tolak gaji pokok negatif.
        if ($gajiPokok < 0) {
            throw new InvalidArgumentException(
                'Gaji pokok tidak boleh negatif'
            );
        }
    }

    /** TODO 2: kembalikan gaji pokok apa adanya. */
    public function hitungGaji(): float
    {
        return $this->gajiPokok;
    }

    abstract public function jenis(): string;

    public function getNama(): string
    {
        return $this->nama;
    }

    public function getNip(): string
    {
        return $this->nip;
    }

    public function __toString(): string
    {
        return sprintf(
            '%-14s %-9s %-20s Rp%s',
            $this->nip,
            $this->jenis(),
            $this->nama,
            number_format(
                $this->hitungGaji(),
                2,
                ',',
                '.'
            )
        );
    }
}


class PegawaiTetap extends Pegawai
{
    protected const TUNJANGAN_PER_TAHUN = 0.02;
    protected const TUNJANGAN_MAKSIMUM = 0.40;

    public function __construct(
        string $nip,
        string $nama,
        float $gajiPokok,
        protected readonly int $masaKerjaTahun,
    ) {
        parent::__construct($nip, $nama, $gajiPokok);
    }

    /**
     * TODO 4: gaji dasar induk + tunjangan masa kerja.
     */
    public function hitungGaji(): float
    {
        $tunjangan = min(
            $this->masaKerjaTahun * self::TUNJANGAN_PER_TAHUN,
            self::TUNJANGAN_MAKSIMUM
        );

        return parent::hitungGaji() * (1 + $tunjangan);
    }

    public function jenis(): string
    {
        return 'TETAP';
    }
}


class PegawaiKontrak extends Pegawai
{
    public function __construct(
        string $nip,
        string $nama,
        float $gajiPokok,
        private readonly int $bulanKontrak,
    ) {
        parent::__construct($nip, $nama, $gajiPokok);
    }

    public function jenis(): string
    {
        return 'KONTRAK';
    }

    public function getBulanKontrak(): int
    {
        return $this->bulanKontrak;
    }
}


// Dosen adalah turunan dari PegawaiTetap.
class Dosen extends PegawaiTetap
{
    protected const TUNJANGAN_FUNGSIONAL = 0.10;

    public function hitungGaji(): float
    {
        return parent::hitungGaji()
            * (1 + self::TUNJANGAN_FUNGSIONAL);
    }

    public function jenis(): string
    {
        return 'DOSEN';
    }
}


/**
 * Gaji dihitung berdasarkan jumlah hari kerja.
 */
class PegawaiHarian extends Pegawai
{
    public function __construct(
        string $nip,
        string $nama,
        float $gajiPerHari,
        private readonly int $hariKerja,
    ) {
        parent::__construct($nip, $nama, $gajiPerHari);
    }

    public function hitungGaji(): float
    {
        return parent::hitungGaji() * $this->hariKerja;
    }

    public function jenis(): string
    {
        return 'HARIAN';
    }

    public function getHariKerja(): int
    {
        return $this->hariKerja;
    }
}