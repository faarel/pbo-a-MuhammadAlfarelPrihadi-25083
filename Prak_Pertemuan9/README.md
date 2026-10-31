# Praktikum PBO — Pertemuan 9

**Materi:** Enkapsulasi, Immutability, Komposisi (Buku–Bab), Package, dan Layered Structure pada Sistem Perpustakaan (Java)

| | |
|---|---|
| **Nama** | Muhammad Alfarel Prihadi |
| **NPM** | 4525210083 |
| **Program Studi** | Teknik Informatika |
| **Universitas** | Universitas Pancasila |

---

## 1. Tujuan Praktikum

- Membuat program Java sederhana berupa **Sistem Perpustakaan** yang dipisah ke beberapa package (`model`, `service`, `util`).
- Memahami **enkapsulasi**: atribut dibuat `private`, diakses lewat getter, dan banyak yang `final` (immutable).
- Memahami **relasi komposisi**: class `Buku` punya daftar `Bab`, dan `Bab` dibuat sebagai *nested class* di dalam `Buku`.
- Memisahkan **logika bisnis** (class `PerpustakaanService`) dari data (class `model`) dan dari tampilan (`App`).
- Belajar validasi input dengan `IllegalArgumentException` / `IllegalStateException`.
- Menggunakan `LocalDate` untuk tanggal dan `Format` untuk memformat tanggal dan rupiah.

## 2. Penjelasan Program

Praktikum ini hanya berisi implementasi **Java**. Tidak ada file PHP di folder. Di kode juga tidak ada abstract class, interface, enum, atau class turunan (inheritance), jadi bagian itu tidak dibahas.

### Package `model`
- **`Anggota`**: data anggota (`nomorAnggota`, `nama`, `jenisKeanggotaan`). Semua atribut `final`. Constructor melempar `IllegalArgumentException` kalau nomor anggota atau nama kosong.
- **`Buku`**: data buku (`nomorInduk`, `judul`, `penulis`, `jumlahEksemplar`) dan daftar `Bab`.
  - `tambahBab()` membuat objek `Bab` baru, `getBab()` mengembalikan `List.copyOf` (salinan, tidak bisa diubah dari luar).
  - `tersedia()`, `kurangiStok()`, dan `tambahStok()` mengatur stok. `kurangiStok()` melempar `IllegalStateException` jika stok habis.
  - `Bab` adalah `public static final class` bersarang dengan constructor `private`, jadi `Bab` hanya bisa dibuat lewat `Buku` (**komposisi**).
- **`Peminjaman`**: transaksi pinjam. Menyimpan `Anggota`, `Buku`, tanggal pinjam, tanggal jatuh tempo (`tanggalPinjam.plusDays(lamaHari)`), dan tanggal kembali (awalnya `null`). `sudahKembali()` mengecek apakah tanggal kembali sudah terisi.

### Package `service`
- **`PerpustakaanService`**: menyimpan list koleksi, anggota, dan peminjaman.
  - `tambahKoleksi()` dan `daftarkanAnggota()` menambah data (tidak menambah kalau nomor induk / nomor anggota sudah ada).
  - `cariKoleksi()` dan `cariAnggota()` mencari data (tidak peduli huruf besar/kecil).
  - `pinjam()` memvalidasi anggota, buku, stok, dan batas maksimal **3 pinjaman aktif**, lalu mengurangi stok dan membuat ID otomatis seperti `PJ-001`.
  - `kembalikan()` mengisi tanggal kembali, menambah stok lagi, lalu mengembalikan nilai denda.
  - `hitungDenda()` menghitung denda per hari terlambat: **Mahasiswa Rp1.000**, **Dosen Rp500**, selain itu Rp0.
  - Getter list mengembalikan `Collections.unmodifiableList`.

### Package `util`
- **`Format`**: *utility class* (`final`, constructor `private`). `formatRupiah()` memformat nominal ke rupiah, `formatTanggal()` memformat tanggal ke `dd MMMM yyyy` (locale Indonesia) dan mengembalikan `"-"` jika `null`.

### `App.java` (class Main)
Titik masuk program (`main`). Alurnya:
1. Membuat `PerpustakaanService`.
2. Menambah 3 buku (`B-001`, `B-002`, `B-003`) beserta bab-babnya.
3. Mendaftarkan 3 anggota (2 Mahasiswa, 1 Dosen).
4. Membuat 2 peminjaman pada 20 Oktober 2026 selama 14 hari: Siti Aisyah meminjam `B-001` dan Dr. Hendra Gunawan meminjam `B-003`.
5. Mengembalikan peminjaman `PJ-001` terlambat 5 hari untuk simulasi denda.
6. Mencetak tabel koleksi, tabel transaksi, dan rincian denda lewat method `printTable()`.

## 3. Struktur Folder

```
Prak_Pertemuan9/
├── .vscode/
│   └── settings.json
├── src/
│   └── main/
│       └── java/
│           └── id/ac/univpancasila/perpustakaan/
│               ├── App.java
│               ├── model/
│               │   ├── Anggota.java
│               │   ├── Buku.java
│               │   └── Peminjaman.java
│               ├── service/
│               │   └── PerpustakaanService.java
│               └── util/
│                   └── Format.java
├── docs/
│   └── screenshots/          <- taruh screenshot di sini
│       ├── before/
│       └── after/
└── README.md
```

> Folder `docs/screenshots/` belum ada di project, silakan dibuat sendiri untuk menyimpan gambar.



## 5. Screenshot Hasil Program 

 <img width="1190" height="887" alt="image" src="https://github.com/user-attachments/assets/65365aec-852c-4cdd-9f84-094b518be438" />
<img width="1093" height="1027" alt="image" src="https://github.com/user-attachments/assets/026d2daf-8bdf-4f40-bc86-83ae9bb132c8" />
<img width="1176" height="973" alt="image" src="https://github.com/user-attachments/assets/d72c7234-bbed-4fa0-8cfc-9b37ac536353" />
<img width="1296" height="907" alt="image" src="https://github.com/user-attachments/assets/0f175ee7-096f-4cbe-8ea4-a79438042ecf" />
<img width="1143" height="883" alt="image" src="https://github.com/user-attachments/assets/9be65d85-4e13-41a3-b640-ef2b1ed087d1" />
<img width="1197" height="973" alt="image" src="https://github.com/user-attachments/assets/7e485abd-628e-455d-ac81-86b8b28b799d" />







## 7. Kesimpulan

Dari praktikum ini saya jadi lebih paham cara menyusun program Java yang rapi: data ada di package `model`, logika ada di `service`, dan fungsi bantu ada di `util`. Penerapan enkapsulasi lewat atribut `private final` dan getter bikin data lebih aman, apalagi `getBab()` dan getter list di service hanya mengembalikan salinan atau list yang tidak bisa diubah. Relasi komposisi antara `Buku` dan `Bab` juga membantu saya mengerti bahwa sebuah objek bisa "memiliki" objek lain dan mengatur pembuatannya sendiri. Validasi dengan exception, seperti batas 3 pinjaman, stok habis, atau data kosong, membuat program lebih tahan terhadap input yang salah.
