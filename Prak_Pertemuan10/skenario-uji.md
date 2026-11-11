# Skenario Kegagalan - Sistem Perpustakaan

Nama: Muhammad Alfarel Prihadi - NPM 4525210083 - Teknik Informatika, Universitas Pancasila

Setiap skenario di bawah **sudah dijalankan** pada program Java dan PHP. Masukan tiap skenario
disimpan di folder `uji/` (berkas `NN-*.txt`) dan keluaran aslinya ada di `uji/hasil/java/` dan
`uji/hasil/php/`. Seluruh skenario dijalankan ulang dengan `uji/jalankan-uji.sh`.

Kondisi awal data: buku `978-1` (3 eksemplar), `978-2` (1), `978-3` (5), `978-4` (2);
anggota `A001`, `A002`, `A003`; batas pinjam 3 buku.

| No | Masukan / tindakan | Kegagalan yang diharapkan | Jenis exception | Pesan yang ditampilkan | Hasil uji |
|----|--------------------|---------------------------|-----------------|------------------------|-----------|
| 1  | Menu 1, anggota `A001`, ISBN `978-9` (tidak terdaftar) | Buku tidak ditemukan | BukuTidakDitemukanException | Buku dengan ISBN 978-9 tidak ditemukan. Periksa ISBN pada menu 3 (Lihat daftar buku). | Lulus (Java dan PHP) |
| 2  | Menu 1, anggota `A999` (tidak terdaftar), ISBN `978-1` | Anggota tidak ditemukan | AnggotaTidakDitemukanException | Anggota dengan ID A999 belum terdaftar. Gunakan ID anggota yang terdaftar: A001, A002, A003. | Lulus |
| 3  | `A001` pinjam `978-2` (berhasil, stok jadi 0), lalu `A002` pinjam `978-2` | Stok habis | BukuSedangDipinjamException | Buku "Basis Data" (ISBN 978-2) sedang dipinjam semua, stok habis. Coba buku lain atau tunggu ada yang mengembalikan. | Lulus |
| 4  | `A001` pinjam `978-1`, `978-3`, `978-4` (berhasil semua), lalu pinjam `978-1` lagi | Batas pinjam terlampaui | BatasPinjamTerlampauiException | Anggota A001 sudah mencapai batas peminjaman maksimum (3 buku). Anggota harus mengembalikan buku lebih dulu. | Lulus |
| 5  | Menu 1, ID anggota dikosongkan (Enter), lalu hanya spasi, lalu `A001` | Masukan kosong ditolak, diminta ulang | (validasi masukan, dicatat `MasukanKosong`) | Masukan tidak boleh kosong atau hanya spasi. Silakan ulangi. | Lulus, lalu pinjam berhasil |
| 6  | Menu 1, `A001`, ISBN hanya spasi, lalu `  978-3  ` (diapit spasi) | Spasi saja ditolak; spasi di tepi dibuang (trim) | (validasi masukan, `MasukanKosong`) | Masukan tidak boleh kosong atau hanya spasi. Silakan ulangi. | Lulus, pinjam `978-3` berhasil |
| 7  | Di menu: `abc` dan `1.5`; di jumlah eksemplar: `dua` | Huruf/desimal saat diminta angka | NumberFormatException di Java (`MasukanBukanAngka` di PHP) | "abc" bukan bilangan bulat yang valid (huruf, desimal, atau terlalu besar). Silakan ulangi. | Lulus |
| 8  | Di menu: `-1`; di jumlah eksemplar: `-5` dan `0` | Angka negatif / nol ditolak | (validasi rentang, `MasukanDiLuarRentang`) | Angka harus antara 1 dan 1000 (negatif tidak diterima). Silakan ulangi. | Lulus |
| 9  | Di jumlah eksemplar: `99999999999` dan `5000` | Angka sangat besar | NumberFormatException di Java (`MasukanBukanAngka` di PHP); `5000` lewat `MasukanDiLuarRentang` | "99999999999" bukan bilangan bulat yang valid (huruf, desimal, atau terlalu besar). Silakan ulangi. | Lulus |
| 10 | Menu 4, nama berkas `folder-tidak-ada/daftar.txt` | Berkas ekspor tidak dapat ditulis | NoSuchFileException (Java), RuntimeException (PHP) | Ekspor gagal (NoSuchFileException): folder-tidak-ada/daftar.txt | Lulus |
| 11 | Menu 5 (simulasi gagal di tengah penulisan, setelah 2 baris buku) | Exception di tengah penulisan, berkas tetap tertutup | IOException (Java), RuntimeException (PHP) | Pesan `[INFO] Berkas ditutup ...` muncul lebih dulu, lalu: Ekspor gagal (IOException): Simulasi kegagalan di tengah penulisan (setelah 2 baris buku) | Lulus, `simulasi-gagal.txt` berisi header + 2 buku |
| 12 | Menu 4, nama berkas dikosongkan (default) | Kontrol: ekspor normal, tidak gagal | - | [OK] 4 buku diekspor ke hasil-ekspor/daftar-buku.txt | Lulus |
| 13 | Di menu: `99` dan `6` | Pilihan menu di luar rentang | (validasi rentang, `MasukanDiLuarRentang`) | Angka harus antara 0 dan 5 (negatif tidak diterima). Silakan ulangi. | Lulus |
| 14 | `A002` pinjam `978-3`, lalu menu 3 | Kontrol: peminjaman berhasil | - | [OK] A002 berhasil meminjam "Algoritma dan Struktur Data". Sisa eksemplar: 4. Total pinjaman A002: 1 | Lulus |
| 15 | Masukan berhenti mendadak (end of input) tanpa memilih menu 0 | Program berhenti rapi, tanpa stack trace | (penanda `MasukanSelesai`) | Masukan habis. Program selesai. | Lulus |

