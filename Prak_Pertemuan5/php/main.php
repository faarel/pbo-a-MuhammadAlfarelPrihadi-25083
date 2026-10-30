<?php
declare(strict_types=1);

require_once __DIR__ . '/BangunDatar.php';

$daftar = [
    new Lingkaran(7),
    new Persegi(5),
    new Segitiga(3, 4, 5),
];

echo '=== Bangun Datar ===', PHP_EOL;

foreach ($daftar as $b) {
    echo '  ', $b, PHP_EOL;
}

$total = array_sum(
    array_map(
        fn (BangunDatar $b): float => $b->luas(),
        $daftar
    )
);

printf('%s  Total luas: %.2f%s', PHP_EOL, $total, PHP_EOL);

echo PHP_EOL;
echo 'Periksa: Lingkaran(7) luas = 153,94 ; Persegi(5) luas = 25,00', PHP_EOL;
echo 'Segitiga (3,4,5) luas = 6,00', PHP_EOL;