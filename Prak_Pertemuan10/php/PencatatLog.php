<?php
declare(strict_types=1);

/**
 * Logging sederhana: satu baris per kejadian ke berkas log.
 * Format: waktu | jenis exception | data penyebab | pesan
 */
final class PencatatLog
{
    public function __construct(private readonly string $berkasLog)
    {
    }

    public function catat(string $jenis, string $dataPenyebab, string $pesan): void
    {
        $baris = date('Y-m-d H:i:s') . " | {$jenis} | {$dataPenyebab} | {$pesan}" . PHP_EOL;

        $folder = dirname($this->berkasLog);
        if (!is_dir($folder)) {
            @mkdir($folder, 0777, true);
        }
        if (@file_put_contents($this->berkasLog, $baris, FILE_APPEND | LOCK_EX) === false) {
            // Gagal mencatat log tidak boleh menjatuhkan program.
            fwrite(STDERR, "[PERINGATAN] Log tidak dapat ditulis: {$this->berkasLog}" . PHP_EOL);
        }
    }
}
