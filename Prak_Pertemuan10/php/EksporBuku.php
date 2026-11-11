<?php
declare(strict_types=1);

require_once __DIR__ . '/LibraryService.php';

/**
 * Sesi 10 Langkah 5 - ekspor daftar buku ke berkas teks.
 * PHP tidak punya try-with-resources, jadi berkas ditutup manual di blok finally
 * agar tetap tertutup walau terjadi exception di tengah penulisan.
 */
final class EksporBuku
{
    public const int TANPA_GAGAL = -1;

    /**
     * @param int $gagalSetelahBaris untuk pembuktian: bila >= 0, sengaja melempar
     *                               exception setelah sekian baris buku ditulis.
     * @return int jumlah buku yang ditulis
     * @throws RuntimeException bila berkas tidak dapat dibuka atau ditulis
     */
    public function eksporKeBerkas(LibraryService $service, string $tujuan, int $gagalSetelahBaris = self::TANPA_GAGAL): int
    {
        // @ menekan warning bawaan PHP; kegagalan ditangani lewat exception di bawah.
        $berkas = @fopen($tujuan, 'w');
        if ($berkas === false) {
            throw new RuntimeException("Tidak dapat membuka berkas untuk ditulis: {$tujuan}");
        }

        try {
            $this->tulisBaris($berkas, 'DAFTAR BUKU PERPUSTAKAAN');
            $this->tulisBaris($berkas, 'Diekspor: ' . date('Y-m-d H:i:s'));
            $this->tulisBaris($berkas, 'No | ISBN | Judul | Sisa Eksemplar');

            $nomor = 0;
            foreach ($service->daftarIsbn() as $isbn) {
                if ($nomor === $gagalSetelahBaris) {
                    throw new RuntimeException("Simulasi kegagalan di tengah penulisan (setelah {$nomor} baris buku)");
                }
                $nomor++;
                $this->tulisBaris($berkas, "{$nomor} | {$isbn} | {$service->judulDari($isbn)} | {$service->sisaEksemplar($isbn)}");
            }
            $this->tulisBaris($berkas, "Total judul: {$nomor}");
            return $nomor;
        } finally {
            // Selalu dijalankan: setelah return, setelah exception, maupun normal.
            fclose($berkas);
            $status = is_resource($berkas) ? 'MASIH TERBUKA' : 'tertutup';
            echo "[INFO] Berkas ditutup di blok finally (status: {$status}): {$tujuan}" . PHP_EOL;
        }
    }

    /**
     * @param resource $berkas
     * @throws RuntimeException bila penulisan gagal
     */
    private function tulisBaris($berkas, string $teks): void
    {
        if (fwrite($berkas, $teks . PHP_EOL) === false) {
            throw new RuntimeException('Gagal menulis ke berkas');
        }
    }
}
