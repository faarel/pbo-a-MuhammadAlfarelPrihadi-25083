#!/usr/bin/env bash
# Menjalankan semua skenario (berkas masukan *.txt) pada Java dan PHP,
# lalu menyimpan keluarannya di uji/hasil/. Jalankan dari folder uji/.
# Prasyarat Java: sudah dikompilasi (mvn clean compile -> java/target/classes).
set -u
cd "$(dirname "$0")"
UJI="$PWD"
rm -rf hasil ../java/logs ../java/hasil-ekspor ../php/logs ../php/hasil-ekspor
mkdir -p hasil/java hasil/php

for f in [0-9][0-9]-*.txt; do
  nama="${f%.txt}"
  ( cd ../java && java -cp target/classes id.ac.univpancasila.perpustakaan.App < "$UJI/$f" > "$UJI/hasil/java/$nama.txt" 2>&1 ; echo "exit=$?" >> "$UJI/hasil/java/$nama.txt" )
  ( cd ../php  && php app.php < "$UJI/$f" > "$UJI/hasil/php/$nama.txt" 2>&1 ; echo "exit=$?" >> "$UJI/hasil/php/$nama.txt" )
  echo "selesai: $nama"
done
