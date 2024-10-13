# Library Management System with Java OOP

<!-- Discraimer -->

> This project is for learning purpose. EVERY DATA IN THE SQL FILE IS JUST FOR LEARNING PURPOSE. YOU ARE FREE TO USE IT FOR YOUR OWN PURPOSE. But, if you want to use it for commercial purposes, please recreate your own sql database. It's not a production ready.

Big Project about Book Management with Java OOP (Learining purpose)

- bgsix: Is the team name: Big Group Six (Nhóm 6)
- Members:
  - Trần Thành Long (Leader) [neilfranci](https://github.com/neilfranci)
  - Nguyễn Vũ Hải Đăng [nguyendang3002](https://github.com/nguyendang3002)
  - Vũ Văn Sơn [sunyn582](https://github.com/sunyn582)

- SPHM: Spring Postgresql Htmx Maven (the technology stack)

<div align="center">
  <span>English</span> |
  <a href="https://github.com/neilfranci/book-management-SJPM/blob/main/translation/README_vi_VN.md">Vietnamese</a>
</div>

<br/>
<div align="center">
  Url list:
  <a href="https://github.com/neilfranci/book-management-SJPM/blob/main/ALL_URLS.md">ALL_URLS</a>
</div>

## Table of Contents

### 1. Pre-requisite for Windows

   1. [Install the Postgresql](#1-install-the-postgresql-download)
   2. [Adding necessary program to path](#2-adding-necessary-program-to-path)
   3. [Creating a database](#3-creating-a-database)
   4. [Importing the database](#4-importing-the-database)
   5. [Install maven](#1-install-maven)
   6. [Install GraalVM native-image](#2-install-graalvm) (Java SDK replacement)
   7. ~~[Install Pre-requisite for GraalVM native-image (Production)](#3-install-pre-requisite-for-graalvm-native-image-to-build-the-production-build)~~

### 2. Setup Project

1. [Clone the project](#1-clone-the-project)
2. [Open the project in your favorite IDE](#2-open-the-project-in-your-favorite-ide)

### 3. [Run the Project](#3-run-the-project)

1. [Open the .env.template file in this path](#31-configure-the-environment-variables-env-file)
2. [Running the server (Development)](#32-running-the-project-development)
3. [Building self-contained jar (Production)](#33-building-self-contained-jar-production)
4. ~~[Building the native image (Production)](#34-building-the-native-image-production-not-working-right-now)~~

---

## Postgresql installation for Windows

### 1. [Install the Postgresql (download)](https://www.enterprisedb.com/downloads/postgres-postgresql-downloads)

1.1 After press install, and next.

1.2 You now in "Select Components" part

1.3 Uncheck Stack Builder

![alt text](assets/psql-install-1.png)

1.4 press next, until you meet password for superuser
type your own password

`default port is 5432`

1.5 keep pressing next until it in "Installing" part.

1.6 Wait for it done, and Finish. DONE!

---

### 2. Adding necessary program to path

Default install path is:

```pwsh
C:\Program Files\PostgreSQL\16\bin
```

Open system enviroment variable on Windows.

<img src="assets\enviroment-variable-1.png" height="250">

<img src="assets\enviroment-variable-2.png" height="400">

- Double click on the Path in the system section

<img src="assets\system-path.png" height="200">

- Click on new
- Paste the path
- CLick OK

<img src="assets/add-path.png" height="400">

---

### 3. Creating a database

#### 3.1. Verify Path is added to the enviroment

- Open the command prompt or powershell

```pwsh
psql -V

# Output : psql (PostgreSQL) 16.4
```

#### 3.2. If the previous command is return like above then continue with next part

- Create a database

```pwsh
createdb -U postgres book-management
```

enter your password from [#1.4](#1-install-the-postgresql-download)

> We using the default superuser "postgres" here

#### 3.3 Verify dababase is created

```pwsh
psql -U postgres

# then in the console
postgres=#

# type \l (not the number 1)

postgres=# \l


```

> \q to quit the console or Ctrl + C

- Example output

<img src="assets/psql-l.png">

#### 3.4. Script to create database and import database (MUST HAVE THE .SQL FILE)

```pwsh
script\drcrim.cmd password
```

> 1. **password** is the password from [#1.4](#1-install-the-postgresql-download)

---

### 4. Importing the database

- Open the Terminal in the project folder (root) [follow this](#setup-project)
- The book-management.sql is in the root of the project (using relative path to import the database)

- If `book-management.sql` is in the root of the project then run this command

```pwsh
psql -U postgres -f .\book-management.sql book-management
```

`---------^username----^file path----------- ^database name`

- Verify the database is imported

```pwsh
psql -U postgres -d book-management

# then in the console

SELECT author FROM book LIMIT 5;      # to see the data
```

### 1. Install maven

- Download maven from <https://maven.apache.org/download.cgi>
  - Binary zip archive apache-maven-3.9.9-bin.zip
- Extract the zip file to the desired location
  - Example: `C:\apache-maven-3.9.9` (make sure there is a folder `bin` in the `apache-maven-3.9.9` folder)
- Add the maven path to the system environment variable
  - Open the environment variable
  - Double Click on the `Path` in the user section
  - Click on new
  - Paste the path `C:\apache-maven-3.9.9\bin`
  - Click OK

- Verify the maven installation

```pwsh
mvn -v
# Output : Apache Maven 3.9.9
```

### 2. Install GraalVM

- Download GraalVM from <https://www.graalvm.org/downloads/>
  - Make sure to choose Java 22 version and Windows 64 bit
- Extract the zip file to the desired location
  - Example: `C:\Program Files\Java\graalvm-jdk-22.0.2+9.1\bin` (make sure there is a folder `bin` in the `graalvm-jdk-22.0.2+9.1` folder)
- Add the GraalVM path to the system environment variable
  - Open the environment variable
  - Double Click on the `Path` in the user section
  - Click on new
  - Paste the path `C:\Program Files\Java\graalvm-jdk-22.0.2+9.1\bin`
  - Click OK

- Verify the GraalVM installation

```pwsh
java -version
# Output : 
# java version "22.0.2" 2024-07-16
# Java(TM) SE Runtime Environment Oracle GraalVM 22.0.2+9.1 (build 22.0.2+9-jvmci-b01)
# Java HotSpot(TM) 64-Bit Server VM Oracle GraalVM 22.0.2+9.1 (build 22.0.2+9-jvmci-b01, mixed mode, sharing)
```

### 3. Install Pre-requisite for GraalVM native-image to build the Production build

- Follow the instruction from <https://www.graalvm.org/latest/docs/getting-started/windows/#prerequisites-for-native-image-on-windows>

---

## Setup Project

### 1. Clone the project

```bash
git clone https://github.com/neilfranci/book-management-SJPM.git
```

### 2. Open the project in your favorite IDE

2.1 VSCode

cd to the project folder

```bash
cd book-management-SJPM
```

then open the project in vscode with this command

```bash
code .
```

install vs code extension for java <https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack>

then wait for the extension to install

```bash
mvn clean install
```

### 3. Run the Project

#### 3.1 Configure the environment variables (`.env` file)

- create a new file `.env` in the root and copy the content from `.env.template` to `.env`

- replace the value with your own value

#### 3.2 Running the project (Development)

```bash
mvn spring-boot:run
```

#### 3.3 Building self-contained jar (Production)

```bash
mvn clean package
```

- this will create a jar file in the target folder `/target/bookmanagement-{version}.jar`

- then to run jar file:

```bash
java -jar target/bookmanagement-{version}.jar
```

#### ~~3.4 building the native image (Production) (NOT WORKING RIGHT NOW)~~

```bash
mvn clean package -Pnative
```

- then run the executable file in the target folder `.\taget` (the native image is a single file) in this case the file is `bookmanagement.exe

---

## FAQ

### 1. Cannot drop the currently open database, database "book-management" already exists

Answer: Just drop the database

```pwsh
dropdb -U postgres book-management
```

### 2. DTO? What is that?

Answer: Data Transfer Object (DTO) is a design pattern used to transfer data between data access objects and value objects. The data is transferred in the form of data transfer objects. DATABASE Class > Repository Class > Service Class > Controller Class > DTO Class > Sendalbe Data to Client
