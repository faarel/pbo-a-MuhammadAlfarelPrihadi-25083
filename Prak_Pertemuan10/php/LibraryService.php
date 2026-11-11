<?php
declare(strict_types=1);

require_once __DIR__ . '/exceptions.php';

/**
 * Sesi 10 Langkah 2 - HASIL REFACTORING dari LibraryServiceKotor (PHP).
 *
 * Method pinjam() memakai guard clause: setiap syarat yang tidak terpenuhi
 * langsung melempar exception yang menjelaskan alasannya. Logika utama
 * ada di paling bawah dan rata di kiri (tidak ada if bersarang).
 */
final class LibraryService
{
    public const int BATAS_PINJAM = 3;

    /** @var array<string,int> */
    private array $stok = [];
    /** @var array<string,string> */
    private array $judul = [];
    /** @var array<string,int> */
    private array $pinjaman = [];

    /**
     * @throws InvalidArgumentException bila data tidak valid (kesalahan pemrogram)
     */
    public function tambahBuku(string $isbn, string $judulBuku, int $eksemplar): void
    {
        // App sudah memvalidasi masukan; pelanggaran di sini berarti bug di pemanggil.
        if (trim($isbn) === '') {
            throw new InvalidArgumentException('ISBN tidak boleh kosong');
        }
        if (trim($judulBuku) === '') {
            throw new InvalidArgumentException('Judul tidak boleh kosong');
        }
        if ($eksemplar <= 0) {
            throw new InvalidArgumentException("Jumlah eksemplar harus lebih dari 0, diterima: {$eksemplar}");
        }
        $this->stok[$isbn] = ($this->stok[$isbn] ?? 0) + $eksemplar;
        $this->judul[$isbn] = $judulBuku;
    }

    /**
     * @throws InvalidArgumentException bila ID kosong
     */
    public function daftarkanAnggota(string $idAnggota): void
    {
        if (trim($idAnggota) === '') {
            throw new InvalidArgumentException('ID anggota tidak boleh kosong');
        }
        $this->pinjaman[$idAnggota] ??= 0;
    }

    /**
     * Meminjam satu eksemplar buku.
     *
     * @throws AnggotaTidakDitemukanException jika anggota belum terdaftar
     * @throws BukuTidakDitemukanException    jika ISBN tidak terdaftar
     * @throws BukuSedangDipinjamException    jika stok buku habis
     * @throws BatasPinjamTerlampauiException jika anggota sudah meminjam sebanyak batas
     */
    public function pinjam(string $idAnggota, string $isbn): void
    {
        if (!array_key_exists($idAnggota, $this->pinjaman)) {
            throw new AnggotaTidakDitemukanException($idAnggota);
        }
        if (!array_key_exists($isbn, $this->stok)) {
            throw new BukuTidakDitemukanException($isbn);
        }
        if ($this->stok[$isbn] <= 0) {
            throw new BukuSedangDipinjamException($isbn, $this->judul[$isbn]);
        }
        if ($this->pinjaman[$idAnggota] >= self::BATAS_PINJAM) {
            throw new BatasPinjamTerlampauiException($idAnggota, self::BATAS_PINJAM);
        }

        // Logika utama: semua syarat sudah lolos.
        $this->stok[$isbn]--;
        $this->pinjaman[$idAnggota]++;
    }

    public function sisaEksemplar(string $isbn): int
    {
        return $this->stok[$isbn] ?? 0;
    }

    public function judulDari(string $isbn): ?string
    {
        return $this->judul[$isbn] ?? null;
    }

    public function jumlahPinjaman(string $idAnggota): int
    {
        return $this->pinjaman[$idAnggota] ?? 0;
    }

    /**
     * Daftar ISBN sesuai urutan buku ditambahkan.
     *
     * @return list<string>
     */
    public function daftarIsbn(): array
    {
        // Kunci numerik-string seperti "123" otomatis menjadi int di PHP, jadi di-cast.
        return array_map('strval', array_keys($this->stok));
    }
}
