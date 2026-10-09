# Praktikum PBO — Pertemuan 5
## Materi: Abstract Class, Method Overriding, dan Polymorphism

**Nama:** Muhammad Alfarel Prihadi  
**NPM:** 4525210083  
**Program Studi:** Teknik Informatika  
**Universitas:** Universitas Pancasila  

## Tujuan Praktikum
Memahami penggunaan abstract class dan penerapan polymorphism melalui objek-objek yang memiliki bentuk atau perilaku berbeda.

## Penjelasan Program
Program Java pada folder `java` menggunakan abstract class `BangunDatar` sebagai class dasar. Class `Lingkaran`, `Persegi`, `Segitiga`, dan `Trapesium` menjadi turunannya serta menyediakan perhitungan luas dan keliling masing-masing. Method `luas()` dan `keliling()` diimplementasikan sesuai rumus setiap bangun datar. Pada `Main.java`, beberapa objek disimpan dalam satu daftar bertipe `BangunDatar` dan diproses melalui perulangan.

Folder PHP berisi contoh implementasi abstract class `BangunDatar` beserta class turunannya. Folder ini juga memiliki contoh program notifikasi yang menunjukkan penggunaan abstract class sebagai kontrak untuk beberapa jenis notifikasi.

## Screenshot Kode (Before)
Simpan screenshot kode program pada folder `screenshots` dengan nama `before.png`, lalu tampilkan di bawah ini.

![Screenshot kode sebelum dijalankan](screenshots/before.png)

## Screenshot Hasil Program (After)
Simpan screenshot hasil menjalankan program dengan nama `after.png` di folder `screenshots`.

![Screenshot hasil program](screenshots/after.png)

## Kesimpulan
Dari praktikum ini, saya memahami bahwa abstract class dapat menyediakan kerangka umum untuk class turunan. Method overriding memungkinkan setiap bangun datar menghitung luas dan keliling dengan caranya sendiri, sementara polymorphism memungkinkan objek berbeda diproses melalui tipe induk yang sama.
