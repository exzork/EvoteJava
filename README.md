# EvoteJava

[![GitHub release](https://img.shields.io/github/release/muhammadeko/EvoteJava.svg)](https://github.com/muhammadeko/EvoteJava/releases/)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)    

--------------
Aplikasi ini dibuat untuk memenuhi tugas akhir mata kuliah Pemrograman Berbasis Objek kelompok 6, Jurusan Informatika, UPN "Veteran" Jawa Timur.


| Nama                   |    NPM      |
|------------------------|-------------|
| Muhammad Eko Prasetyo  | 19081010097 |
| Joni Bastian           | 19081010071 |
| Ario Hartoko           | 19081010125 |

----------
Penggunaan Aplikasi :

1. Jika belum pernah menjalankan Aplikasi ini, maka pada saat berada di menu Login mohon tekan CTRL + ALT + P untuk membuka konfigurasi.
2. Isi Konfigurasi mulai dari yang paling kiri (Database, Admin, lalu SMTP) mohon pastikan status Database dan SMTP ada dalam keadaan "Terkoneksi". Jika nanti akan merubah data Admin mohon tekan tombol tes koneksi mysql terlebih dahulu.
3. Jika ingin merubah tampilan ( Look and Feel ) terdapat dropdown diatas tombol simpan.
4. Simpan Konfigurasi dan Aplikasi siap digunakan.

Demo Penggunaan Aplikasi dan Penjelasan Source Code secara Singkat :

[![IMAGE ALT TEXT HERE](http://img.youtube.com/vi/lmu1he-2rkc/0.jpg)](http://www.youtube.com/watch?v=lmu1he-2rkc)

--------
Fitur Aplikasi :

Aplikasi ini menggunakan GUI java swing agar memudahkan user dalam menggunakan aplikasi, lalu aplikasi ini juga menggunakan database agar memudahkan penyimpanan data.
Penginstall aplikasi dapat mengakses menu config yang merupakan panel untuk mengubah lokasi database, akun admin, setting SMTP agar memudahkan dalam penginstallan dan pengaturan sesuai kebutuhan, selain itu juga dapat mengubah tema look and feel sesuai kebutuhan dan kenyamanan
Fitur utama Aplikasi E-voting dibagi menjadi 3 berdasarkan menurut pengguna :


1.	Admin
-	Dalam panel utama admin, dapat dengan mudah membuat event hanya dengan menginputkan nama event(contoh : Pemira 2021) dan akan secara otomatis tersimpan dalam database, lalu admin juga dapat meng-edit event sesiau kebutuhan, dan juga dapat menghapus event yang dipilih.
-	Selain membuat event, admin dapat membuat user baru dengan menginputkan nama, npm, dan email, lalu password akan dibuat secara random oleh sistem lalu dikirim ke email tersebut agar terjaga kerahasiaan user, selain membuat, admin juga dapat meng-edit dan menghapus data user tersebut.
-	Berikutnya admin dapat membuat panitia dengan data user yang sudah tersedia sehingga admin hanya perlu meng klik event yang diinginkan lalu hanya menginputkan npm dari panitia tersebut, sama seperti sebelumnya admin juga dapat mengubah ataupun menghapus data panitia yang diinginkan.
 
 
2.	Panitia
-	Panitia yang sudah terdaftar dalam sebuah event dapat mengakses panel panitia dan membuat pemilihan yang terdapat dalam event tersebut hanya dengan meng-klik event dan menginputkan nama pemilihan(contoh : Ketua Himatifa 2021) tersebut, panitia juga dapat melakukan edit dan hapus terhadap pemilihan yang sudah terdaftar.
-	Panitia juga dapat mendaftarkan paslon yang akan bersaing dalam pemilihan tersebut dengan lengkap dengan menginputkan npm ketua dan wakilnya, foto, visi, misi, dan apabila kurang sesuai, panitia dapat meng-edit data ataupun menghapus paslon.
-	Selain itu panitia juga dapat mendaftarkan NPM berapa saja yang akan menjadi DPT(Daftar Pemilih Tetap) agar hanya user tertentu saja yang dapat melakukan pemungutan suara dalam event tersebut, selain itu juga panitia dapat menghapus data DPT tersebut.
- Setelah pemungutan suara berakhir panitia dapat mengakhiri event dan sistem akan menampilkan warning apabila masih ada DPT yang belum memilih, dan apabila sudah selesai maka sistem akan menampilkan hasil akhir dari masing masing pemilihan secara lengkap.


3.	Pemilih
- Pemilih yang sudah terdaftar dapat mengakses panel user dan sistem akan menampilkan event yang sedang berlangsung berdasarkan dimana pemilih tersebut merupakan DPT dan dapat klik event yang ingin diakses, namun apabila pemilih sudah melakukan pemilihan dalam event tersebut maka event tersebut tidak akan tampil.
- Setelah mengakses event, sistem akan secara berurut menampilkan salah satu pemilihan dan menampilkan paslon paslon yang tersedia dalam pemilihan tersebut, namun apabila pemilih dapat memencet tombol selanjutnya atau sebelumnya apabila masih kurang yakin dan ingin men-cek pemilihan laiinya terlebih dahulu, dan apabila sudah yakin pemilih dapat memilih dengan mengklik salah satu paslon dari setiap pemilihan dan menyelesaikan pemilihan.

