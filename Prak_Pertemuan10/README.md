# Praktikum PBO Pertemuan 10 - Exception Handling dan Pemrograman Defensif

| | |
|---|---|
| Nama | Muhammad Alfarel Prihadi |
| NPM | 4525210083 |
| Program Studi | Teknik Informatika |
| Universitas | Universitas Pancasila |

## Tujuan praktikum

1. Membuat hierarki custom exception yang membawa data, bukan hanya pesan.
2. Mengganti `if` bersarang dengan guard clause.
3. Membuat program tetap berjalan wajar untuk masukan pengguna yang salah.
4. Menyusun daftar skenario kegagalan (`skenario-uji.md`) sebagai bahan unit test sesi 11.

## Penjelasan program

Sistem Perpustakaan konsol dengan dua implementasi yang setara: Java dan PHP.
Menu: pinjam buku, tambah buku, lihat daftar buku, ekspor daftar buku, dan simulasi ekspor gagal.

```
Prak_Pertemuan10_Alfarel/
├── README.md, skenario-uji.md
├── java/                     proyek Maven (Java 21)
│   ├── pom.xml
│   ├── logs/perpustakaan.log, hasil-ekspor/   (hasil uji)
│   └── src/main/java/id/ac/univpancasila/perpustakaan/
│       ├── App.java                    menu, validasi masukan, satu catch domain
│       ├── LibraryServiceKotor.java    versi sebelum refactoring (pembanding, jangan dihapus)
│       ├── LibraryService.java         versi guard clause
│       ├── EksporBuku.java             ekspor dengan try-with-resources
│       ├── PencatatLog.java            logging
│       └── exception/                  PerpustakaanException + 4 turunan
├── php/                      app.php, exceptions.php, LibraryServiceKotor.php,
│                             LibraryService.php, EksporBuku.php (finally), PencatatLog.php
└── uji/                      berkas masukan skenario, jalankan-uji.sh, hasil/ (keluaran asli)
```

- **Exception**: `BukuTidakDitemukanException` (isbn), `AnggotaTidakDitemukanException` (idAnggota),
  `BukuSedangDipinjamException` (isbn, judul; dipakai saat stok habis), `BatasPinjamTerlampauiException`
  (idAnggota, batas). Semuanya turunan `PerpustakaanException` (unchecked) dengan atribut, constructor, getter,
  dan pesan yang menyebut datanya.
- **Guard clause**: `pinjam()` memeriksa anggota, buku, stok, dan batas pinjam satu per satu, masing-masing
  langsung `throw`. Kedalaman `if` pada `LibraryServiceKotor.pinjam()` adalah 4 tingkat; pada `LibraryService.pinjam()` hanya 1.
- **Masukan**: kosong, spasi saja, huruf, desimal, negatif, di luar rentang, dan angka terlalu besar ditolak
  dan diminta ulang. Masukan yang habis (Ctrl+D / Ctrl+Z) mengakhiri program dengan rapi.
- **Satu catch domain**: di `App`, `catch (PerpustakaanException e)` memakai pemeriksaan tipe untuk mengambil
  data spesifik (isbn, idAnggota, judul, batas).
- **Ekspor**: Java memakai try-with-resources, PHP memakai `finally`. Menu 5 sengaja melempar exception di
  tengah penulisan; pesan `[INFO] Berkas ditutup ...` tampil sebelum pesan gagal sebagai bukti berkas tertutup.
- **Logging**: setiap kegagalan dicatat ke `logs/perpustakaan.log` dengan format
  `waktu | jenis exception | data penyebab | pesan` (zona WIB). Contoh:
  `2026-11-11 19:45:57 | BukuTidakDitemukanException | isbn=978-9 | Buku dengan ISBN 978-9 tidak ditemukan`.
  Kesalahan masukan (kosong, di luar rentang) juga dicatat dengan jenis `MasukanKosong`, `MasukanDiLuarRentang`, dan sebagainya.

## Cara menjalankan

Prasyarat: JDK 21, Maven, dan PHP 8.3 atau lebih baru. Buka folder `Prak_Pertemuan10_Alfarel` di VS Code.

**Java** (dari folder `java/`):

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass=id.ac.univpancasila.perpustakaan.App
```

**PHP** (dari folder `php/`):

```bash
php app.php
```

**Menjalankan seluruh skenario uji** (Linux/macOS/Git Bash, setelah `mvn clean compile`):

```bash
cd uji
./jalankan-uji.sh
```

Anggota yang terdaftar: `A001`, `A002`, `A003`. Batas pinjam 3 buku.

## Contoh pengujian

Pinjam ISBN yang tidak ada (`uji/hasil/java/01-isbn-tidak-terdaftar.txt`):

```
Pilih menu (0-5): ID anggota: ISBN buku: [GAGAL] Buku dengan ISBN 978-9 tidak ditemukan. Periksa ISBN pada menu 3 (Lihat daftar buku).
```

Ekspor gagal di tengah penulisan (Java):

```
[INFO] Berkas ditutup otomatis (try-with-resources): hasil-ekspor/simulasi-gagal.txt
[GAGAL] Ekspor gagal (IOException): Simulasi kegagalan di tengah penulisan (setelah 2 baris buku)
```

Daftar lengkap 15 skenario beserta pesannya ada di [skenario-uji.md](skenario-uji.md). Hasil ekspor contoh:
`java/hasil-ekspor/daftar-buku.txt` dan `php/hasil-ekspor/daftar-buku.txt`.

## Catatan lingkungan

- Dikompilasi dengan Java 21 (`maven.compiler.release=21`). Modul menyebut Java 25, tetapi tugas ini memakai JDK 21.
- `pom.xml` tidak mengikat versi Maven tertentu. Di lingkungan pengerjaan, Maven Central tidak dapat diakses,
  sehingga `mvn clean compile` belum dijalankan di sana; kode dikompilasi dengan `javac --release 21` (compiler yang sama)
  dan seluruh skenario dijalankan dari `target/classes`. Jalankan `mvn clean compile` di komputer sendiri sebelum demo.
- PHP diuji pada versi 8.3.6 (`public const int` memerlukan PHP 8.3 atau lebih baru).

## Kesimpulan

Exception yang membawa data membuat kegagalan bisa diolah (ditampilkan, dicatat, diubah menjadi respons HTTP pada
sesi 13-14), bukan hanya dibaca sebagai kalimat. Guard clause membuat `pinjam()` rata dan setiap alasan gagal
terlihat jelas, sedangkan versi `Kotor` hanya mengembalikan `false` tanpa alasan. Validasi masukan dan satu blok
`catch (PerpustakaanException e)` membuat program tidak pernah berhenti karena stack trace, try-with-resources dan
`finally` menjamin berkas tertutup, dan log memberi jejak untuk menelusuri sebab kegagalan.
