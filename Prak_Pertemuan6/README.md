# Praktikum PBO — Pertemuan 6
## Materi: Abstract Class, Interface, dan Enum

**Nama:** Muhammad Alfarel Prihadi  
**NPM:** 4525210083  
**Program Studi:** Teknik Informatika  
**Universitas:** Universitas Pancasila  

## Tujuan Praktikum
Memahami perbedaan abstract class dan interface serta menerapkan enum untuk mengelompokkan pilihan nilai yang sudah ditentukan.

## Penjelasan Program
Pada folder `java`, abstract class `Kendaraan` menyimpan data umum seperti merek dan tahun, serta mendefinisikan method yang harus disediakan oleh class turunannya. Class `Mobil` dan `Sepeda` mewarisi `Kendaraan`. Keduanya mengimplementasikan interface `Movable`, sedangkan `Mobil` juga mengimplementasikan `Fuelable` karena kendaraan ini menggunakan bahan bakar.

Interface `Movable` menetapkan perilaku untuk objek yang dapat bergerak. Interface `Fuelable` menetapkan operasi pengisian bahan bakar dan informasi kapasitas tangki. Enum `TipeBahanBakar` berisi jenis bahan bakar, label, biaya pengisian, dan informasi apakah jenis tersebut ramah lingkungan. File `Main.java` menjalankan contoh penggunaan class dan interface tersebut.

Folder `php` menyediakan implementasi konsep serupa menggunakan PHP, termasuk interface, enum, abstract class, dan trait.

## Screenshot Kode (Before)
Simpan screenshot kode program pada folder `screenshots` dengan nama `before.png`, lalu tampilkan di bawah ini.

![Screenshot kode sebelum dijalankan](screenshots/before.png)

## Screenshot Hasil Program (After)
Simpan screenshot hasil program dengan nama `after.png` di folder `screenshots`.

![Screenshot hasil program](screenshots/after.png)

## Kesimpulan
Praktikum ini membantu saya memahami bahwa abstract class cocok untuk menampung data atau perilaku umum, sedangkan interface digunakan untuk menentukan kemampuan yang harus dimiliki suatu class. Enum membuat pilihan nilai lebih terkontrol, sehingga program lebih terstruktur dan mudah dipahami.
