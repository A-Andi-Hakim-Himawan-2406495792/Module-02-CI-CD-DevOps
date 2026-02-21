# Reflection – Clean Code & Secure Coding Practices

Refleksi ini ditulis berdasarkan pengalaman mengerjakan tutorial dan exercise **Spring Boot EShop**, khususnya dalam menerapkan prinsip **Clean Code** dan **Secure Coding Practices**.

---

## Clean Code Practices yang Diterapkan

### 1. Struktur Project Mengikuti Pola MVC

Struktur project dibagi ke dalam beberapa package seperti `controller`, `service`, `repository`, dan `model`. Pembagian ini membuat kode lebih rapi dan mudah dipahami karena setiap layer memiliki tanggung jawab yang jelas:

* **Controller** menangani request dan response dari client
* **Service** berisi business logic
* **Repository** bertugas mengelola data
* **Model** merepresentasikan entitas domain (Product)

Dengan struktur seperti ini, alur aplikasi menjadi lebih jelas dan perubahan pada satu layer tidak langsung berdampak ke layer lainnya.

---

### 2. Penamaan Class dan Method yang Konsisten

Penamaan class dan method mengikuti fungsinya secara langsung, misalnya:

* `ProductService`, `ProductServiceImpl`
* `ProductRepository`
* `create`, `findAll`, `findById`

Penamaan yang deskriptif ini meningkatkan keterbacaan kode dan mengurangi kebutuhan akan komentar tambahan.

---

### 3. Pengembangan Fitur Secara Bertahap

Pengembangan dilakukan menggunakan branch terpisah untuk setiap fitur, seperti `list-product`, `edit-product`, dan `delete-product`.
Pendekatan ini membantu fokus pada satu fitur dalam satu waktu dan mempermudah proses debugging ketika terjadi error.

---

## Masalah yang Ditemui Selama Pengembangan

### 1. Ketidakkonsistenan Kontrak Antar Layer

Masalah paling terasa muncul saat menambahkan fitur **edit** dan **delete**.

Di layer `ProductService` sudah terdapat method seperti:

* `findById`
* `update`
* `deleteById`

Namun method tersebut belum tersedia di `ProductRepository`, sehingga muncul error seperti:

* `cannot find symbol`
* `does not override abstract method`

Hal ini menunjukkan bahwa perubahan di service tidak diiringi dengan penyesuaian di repository.

**Solusi:**
Pastikan setiap method yang didefinisikan di service memiliki implementasi yang sesuai dan konsisten di repository.

---

### 2. Implementasi Repository yang Terlalu Sederhana

Repository masih menggunakan `List<Product>` tanpa validasi tambahan:

* Tidak ada pengecekan ID unik
* Update dan delete bisa dilakukan tanpa memastikan data benar-benar ada

Meskipun masih sesuai dengan tahap tutorial (belum menggunakan database), pendekatan ini berisiko jika diterapkan di aplikasi nyata.

**Solusi:**
Tambahkan validasi sederhana, misalnya:

* Mengecek keberadaan ID sebelum update atau delete
* Mengembalikan `Optional<Product>` atau `null` jika data tidak ditemukan

---

### 3. Minimnya Error Handling

Jika product dengan ID tertentu tidak ditemukan, alur aplikasi belum menangani kondisi tersebut secara eksplisit.
Hal ini berpotensi menimbulkan bug tersembunyi atau error saat runtime.

**Solusi:**
Tambahkan handling di layer service agar kondisi gagal tetap aman dan terkontrol.

---

## Secure Coding Practices yang Sudah Terlihat

### 1. Akses Data Melalui Service

Controller tidak langsung memanipulasi data, melainkan melalui service layer.
Pendekatan ini membantu menjaga alur logika tetap terkontrol dan mengurangi risiko penyalahgunaan akses data.

---

### 2. Enkapsulasi Data

Field seperti `productData` di repository dibuat `private`.
Meskipun terlihat sederhana, hal ini penting untuk mencegah modifikasi data secara langsung dari luar class.

---

## Hal yang Masih Bisa Ditingkatkan

* Menambahkan unit test di folder `test` untuk memastikan fitur list, edit, dan delete berjalan sesuai ekspektasi (khusus bagian sebelum exercise 1).
* Memperjelas tanggung jawab setiap method seiring bertambahnya fitur.
* Menjaga konsistensi antar layer agar error seperti `cannot find symbol` bisa dihindari sejak awal.

---

# Reflection – Clean Code & Secure Coding Practices Part 2

## 1. Clean Code dan Unit Testing

