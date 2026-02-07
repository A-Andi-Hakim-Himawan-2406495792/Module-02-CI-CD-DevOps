# Reflection – Clean Code & Secure Coding Practices Part 1

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

Tentu, ini draf refleksi yang bisa kamu masukkan ke dalam `README.md`. Isinya teknis tapi bahasanya mengalir, khas mahasiswa Ilkom yang ngerti materi tapi nggak kaku kayak buku teks.

Silakan copy-paste bagian di bawah ini ke file `README.md` kamu (biasanya di bagian paling bawah atau buat sub-header baru).

---
# Reflection – Clean Code & Secure Coding Practices Part 2

### 1. Clean Code and Unit Testing

**Perasaan setelah menulis Unit Test:**
Setelah menulis unit test, rasanya campur aduk tapi dominan lega. Awalnya memang terasa ribet karena harus menulis kode ekstra (bahkan kadang baris kode test-nya lebih banyak dari kode aslinya). Tapi setelah test-nya *passed*, ada rasa aman ("sense of security") yang besar saat saya mau melakukan refactoring atau menambah fitur baru. Saya jadi tidak takut kode saya bakal merusak fitur yang sudah jalan sebelumnya.

**Berapa banyak Unit Test yang harus dibuat?**
Sebenarnya tidak ada jumlah pastinya. Prinsipnya bukan "semakin banyak semakin bagus", tapi **"semakin mencakup banyak skenario semakin bagus"**. Test yang kita buat harus mencakup:

* **Happy Path:** Skenario normal dimana user memasukkan input yang benar.
* **Negative Cases:** Saat user memasukkan input aneh atau salah (misal: quantity negatif, nama kosong).
* **Edge Cases:** Kasus batas (misal: list kosong, atau input maksimum karakter).

**Tentang Code Coverage 100%:**
Punya 100% code coverage itu bagus sebagai metrik bahwa setiap baris kode kita *pernah dieksekusi* oleh test. **TAPI**, 100% coverage **tidak menjamin** kode kita bebas dari bug atau error.

* **Alasannya:** Code coverage hanya menghitung baris yang "terjangkau". Ia tidak bisa mendeteksi *logic error*.
* **Contoh:** Kalau saya punya fungsi penjumlahan `add(a, b)` tapi di dalamnya saya salah tulis jadi `return a - b;`, test saya mungkin tetap jalan dan coverage 100% (karena barisnya dijalankan), tapi hasilnya salah secara logika. Atau, mungkin ada *requirements* yang terlewat yang sama sekali belum ada kodenya (missing features), ini tidak akan terdeteksi oleh code coverage.

### 2. Clean Code in Functional Testing

**Masalah pada kode Functional Test yang baru:**
Jika saya membuat functional test baru untuk menghitung jumlah item dengan cara *copy-paste* setup prosedur dan variabel instance dari `CreateProductFunctionalTest.java`, maka kode saya akan bermasalah dalam hal **Clean Code**.

Potensi masalah yang terjadi adalah **Code Duplication** (pelanggaran prinsip DRY - *Don't Repeat Yourself*).

* **Alasannya:** Saya akan memiliki kode setup (konfigurasi port, base URL, inisialisasi server) yang sama persis di dua (atau lebih) file yang berbeda.
* **Dampaknya:** Jika di masa depan saya perlu mengubah konfigurasi setup (misalnya ganti port default atau logic inisialisasi), saya harus mengeditnya manual di *semua* file test. Ini rentan error dan melelahkan (maintenance nightmare).

**Saran Perbaikan:**
Untuk membuat kode lebih bersih, saya bisa menerapkan konsep **Inheritance (Pewarisan)** atau membuat **Base Test Class**.

1. Buat satu class induk (misalnya `BaseFunctionalTest`) yang berisi semua konfigurasi umum: setup port, base URL, dan inisialisasi WebDriver.
2. Test suite lainnya (`CreateProductFunctionalTest`, `CountItemFunctionalTest`, dll) cukup `extends` class induk tersebut.
3. Dengan begitu, setiap test class hanya akan fokus pada logic pengetesan spesifik fitur tersebut, tanpa perlu mengulang kode setup yang sama. Kode jadi lebih rapi, modular, dan mudah di-maintain.

## Kesimpulan

Mengikuti tutorial ini membantu memahami dasar struktur Spring Boot dan MVC. Namun, ketika fitur bertambah (edit dan delete), terlihat jelas pentingnya konsistensi antar layer dan perencanaan method sejak awal.  
Kode sudah cukup rapi untuk tahap pembelajaran, tapi masih banyak ruang untuk diperbaiki agar lebih clean, aman, dan siap dikembangkan lebih lanjut.
