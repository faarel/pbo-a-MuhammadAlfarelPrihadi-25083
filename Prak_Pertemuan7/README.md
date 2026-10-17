# Praktikum Pemrograman Berorientasi Objek (PBO) - Pertemuan 7

Nama: Muhammad Alfarel Prihadi  
NPM: 4525210083  
Program Studi: Teknik Informatika  
Universitas: Universitas Pancasila  
Mata Kuliah: Pemrograman Berorientasi Objek (PBO)  
Pertemuan: 7

## Tujuan Praktikum

- Memahami perbedaan komposisi dan agregasi pada relasi antar kelas.
- Menerapkan komposisi pada kelas `Buku` dan `Bab`.
- Menerapkan agregasi pada kelas `Jurusan` dan `Mahasiswa`.
- Menjaga enkapsulasi dan validasi data di dalam kelas.
- Membuat rancangan class diagram sistem perpustakaan dari deskripsi kebutuhan.

## Materi Praktikum

**Komposisi** adalah relasi "memiliki" yang kuat. Objek bagian tidak bisa berdiri sendiri tanpa induknya. Contohnya `Bab` yang hanya ada di dalam `Buku`.

**Agregasi** adalah relasi "memiliki" yang lebih longgar. Objek bagian dibuat di luar dan tetap ada walaupun induknya dihapus. Contohnya `Mahasiswa` dan `Jurusan`.

**Enkapsulasi** dilakukan dengan atribut `private`, validasi di constructor dan method, serta getter yang mengembalikan salinan list yang tidak bisa diubah.

## Penjelasan Program

Program ada di folder `src` dengan versi Java dan PHP.

- `model/Buku.java`: contoh komposisi. `Bab` dibuat di dalam `Buku` lewat `tambahBab()`, constructor `Bab` bersifat `private`, dan `getBab()` mengembalikan `List.copyOf` sehingga list tidak bisa diubah dari luar. Constructor menolak jumlah eksemplar negatif.
- `model/Jurusan.java`: contoh agregasi. `Mahasiswa` dibuat di luar lalu dimasukkan lewat `daftarkan()` (mencegah NIM ganda) dan bisa dilepas lewat `keluarkan()`.
- `service/PerpustakaanService.java`: kerangka layanan perpustakaan dengan method `tambahKoleksi()`, `daftarkanAnggota()`, `cariKoleksi()`, `pinjam()`, dan `kembalikan()`.
- `Main.java`: program utama untuk mencoba komposisi dan agregasi.
- `php/relasi.php` dan `php/main.php`: versi PHP dari program yang sama.
- `uml/perpustakaan-kerangka.puml`: class diagram sistem perpustakaan.
- `deskripsi-kebutuhan.md`: deskripsi kebutuhan sistem perpustakaan.

**Cara kerja `Main.java`:**
1. Membuat objek `Buku` lalu menambahkan tiga bab.
2. Mencetak data buku dan babnya.
3. Mencoba mengosongkan list bab dari luar, hasilnya ditolak karena list tidak bisa diubah.
4. Membuat mahasiswa Ani dan Budi, lalu mendaftarkannya ke jurusan Teknik Informatika.
5. Mengeluarkan Budi dari jurusan, dan objek Budi tetap ada.

Program tidak meminta input dari pengguna.

**Contoh output:**

```text
=== Komposisi: Buku memiliki Bab ===
  Pemrograman Berorientasi Objek   A. W. Pribadi    [3 eks, 3 bab]
    Bab 1: Paradigma Objek
    Bab 2: Enkapsulasi
    Bab 3: Pewarisan dan Polimorfisme

  Mencoba mengubah koleksi internal dari luar...
    Ditolak — koleksi bab tidak bisa diubah dari luar. Benar.

=== Agregasi: Jurusan menaungi Mahasiswa ===
  TI - Teknik Informatika (2 mahasiswa)
  Setelah Budi pindah: TI - Teknik Informatika (1 mahasiswa)
  Budi tetap ada sebagai objek: 2024002 Budi Santoso

  Itulah bedanya: Bab tidak bisa hidup di luar Buku,
  tetapi Mahasiswa tetap ada di luar Jurusan.
```

## Screenshot Before dan After

### A. Screenshot Before

Kondisi program sebelum dijalankan atau dilengkapi.
Buku.java
<img width="1051" height="868" alt="image" src="https://github.com/user-attachments/assets/bd8b8cd0-b0a7-4e8f-af32-bb644d1a9557" />

Jurusan.java
<img width="1057" height="892" alt="image" src="https://github.com/user-attachments/assets/18e88c51-9c0c-4fa9-a13a-c44754f73029" />

PerpustakaanService.java
<img width="1048" height="897" alt="image" src="https://github.com/user-attachments/assets/9ea7d177-e186-4b2b-a6b5-959157b08901" />

Relasi.php
<img width="1142" height="893" alt="image" src="https://github.com/user-attachments/assets/063233fe-7716-44eb-91fd-d1935cc60ce1" />





### B. Screenshot After

Kondisi program setelah dilengkapi dan dijalankan.
Main.php
<img width="1082" height="941" alt="image" src="https://github.com/user-attachments/assets/21893bb1-6314-4452-a646-81d6baf462b7" />

Main.java
<img width="1130" height="972" alt="image" src="https://github.com/user-attachments/assets/11009a90-bbfc-4304-825a-009d958cbe30" />



## Kesimpulan

Pada praktikum ini saya belajar bahwa relasi antar kelas punya tingkat keterikatan yang berbeda. Pada komposisi, `Bab` dibuat dan dijaga oleh `Buku`, sehingga tidak bisa dibuat atau diubah dari luar. Pada agregasi, `Mahasiswa` dibuat di luar `Jurusan` dan tetap ada walaupun dikeluarkan dari jurusan.

Saya juga memahami pentingnya enkapsulasi dan validasi, misalnya menolak jumlah eksemplar negatif, nomor bab yang tidak valid, dan stok yang habis. Dengan membuat versi Java dan PHP, saya melihat konsep yang sama bisa diterapkan di bahasa berbeda. Class diagram yang dibuat dari deskripsi kebutuhan juga membantu saya menentukan kelas dan relasinya sebelum menulis kode.
