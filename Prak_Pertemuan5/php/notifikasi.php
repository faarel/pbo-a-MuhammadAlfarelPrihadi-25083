<?php
declare(strict_types=1);

abstract class Notifikasi
{
    public function __construct(public readonly string $tujuan)
    {
    }

    public function getTujuan(): string
    {
        return $this->tujuan;
    }

    abstract public function kirim(string $pesan): void;

    abstract public function saluran(): string;
}

class Email extends Notifikasi
{
    public function saluran(): string
    {
        return 'Email';
    }

    public function kirim(string $pesan): void
    {
        echo sprintf(
            "[%s] Mengirim email ke <%s>:\n\"%s\"\n\n",
            $this->saluran(),
            $this->tujuan,
            $pesan
        );
    }
}

class SMS extends Notifikasi
{
    public function saluran(): string
    {
        return 'SMS';
    }

    public function kirim(string $pesan): void
    {
        echo sprintf(
            "[%s] SMS terkirim ke nomor %s: %s\n\n",
            $this->saluran(),
            $this->tujuan,
            $pesan
        );
    }
}

class WhatsApp extends Notifikasi
{
    public function saluran(): string
    {
        return 'WhatsApp';
    }

    public function kirim(string $pesan): void
    {
        echo sprintf(
            "[%s] WA chat ke %s -> %s\n\n",
            $this->saluran(),
            $this->tujuan,
            $pesan
        );
    }
}

/**
 * @param Notifikasi[] $daftar
 */
function kirimSemua(array $daftar, string $pesan): void
{
    foreach ($daftar as $notifikasi) {
        $notifikasi->kirim($pesan);
    }
}

if (PHP_SAPI !== 'cli') {
    header('Content-Type: text/plain');
}

kirimSemua([
    new Email('ani@univpancasila.ac.id'),
    new SMS('081234567890'),
    new WhatsApp('081234567890'),
], 'Buku yang Anda pesan sudah tersedia.');