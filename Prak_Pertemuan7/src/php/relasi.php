<?php
declare(strict_types=1);

/**
 * Sesi 7 — komposisi dan agregasi (PHP).
 * Bandingkan dengan versi Java.
 */

enum JenisKeanggotaan: string
{
    case MAHASISWA = 'MAHASISWA';
    case DOSEN = 'DOSEN';
    case UMUM = 'UMUM';
}

// [UML: enum StatusKoleksi]
enum StatusKoleksi: string
{
    case TERSEDIA = 'TERSEDIA';
    case DIPINJAM = 'DIPINJAM';
    case HILANG = 'HILANG';
}

/**
 * ============================================================
 * [UML: package model -> class Bab]
 * Representasi kotak Bab dengan atribut -nomor dan -judul
 */
final class Bab
{
    // [UML Atribut]: -nomor : int (private) [Multiplicity 1: tepat satu]
    private int $nomor;

    // [UML Atribut]: -judul : String (private) [Multiplicity 1: tepat satu]
    private string $judul;

    // TODO 1: constructor nomor dan judul
    public function __construct(int $nomor, string $judul)
    {
        $this->nomor = $nomor;
        $this->judul = $judul;
    }

    public function getNomor(): int { return $this->nomor; }

    public function getJudul(): string { return $this->judul; }

    public function __toString(): string
    {
        return sprintf('Bab %d: %s', $this->nomor, $this->judul);
    }
}

final class Buku
{
    /** @var Bab[] */
    private array $bab = [];
    private string $nomorInduk;
    private string $judul;
    private string $penulis;
    private int $jumlahEksemplar;
    private StatusKoleksi $status;

    public function __construct(
        string $nomorInduk,
        string $judul,
        string $penulis,
        int $jumlahEksemplar,
        StatusKoleksi $status = StatusKoleksi::TERSEDIA
    ) {
        if ($jumlahEksemplar < 0) {
            throw new InvalidArgumentException('Jumlah eksemplar tidak boleh negatif.');
        }

        $this->nomorInduk = $nomorInduk;
        $this->judul = $judul;
        $this->penulis = $penulis;
        $this->jumlahEksemplar = $jumlahEksemplar;
        $this->status = $status;
    }

    // TODO 3: instansiasi Bab di dalam Buku (komposisi)
    public function tambahBab(int $nomor, string $judulBab): void
    {
        $this->bab[] = new Bab($nomor, $judulBab);
    }

    /** @return Bab[] */
    public function getBab(): array { return $this->bab; }

    public function tersedia(): bool { return $this->jumlahEksemplar > 0; }

    // TODO 4: kurangi stok jika tersedia
    public function kurangiStok(): void
    {
        if (!$this->tersedia()) {
            throw new UnderflowException('Stok buku habis.');
        }

        $this->jumlahEksemplar--;
        if ($this->jumlahEksemplar === 0) {
            $this->status = StatusKoleksi::DIPINJAM;
        }
    }

    public function tambahStok(): void
    {
        $this->jumlahEksemplar++;
        $this->status = StatusKoleksi::TERSEDIA;
    }

    public function getNomorInduk(): string { return $this->nomorInduk; }
    public function getJudul(): string { return $this->judul; }
    public function getStatus(): StatusKoleksi { return $this->status; }

    public function __toString(): string
    {
        return sprintf(
            '%-32s %-16s [%d eks, %d bab, status: %s]',
            $this->judul,
            $this->penulis,
            $this->jumlahEksemplar,
            count($this->bab),
            $this->status->value
        );
    }
}

final class Mahasiswa
{
    private string $nim;
    private string $nama;
    private JenisKeanggotaan $jenis;

    public function __construct(
        string $nim,
        string $nama,
        JenisKeanggotaan $jenis = JenisKeanggotaan::MAHASISWA
    ) {
        $this->nim = $nim;
        $this->nama = $nama;
        $this->jenis = $jenis;
    }

    public function getNim(): string { return $this->nim; }
    public function getNama(): string { return $this->nama; }
    public function getJenis(): JenisKeanggotaan { return $this->jenis; }

    public function __toString(): string
    {
        return sprintf('%s %s (%s)', $this->nim, $this->nama, $this->jenis->value);
    }
}

final class Jurusan
{
    /** @var Mahasiswa[] */
    private array $mahasiswa = [];
    private string $kode;
    private string $nama;

    public function __construct(string $kode, string $nama)
    {
        $this->kode = $kode;
        $this->nama = $nama;
    }

    // TODO 5: tambah mahasiswa, cegah duplikat NIM (agregasi)
    public function daftarkan(Mahasiswa $m): void
    {
        foreach ($this->mahasiswa as $existing) {
            if ($existing->getNim() === $m->getNim()) {
                return;
            }
        }

        $this->mahasiswa[] = $m;
    }

    // TODO 6: hapus dari array tanpa merusak objek asli
    public function keluarkan(Mahasiswa $m): void
    {
        $this->mahasiswa = array_values(array_filter(
            $this->mahasiswa,
            fn(Mahasiswa $x) => $x->getNim() !== $m->getNim()
        ));
    }

    /** @return Mahasiswa[] */
    public function getMahasiswa(): array { return $this->mahasiswa; }

    public function __toString(): string
    {
        return sprintf('%s - %s (%d mahasiswa)', $this->kode, $this->nama, count($this->mahasiswa));
    }
}