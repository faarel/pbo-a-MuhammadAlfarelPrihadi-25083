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
AntiPattern java
<img width="965" height="921" alt="image" src="https://github.com/user-attachments/assets/b4edb9c0-1710-42dc-9e6c-c068dddb226d" />
Bangun datar php
<img width="1115" height="865" alt="image" src="https://github.com/user-attachments/assets/d86dd9a0-b859-4584-87c0-1e2d7cde6e64" />
Bangun datar java
<img width="1241" height="788" alt="image" src="https://github.com/user-attachments/assets/fe7ea51b-dcda-43f3-b8f3-0e0f9485f3a7" />
Lingkaran java
<img width="1022" height="541" alt="image" src="https://github.com/user-attachments/assets/be1618ee-e12c-406f-ad42-672e08ce13c0" />
Notifikasi php
<img width="1002" height="862" alt="image" src="https://github.com/user-attachments/assets/7f3f48c2-4ee6-4568-be2b-d73183326a32" />
Persegi java
<img width="718" height="453" alt="image" src="https://github.com/user-attachments/assets/8bec8a75-9675-4b0e-b8fe-5d4600cc7ac0" />
main php
<img width="1327" height="722" alt="image" src="https://github.com/user-attachments/assets/b308f15c-6ef4-44ab-ac82-8f1c3804ddc6" />
main java
<img width="1142" height="872" alt="image" src="https://github.com/user-attachments/assets/f573b4d2-b4c3-4af4-99a4-94352b39b338" />



## Screenshot Hasil Program (After)
Hasil Java
<img width="1128" height="923" alt="image" src="https://github.com/user-attachments/assets/d73c84c2-889c-4388-b72c-75575eb44ec2" />
Hasil php
<img width="1322" height="868" alt="image" src="https://github.com/user-attachments/assets/9d6b20a3-ef7e-46af-a12f-24b6b755d2f2" />




## Kesimpulan
Dari praktikum ini, saya memahami bahwa abstract class dapat menyediakan kerangka umum untuk class turunan. Method overriding memungkinkan setiap bangun datar menghitung luas dan keliling dengan caranya sendiri, sementara polymorphism memungkinkan objek berbeda diproses melalui tipe induk yang sama.
