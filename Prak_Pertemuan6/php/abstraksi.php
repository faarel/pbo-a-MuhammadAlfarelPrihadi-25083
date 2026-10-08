<?php
declare(strict_types=1);

// ══ INTERFACE — kontrak "apa yang bisa dilakukan" ═══════════════
interface Movable
{
    public function bergerak(): void;
    public function kecepatanMaksimum(): float;
}

interface Fuelable
{
    public function isiBahanBakar(float $jumlah): void;
    public function kapasitasTangki(): float;
    public function tipeBahanBakar(): TipeBahanBakar;
}

// ══ ENUM (PHP 8.1+) — backed enum, punya nilai string ══════════
enum TipeBahanBakar: string
{
    case Bensin = 'bensin';
    case Solar = 'solar';
    case Listrik = 'listrik';

    /** TODO 2: kembalikan label yang enak dibaca. */
    public function label(): string
    {
        return match ($this) {
            self::Bensin => 'Bensin',
            self::Solar => 'Solar',
            self::Listrik => 'Listrik',
        };
    }

    /** TODO 3: Bensin 12000 · Solar 10500 · Listrik 2500 */
    public function hargaPerSatuan(): float
    {
        return match ($this) {
            self::Bensin => 12000,
            self::Solar => 10500,
            self::Listrik => 2500,
        };
    }

    /** TODO 4 */
    public function biayaPengisian(float $jumlah): float
    {
        return $jumlah * $this->hargaPerSatuan();
    }

    /** TODO 5: hanya Listrik yang ramah lingkungan. */
    public function ramahLingkungan(): bool
    {
        return $this === self::Listrik;
    }
}

// ══ TRAIT — penggunaan ulang horizontal, khas PHP ══════════════
trait Loggable
{
    /**
     * TODO 6: cetak baris log berformat:
     *         [14:32:05] Mobil: servis berkala selesai
     */
    public function log(string $pesan): void
    {
        printf('[%s] %s: %s', date('H:i:s'), static::class, $pesan) . PHP_EOL;
    }
} 

// ══ ABSTRACT CLASS — kode yang benar-benar sama ═══════════════
abstract class Kendaraan
{
    public function __construct(
        protected readonly string $merek,
        protected readonly int $tahun,
    ) {}

    /** TODO 7: umur kendaraan, tidak boleh negatif. */
    public function umur(int $tahunSekarang): int
    {
        return max(0, $tahunSekarang - $this->tahun);
    }

    abstract public function jumlahRoda(): int;

    public function __toString(): string
    {
        return sprintf('%s (%d, %d roda)', $this->merek, $this->tahun, $this->jumlahRoda());
    }
}

final class Mobil extends Kendaraan implements Movable, Fuelable
{
    use Loggable;

    private float $isiTangki = 0;

    public function __construct(
        string $merek,
        int $tahun,
        private readonly float $kapasitas
    ) {
        parent::__construct($merek, $tahun);
    }

    public function jumlahRoda(): int
    {
        return 4;
    }

    // TODO 8: lengkapi kontrak Movable dan Fuelable.
    public function bergerak(): void
    {
        echo "Mobil sedang bergerak." . PHP_EOL;
    }

    public function kecepatanMaksimum(): float
    {
        return 180.0;
    }

    public function isiBahanBakar(float $jumlah): void
    {
        if ($jumlah < 0) {
            echo "Jumlah bahan bakar tidak boleh negatif." . PHP_EOL;
            return;
        }
        

        $this->isiTangki = min(
            $this->kapasitas,
            $this->isiTangki + $jumlah
        );

        echo "Bahan bakar berhasil diisi." . PHP_EOL;
    }

    public function kapasitasTangki(): float
    {
        return $this->kapasitas;
    }

    public function tipeBahanBakar(): TipeBahanBakar
    {
        return TipeBahanBakar::Bensin;
    }

    public function getIsiTangki(): float
    {
        return $this->isiTangki;
    }
}

// ══ SEPEDA — Movable tetapi bukan Fuelable ═════════════════════
final class Sepeda extends Kendaraan implements Movable
{
    public function jumlahRoda(): int
    {
        return 2;
    }

    public function bergerak(): void
    {
        echo "Sepeda sedang dikayuh." . PHP_EOL;
    }

    public function kecepatanMaksimum(): float
    {
        return 40.0;
    }
}

// ══ PESANAN — bukan Kendaraan, tetapi menggunakan Loggable ════
final class Pesanan
{
    use Loggable;

    public function __construct(
        private readonly string $nama,
        private readonly float $total
    ) {}

    public function tampilkan(): void
    {
        echo "Pesanan: {$this->nama}, Total: Rp " . number_format(
            $this->total,
            0,
            ',',
            '.'
        ) . PHP_EOL;
    }
}