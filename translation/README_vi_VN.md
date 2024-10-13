# Library Management System with Java OOP

<!-- Discraimer -->
> Dự án này chỉ dành cho mục đích học tập. MỌI DỮ LIỆU TRONG TẬP TIN SQL CHỈ DÀNH CHO MỤC ĐÍCH HỌC TẬP. BẠN CÓ THỂ SỬ DỤNG NÓ CHO MỤC ĐÍCH CÁ NHÂN CỦA BẠN. Nhưng, nếu bạn muốn sử dụng nó cho mục đích thương mại, hãy tạo cơ sở dữ liệu SQL của riêng bạn. Đây không phải là dự án thực tế.

Dự án lớn về Quản lý Sách với Java OOP (Mục đích học tập)

- bgsix: Là tên nhóm: Big Group Six (nhóm 6)
- Thành viên:
  - Trần Thành Long (Leader) [neilfranci](https://github.com/neilfranci)
  - Nguyễn Vũ Hải Đăng [nguyendang3002](https://github.com/nguyendang3002)
  - Vũ Văn Sơn [sunyn582](https://github.com/sunyn582)

- SPHM: Spring Postgresql Htmx Maven (công nghệ sử dụng)

<div align="center">
  <a href="https://github.com/neilfranci/book-management-SJPM">English</a> |
  <span>Vietnamese</span>
</div>
<br/>

## Mục Lục

### 1. Yêu cầu cài đặt trước cho Windows

1. [Cài đặt Postgresql](#1-cài-đặt-postgresql-download)
2. [Thêm chương trình cần thiết vào đường dẫn](#2-thêm-chương-trình-cần-thiết-vào-đường-dẫn)
3. [Tạo cơ sở dữ liệu](#3-tạo-cơ-sở-dữ-liệu)
4. [Nhập cơ sở dữ liệu](#4-nhập-cơ-sở-dữ-liệu)
5. [Cài đặt Maven](#1-cài-đặt-maven)
6. [Cài đặt GraalVM native-image](#2-cài-đặt-graalvm) (Thay thế Java SDK)
7. [Cài đặt các yêu cầu cho GraalVM native-image (Production)](#3-cài-đặt-các-yêu-cầu-cho-graalvm-native-image-để-xây-dựng-bản-production)

### 2. Setup Dự án

1. [Clone dự án](#1-clone-dự-án)
2. [Mở dự án trong IDE yêu thích của bạn](#2-mở-dự-án-trong-ide-yêu-thích-của-bạn)

### 3. [Chạy Dự án](#3-chạy-dự-án)

1. [Chỉnh sửa tệp `.env.template` trong đường dẫn này](#31-chỉnh-sửa-tệp-env-server)
2. [Chạy server (Development)](#32-chạy-server-development)
3. [Xây dựng native image (Production)](#33-xây-dựng-native-image-production)

---

## Cài đặt Postgresql cho Windows

### 1. [Cài đặt Postgresql (download)](https://www.enterprisedb.com/downloads/postgres-postgresql-downloads)

1.1 Nhấn vào Setup, sau đó nhấn "Next".

1.2 Bạn đang ở phần "Select Components".

1.3 Bỏ chọn "Stack Builder".

![alt text](assets/psql-install-1.png)

1.4 Nhấn "Next" cho đến khi gặp phần nhập mật khẩu cho superuser. Nhập mật khẩu của bạn.

`Cổng mặc định là 5432`

1.5 Tiếp tục nhấn "Next" cho đến khi gặp phần "Installing".

1.6 Đợi cho đến khi hoàn tất và nhấn "Finish". XONG!

### 2. Thêm chương trình cần thiết vào đường dẫn

Đường dẫn cài đặt mặc định là:

```pwsh
C:\Program Files\PostgreSQL\16\bin
```

Mở biến môi trường hệ thống trên Windows.

<img src="../assets/enviroment-variable-1.png" height="250">

<img src="../assets/enviroment-variable-2.png" height="400">

- Nhấp đúp vào "Path" trong phần hệ thống.

<img src="../assets/system-path.png" height="200">

- Nhấn "New"
- Dán đường dẫn vào
- Nhấn "OK"

<img src="../assets/add-path.png" height="400">

### 3. Tạo cơ sở dữ liệu

#### 3.1. Xác minh đường dẫn đã được thêm vào môi trường

- Mở Command Prompt hoặc PowerShell

```pwsh
psql -V

# Kết quả : psql (PostgreSQL) 16.4
```

#### 3.2. Nếu lệnh trên trả về như trên thì tiếp tục phần tiếp theo

- Tạo cơ sở dữ liệu

```pwsh
createdb -U postgres book-management
```

- Nhập mật khẩu của bạn từ [Cài đặt postgresql](#1-cài-đặt-postgresql-download).

> Ở đây chúng ta sử dụng superuser mặc định "postgres".

#### 3.3 Xác minh cơ sở dữ liệu đã được tạo

```pwsh
psql -U postgres

# Sau đó trong console
postgres=#

# Nhập: \l (không phải số 1)

postgres=# \l

```

> Nhập \q để thoát khỏi console hoặc Ctrl + C

- Kết quả ví dụ:

<img src="../assets/psql-l.png">

### 4. Nhập cơ sở dữ liệu

- Mở Terminal trong thư mục dự án (root)  [Theo hướng dẫn này](#thiết-lập-dự-án)
- Tệp book-management.sql nằm ở thư mục gốc của dự án (sử dụng đường dẫn tương đối để nhập cơ sở dữ liệu).

- Nếu có file `book-management.sql` ở root thì chạy lệnh sau:

```pwsh
psql -U postgres -f .\book-management.sql book-management
```

`---------^username----^file path----------- ^database name`

- Xác minh cơ sở dữ liệu đã được nhập

```pwsh
psql -U postgres -d book-management

# Sau đó trong console

SELECT author FROM book LIMIT 5;       # để xem dữ liệu
```

### 1. Cài đặt Maven

- Tải Maven từ <https://maven.apache.org/download.cgi>
  - Tệp zip binary `apache-maven-3.9.9-bin.zip`
- Giải nén tệp zip đến vị trí mong muốn
  - Ví dụ: `C:\apache-maven-3.9.9` (đảm bảo có thư mục `bin` trong thư mục `apache-maven-3.9.9`)
- Thêm đường dẫn Maven vào biến môi trường hệ thống
  - Mở biến môi trường
  - Nhấp đúp vào `Path` trong phần người dùng
  - Nhấn "New"
  - Dán đường dẫn `C:\apache-maven-3.9.9\bin`
  - Nhấn "OK"

- Xác minh cài đặt Maven

```pwsh
mvn -v
# Kết quả: Apache Maven 3.9.9
```

### 2. Cài đặt GraalVM

- Tải GraalVM từ <https://www.graalvm.org/downloads/>
  - Đảm bảo chọn phiên bản Java 22 và Windows 64 bit
- Giải nén tệp zip đến vị trí mong muốn
  - Ví dụ: `C:\Program Files\Java\graalvm-jdk-22.0.2+9.1\bin` (đảm bảo có thư mục `bin` trong thư mục `graalvm-jdk-22.0.2+9.1`)
- Thêm đường dẫn GraalVM vào biến môi trường hệ thống
  - Mở biến môi trường
  - Nhấp đúp vào `Path` trong phần người dùng
  - Nhấn "New"
  - Dán đường dẫn `C:\Program Files\Java\graalvm-jdk-22.0.2+9.1\bin`
  - Nhấn "OK"

- Xác minh cài đặt GraalVM

```pwsh
java -version
# Kết quả: 
# java version "22.0.2" 2024-07-16
# Java(TM) SE Runtime Environment Oracle GraalVM 22.0.2+9.1 (build 22.0.2+9-jvmci-b01)
# Java HotSpot(TM) 64-Bit Server VM Oracle GraalVM 22.0.2+9.1 (build 22.0.2+9-jvmci-b01, mixed mode, sharing)
```

### 3. Cài đặt các yêu cầu cho GraalVM native-image để xây dựng bản Production

- Thực hiện theo hướng dẫn từ <https://www.graalvm.org/latest/docs/getting-started/windows/#prerequisites-for-native-image-on-windows>

## Thiết lập Dự án

### 1. Clone dự án

```bash
git clone https://github.com/neilfranci/book-management-SJPM.git
```

### 2. Mở dự án trong IDE yêu thích của bạn

2.1 VSCode

cd đến thư mục dự án

```bash
cd book-management-SJPM
```

rồi thực hiện lệnh sau để mở VSCode:

```bash
code .
```

Cài đặt extension VS Code cho Java từ <https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack>

Sau đó đợi extension cài đặt xong

```bash
mvn clean install
```

### 3. Chạy Project

#### 3.1 Cấu hình biến môi trường (`.env` file)

- Tạo một tệp mới `.env` ở cùng đường dẫn và sao chép nội dung từ `.env.template` sang `.env`.

- Thay thế giá trị bằng giá trị của bạn.

#### 3.2 Chạy Project (Development)

```bash
mvn spring-boot:run
```

#### 3.3 Build native image (Production) (Không khả dụng bây giờ)

```bash
mvn clean package -Pnative
```

- Sau đó

 chạy tệp thực thi trong thư mục target
`.\target` (native image là một tệp duy nhất), trong trường hợp này, tệp là `bookmanagement.exe`.

---

### FAQ

#### 1. Cannot drop the currently open database, database "book-management" already exists

Answer: Chỉ cần xóa cơ sở dữ liệu

```pwsh
dropdb -U postgres book-management
```

### 2. DTO? Đó là gì?

Answer:  Data Transfer Object (DTO) là một mẫu thiết kế được sử dụng để chuyển dữ liệu giữa các đối tượng truy cập dữ liệu và các đối tượng giá trị. Dữ liệu được chuyển dưới dạng các đối tượng truyền dữ liệu. Lớp DATABASE > Lớp Repository > Lớp Service > Lớp Controller > Lớp DTO > Dữ liệu gửi đến Client
