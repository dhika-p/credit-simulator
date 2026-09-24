# Credit Simulator

Aplikasi console untuk menghitung simulasi cicilan bulanan kredit kendaraan (mobil/motor).
Ditulis dengan **Java 17**, tanpa library/framework eksternal (JUnit hanya dipakai untuk test).

## Prasyarat


| Cara | Kebutuhan |
|---|---|
| Native (macOS / Linux) | Java 17 atau lebih baru. Maven tidak perlu diinstal (sudah ada Maven Wrapper). |
| Docker | Docker |

## Cara Menjalankan

### macOS / Linux

```bash
git clone https://github.com/dhika-p/credit-simulator.git
cd credit-simulator

# mode interaktif
./credit_simulator

# mode file
./credit_simulator file_inputs.txt
```

`bin/credit_simulator` juga bisa dipakai dengan argumen yang sama.



### Windows



Dari PowerShell atau cmd:

```powershell
.\mvnw.cmd -DskipTests package
java -jar target\credit-simulator.jar
java -jar target\credit-simulator.jar file_inputs.txt
```

### Docker

Image: https://hub.docker.com/r/dhikap/credit-simulator

```bash
# mode interaktif (wajib pakai -it)
docker run -it --rm dhikap/credit-simulator

# mode file, memakai file_inputs.txt yang sudah ada di dalam image
docker run --rm dhikap/credit-simulator file_inputs.txt

# mode file, memakai file sendiri
docker run --rm -v "$(pwd)/data.txt:/app/data.txt" dhikap/credit-simulator data.txt
```

Flag `-it` diperlukan pada mode interaktif agar keyboard terhubung ke aplikasi. Tanpa `-it`, aplikasi akan
keluar dengan pesan `Input berakhir tiba-tiba. Aplikasi keluar.`

Build image sendiri dari source:

```bash
docker build -t credit-simulator .
docker run -it --rm credit-simulator
```

## Perintah di Menu


| Perintah | Keterangan |
|---|---|
| `calculate` | Input data kendaraan dan pinjaman satu per satu, lalu tampilkan cicilan |
| `load` | Ambil data dari web service (JSON), lalu hitung otomatis |
| `load -url "<url>"` | Sama seperti `load`, tetapi memakai URL lain |
| `file <path>` | Baca simulasi dari file, lalu hitung otomatis |
| `save "<nama>"` | Simpan hasil simulasi terakhir sebagai sheet |
| `sheet` | Tampilkan daftar sheet yang tersimpan |
| `sheet "<nama>"` | Tampilkan hasil simulasi di sheet tersebut |
| `show` | Tampilkan semua perintah yang tersedia |
| `exit` | Keluar dari aplikasi |

Contoh input `calculate`:

```
> calculate
Jenis Kendaraan (Motor/Mobil): mobil
Kondisi Kendaraan (Baru/Bekas): bekas
Tahun Kendaraan (4 digit): 2020
Harga Kendaraan (maks 1 M): 100000000
Tenor Pinjaman (1-6 tahun): 3
Jumlah DP: 25000000
```

Jika ada input yang salah, aplikasi menampilkan pesan error lalu meminta input yang sama diulang,
tanpa mengulang dari awal.

Contoh output:

```
--- Hasil Simulasi ---
Jenis Kendaraan  : Mobil
Kondisi          : Bekas
Tahun Kendaraan  : 2020
Harga Kendaraan  : Rp. 100,000,000.00
Uang Muka (DP)   : Rp. 25,000,000.00
Pokok Pinjaman   : Rp. 75,000,000.00
Tenor            : 3 tahun

tahun 1 : Rp. 2,250,000.00/bln , Suku Bunga : 8%
tahun 2 : Rp. 2,432,250.00/bln , Suku Bunga : 8,1%
tahun 3 : Rp. 2,641,423.50/bln , Suku Bunga : 8,6%

Rata-rata cicilan : Rp. 2,441,224.50/bln
Total pembayaran  : Rp. 87,884,082.00
```

### Menyimpan dan membuka sheet

Setiap hasil dari `calculate`, `load`, atau `file` bisa disimpan dengan nama, lalu dibuka lagi kapan saja
tanpa input ulang:

```
> save "simulasi avanza"
Sheet 'simulasi avanza' tersimpan.

> sheet
Sheet tersimpan:
  simulasi avanza   Mobil Bekas 2020, Rp. 100,000,000.00, 3 tahun
  motor-bekas       Motor Bekas 2019, Rp. 15,000,000.00, 6 tahun

> sheet "simulasi avanza"
=== Sheet: simulasi avanza ===
--- Hasil Simulasi ---
...
```

- Nama sheet harus unik, tanpa membedakan huruf besar/kecil (`Avanza` dan `avanza` dianggap sama).
- Nama yang mengandung spasi harus diberi tanda kutip. Tanpa spasi, tanda kutip boleh dihilangkan.
- `save` menyimpan hasil terakhir. Untuk `file` yang berisi beberapa simulasi, yang disimpan adalah
  simulasi valid terakhir.
