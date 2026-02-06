# Reflection – Clean Code & Secure Coding Practices

Refleksi ini dibuat berdasarkan proses pengerjaan tutorial dan exercise Spring Boot EShop.
---

## Clean Code Practices yang Terlihat

### 1. Struktur Project Mengikuti MVC
Pembagian package menjadi `controller`, `service`, `repository`, dan `model` sangat membantu menjaga kode tetap rapi.  
Setiap layer punya peran jelas:
- **Controller** fokus ke request dan response
- **Service** fokus ke business logic
- **Repository** fokus ke pengelolaan data
- **Model** merepresentasikan domain (Product)

Dengan struktur ini, alur aplikasi lebih mudah dipahami dan perubahan di satu layer tidak terlalu berdampak ke layer lain.

---

### 2. Penamaan Class dan Method Konsisten
Nama class dan method mengikuti apa yang diajarkan di tutorial dan cukup deskriptif, seperti:
- `ProductService`, `ProductServiceImpl`
- `ProductRepository`
- `create`, `findAll`, `findById`

Ini bikin kode lebih readable dan tidak perlu banyak komentar tambahan.

---

### 3. Kode Dibangun Bertahap Sesuai Fitur
Pengembangan dilakukan per-branch (`list-product`, `edit-product`, `delete-product`).  
Pendekatan ini membantu fokus ke satu fitur dalam satu waktu dan memudahkan debugging ketika ada error.

---

## Masalah yang Ditemui Selama Pengembangan

### 1. Kontrak Antar Layer Tidak Konsisten
Masalah paling terasa muncul saat menambahkan fitur **edit** dan **delete**.

Di `ProductService`, sudah ada method seperti:
- `findById`
- `update`
- `deleteById`

Namun di `ProductRepository`, method tersebut belum ada.  
Akibatnya muncul error seperti:
- `cannot find symbol`
- `does not override abstract method`

Ini menunjukkan bahwa perubahan di service tidak diikuti oleh repository.

**Perbaikan:**  
Pastikan setiap method yang dipanggil oleh service memang tersedia di repository, dan namanya konsisten.

---

### 2. Repository Masih Terlalu Sederhana
Repository masih menggunakan `List<Product>` tanpa validasi:
- Tidak ada pengecekan ID unik
- Update dan delete bisa dilakukan tanpa memastikan data benar-benar ada

Walaupun ini masih sesuai dengan tahap tutorial (belum pakai database), hal ini bisa berbahaya kalau diterapkan di aplikasi nyata.

**Perbaikan:**  
Tambahkan validasi sederhana, misalnya:
- Cek ID sebelum update/delete
- Kembalikan `null` atau `Optional<Product>` jika data tidak ditemukan

---

### 3. Minim Error Handling
Jika product dengan ID tertentu tidak ditemukan, alur aplikasi belum menangani kondisi tersebut secara eksplisit.  
Hal ini bisa menyebabkan bug tersembunyi atau error runtime.

**Perbaikan:**  
Tambahkan handling di service layer agar kondisi gagal tetap aman dan terkontrol.

---

## Secure Coding Practices yang Sudah Ada

### 1. Data Tidak Diakses Langsung dari Controller
Controller tidak memanipulasi data secara langsung, melainkan lewat service.  
Ini mengurangi risiko logic bercampur dan akses data yang tidak terkontrol.

---

### 2. Enkapsulasi Data
Field seperti `productData` di repository dibuat `private`.  
Walaupun terlihat sepele, ini penting untuk mencegah perubahan data dari luar class.

---

## Hal yang Bisa Ditingkatkan

- Tambahkan unit test di folder `test` untuk memastikan fitur list, edit, dan delete berjalan sesuai ekspektasi. (hanya untuk bagian sebelum exercise 1)
- Perjelas tanggung jawab setiap method, terutama saat fitur mulai bertambah.
- Jaga konsistensi antar layer supaya error seperti `cannot find symbol` bisa dihindari sejak awal.

---

## Kesimpulan

Mengikuti tutorial ini membantu memahami dasar struktur Spring Boot dan MVC. Namun, ketika fitur bertambah (edit dan delete), terlihat jelas pentingnya konsistensi antar layer dan perencanaan method sejak awal.  
Kode sudah cukup rapi untuk tahap pembelajaran, tapi masih banyak ruang untuk diperbaiki agar lebih clean, aman, dan siap dikembangkan lebih lanjut.