Catatan: baris 12 dan 14 adalah kontrol (jalur normal) agar jelas bahwa program tidak hanya bisa gagal.
Selain itu, kesalahan pemrogram pada service diuji terpisah (bukan lewat menu, karena `App` sudah menjaganya):
`tambahBuku("978-1", "X", -5)` menghasilkan `IllegalArgumentException` (Java) / `InvalidArgumentException` (PHP)
dengan pesan "Jumlah eksemplar harus lebih dari 0, diterima: -5", dan ISBN spasi saja menghasilkan "ISBN tidak boleh kosong".

## Pertanyaan yang harus dijawab

**1. Mana yang kesalahan pengguna, mana yang kesalahan pemrogram? Bagaimana pengaruhnya pada jenis exception?**

Skenario 1 sampai 15 semuanya kesalahan pengguna (ISBN salah, anggota tidak ada, stok habis, batas pinjam,
huruf/negatif/kosong, nama berkas salah). Itu kegagalan yang wajar terjadi dan harus dijelaskan kepada pengguna,
sehingga diwakili exception domain (turunan `PerpustakaanException`) yang membawa data, ditangkap, dicatat,
lalu program lanjut. Kesalahan pemrogram adalah pemanggilan `LibraryService` dengan argumen yang tidak boleh
terjadi (misalnya eksemplar negatif); untuk itu dipakai exception bawaan (`IllegalArgumentException`) yang tidak
masuk hierarki domain. Pemisahan ini membuat satu `catch (PerpustakaanException e)` hanya menangkap kegagalan
yang memang bisa dijelaskan ke pengguna, sedangkan bug tidak ikut tertelan dan jatuh ke jaring pengaman
di lapisan paling luar yang mencatatnya sebagai "kesalahan tak terduga".

**2. PerpustakaanException diturunkan dari RuntimeException atau Exception? Alasannya.**

Dari `RuntimeException` (unchecked). Pertama, kegagalannya menandakan permintaan yang tidak sah, bukan keadaan luar
yang perlu dipulihkan di setiap lapisan. Kedua, kalau checked, setiap method yang memanggil `pinjam()` harus
menulis `throws` atau `try-catch`, padahal yang tahu cara menanganinya hanya `App`; kode di tengah menjadi penuh
deklarasi. Ketiga, pada sesi 13 dan 14 exception ini akan diubah menjadi respons HTTP di satu tempat, yang cocok
dengan model unchecked. Latihan mandiri 1 (mengubah menjadi checked) tidak saya kerjakan.
Di PHP semua exception memang setara unchecked, sehingga `@throws` di PHPDoc yang menggantikan peran kompilator.

**3. (Setelah Latihan) Apa yang terjadi saat catch dikosongkan dengan sengaja?**

Pada salinan proyek Java (bukan pada hasil akhir), isi `catch (PerpustakaanException e)` saya kosongkan lalu
menjalankan skenario 1 (ISBN `978-9` tidak terdaftar). Hasilnya: tidak ada pesan apa pun, tidak ada baris di
berkas log (folder `logs` bahkan tidak terbentuk), dan program langsung kembali ke menu seolah-olah peminjaman
berhasil. Pengguna tidak tahu bahwa peminjaman gagal, dan pengembang tidak punya jejak untuk mencari sebabnya.
Kegagalan "hilang tanpa jejak". Kode sudah dikembalikan ke versi yang benar.
