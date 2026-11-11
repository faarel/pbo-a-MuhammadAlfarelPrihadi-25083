<?php
declare(strict_types=1);

/**
 * Sesi 10 - hierarki exception domain (PHP).
 *
 * PHP tidak mengenal checked exception; seluruh exception setara dengan
 * unchecked di Java. Karena itu anotasi @throws pada PHPDoc menjadi jauh
 * lebih penting: pembaca kode tidak dibantu kompilator.
 */

abstract class PerpustakaanException extends RuntimeException
{
}

final class BukuTidakDitemukanException extends PerpustakaanException
{
    // Constructor property promotion: $isbn tersimpan sebagai properti readonly.
    public function __construct(private readonly string $isbn)
    {
        parent::__construct("Buku dengan ISBN {$isbn} tidak ditemukan");
    }

    public function getIsbn(): string
    {
        return $this->isbn;
    }
}

final class AnggotaTidakDitemukanException extends PerpustakaanException
{
    public function __construct(private readonly string $idAnggota)
    {
        parent::__construct("Anggota dengan ID {$idAnggota} belum terdaftar");
    }

    public function getIdAnggota(): string
    {
        return $this->idAnggota;
    }
}

final class BukuSedangDipinjamException extends PerpustakaanException
{
    public function __construct(
        private readonly string $isbn,
        private readonly string $judul,
    ) {
        parent::__construct("Buku \"{$judul}\" (ISBN {$isbn}) sedang dipinjam semua, stok habis");
    }

    public function getIsbn(): string
    {
        return $this->isbn;
    }

    public function getJudul(): string
    {
        return $this->judul;
    }
}

final class BatasPinjamTerlampauiException extends PerpustakaanException
{
    public function __construct(
        private readonly string $idAnggota,
        private readonly int $batas,
    ) {
        parent::__construct("Anggota {$idAnggota} sudah mencapai batas peminjaman maksimum ({$batas} buku)");
    }

    public function getIdAnggota(): string
    {
        return $this->idAnggota;
    }

    public function getBatas(): int
    {
        return $this->batas;
    }
}