- Membuka sheet menampilkan hasil yang tersimpan, tanpa menghitung ulang.
- Sheet hanya tersimpan selama aplikasi berjalan, dan hilang setelah `exit`.

## Format File Input

Satu baris untuk satu simulasi, dipisahkan koma:

```
# jenis,kondisi,tahun,harga,tenor,dp
Mobil,Bekas,2020,100000000,3,25000000
Motor,Baru,2026,30000000,2,10500000
```


Contoh lengkap ada di [`file_inputs.txt`](file_inputs.txt).

## Konfigurasi

Tidak ada file konfigurasi. Aplikasi berjalan dengan nilai default, dan nilai itu bisa ditimpa dengan
environment variable:

| Variabel | Default | Keterangan |
|---|---|---|
| `CREDIT_SIMULATOR_API_URL` | Mock endpoint di GitHub Gist | URL JSON yang dipakai perintah `load` |
| `CREDIT_SIMULATOR_TIMEOUT_SECONDS` | `10` | Batas waktu request HTTP |

Urutan prioritas URL untuk `load`:

1. `load -url "<url>"` di menu
2. Environment variable `CREDIT_SIMULATOR_API_URL`
3. Nilai default

Contoh:

```bash
CREDIT_SIMULATOR_API_URL="https://contoh.com/loan.json" ./credit_simulator
docker run -it --rm -e CREDIT_SIMULATOR_API_URL="https://contoh.com/loan.json" dhikap/credit-simulator
```

Struktur JSON yang diharapkan (satu objek):

```json
{
  "vehicleType": "Mobil",
  "vehicleCondition": "Baru",
  "vehicleYear": 2026,
  "totalLoanAmount": 500000000,
  "loanTenure": 3,
  "downPayment": 175000000
}
```

## Aturan Perhitungan dan Validasi

### Validasi

| Input | Aturan |
|---|---|
| Jenis kendaraan | `Mobil` atau `Motor` (tidak peka huruf besar/kecil) |
| Kondisi | `Baru` atau `Bekas` (tidak peka huruf besar/kecil) |
| Tahun | Angka. Kendaraan **Baru** minimal tahun sekarang − 1 |
| Harga kendaraan | Maksimal Rp1.000.000.000 |
| Tenor | 1 sampai 6 tahun |
| DP | Baru minimal 35% harga, Bekas minimal 25% harga, dan harus lebih kecil dari harga |

### Suku bunga

- Bunga dasar: **Mobil 8%**, **Motor 9%**.
- Mulai tahun ke-2, bunga naik **0,1%** di tahun genap dan **0,5%** di tahun ganjil.
  Contoh Mobil tenor 6: 8% → 8,1% → 8,6% → 8,7% → 9,2% → 9,3%.

### Rumus cicilan

Mengikuti `Rumus.xlsx` dari repository contoh:

```
pokok(1)      = harga kendaraan − DP
total(n)      = pokok(n) + pokok(n) × bunga(n)
sisaTenor(n)  = tenor − n + 1
cicilan/bln   = total(n) / (sisaTenor(n) × 12)
cicilan/thn   = cicilan/bln × 12
pokok(n+1)    = total(n) − cicilan/thn
```

Seluruh perhitungan memakai `BigDecimal`. Pembulatan ke 2 desimal hanya dilakukan saat ditampilkan.


## Unit Test

```bash
./mvnw test          # macOS / Linux / Git Bash
.\mvnw.cmd test      # Windows PowerShell / cmd
```

| Test | Yang diuji |
|---|---|
| `InstallmentStrategyFactoryTest` | Rumus cicilan cocok dengan `Rumus.xlsx` |
| `SimpleJsonParserTest` | Parsing JSON tanpa library |
| `WebServiceDataSourceTest` | Ambil data dari web service lalu dihitung (memakai server HTTP lokal, tanpa internet) |
| `FileDataSourceTest` | Baca file input lalu dihitung |

Test tidak membutuhkan internet, sehingga tetap lulus walaupun endpoint web service sedang mati.

## CI/CD

Workflow ada di [`.github/workflows/ci-master.yml`](.github/workflows/ci-master.yml)

## info

1. **Endpoint `load`**: URL `mocky.io` di soal sudah tidak aktif (HTTP 404), sehingga default diganti ke mock
   di GitHub Gist dengan struktur JSON di atas. URL bisa diganti tanpa build ulang.
2. **Aturan DP kendaraan Bekas**: di soal tertulis "Baru ≥ 25%" dua kali, dianggap typo dan maksudnya kendaraan **Bekas**.
3. **Rumus cicilan** mengikuti `Rumus.xlsx`
4. **`load`** adalah perintah di menu, bukan dijalankan otomatis saat aplikasi mulai.