Setelah menulis unit test, proses awal terasa cukup rumit karena perlu menambahkan banyak kode pengujian. Namun setelah seluruh test berhasil dijalankan, muncul rasa aman saat melakukan refactoring atau menambahkan fitur baru. Unit test membantu memastikan perubahan yang dilakukan tidak merusak fitur yang sudah ada sebelumnya.

Tidak ada jumlah pasti untuk unit test yang harus dibuat. Fokus utama bukan pada banyaknya test, melainkan pada cakupan skenario yang diuji, seperti:

* **Happy path**, ketika input valid
* **Negative cases**, ketika input tidak valid
* **Edge cases**, seperti data kosong atau batas nilai tertentu

Code coverage 100% menunjukkan bahwa semua baris kode telah dieksekusi oleh test, tetapi tidak menjamin kode bebas dari bug. Coverage tidak dapat mendeteksi kesalahan logika atau kebutuhan fitur yang belum terimplementasi.

---

## 2. Clean Code pada Functional Testing

Jika functional test baru dibuat dengan cara menyalin setup dari test sebelumnya, maka akan terjadi **code duplication** yang melanggar prinsip DRY (*Don't Repeat Yourself*). Dampaknya adalah kode menjadi sulit dirawat dan perubahan konfigurasi harus dilakukan di banyak tempat.

Pendekatan yang lebih clean adalah membuat **Base Functional Test Class**, misalnya `BaseFunctionalTest`, yang berisi seluruh setup umum seperti konfigurasi port dan inisialisasi server. Test lain cukup mewarisi class tersebut dan fokus pada pengujian fitur masing-masing.

---

## Kesimpulan

Tutorial ini membantu memahami dasar Spring Boot dan pola MVC. Namun saat fitur mulai bertambah, seperti edit dan delete, terlihat jelas pentingnya konsistensi antar layer dan perencanaan method sejak awal.
Secara keseluruhan, kode sudah cukup rapi untuk tahap pembelajaran, tetapi masih memiliki banyak ruang untuk ditingkatkan agar lebih clean, aman, dan siap dikembangkan lebih lanjut.


---

# Reflection – CI/CD & DevOps (Module 02)

## 1. Code Quality Issues yang Diperbaiki

Selama exercise ini, saya menemukan cukup banyak isu kualitas kode yang di-flag oleh PMD, dan hampir semua file test kena—mulai dari `ProductControllerTest.java`, `HomeControllerTest.java`, `CreateProductFunctionalTest.java`, `HomePageFunctionalTest.java`, `ProductTest.java`, `ProductRepositoryTest.java`, `ProductServiceImplTest.java`, sampai `EshopApplicationTest.java`.

Ada tiga jenis *warning* utama yang muncul. Pertama, `JUnitTestsShouldIncludeAssert`—PMD menganggap test yang tidak punya *assertion* eksplisit bukan test yang valid, meskipun saya sudah pakai `.andExpect()` dari `MockMvc` dan `Mockito.verify()`. Untuk kasus seperti ini, saya menambahkan `@SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")` karena memang *false positive*—testnya sudah benar secara logika, hanya PMD yang tidak bisa mendeteksinya. Kedua, `JUnitAssertionsShouldIncludeMessage`—setiap *assertion* harus disertai pesan yang menjelaskan konteksnya, misalnya `assertEquals(5, productList.size(), "Product list size should be 5")`. Tujuannya biar kalau CI gagal, pesannya langsung informatif dan gampang di-debug. Ketiga, `JUnitTestContainsTooManyAsserts`—satu test method idealnya hanya menguji satu behavior. Kalau terlalu banyak assert dalam satu method, saya pecah jadi beberapa test yang lebih fokus.

Selain tiga isu PMD tersebut, saya juga sekalian mengganti anotasi `@MockBean` yang sudah *deprecated* menjadi `@MockitoBean`, biar kode tetap *up-to-date* dengan *best practice* Spring Boot versi terbaru.

---

## 2. Implementasi CI/CD

Menurut saya, implementasi GitHub Actions yang ada sudah memenuhi definisi *Continuous Integration* (CI) maupun *Continuous Deployment* (CD). Dari sisi CI, setiap kali saya *push* kode baru atau membuat *pull request*, pipeline otomatis menjalankan seluruh *test suite*, mengukur *code coverage*, serta memindai kualitas dan keamanan kode menggunakan PMD dan OSSF Scorecard—sehingga kode yang bermasalah bisa terdeteksi sebelum sempat masuk ke branch `main`.

Dari sisi CD, saya sudah menghubungkan repository ke PaaS sehingga setiap perubahan yang masuk ke `main` akan otomatis men-*trigger build* Docker dan *deployment* aplikasi tanpa perlu intervensi manual. Dengan begitu, saya benar-benar tidak perlu lagi *build* dan *deploy* dari laptop sendiri setiap kali ada pembaruan.

