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
fuelable java
<img width="1031" height="392" alt="image" src="https://github.com/user-attachments/assets/ae04a44c-7683-4e81-9dac-4c64ce7870e4" />
kendaraan java
<img width="1440" height="626" alt="image" src="https://github.com/user-attachments/assets/23e2552e-4a92-462a-b61d-bf4eaadfa7a1" />
Mobil java
<img width="1158" height="873" alt="image" src="https://github.com/user-attachments/assets/b5c3f5e7-0975-4849-b9f9-4446003b4823" />
moovable java
<img width="1072" height="562" alt="image" src="https://github.com/user-attachments/assets/74ffb0a5-4062-4ec3-b07c-9e01f826062b" />
tipe bahan bakar java
<img width="1112" height="848" alt="image" src="https://github.com/user-attachments/assets/2710ea72-04d2-4d25-bfda-3ebd78974d67" />
abstraksi php
<img width="910" height="896" alt="image" src="https://github.com/user-attachments/assets/69274880-91ee-4295-9207-818425d1800f" />






## Screenshot Hasil Program (After)
main php
<img width="1136" height="895" alt="image" src="https://github.com/user-attachments/assets/7a1b4fdd-ea0c-4c75-baca-940355d07f96" />
main java
<img width="1203" height="985" alt="image" src="https://github.com/user-attachments/assets/f80c903c-7560-4a98-a36d-3716e5244500" />



## Kesimpulan
Praktikum ini membantu saya memahami bahwa abstract class cocok untuk menampung data atau perilaku umum, sedangkan interface digunakan untuk menentukan kemampuan yang harus dimiliki suatu class. Enum membuat pilihan nilai lebih terkontrol, sehingga program lebih terstruktur dan mudah dipahami.
