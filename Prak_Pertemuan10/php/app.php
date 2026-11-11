<?php
declare(strict_types=1);

/**
 * Berkas utama PHP - antarmuka konsol Sistem Perpustakaan (Sesi 10 Langkah 3-5).
 * Jalankan dengan:  php app.php
 */

require_once __DIR__ . '/exceptions.php';
require_once __DIR__ . '/LibraryService.php';
require_once __DIR__ . '/PencatatLog.php';
require_once __DIR__ . '/EksporBuku.php';

/** Penanda bahwa masukan sudah habis (Ctrl+D / Ctrl+Z atau input dari berkas selesai). */
final class MasukanSelesai extends RuntimeException
{
}

final class App
{
    private const int MAKS_EKSEMPLAR = 1000;
    private const string EKSPOR_DEFAULT = 'hasil-ekspor/daftar-buku.txt';
    private const string EKSPOR_SIMULASI = 'hasil-ekspor/simulasi-gagal.txt';

    private LibraryService $service;
    private PencatatLog $log;
    private EksporBuku $eksporBuku;

    public function __construct()
    {
        $this->service = new LibraryService();
        $this->log = new PencatatLog('logs/perpustakaan.log');
        $this->eksporBuku = new EksporBuku();
    }

    public function jalankan(): void
    {
        $this->siapkanData();
        echo "=== SISTEM PERPUSTAKAAN ===" . PHP_EOL;
        echo 'Anggota terdaftar: A001, A002, A003 (batas pinjam ' . LibraryService::BATAS_PINJAM . ' buku)' . PHP_EOL;

        try {
            while (true) {
                $this->tampilkanMenu();
                $pilihan = $this->bacaAngka('Pilih menu (0-5): ', 0, 5);
                if ($pilihan === 0) {
                    echo 'Terima kasih. Program selesai.' . PHP_EOL;
                    return;
                }
                try {
                    $this->prosesMenu($pilihan);
                } catch (PerpustakaanException $e) {
                    // Satu catch untuk SELURUH kegagalan domain (Langkah 4).
                    $this->tanganiKegagalanDomain($e);
                } catch (MasukanSelesai $e) {
                    throw $e;
                } catch (RuntimeException $e) {
                    // Kegagalan berkas (gagal buka/tulis) dilempar sebagai RuntimeException.
                    $pesan = 'Ekspor gagal (' . $e::class . '): ' . $e->getMessage();
                    echo '[GAGAL] ' . $pesan . PHP_EOL;
                    $this->log->catat($e::class, 'berkas/proses ekspor', $pesan);
                } catch (Throwable $e) {
                    // Lapisan paling luar: jaring pengaman untuk bug tak terduga (termasuk TypeError).
                    $pesan = 'Terjadi kesalahan tak terduga: ' . $e->getMessage();
                    echo '[GAGAL] ' . $pesan . PHP_EOL;
                    $this->log->catat($e::class, 'tidak diketahui', $pesan);
                }
            }
        } catch (MasukanSelesai) {
            echo PHP_EOL . 'Masukan habis. Program selesai.' . PHP_EOL;
        }
    }

    private function siapkanData(): void
    {
        $this->service->tambahBuku('978-1', 'Pemrograman Berorientasi Objek', 3);
        $this->service->tambahBuku('978-2', 'Basis Data', 1);
        $this->service->tambahBuku('978-3', 'Algoritma dan Struktur Data', 5);
        $this->service->tambahBuku('978-4', 'Jaringan Komputer', 2);
        $this->service->daftarkanAnggota('A001');
        $this->service->daftarkanAnggota('A002');
        $this->service->daftarkanAnggota('A003');
        if (!is_dir('hasil-ekspor') && !@mkdir('hasil-ekspor', 0777, true)) {
            fwrite(STDERR, '[PERINGATAN] Folder hasil-ekspor tidak dapat dibuat' . PHP_EOL);
        }
    }

    private function tampilkanMenu(): void
    {
        echo PHP_EOL . '--- MENU ---' . PHP_EOL;
        echo '1. Pinjam buku' . PHP_EOL;
        echo '2. Tambah buku' . PHP_EOL;
        echo '3. Lihat daftar buku' . PHP_EOL;
        echo '4. Ekspor daftar buku ke berkas' . PHP_EOL;
        echo '5. Uji ekspor gagal di tengah penulisan (simulasi)' . PHP_EOL;
        echo '0. Keluar' . PHP_EOL;
    }

    private function prosesMenu(int $pilihan): void
    {
        match ($pilihan) {
            1 => $this->pinjamBuku(),
            2 => $this->tambahBuku(),
            3 => $this->lihatDaftarBuku(),
            4 => $this->eksporDaftarBuku(),
            5 => $this->simulasiEksporGagal(),
            default => throw new LogicException("Menu tidak dikenal: {$pilihan}"),
        };
    }

    // ---------------------------------------------------------------- menu

    private function pinjamBuku(): void
    {
        $idAnggota = $this->bacaTeks('ID anggota: ');
        $isbn = $this->bacaTeks('ISBN buku: ');
        $this->service->pinjam($idAnggota, $isbn);
        echo "[OK] {$idAnggota} berhasil meminjam \"{$this->service->judulDari($isbn)}\". "
            . "Sisa eksemplar: {$this->service->sisaEksemplar($isbn)}. "
            . "Total pinjaman {$idAnggota}: {$this->service->jumlahPinjaman($idAnggota)}" . PHP_EOL;
    }

    private function tambahBuku(): void
    {
        $isbn = $this->bacaTeks('ISBN: ');
        $judul = $this->bacaTeks('Judul: ');
        $eksemplar = $this->bacaAngka('Jumlah eksemplar (1-' . self::MAKS_EKSEMPLAR . '): ', 1, self::MAKS_EKSEMPLAR);
        $this->service->tambahBuku($isbn, $judul, $eksemplar);
        echo "[OK] Buku \"{$judul}\" ditambahkan. Sisa eksemplar: {$this->service->sisaEksemplar($isbn)}" . PHP_EOL;
    }

    private function lihatDaftarBuku(): void
    {
        echo 'No | ISBN | Judul | Sisa Eksemplar' . PHP_EOL;
        $nomor = 0;
        foreach ($this->service->daftarIsbn() as $isbn) {
            $nomor++;
            echo "{$nomor} | {$isbn} | {$this->service->judulDari($isbn)} | {$this->service->sisaEksemplar($isbn)}" . PHP_EOL;
        }
    }

    private function eksporDaftarBuku(): void
    {
        $nama = trim($this->bacaBaris('Nama berkas (Enter = ' . self::EKSPOR_DEFAULT . '): '));
        $tujuan = $nama === '' ? self::EKSPOR_DEFAULT : $nama;
        $jumlah = $this->eksporBuku->eksporKeBerkas($this->service, $tujuan);
        echo "[OK] {$jumlah} buku diekspor ke {$tujuan}" . PHP_EOL;
    }

    private function simulasiEksporGagal(): void
    {
        $this->eksporBuku->eksporKeBerkas($this->service, self::EKSPOR_SIMULASI, 2);
        echo '[OK] Seharusnya tidak sampai sini.' . PHP_EOL;
    }

    // ----------------------------------------------- penanganan kegagalan

    private function tanganiKegagalanDomain(PerpustakaanException $e): void
    {
        // Pemeriksaan tipe untuk mengambil data spesifik dari exception tertentu.
        [$data, $saran] = match (true) {
            $e instanceof BukuTidakDitemukanException => [
                'isbn=' . $e->getIsbn(),
                'Periksa ISBN pada menu 3 (Lihat daftar buku).',
            ],
            $e instanceof AnggotaTidakDitemukanException => [
                'idAnggota=' . $e->getIdAnggota(),
                'Gunakan ID anggota yang terdaftar: A001, A002, A003.',
            ],
            $e instanceof BukuSedangDipinjamException => [
                'isbn=' . $e->getIsbn() . ', judul=' . $e->getJudul(),
                'Coba buku lain atau tunggu ada yang mengembalikan.',
            ],
            $e instanceof BatasPinjamTerlampauiException => [
                'idAnggota=' . $e->getIdAnggota() . ', batas=' . $e->getBatas(),
                'Anggota harus mengembalikan buku lebih dulu.',
            ],
            default => ['-', ''],
        };
        echo '[GAGAL] ' . $e->getMessage() . '. ' . $saran . PHP_EOL;
        $this->log->catat($e::class, $data, $e->getMessage());
    }

    // --------------------------------------------------------- baca masukan

    private function bacaBaris(string $prompt): string
    {
        echo $prompt;
        $baris = fgets(STDIN);
        if ($baris === false) {
            throw new MasukanSelesai();
        }
        return rtrim($baris, "\r\n");
    }

    /** Meminta teks sampai pengguna mengisi sesuatu (kosong dan spasi saja ditolak). */
    private function bacaTeks(string $prompt): string
    {
        while (true) {
            $teks = trim($this->bacaBaris($prompt));
            if ($teks !== '') {
                return $teks;
            }
            $this->masukanTidakValid('MasukanKosong', 'masukan kosong/spasi',
                'Masukan tidak boleh kosong atau hanya spasi. Silakan ulangi.');
        }
    }

    /** Meminta bilangan bulat dalam rentang [min, max]; salah input -> diminta ulang. */
    private function bacaAngka(string $prompt, int $min, int $max): int
    {
        while (true) {
            $teks = trim($this->bacaBaris($prompt));
            if ($teks === '') {
                $this->masukanTidakValid('MasukanKosong', 'masukan kosong/spasi',
                    'Masukan tidak boleh kosong atau hanya spasi. Silakan ulangi.');
                continue;
            }
            // Hanya bilangan bulat (tanda +/- opsional); huruf dan desimal ditolak.
            if (preg_match('/^[+-]?\d+$/', $teks) !== 1
                || strlen(ltrim(ltrim($teks, '+-'), '0')) > 9) {
                $this->masukanTidakValid('MasukanBukanAngka', "masukan={$teks}",
                    "\"{$teks}\" bukan bilangan bulat yang valid (huruf, desimal, atau terlalu besar). Silakan ulangi.");
                continue;
            }
            $angka = (int) $teks;
            if ($angka >= $min && $angka <= $max) {
                return $angka;
            }
            $this->masukanTidakValid('MasukanDiLuarRentang', "masukan={$teks}",
                "Angka harus antara {$min} dan {$max} (negatif tidak diterima). Silakan ulangi.");
        }
    }

    private function masukanTidakValid(string $jenis, string $data, string $pesan): void
    {
        echo '[MASUKAN] ' . $pesan . PHP_EOL;
        $this->log->catat($jenis, $data, $pesan);
    }
}

// Semua path relatif (log, hasil-ekspor) dihitung dari folder php/, di mana pun dijalankan.
chdir(__DIR__);
date_default_timezone_set('Asia/Jakarta'); // WIB, konsisten dengan versi Java
(new App())->jalankan();
